package mix.smoothhotbar.mixin;

import mix.smoothhotbar.LegacyView;
import mix.smoothhotbar.SmoothHotbar;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.JumpingMount;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudMixin {
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$hotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        LegacyView view = LegacyView.of(context);
        if (!SmoothHotbar.active(view)) {
            return;
        }
        if (!SmoothHotbar.begin(view)) {
            SmoothHotbar.hotbar(view);
            SmoothHotbar.end(view);
        }
        ci.cancel();
    }

    @Inject(method = {"renderStatusBars", "renderMountHealth", "renderHeldItemTooltip"},
            at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$beginPart(DrawContext context, CallbackInfo ci) {
        if (SmoothHotbar.begin(LegacyView.of(context))) {
            ci.cancel();
        }
    }

    @Inject(method = {"renderStatusBars", "renderMountHealth", "renderHeldItemTooltip"}, at = @At("RETURN"))
    private void smoothhotbar$endPart(DrawContext context, CallbackInfo ci) {
        SmoothHotbar.end(LegacyView.of(context));
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$beginExperience(DrawContext context, int x, CallbackInfo ci) {
        if (SmoothHotbar.begin(LegacyView.of(context))) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceBar", at = @At("RETURN"))
    private void smoothhotbar$endExperience(DrawContext context, int x, CallbackInfo ci) {
        SmoothHotbar.end(LegacyView.of(context));
    }

    @Inject(method = "renderMountJumpBar", at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$beginJumpBar(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
        if (SmoothHotbar.begin(LegacyView.of(context))) {
            ci.cancel();
        }
    }

    @Inject(method = "renderMountJumpBar", at = @At("RETURN"))
    private void smoothhotbar$endJumpBar(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
        SmoothHotbar.end(LegacyView.of(context));
    }
}
