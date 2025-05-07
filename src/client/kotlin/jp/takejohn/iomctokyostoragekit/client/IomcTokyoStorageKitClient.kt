package jp.takejohn.iomctokyostoragekit.client

import jp.takejohn.iomctokyostoragekit.client.gui.SearchScreen
import jp.takejohn.iomctokyostoragekit.client.highlight.HighlightMarkerClient
import jp.takejohn.iomctokyostoragekit.client.logger.ContainerLogger
import jp.takejohn.iomctokyostoragekit.client.logger.ContainerLoggerManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class IomcTokyoStorageKitClient : ClientModInitializer {
    companion object {
        val LOGGER: Logger by lazy {
            LoggerFactory.getLogger("iomcTokyoStorageKit")
        }
    }

    private class KeyBindings {
        val openSearchScreen: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding(
            "key.iomctokyostoragekit.open_search_screen",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "category.iomctokyostoragekit.key_bindings",
        ))

        val toggleLogging: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding(
            "key.iomctokyostoragekit.toggle_logging",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.iomctokyostoragekit.key_bindings",
        ))
    }

    override fun onInitializeClient() {
        val keyBindings = KeyBindings()

        // キーバインドの呼び出し
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (keyBindings.openSearchScreen.wasPressed()) {
                client.setScreen(SearchScreen(client.currentScreen))
            }

            if (keyBindings.toggleLogging.wasPressed()) {
                ContainerLoggerManager.toggle()
            }
        }

        // 1ティック経過するごとにハイライトの継続時間を減少
        ClientTickEvents.END_CLIENT_TICK.register {
            HighlightMarkerClient.tick()
        }

        WorldRenderEvents.LAST.register { context ->
            HighlightMarkerClient.renderWorld(context)
        }

        UseBlockCallback.EVENT.register { player, world, hand, hitResult ->
            if (ContainerLoggerManager.isEnabled() && !player.isSpectator && hand == Hand.MAIN_HAND && world is ClientWorld) {
                ContainerLoggerManager.setLastInteraction(ContainerLogger.Interaction(player, world, hand, hitResult))
            }
            return@register ActionResult.PASS
        }
    }
}
