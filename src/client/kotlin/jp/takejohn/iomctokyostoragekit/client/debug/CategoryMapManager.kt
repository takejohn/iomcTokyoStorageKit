package jp.takejohn.iomctokyostoragekit.client.debug

import jp.takejohn.iomctokyostoragekit.client.IomcTokyoStorageKitClient
import jp.takejohn.iomctokyostoragekit.client.io.IOUtils
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

object CategoryMapManager {
    private var categoryMap: CategoryMap? = null

    fun toggle() {
        categoryMap?.let { stop(it) } ?: start()
    }

    fun isEnabled(): Boolean = categoryMap != null

    fun setStartPos(blockPos: BlockPos) {
        categoryMap?.let {
            if (it.setStart(blockPos)) {
                IomcTokyoStorageKitClient.sendMessage(Text.translatable(
                    "debug.iomctokyostoragekit.category_map.set_start",
                    blockPos.toShortString()
                ))
            }
        }
    }

    fun setEndPos(blockPos: BlockPos) {
        categoryMap?.let {
            if (it.setEnd(blockPos)) {
                IomcTokyoStorageKitClient.sendMessage(Text.translatable(
                        "debug.iomctokyostoragekit.category_map.set_end",
                        blockPos.toShortString()
                ))
            }
        }
    }

    fun saveCategoryAs(name: String) {
        categoryMap?.let {
            if (it.getStart() == null || it.getEnd() == null) {
                IomcTokyoStorageKitClient.sendMessage(
                    Text.translatable("debug.iomctokyostoragekit.category_map.position_not_set")
                )
            }
            it.saveCategoryAs(name)
        }
    }

    private fun start() {
        categoryMap = CategoryMap()
        IomcTokyoStorageKitClient.sendMessage(
            Text.translatable("debug.iomctokyostoragekit.category_map.start")
        )
    }

    private fun stop(categoryMap: CategoryMap) {
        val content = categoryMap.serialize()
        IOUtils.saveDateTimeNamedJsonFile(content, "category_maps").handle { _, e ->
            if (e != null) {
                IomcTokyoStorageKitClient.LOGGER.error("Error occurred while saving container log", e)
            }
        }
        this.categoryMap = null
        IomcTokyoStorageKitClient.sendMessage(
            Text.translatable("debug.iomctokyostoragekit.category_map.end")
        )
    }
}
