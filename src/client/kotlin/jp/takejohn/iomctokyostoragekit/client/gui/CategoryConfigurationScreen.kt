package jp.takejohn.iomctokyostoragekit.client.gui

import jp.takejohn.iomctokyostoragekit.client.debug.CategoryMapManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text

private const val MARGIN = 5
private const val PADDING = 5
private const val BASE_HEIGHT = 20
private const val BUTTON_WIDTH = 50

class CategoryConfigurationScreen(val parent: Screen?) : Screen(Text.translatable("gui.iomctokyostoragekit.configure_category")) {
    private var nameTextField: TextFieldWidget? = null

    override fun init() {
        addDrawableChild(createTitleText())
        addDrawableChild(createNameTextField())
        addDrawableChild(createConfirmButton())
    }

    override fun close() {
        (client ?: MinecraftClient.getInstance()).setScreen(parent)
    }

    private fun createTitleText() = TextWidget(
        MARGIN,
        MARGIN,
        width - MARGIN * 2,
        BASE_HEIGHT,
        title,
        textRenderer,
    )

    private fun createNameTextField(): TextFieldWidget {
        val widget = TextFieldWidget(
            textRenderer,
            MARGIN,
            MARGIN + BASE_HEIGHT + PADDING,
            width - MARGIN - (PADDING + BUTTON_WIDTH + MARGIN),
            BASE_HEIGHT,
            Text.empty(),
        )
        nameTextField = widget
        return widget
    }

    private fun createConfirmButton(): ButtonWidget = ButtonWidget.builder(
        Text.translatable("gui.iomctokyostoragekit.confirm")
    ) {
        button -> CategoryMapManager.saveCategoryAs(nameTextField!!.text)
        close()
    }.dimensions(
        width - (BUTTON_WIDTH + MARGIN),
        MARGIN + BASE_HEIGHT + PADDING,
        BUTTON_WIDTH,
        BASE_HEIGHT,
    ).build()
}
