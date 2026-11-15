package mix.smoothhotbar;

import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ModernView implements HotbarView {
    private static final ModernView INSTANCE = new ModernView();

    private static final Identifier[] SPRITES = new Identifier[HudSprite.values().length];

    static {
        for (HudSprite sprite : HudSprite.values()) {
            SPRITES[sprite.ordinal()] = Identifier.withDefaultNamespace(sprite.path());
        }
    }

    private GuiGraphicsExtractor graphics;
    private Minecraft minecraft;
    private Player player;

    private ModernView() {
    }

    public static ModernView of(GuiGraphicsExtractor graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity camera = minecraft.getCameraEntity();
        INSTANCE.graphics = graphics;
        INSTANCE.minecraft = minecraft;
        INSTANCE.player = camera instanceof Player ? (Player) camera : minecraft.player;
        return INSTANCE;
    }

    private ItemStack stack(int slot) {
        return slot == OFFHAND ? player.getOffhandItem() : player.getInventory().getItem(slot);
    }

    @Override
    public boolean ready() {
        return player != null && minecraft.player != null;
    }

    @Override
    public int screenWidth() {
        return graphics.guiWidth();
    }

    @Override
    public int screenHeight() {
        return graphics.guiHeight();
    }

    @Override
    public void push() {
        graphics.pose().pushMatrix();
    }

    @Override
    public void pop() {
        graphics.pose().popMatrix();
    }

    @Override
    public void translate(float x, float y) {
        graphics.pose().translate(x, y);
    }

    @Override
    public void scale(float x, float y) {
        graphics.pose().scale(x, y);
    }

    @Override
    public void raise() {
    }

    @Override
    public void clip(int x1, int y1, int x2, int y2) {
        graphics.enableScissor(x1, y1, x2, y2);
    }

    @Override
    public void unclip() {
        graphics.disableScissor();
    }

    @Override
    public void sprite(HudSprite sprite, int x, int y) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES[sprite.ordinal()], x, y, sprite.width(), sprite.height());
    }

    @Override
    public void attackProgress(int x, int y, int pixels) {
        if (pixels > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES[HudSprite.ATTACK_PROGRESS.ordinal()],
                    18, 18, 0, 18 - pixels, x, y + 18 - pixels, 18, pixels);
        }
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, int argb) {
        graphics.fill(x1, y1, x2, y2, argb);
    }

    @Override
    public boolean hasItem(int slot) {
        return !stack(slot).isEmpty();
    }

    @Override
    public int itemKey(int slot) {
        ItemStack stack = stack(slot);
        return stack.isEmpty() ? 0 : 31 * System.identityHashCode(stack.getItem()) + stack.getCount();
    }

    @Override
    public int bobTime(int slot) {
        return stack(slot).getPopTime();
    }

    @Override
    public void drawItem(int slot, int x, int y, int seed) {
        graphics.item(player, stack(slot), x, y, seed);
    }

    @Override
    public float cooldown(int slot) {
        ItemStack stack = stack(slot);
        return stack.isEmpty() ? 0f : minecraft.player.getCooldowns().getCooldownPercent(stack, 0f);
    }

    @Override
    public void drawOverlay(int slot, int x, int y, boolean cooldown) {
        SmoothHotbar.suppressCooldown = !cooldown;
        try {
            graphics.itemDecorations(minecraft.font, stack(slot), x, y);
        } finally {
            SmoothHotbar.suppressCooldown = false;
        }
    }

    @Override
    public int selectedSlot() {
        return player.getInventory().getSelectedSlot();
    }

    @Override
    public boolean offhandOnLeft() {
        return player.getMainArm() != HumanoidArm.LEFT;
    }

    @Override
    public float attackCharge() {
        if (minecraft.options.attackIndicator().get() != AttackIndicatorStatus.HOTBAR) {
            return -1f;
        }
        return minecraft.player.getAttackStrengthScale(0f);
    }

    @Override
    public boolean busy() {
        return minecraft.player.isUsingItem() || minecraft.player.getAttackStrengthScale(0f) < 1f;
    }

    @Override
    public float health() {
        return player.getHealth();
    }

    @Override
    public boolean screenOpen() {
        return !minecraft.mouseHandler.isMouseGrabbed();
    }

    @Override
    public boolean spectator() {
        return player.isSpectator();
    }
}
