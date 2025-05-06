package jp.takejohn.iomctokyostoragekit.client.gui

import jp.takejohn.iomctokyostoragekit.client.item.ItemLocationList
import jp.takejohn.iomctokyostoragekit.client.item.ItemLocationListLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text
import java.util.concurrent.atomic.AtomicReference

private const val MARGIN = 5
private const val PADDING = 5
private const val BASE_HEIGHT = 20
private const val BUTTON_WIDTH = 50

class SearchScreen(val parent: Screen?) : Screen(Text.translatable("gui.iomctokyostoragekit.item_search")){
    var titleText: TextWidget? = null

    var queryTextField: TextFieldWidget? = null

    var searchButton: ButtonWidget? = null

    val itemLocationList = AtomicReference<ItemLocationList?>()

    var itemList: ItemListWidget? = null

    override fun init() {
        addDrawableChild(createTitleText())
        addDrawableChild(createQueryTextField())
        addDrawableChild(createSearchButton())
        itemLocationList.get()?.let {
            addDrawableChild(createItemList(it))
        }
    }

    override fun close() {
        (client ?: MinecraftClient.getInstance()).setScreen(parent)
    }

    private fun createTitleText(): TextWidget {
        val widget = TextWidget(
            MARGIN,
            MARGIN,
            width - MARGIN * 2,
            BASE_HEIGHT,
            title,
            textRenderer,
        )
        titleText = widget
        return widget
    }

    private fun createQueryTextField(): TextFieldWidget {
        val widget = TextFieldWidget(
            textRenderer,
            MARGIN,
            MARGIN + BASE_HEIGHT + PADDING,
            width - MARGIN - (PADDING + BUTTON_WIDTH + MARGIN),
            BASE_HEIGHT,
            Text.empty(),
        )
        queryTextField = widget
        return widget
    }

    private fun createSearchButton(): ButtonWidget {
        val widget = ButtonWidget.builder(
            Text.translatable("gui.iomctokyostoragekit.search"),
            this::search
        )
            .dimensions(
                width - (BUTTON_WIDTH + MARGIN),
                MARGIN + BASE_HEIGHT + PADDING,
                BUTTON_WIDTH,
                BASE_HEIGHT,
            )
            .build()
        searchButton = widget
        return widget
    }

    private fun search(button: ButtonWidget) {
        ItemLocationListLoader.loadItems().thenAccept { itemLocationList ->
            this.itemLocationList.set(itemLocationList)
            queryTextField?.let {
                val matchedItems = itemLocationList.searchItems(it.text)
                remove(itemList)
                addDrawableChild(createItemList(matchedItems))
            }
        }
    }

    private fun createItemList(itemLocationList: ItemLocationList): ItemListWidget {
        val widget = ItemListWidget(
            itemLocationList,
            MARGIN,
            MARGIN + BASE_HEIGHT + PADDING + BASE_HEIGHT + PADDING,
            width - MARGIN * 2,
            height - (MARGIN + BASE_HEIGHT + PADDING + BASE_HEIGHT + PADDING) - MARGIN,
            Text.empty(),
            textRenderer,
        )
        itemList = widget
        return widget
    }
}
