package mix.smoothhotbar.mixin;

import mix.smoothhotbar.SmoothHotbar;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemCooldowns.class)
public class CooldownMixin {
    @Inject(method = "getCooldownPercent", at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$hideVanillaShade(ItemStack stack, float partialTick, CallbackInfoReturnable<Float> cir) {
        if (SmoothHotbar.suppressCooldown) {
            cir.setReturnValue(0f);
        }
    }
}
