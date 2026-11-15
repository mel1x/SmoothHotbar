package mix.smoothhotbar;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

final class ItemOverlay {
    private ItemOverlay() {
    }

    static float cooldown(PlayerEntity player, ItemStack stack) {
        return player.getItemCooldownManager().getCooldownProgress(stack, 0f);
    }

    static void draw(DrawContext context, TextRenderer textRenderer, ItemStack stack, int x, int y) {
        context.drawStackOverlay(textRenderer, stack, x, y);
    }
}
