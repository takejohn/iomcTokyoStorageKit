package jp.takejohn.iomctokyostoragekit.client.gui

import jp.takejohn.iomctokyostoragekit.client.highlight.HighlightMarkerClient
import jp.takejohn.iomctokyostoragekit.client.item.ItemLocation
import jp.takejohn.iomctokyostoragekit.client.item.ItemLocationList
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
) : ScrollableWidget(x, y, width, height, message) {
    inner class TooltipWithMousePosition(val tooltip: List<Text>, val mouseX: Int, val mouseY: Int)

    var hoveredTooltip: TooltipWithMousePosition? = null

    private val columns = (width - MARGIN * 2) / ITEM_SIZE

    val startX = x + (width - columns * ITEM_SIZE) / 2

    val startY = y + MARGIN

    // itemLocationList.size() を column で割った値の切り上げ
    override fun getContentsHeight(): Int = (itemLocationList.size() + columns - 1) / columns

    override fun getDeltaYPerScroll(): Double = ITEM_SIZE.toDouble()

    override fun renderContents(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        for ((i, itemLocation) in itemLocationList.withIndex()) {
            val (itemX, itemY) = getPositionByIndex(i)
            val itemStack = ItemStack(itemLocation.item)
            context.drawItem(itemStack, itemX, itemY)

            // ホバーしている場合、ツールチップを描画
            if (mouseX in itemX until (itemX + ITEM_SIZE) && mouseY in itemY until (itemY + ITEM_SIZE)) {
                val locationText = Text.literal(itemLocation.locate.toString())
                val categoryText = Text.translatable(
                    "gui.iomctokyostoragekit.category",
                    itemLocation.category
                )
                val tooltip = listOf(itemStack.name, locationText, categoryText)
                hoveredTooltip = TooltipWithMousePosition(tooltip, mouseX, mouseY)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        // 左クリック
        if (button == 0) {
            val index = getIndexByPosition(mouseX.toInt(), mouseY.toInt())
            if (index >= 0) {
                val itemLocation: ItemLocation = itemLocationList[index]
                HighlightMarkerClient.highlightAt(itemLocation.locate)
                return true
            }
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun drawBox(context: DrawContext?, x: Int, y: Int, width: Int, height: Int) {
        // 背景を透明にするため、何もしない
    }

    override fun appendClickableNarrations(builder: NarrationMessageBuilder?) {
        // TODOかも
    }

    private fun getPositionByIndex(index: Int): Pair<Int, Int> {
        val column = index % columns
        val row = index / columns
        val posX = startX + column * ITEM_SIZE
        val posY = startY + row * ITEM_SIZE
        return Pair(posX, posY)
    }

    // 画面上の x, y 座標からアイテムのインデックスを求める。
    // 対応するアイテムがない場合は -1 を返す。
    private fun getIndexByPosition(posX: Int, posY: Int): Int {
        val column = (posX - startX) / ITEM_SIZE
        val row = (posY - startY) / ITEM_SIZE
        if (column in 0 until columns) {
            val index = row * columns + column
            if (index < itemLocationList.size()) {
                return index
            }
        }
        return -1
    }
}
