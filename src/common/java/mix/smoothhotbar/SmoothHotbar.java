package mix.smoothhotbar;

public final class SmoothHotbar {
    private static final float HUD_HIDE_DISTANCE = 64f;
    private static final float HOTBAR_HIDE_DISTANCE = 26f;

    public static boolean suppressCooldown;

    private static int pushed;

    private SmoothHotbar() {
    }

    public static boolean active(HotbarView view) {
        return SmoothHotbarConfig.INSTANCE.enabled && view.ready();
    }

    public static boolean begin(HotbarView view) {
        if (!active(view)) {
            return false;
        }
        SmoothHotbarConfig cfg = SmoothHotbarConfig.INSTANCE;
        HotbarState.update(view);
        if (cfg.hideWholeHud && HotbarState.fullyHidden()) {
            return true;
        }

        float scale = cfg.scalePercent / 100f;
        float centerX = view.screenWidth() / 2f;
        float bottom = view.screenHeight();
        float slide = cfg.hideWholeHud ? HotbarState.hidden() * (HUD_HIDE_DISTANCE + cfg.bottomOffset) : 0f;

        view.push();
        pushed++;
        view.translate(0f, slide - cfg.bottomOffset);
        if (scale != 1f) {
            view.translate(centerX, bottom);
            view.scale(scale, scale);
            view.translate(-centerX, -bottom);
        }
        return false;
    }

    public static void end(HotbarView view) {
        if (pushed > 0) {
            pushed--;
            view.pop();
        }
    }

    public static boolean hotbar(HotbarView view) {
        if (!active(view)) {
            return false;
        }
        SmoothHotbarConfig cfg = SmoothHotbarConfig.INSTANCE;
        if (cfg.hideWholeHud) {
            HotbarRenderer.render(view);
            return true;
        }
        if (HotbarState.fullyHidden()) {
            return true;
        }

        float hidden = HotbarState.hidden();
        if (hidden > 0f) {
            float scale = cfg.scalePercent / 100f;
            view.push();
            view.translate(0f, hidden * (HOTBAR_HIDE_DISTANCE + cfg.bottomOffset / scale));
            HotbarRenderer.render(view);
            view.pop();
        } else {
            HotbarRenderer.render(view);
        }
        return true;
    }
}
