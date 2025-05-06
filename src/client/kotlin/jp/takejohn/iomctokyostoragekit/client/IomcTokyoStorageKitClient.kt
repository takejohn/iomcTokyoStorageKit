package jp.takejohn.iomctokyostoragekit.client

import jp.takejohn.iomctokyostoragekit.client.gui.SearchScreen
import jp.takejohn.iomctokyostoragekit.client.highlight.HighlightMarkerClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

class IomcTokyoStorageKitClient : ClientModInitializer {
    private companion object {
        var keyBindingSearch: KeyBinding? = null
    }

    override fun onInitializeClient() {
        keyBindingSearch = KeyBindingHelper.registerKeyBinding(KeyBinding(
            "key.iomctokyostoragekit.open_search_screen",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "category.iomctokyostoragekit.key_bindings"
        ))

        // キーが押されるとアイテム検索画面を表示
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            keyBindingSearch?.let {
                if (it.wasPressed()) {
                    client.setScreen(SearchScreen(client.currentScreen))
                }
            }
        }

        // 1ティック経過するごとにハイライトの継続時間を減少
        ClientTickEvents.END_CLIENT_TICK.register {
            HighlightMarkerClient.tick()
        }
    }
}
