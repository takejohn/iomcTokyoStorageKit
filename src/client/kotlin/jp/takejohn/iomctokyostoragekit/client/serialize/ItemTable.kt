package jp.takejohn.iomctokyostoragekit.client.serialize

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.util.Identifier

@Serializable(ItemTable.Serializer::class)
data class ItemTable(private val inner: Map<Identifier, ItemRecord>) : Map<Identifier, ItemRecord> by inner {
    object Serializer : KSerializer<ItemTable> {
        private val delegateSerializer = MapSerializer(IdentifierSerializer, ItemRecord.serializer())

        // serialName は一意である必要があるため、このModのパッケージ名を含めておく
        override val descriptor: SerialDescriptor = SerialDescriptor(
            "jp.takejohn.iomctokyostoragekit.ItemTable",
            delegateSerializer.descriptor,
        )

        override fun serialize(encoder: Encoder, value: ItemTable) =
            encoder.encodeSerializableValue(delegateSerializer, value)

        override fun deserialize(decoder: Decoder): ItemTable =
            ItemTable(decoder.decodeSerializableValue(delegateSerializer))
    }
}
