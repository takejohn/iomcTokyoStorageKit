package jp.takejohn.iomctokyostoragekit.client.serialize

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.util.Identifier

object IdentifierSerializer: KSerializer<Identifier> {
    private val delegateSerializer = String.serializer()

    // serialName は一意である必要があるため、このModのパッケージ名を含めておく
    override val descriptor: SerialDescriptor = SerialDescriptor(
        "jp.takejohn.iomctokyostoragekit.Identifier",
        delegateSerializer.descriptor
    )

    override fun serialize(encoder: Encoder, value: Identifier) =
        encoder.encodeSerializableValue(delegateSerializer, value.toString())

    override fun deserialize(decoder: Decoder): Identifier =
        Identifier.of(decoder.decodeSerializableValue(delegateSerializer))
}
