package mix.smoothhotbar;

final class HotbarRenderer {
    private static final int WIDTH = 182;
    private static final int HEIGHT = 22;
    private static final int SLOT = 20;
    private static final int COOLDOWN_COLOR = 0x7FFFFFFF;

    private HotbarRenderer() {
    }

    static void render(HotbarView view) {
        SmoothHotbarConfig cfg = SmoothHotbarConfig.INSTANCE;
        boolean vanilla = cfg.design == Design.VANILLA;
        int x0 = view.screenWidth() / 2 - WIDTH / 2;
        int y0 = view.screenHeight() - HEIGHT;

        boolean offhand = view.hasItem(HotbarView.OFFHAND);
        boolean offhandLeft = view.offhandOnLeft();
        int offhandItemX = offhandLeft ? x0 - 26 : x0 + WIDTH + 10;

        if (vanilla) {
            view.sprite(HudSprite.HOTBAR, x0, y0);
            if (offhand) {
                if (offhandLeft) {
                    view.sprite(HudSprite.OFFHAND_LEFT, x0 - 29, y0 - 1);
                } else {
                    view.sprite(HudSprite.OFFHAND_RIGHT, x0 + WIDTH, y0 - 1);
                }
            }
        } else {
            int radius = cfg.cornerRadius;
            int slotRadius = Math.min(radius, 2);
            int background = Shapes.argb(cfg.backgroundOpacity * 255 / 100, cfg.backgroundColor);
            int slotColor = cfg.slotBackgrounds ? Shapes.argb(0x18, cfg.accentColor) : 0;

            Shapes.fill(view, x0, y0, WIDTH, HEIGHT, radius, background);
            for (int slot = 0; slot < 9; slot++) {
                Shapes.fill(view, x0 + 2 + slot * SLOT, y0 + 2, 18, 18, slotRadius, slotColor);
            }
            if (offhand) {
                Shapes.fill(view, offhandItemX - 3, y0, HEIGHT, HEIGHT, radius, background);
                Shapes.fill(view, offhandItemX - 1, y0 + 2, 18, 18, slotRadius, slotColor);
            }
        }

        float selection = HotbarState.selection();
        if (HotbarState.wrapping()) {
            int edge = vanilla ? 1 : 0;
            view.clip(x0 - edge, y0 - 1, x0 + WIDTH + edge, y0 + HEIGHT + 1);
            drawSelection(view, cfg, vanilla, x0, y0, selection);
            drawSelection(view, cfg, vanilla, x0, y0, selection < 4f ? selection + 9f : selection - 9f);
            view.unclip();
        } else {
            drawSelection(view, cfg, vanilla, x0, y0, selection);
        }

        float grow = cfg.selectedScalePercent / 100f - 1f;
        for (int slot = 0; slot < 9; slot++) {
            drawSlot(view, cfg, slot, x0 + 3 + slot * SLOT, y0 + 3, slot + 1, HotbarState.glow(slot), grow);
        }
        if (offhand) {
            drawSlot(view, cfg, HotbarView.OFFHAND, offhandItemX, y0 + 3, 10, 0f, 0f);
        }

        float charge = view.attackCharge();
        if (charge >= 0f && charge < 1f) {
            if (vanilla) {
                int gaugeX = offhandLeft ? x0 + WIDTH + 6 : x0 - 22;
                view.sprite(HudSprite.ATTACK_BACKGROUND, gaugeX, y0 + 2);
                view.attackProgress(gaugeX, y0 + 2, Math.min(18, (int) (charge * 19f)));
            } else {
                int gaugeX = offhandLeft ? x0 + WIDTH + 4 : x0 - 10;
                int filled = Math.round(charge * 18f);
                int background = Shapes.argb(cfg.backgroundOpacity * 255 / 100, cfg.backgroundColor);
                Shapes.fill(view, gaugeX, y0, 6, HEIGHT, Math.min(cfg.cornerRadius, 2), background);
                view.fill(gaugeX + 2, y0 + 2, gaugeX + 4, y0 + 20, Shapes.argb(0x30, cfg.accentColor));
                view.fill(gaugeX + 2, y0 + 20 - filled, gaugeX + 4, y0 + 20, Shapes.argb(0xE6, cfg.accentColor));
            }
        }
    }

    private static void drawSelection(HotbarView view, SmoothHotbarConfig cfg, boolean vanilla,
                                      int x0, int y0, float position) {
        view.push();
        view.translate(position * SLOT, 0f);
        if (vanilla) {
            view.sprite(HudSprite.SELECTION, x0 - 1, y0 - 1);
        } else {
            int radius = Math.min(cfg.cornerRadius, 3);
            Shapes.fill(view, x0 + 2, y0 + 2, 18, 18, Math.max(0, radius - 1), Shapes.argb(0x38, cfg.accentColor));
            Shapes.ring(view, x0 + 1, y0 + 1, SLOT, SLOT, radius, Shapes.argb(0xE6, cfg.accentColor));
        }
        view.pop();
    }

    private static void drawSlot(HotbarView view, SmoothHotbarConfig cfg, int slot, int x, int y, int seed,
                                 float glow, float grow) {
        if (!view.hasItem(slot)) {
            return;
        }
        boolean scaled = glow != 0f && grow > 0f;
        if (scaled) {
            float scale = 1f + grow * glow;
            view.push();
            view.translate(x + 8f, y + 8f - glow);
            view.scale(scale, scale);
            view.translate(-(x + 8f), -(y + 8f));
        }

        float bob = HotbarState.bob(slot);
        if (bob > 0f) {
            float squash = 1f + bob / 5f;
            view.push();
            view.translate(x + 8f, y + 12f);
            view.scale(1f / squash, (squash + 1f) / 2f);
            view.translate(-(x + 8f), -(y + 12f));
            view.drawItem(slot, x, y, seed);
            view.pop();
        } else {
            view.drawItem(slot, x, y, seed);
        }

        view.drawOverlay(slot, x, y, !cfg.smoothCooldowns);
        if (cfg.smoothCooldowns) {
            float cooldown = HotbarState.cooldown(slot);
            if (cooldown > 0f) {
                view.push();
                view.raise();
                view.translate(x, y + 16f * (1f - cooldown));
                view.scale(1f, cooldown);
                view.fill(0, 0, 16, 16, COOLDOWN_COLOR);
                view.pop();
            }
        }

        if (scaled) {
            view.pop();
        }
    }
}
