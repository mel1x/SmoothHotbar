package mix.smoothhotbar;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

final class Sprites {
    private static final Identifier[] IDS = new Identifier[HudSprite.values().length];

    static {
        for (HudSprite sprite : HudSprite.values()) {
            IDS[sprite.ordinal()] = Identifier.ofVanilla(sprite.path());
        }
    }

    private Sprites() {
    }

    static void draw(DrawContext context, HudSprite sprite, int x, int y) {
        context.drawGuiTexture(IDS[sprite.ordinal()], x, y, sprite.width(), sprite.height());
    }

    static void attackProgress(DrawContext context, int x, int y, int pixels) {
        context.drawGuiTexture(IDS[HudSprite.ATTACK_PROGRESS.ordinal()], 18, 18, 0, 18 - pixels, x, y + 18 - pixels, 18, pixels);
    }
}
