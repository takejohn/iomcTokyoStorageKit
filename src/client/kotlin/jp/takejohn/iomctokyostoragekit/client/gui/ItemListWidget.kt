package jp.takejohn.iomctokyostoragekit.client.gui

import jp.takejohn.iomctokyostoragekit.client.item.ItemLocationList
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ScrollableWidget
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

private const val MARGIN = 5
private const val ITEM_SIZE = 18

class ItemListWidget(
    private val itemLocationList: ItemLocationList,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    message: Text,
    val textRenderer: TextRenderer,
) : ScrollableWidget(x, y, width, height, message) {
    private val columns = (width - MARGIN * 2) / ITEM_SIZE

    // itemLocationList.size() を column で割った値の切り上げ
    override fun getContentsHeight(): Int = (itemLocationList.size() + columns - 1) / columns

    override fun getDeltaYPerScroll(): Double = ITEM_SIZE.toDouble()

    override fun renderContents(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        val startX = x + (width - columns * ITEM_SIZE) / 2
        val startY = y + MARGIN
        for ((i, itemLocation) in itemLocationList.withIndex()) {
            val column = i % columns
            val row = i / columns
            val itemX = startX + column * ITEM_SIZE
            val itemY = startY + row * ITEM_SIZE
            val itemStack = ItemStack(itemLocation.item)
            context.drawItem(itemStack, itemX, itemY)

            // ホバーしている場合、ツールチップを描画
            if (mouseX in itemX until (itemX + 16) && mouseY in itemY until (itemY + 16)) {
                val locationText = Text.literal(itemLocation.locate.toString())
                val categoryText = Text.translatable(
                    "gui.iomctokyostoragekit.category",
                    itemLocation.category
                )
                val tooltip = listOf(itemStack.name, locationText, categoryText)
                context.drawTooltip(textRenderer, tooltip, mouseX, mouseY)
            }
        }
    }

    override fun appendClickableNarrations(builder: NarrationMessageBuilder?) {
        // TODOかも
    }
}
