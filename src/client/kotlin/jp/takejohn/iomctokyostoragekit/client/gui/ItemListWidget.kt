package jp.takejohn.iomctokyostoragekit.client.gui

import jp.takejohn.iomctokyostoragekit.client.item.ItemRecord
import jp.takejohn.iomctokyostoragekit.client.item.ItemTable
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.gui.widget.ElementListWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text

private const val BASE_HEIGHT = 16

private const val PADDING = 0

class ItemListWidget(
    itemTable: ItemTable,
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
        val id: String,
        val data: ItemRecord,
        val width: Int,
        val textRenderer: TextRenderer
    ) : ElementListWidget.Entry<Entry>() {
        private val children = mutableListOf<ClickableWidget>()

        private val nameText = TextWidget(Text.literal(data.name), textRenderer)

        private val locateText = TextWidget(Text.literal(data.locate), textRenderer)

        private val groupText = TextWidget(Text.literal("ジャンル: " + data.group), textRenderer)

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
        for ((key, value) in itemTable) {
            createEntry(key, value)
        }
    }

    private fun createEntry(id: String, data: ItemRecord) {
        val entry = Entry(id, data, width, client.textRenderer)
        addEntry(entry)
    }
}
