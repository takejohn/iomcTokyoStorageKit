package jp.takejohn.iomctokyostoragekit.client.debug

import kotlinx.serialization.Serializable
import net.minecraft.util.math.BlockPos

@Serializable
data class CategoryRegion(
    val name: String,
    val startX: Int,
    val startY: Int,
    val startZ: Int,
    val endX: Int,
    val endY: Int,
    val endZ: Int,
) {
    class Builder {
        var start: BlockPos? = null

        var end: BlockPos? = null

        fun setStart(blockPos: BlockPos): Boolean {
            if (blockPos == start) {
                return false
            }
            start = blockPos
            return true
        }

        fun setEnd(blockPos: BlockPos): Boolean {
            if (blockPos == end) {
                return false
            }
            end = blockPos
            return true
        }

        fun build(name: String): CategoryRegion {
            val start = requireNotNull(start) { "start is null" }
            val end = requireNotNull(end) { "end is null" }
            val (startX, endX) = minMax(start.x, end.x)
            val (startY, endY) = minMax(start.y, end.y)
            val (startZ, endZ) = minMax(start.z, end.z)
            return CategoryRegion(name, startX, startY, startZ, endX, endY, endZ)
        }

        private fun minMax(a: Int, b: Int): Pair<Int, Int> = if (a < b) {
            Pair(a, b)
        } else {
            Pair(b, a)
        }
    }
}
