package jp.takejohn.iomctokyostoragekit.client.debug

import com.google.gson.JsonElement
import com.mojang.serialization.DataResult
import jp.takejohn.iomctokyostoragekit.client.IomcTokyoStorageKitClient
import jp.takejohn.iomctokyostoragekit.client.io.IOUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ContainerLoggerManager {
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
        IomcTokyoStorageKitClient.sendMessage(
            Text.translatable("debug.iomctokyostoragekit.logging.start")
        )
    }

    private fun stop(logger: ContainerLogger) {
        val result: DataResult<JsonElement> = logger.serialize()
        result.mapOrElse({ logJson: JsonElement ->
            IOUtils.saveDateTimeNamedJsonFile(logJson.toString(), "container_logs").handle { _, e ->
                if (e != null) {
                    IomcTokyoStorageKitClient.LOGGER.error("Error occurred while saving container log", e)
                    onLogSaveError()
                }
            }
        }, this::onSerializationError)
        this.logger = null
        MinecraftClient.getInstance().player?.sendMessage(
            Text.translatable("debug.iomctokyostoragekit.logging.stop")
        )
    }

    private fun onSerializationError(error: DataResult.Error<JsonElement>) {
        IomcTokyoStorageKitClient.LOGGER.error(error.toString())
        onLogSaveError()
    }

    private fun onLogSaveError() {
        IomcTokyoStorageKitClient.sendMessage(
            Text.translatable("debug.iomctokyostoragekit.logging.save_failed")
                .setStyle(Style.EMPTY.withColor(Formatting.RED))
        )
    }
}
