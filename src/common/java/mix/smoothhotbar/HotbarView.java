package mix.smoothhotbar;

public interface HotbarView {
    int OFFHAND = 9;
    int SLOT_COUNT = 10;


    boolean ready();

    int screenWidth();

    int screenHeight();

    void push();

    void pop();

    void translate(float x, float y);

    void scale(float x, float y);

    void raise();

    void clip(int x1, int y1, int x2, int y2);

    void unclip();

    void fill(int x1, int y1, int x2, int y2, int argb);

    void sprite(HudSprite sprite, int x, int y);

    void attackProgress(int x, int y, int pixels);

    boolean hasItem(int slot);

    int itemKey(int slot);

    int bobTime(int slot);

    float cooldown(int slot);

    void drawItem(int slot, int x, int y, int seed);

    void drawOverlay(int slot, int x, int y, boolean cooldown);

    int selectedSlot();

    boolean offhandOnLeft();

    float attackCharge();

    boolean busy();

    float health();

    boolean screenOpen();

    boolean spectator();
}
