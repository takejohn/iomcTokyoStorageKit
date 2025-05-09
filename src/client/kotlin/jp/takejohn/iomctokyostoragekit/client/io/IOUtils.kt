package jp.takejohn.iomctokyostoragekit.client.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import net.minecraft.client.MinecraftClient
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

object IOUtils {
    private val logSaveScope by lazy {
        CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    }

    /**
     * Mod用のディレクトリ内に日時を名前にしたJSONファイルを保存する。
     * @param content ファイルの内容
     * @param dir iomcTokyoStorageKitディレクトリより下のディレクトリ名
     */
    fun saveDateTimeNamedJsonFile(content: String, dir: String): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()
        logSaveScope.launch {
            try {
                saveDateTimeNamedJsonFileSync(content, dir)
                future.complete(null)
            } catch (e: Exception) {
                future.completeExceptionally(e)
            }
        }
        return future
    }

    private fun saveDateTimeNamedJsonFileSync(content: String, dir: String) {
        val gameDir = MinecraftClient.getInstance().runDirectory
        val modDir = File(gameDir, "iomcTokyoStorageKit")
        val parentDir = File(modDir, dir)
        parentDir.mkdirs()
        val timestamp: String = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        )
        val file = File(parentDir, "$timestamp.json")
        file.writeText(content, Charsets.UTF_8)
    }
}
