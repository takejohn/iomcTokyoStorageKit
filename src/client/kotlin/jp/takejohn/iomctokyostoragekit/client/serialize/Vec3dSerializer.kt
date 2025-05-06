package jp.takejohn.iomctokyostoragekit.client.serialize

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.DoubleArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.util.math.Vec3d

object Vec3dSerializer : KSerializer<Vec3d> {
    private val delegateSerializer = DoubleArraySerializer()

    // serialName は一意である必要があるため、このModのパッケージ名を含めておく
    override val descriptor: SerialDescriptor = SerialDescriptor(
        "jp.takejohn.iomctokyostoragekit.Vec3d",
        delegateSerializer.descriptor
    )

    override fun serialize(encoder: Encoder, value: Vec3d) {
        val doubleArray = doubleArrayOf(value.x, value.y, value.z)
        return encoder.encodeSerializableValue(delegateSerializer, doubleArray)
    }

    override fun deserialize(decoder: Decoder): Vec3d {
        val doubleArray = decoder.decodeSerializableValue(delegateSerializer)
        return Vec3d(doubleArray[0], doubleArray[1], doubleArray[2])
    }
}
