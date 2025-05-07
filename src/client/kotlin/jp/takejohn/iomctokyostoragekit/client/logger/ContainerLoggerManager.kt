package jp.takejohn.iomctokyostoragekit.client.logger

import com.google.gson.JsonElement
import com.mojang.serialization.DataResult
import jp.takejohn.iomctokyostoragekit.client.IomcTokyoStorageKitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

object ContainerLoggerManager {
    private val logSaveScope by lazy {
        CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    }

    private var logger: ContainerLogger? = null

    fun isEnabled(): Boolean = logger != null

    fun toggle() {
        logger?.let { stop(it) } ?: start()
    }

    fun setLastInteraction(interaction: ContainerLogger.Interaction) {
        logger?.lastInteraction = interaction
    }

    fun log(items: List<ItemStack>) {
        logger?.log(items)
    }

    private fun start() {
        logger = ContainerLogger()
        MinecraftClient.getInstance().player?.sendMessage(
            Text.translatable("debug.iomctokyostoragekit.logging.start"),
            false,
        )
    }

    private fun stop(logger: ContainerLogger) {
        val result: DataResult<JsonElement> = logger.serialize()
        result.mapOrElse({ logJson: JsonElement ->
            logSaveScope.launch {
                saveLog(logJson.toString())
            }
        }, this::onSerializationError)
        this.logger = null
        MinecraftClient.getInstance().player?.sendMessage(
            Text.translatable("debug.iomctokyostoragekit.logging.stop"),
            false,
        )
    }

    private fun saveLog(logString: String) {
        try {
            val gameDir = MinecraftClient.getInstance().runDirectory
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
            val logDir = File(gameDir, "iomcTokyoStorageKit/container_logs")
            logDir.mkdirs()
            val logFile = File(logDir, "$timestamp.json")
            logFile.writeText(logString, Charsets.UTF_8)
        } catch (e: Exception) {
            IomcTokyoStorageKitClient.LOGGER.error("Error occurred while saving container log", e)
            onLogSaveError()
        }
    }

    private fun onSerializationError(error: DataResult.Error<JsonElement>) {
        IomcTokyoStorageKitClient.LOGGER.error(error.toString())
        onLogSaveError()
    }

    private fun onLogSaveError() {
        val client = MinecraftClient.getInstance()
        client.execute {
            client.player?.sendMessage(
                Text.translatable("debug.iomctokyostoragekit.logging.save_failed")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED)),
                false,
            )
        }
    }
}
