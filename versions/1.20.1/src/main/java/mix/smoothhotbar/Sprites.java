package mix.smoothhotbar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

final class Sprites {
    private static final Identifier WIDGETS = new Identifier("textures/gui/widgets.png");
    private static final Identifier ICONS = new Identifier("textures/gui/icons.png");

    private Sprites() {
    }

    static void draw(DrawContext context, HudSprite sprite, int x, int y) {
        RenderSystem.enableBlend();
        switch (sprite) {
            case HOTBAR -> context.drawTexture(WIDGETS, x, y, 0, 0, 182, 22);
            case SELECTION -> context.drawTexture(WIDGETS, x, y, 0, 22, 24, 22);
            case OFFHAND_LEFT -> context.drawTexture(WIDGETS, x, y, 24, 22, 29, 24);
            case OFFHAND_RIGHT -> context.drawTexture(WIDGETS, x, y, 53, 22, 29, 24);
            case ATTACK_BACKGROUND -> context.drawTexture(ICONS, x, y, 0, 94, 18, 18);
            case ATTACK_PROGRESS -> context.drawTexture(ICONS, x, y, 18, 94, 18, 18);
        }
    }

    static void attackProgress(DrawContext context, int x, int y, int pixels) {
        RenderSystem.enableBlend();
        context.drawTexture(ICONS, x, y + 18 - pixels, 18, 112 - pixels, 18, pixels);
    }
}
