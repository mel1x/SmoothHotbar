package mix.smoothhotbar.mixin;

import mix.smoothhotbar.ModernView;
import mix.smoothhotbar.SmoothHotbar;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HudMixin {
    @Inject(method = "extractHotbarAndDecorations", at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$beginGroup(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (SmoothHotbar.begin(ModernView.of(graphics))) {
            ci.cancel();
        }
    }

    @Inject(method = "extractHotbarAndDecorations", at = @At("RETURN"))
    private void smoothhotbar$endGroup(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        SmoothHotbar.end(ModernView.of(graphics));
    }

    @Inject(method = "extractItemHotbar", at = @At("HEAD"), cancellable = true)
    private void smoothhotbar$hotbar(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (SmoothHotbar.hotbar(ModernView.of(graphics))) {
            ci.cancel();
        }
    }
}
