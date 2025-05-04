package jp.takejohn.iomctokyostoragekit.client.item

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(ItemTable.Serializer::class)
data class ItemTable(private val inner: Map<String, ItemRecord>) : Map<String, ItemRecord> by inner {
    object Serializer : KSerializer<ItemTable> {
        private val delegateSerializer = MapSerializer(String.serializer(), ItemRecord.serializer())

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

    fun searchItems(query: String): ItemTable {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            return this
        }

        val matchedItems = this.filter { (key, value) ->
            key.contains(trimmedQuery, true) ||
                    value.name.contains(trimmedQuery, true) ||
                    value.group.contains(trimmedQuery, true)
        }
        return ItemTable(matchedItems)
    }
}
