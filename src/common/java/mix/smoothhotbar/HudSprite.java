package mix.smoothhotbar;

public enum HudSprite {
    HOTBAR("hud/hotbar", 182, 22),
    SELECTION("hud/hotbar_selection", 24, 23),
    OFFHAND_LEFT("hud/hotbar_offhand_left", 29, 24),
    OFFHAND_RIGHT("hud/hotbar_offhand_right", 29, 24),
    ATTACK_BACKGROUND("hud/hotbar_attack_indicator_background", 18, 18),
    ATTACK_PROGRESS("hud/hotbar_attack_indicator_progress", 18, 18);

    private final String path;
    private final int width;
    private final int height;

    HudSprite(String path, int width, int height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }

    public String path() {
        return path;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
