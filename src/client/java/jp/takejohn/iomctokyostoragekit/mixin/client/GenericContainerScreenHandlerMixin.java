package jp.takejohn.iomctokyostoragekit.mixin.client;

import jp.takejohn.iomctokyostoragekit.client.logger.ContainerLoggerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GenericContainerScreenHandler.class)
abstract class GenericContainerScreenHandlerMixin {
    @Inject(method = "onClosed", at = @At("HEAD"))
    private void onClosed(PlayerEntity player, CallbackInfo callbackInfo) {
        if (!player.getWorld().isClient || !ContainerLoggerManager.INSTANCE.isEnabled()) {
            return;
        }

        final ScreenHandler handler = (ScreenHandler)(Object)this;
        final ScreenHandlerType<?> type = handler.getType();

        // 9x3 または 9x6 のインベントリを持つブロックに限定
        if (type != ScreenHandlerType.GENERIC_9X3 && type != ScreenHandlerType.GENERIC_9X6) {
            return;
        }

        final List<ItemStack> items = handler.slots.stream()
                .map(Slot::getStack)
                .filter(itemStack -> !itemStack.isEmpty())
                .toList();
        ContainerLoggerManager.INSTANCE.log(items);
    }
}
