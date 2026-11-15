package mix.smoothhotbar;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

public final class LegacyView implements HotbarView {
    private static final LegacyView INSTANCE = new LegacyView();

    private DrawContext context;
    private MinecraftClient client;
    private PlayerEntity player;

    private LegacyView() {
    }

    public static LegacyView of(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity camera = client.getCameraEntity();
        INSTANCE.context = context;
        INSTANCE.client = client;
        INSTANCE.player = camera instanceof PlayerEntity ? (PlayerEntity) camera : client.player;
        return INSTANCE;
    }

    private ItemStack stack(int slot) {
        return slot == OFFHAND ? player.getOffHandStack() : player.getInventory().getStack(slot);
    }

    @Override
    public boolean ready() {
        return player != null && client.player != null;
    }

    @Override
    public int screenWidth() {
        return context.getScaledWindowWidth();
    }

    @Override
    public int screenHeight() {
        return context.getScaledWindowHeight();
    }

    @Override
    public void push() {
        MatrixOps.push(context);
    }

    @Override
    public void pop() {
        MatrixOps.pop(context);
    }

    @Override
    public void translate(float x, float y) {
        MatrixOps.translate(context, x, y);
    }

    @Override
    public void scale(float x, float y) {
        MatrixOps.scale(context, x, y);
    }

    @Override
    public void raise() {
        MatrixOps.raise(context);
    }

    @Override
    public void clip(int x1, int y1, int x2, int y2) {
        MatrixOps.clip(context, x1, y1, x2, y2);
    }

    @Override
    public void unclip() {
        context.disableScissor();
    }

    @Override
    public void sprite(HudSprite sprite, int x, int y) {
        Sprites.draw(context, sprite, x, y);
    }

    @Override
    public void attackProgress(int x, int y, int pixels) {
        if (pixels > 0) {
            Sprites.attackProgress(context, x, y, pixels);
        }
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, int argb) {
        context.fill(x1, y1, x2, y2, argb);
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
        return stack(slot).getBobbingAnimationTime();
    }

    @Override
    public void drawItem(int slot, int x, int y, int seed) {
        context.drawItem(player, stack(slot), x, y, seed);
    }

    @Override
    public float cooldown(int slot) {
        ItemStack stack = stack(slot);
        return stack.isEmpty() ? 0f : ItemOverlay.cooldown(client.player, stack);
    }

    @Override
    public void drawOverlay(int slot, int x, int y, boolean cooldown) {
        SmoothHotbar.suppressCooldown = !cooldown;
        try {
            ItemOverlay.draw(context, client.textRenderer, stack(slot), x, y);
        } finally {
            SmoothHotbar.suppressCooldown = false;
        }
    }

    @Override
    public int selectedSlot() {
        return SlotAccess.selected(player.getInventory());
    }

    @Override
    public boolean offhandOnLeft() {
        return player.getMainArm() != Arm.LEFT;
    }

    @Override
    public float attackCharge() {
        if (client.options.getAttackIndicator().getValue() != AttackIndicator.HOTBAR) {
            return -1f;
        }
        return client.player.getAttackCooldownProgress(0f);
    }

    @Override
    public boolean busy() {
        return client.player.isUsingItem() || client.player.getAttackCooldownProgress(0f) < 1f;
    }

    @Override
    public float health() {
        return player.getHealth();
    }

    @Override
    public boolean screenOpen() {
        return !client.mouse.isCursorLocked();
    }

    @Override
    public boolean spectator() {
        return player.isSpectator();
    }
}
