package jp.takejohn.iomctokyostoragekit.client.item

import jp.takejohn.iomctokyostoragekit.client.serialize.ItemRecord
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class ItemLocation(id: Identifier, record: ItemRecord) {
    val item: Item = Registries.ITEM.get(id)

    val locate = record.locate

    val category = record.group
}
