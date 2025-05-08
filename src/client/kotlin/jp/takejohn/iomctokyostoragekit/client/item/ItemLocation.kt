package jp.takejohn.iomctokyostoragekit.client.item

import jp.takejohn.iomctokyostoragekit.client.serialize.ItemRecord
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class ItemLocation(id: Identifier, record: ItemRecord) {
    val item: Item = Registries.ITEM.get(id)

    val locate = record.locate

    val category = record.group

    val defaultName = record.name

    val isUnknownItem = item == Items.AIR

    val name: Text = if (isUnknownItem) {
        Text.literal(defaultName)
    } else {
        item.name
    }
}
