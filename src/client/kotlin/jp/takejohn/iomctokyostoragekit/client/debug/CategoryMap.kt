package jp.takejohn.iomctokyostoragekit.client.debug

import kotlinx.serialization.json.Json
import net.minecraft.util.math.BlockPos

class CategoryMap {
    val mutableCategoryRegions = mutableListOf<CategoryRegion>()

    private var currentBuilder = CategoryRegion.Builder()

    fun getStart(): BlockPos? = currentBuilder.start

    fun getEnd(): BlockPos? = currentBuilder.end

    fun setStart(blockPos: BlockPos): Boolean = currentBuilder.setStart(blockPos)

    fun setEnd(blockPos: BlockPos): Boolean = currentBuilder.setEnd(blockPos)

    fun saveCategoryAs(name: String) {
        mutableCategoryRegions.add(currentBuilder.build(name))
        currentBuilder = CategoryRegion.Builder()
    }

    fun categoryRegions(): List<CategoryRegion> = mutableCategoryRegions

    fun serialize(): String = Json.encodeToString(categoryRegions())
}
