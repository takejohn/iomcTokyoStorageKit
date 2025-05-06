package jp.takejohn.iomctokyostoragekit.client.gui

import jp.takejohn.iomctokyostoragekit.client.item.ItemLocation
import jp.takejohn.iomctokyostoragekit.client.item.ItemLocationList
import jp.takejohn.iomctokyostoragekit.client.serialize.ItemRecord
import jp.takejohn.iomctokyostoragekit.client.serialize.ItemTable
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.gui.widget.ElementListWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text
import net.minecraft.util.Identifier

private const val BASE_HEIGHT = 16

private const val PADDING = 0

class ItemListWidget(
    itemLocationList: ItemLocationList,
    minecraftClient: MinecraftClient,
    width: Int,
    height: Int,
    y: Int,
) : ElementListWidget<ItemListWidget.Entry>(
    minecraftClient,
    width,
    height,
    y,
    BASE_HEIGHT * 3 + PADDING * 2
) {
    class Entry(
        val itemLocation: ItemLocation,
        val width: Int,
        val textRenderer: TextRenderer
    ) : ElementListWidget.Entry<Entry>() {
        private val children = mutableListOf<ClickableWidget>()

        private val nameText = TextWidget(itemLocation.item.name, textRenderer)

        private val locateText = TextWidget(Text.literal(itemLocation.locate.toString()), textRenderer)

        private val groupText = TextWidget(
            Text.translatable("gui.iomctokyostoragekit.category", itemLocation.category),
            textRenderer
        )

        init {
            children.add(nameText)
        }

        override fun children(): List<Element> = children

        override fun selectableChildren(): List<Selectable> = children

        override fun render(
            context: DrawContext?,
            index: Int,
            y: Int,
            x: Int,
            entryWidth: Int,
            entryHeight: Int,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            tickDelta: Float
        ) {
            nameText.x = x
            nameText.y = y
            nameText.render(context, mouseX, mouseY, tickDelta)

            locateText.x = x
            locateText.y = y + BASE_HEIGHT + PADDING
            locateText.render(context, mouseX, mouseY, tickDelta)

            groupText.x = x
            groupText.y = y + (BASE_HEIGHT + PADDING) * 2
            groupText.render(context, mouseX, mouseY, tickDelta)
        }
    }

    init {
        for (itemLocation in itemLocationList) {
            createEntry(itemLocation)
        }
    }

    private fun createEntry(itemLocation: ItemLocation) {
        val entry = Entry(itemLocation, width, client.textRenderer)
        addEntry(entry)
    }
}
