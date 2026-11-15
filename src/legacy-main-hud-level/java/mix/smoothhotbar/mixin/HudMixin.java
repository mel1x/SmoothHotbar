package mix.smoothhotbar.mixin;

import mix.smoothhotbar.LegacyView;
import mix.smoothhotbar.SmoothHotbar;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudMixin {
    @Inject(method = {"renderMainHud", "renderExperienceLevel"}, at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$beginGroup(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (SmoothHotbar.begin(LegacyView.of(context))) {
            ci.cancel();
        }
    }

    @Inject(method = {"renderMainHud", "renderExperienceLevel"}, at = @At("RETURN"))
    private void smoothhotbar$endGroup(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        SmoothHotbar.end(LegacyView.of(context));
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$hotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (SmoothHotbar.hotbar(LegacyView.of(context))) {
            ci.cancel();
        }
    }
}
