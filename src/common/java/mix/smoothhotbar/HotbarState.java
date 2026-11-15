package mix.smoothhotbar;

public final class HotbarState {
    private static final long MIN_STEP_NANOS = 200_000L;
    private static final long TICK_NANOS = 50_000_000L;

    private static long now;
    private static long lastUpdate;
    private static long lastActivity;

    private static float selectionFrom;
    private static int selectionTo = -1;
    private static long selectionStart;
    private static float selection;
    private static boolean wrapping;

    private static final float[] glowFrom = new float[9];
    private static final float[] glowTo = new float[9];
    private static final long[] glowStart = new long[9];
    private static final float[] glow = new float[9];

    private static float visibility = 1f;

    private static int prevSelected = -1;
    private static float prevHealth = -1f;
    private static final int[] prevKeys = new int[HotbarView.SLOT_COUNT];
    private static final int[] bobSeen = new int[HotbarView.SLOT_COUNT];
    private static final long[] bobAt = new long[HotbarView.SLOT_COUNT];

    private static final float[] cooldownSeen = new float[HotbarView.SLOT_COUNT];
    private static final float[] cooldownStep = new float[HotbarView.SLOT_COUNT];
    private static final long[] cooldownAt = new long[HotbarView.SLOT_COUNT];

    private HotbarState() {
    }

    public static void update(HotbarView view) {
        SmoothHotbarConfig cfg = SmoothHotbarConfig.INSTANCE;
        long time = System.nanoTime();
        boolean first = lastUpdate == 0L;
        if (!first && time - lastUpdate < MIN_STEP_NANOS) {
            return;
        }
        float dt = first ? 0f : Math.min((time - lastUpdate) / 1.0e9f, 0.1f);
        lastUpdate = time;
        now = time;

        int selected = Math.max(0, Math.min(8, view.selectedSlot()));
        boolean activity = first || selected != prevSelected || view.screenOpen();

        updateSelection(cfg, selected, first);
        prevSelected = selected;

        for (int slot = 0; slot < HotbarView.SLOT_COUNT; slot++) {
            int key = view.itemKey(slot);
            if (key != prevKeys[slot]) {
                prevKeys[slot] = key;
                activity = true;
            }
            int bob = view.bobTime(slot);
            if (bob != bobSeen[slot]) {
                bobSeen[slot] = bob;
                bobAt[slot] = time;
            }
            float cooldown = view.cooldown(slot);
            if (cooldown != cooldownSeen[slot]) {
                float previous = cooldownSeen[slot];
                cooldownStep[slot] = previous > cooldown ? previous - cooldown : 0f;
                cooldownSeen[slot] = cooldown;
                cooldownAt[slot] = time;
            }
        }

        float health = view.health();
        if (cfg.showOnDamage && prevHealth >= 0f && health < prevHealth - 0.01f) {
            activity = true;
        }
        prevHealth = health;

        if (cfg.showOnUse && view.busy()) {
            activity = true;
        }
        if (activity) {
            lastActivity = time;
        }

        boolean visible = !cfg.autoHide || view.spectator()
                || time - lastActivity < cfg.hideDelaySeconds * 1_000_000_000L;
        float target = visible ? 1f : 0f;
        if (cfg.hideAnimMs <= 0 || first) {
            visibility = target;
        } else {
            float step = dt / (cfg.hideAnimMs / 1000f);
            visibility = visibility < target
                    ? Math.min(target, visibility + step)
                    : Math.max(target, visibility - step);
        }
    }

    private static void updateSelection(SmoothHotbarConfig cfg, int selected, boolean first) {
        float progress = progress(cfg, selectionStart);
        selection = selectionFrom + (selectionTo - selectionFrom) * cfg.easing.apply(progress);
        if (progress >= 1f) {
            wrapping = false;
        }

        if (first || selectionTo < 0 || cfg.selectionMs <= 0) {
            selectionFrom = selected;
            selectionTo = selected;
            selection = selected;
            wrapping = false;
        } else if (selected != selectionTo) {
            float current = selection;
            int delta = selected - selectionTo;
            boolean wrap = cfg.wrapPortal && Math.abs(delta) == 8;
            if (wrap) {
                current += delta < 0 ? -9f : 9f;
            } else if (wrapping) {
                if (current < -0.5f) {
                    current += 9f;
                } else if (current > 8.5f) {
                    current -= 9f;
                }
            }
            wrapping = wrap;
            selectionFrom = current;
            selectionTo = selected;
            selectionStart = now;
            selection = current;
        }

        for (int slot = 0; slot < 9; slot++) {
            float target = slot == selected ? 1f : 0f;
            float value = glowFrom[slot] + (glowTo[slot] - glowFrom[slot]) * cfg.easing.apply(progress(cfg, glowStart[slot]));
            if (first || cfg.selectionMs <= 0) {
                glowFrom[slot] = target;
                glowTo[slot] = target;
                value = target;
            } else if (target != glowTo[slot]) {
                glowFrom[slot] = Math.max(0f, Math.min(1f, value));
                glowTo[slot] = target;
                glowStart[slot] = now;
                value = glowFrom[slot];
            }
            glow[slot] = value;
        }
    }

    private static float progress(SmoothHotbarConfig cfg, long start) {
        if (cfg.selectionMs <= 0) {
            return 1f;
        }
        return Math.min(1f, (now - start) / (cfg.selectionMs * 1.0e6f));
    }

    public static float selection() {
        return selection;
    }

    public static boolean wrapping() {
        return wrapping;
    }

    public static float glow(int slot) {
        return glow[slot];
    }

    public static float hidden() {
        return 1f - visibility * visibility * (3f - 2f * visibility);
    }

    public static boolean fullyHidden() {
        return visibility <= 0f;
    }

    private static float tickFraction(long since) {
        return Math.min(1f, (System.nanoTime() - since) / (float) TICK_NANOS);
    }

    public static float bob(int slot) {
        int bob = bobSeen[slot];
        if (bob <= 0) {
            return 0f;
        }
        return Math.max(0f, bob - tickFraction(bobAt[slot]));
    }

    public static float cooldown(int slot) {
        float cooldown = cooldownSeen[slot];
        if (cooldown <= 0f) {
            return 0f;
        }
        return Math.max(0f, Math.min(1f, cooldown - cooldownStep[slot] * tickFraction(cooldownAt[slot])));
    }
}
