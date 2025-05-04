package jp.takejohn.iomctokyostoragekit.client.item

import kotlinx.serialization.Serializable
import net.minecraft.util.math.Vec3d

@Serializable
data class ItemRecord(
    val name: String,
    @Serializable(Vec3dSerializer::class)
    val locate: Vec3d,
    val group: String
)
