package mix.smoothhotbar.mixin;

import mix.smoothhotbar.SmoothHotbar;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemCooldownManager.class)
public class CooldownMixin {
    @Inject(method = "getCooldownProgress", at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$hideVanillaShade(Item item, float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (SmoothHotbar.suppressCooldown) {
            cir.setReturnValue(0f);
        }
    }
}
