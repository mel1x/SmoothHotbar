package mix.smoothhotbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SmoothHotbarConfig {
    public static final SmoothHotbarConfig INSTANCE = new SmoothHotbarConfig();

    private static final Logger LOGGER = LoggerFactory.getLogger("smoothhotbar");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("smoothhotbar.json");

    public boolean enabled = true;
    public Design design = Design.VANILLA;
    public int bottomOffset = 0;
    public int scalePercent = 90;

    public int cornerRadius = 0;
    public int backgroundOpacity = 45;
    public int backgroundColor = 0x000000;
    public boolean slotBackgrounds = true;
    public int accentColor = 0xFFFFFF;

    public Easing easing = Easing.EASE_OUT;
    public int selectionMs = 100;
    public int selectedScalePercent = 100;
    public boolean wrapPortal = true;
    public boolean smoothCooldowns = true;

    public boolean autoHide = false;
    public int hideDelaySeconds = 4;
    public int hideAnimMs = 300;
    public boolean hideWholeHud = true;
    public boolean showOnDamage = true;
    public boolean showOnUse = true;

    public void load() {
        if (!Files.exists(PATH)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(PATH)) {
            SmoothHotbarConfig loaded = GSON.fromJson(reader, SmoothHotbarConfig.class);
            if (loaded != null) {
                copyFrom(loaded);
            }
        } catch (Exception exception) {
            LOGGER.warn("Failed to load config from {}", PATH, exception);
        }
        clamp();
    }

    public void save() {
        clamp();
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException exception) {
            LOGGER.warn("Failed to save config to {}", PATH, exception);
        }
    }

    public void clamp() {
        if (design == null) {
            design = Design.VANILLA;
        }
        if (easing == null) {
            easing = Easing.EASE_OUT;
        }
        bottomOffset = clamp(bottomOffset, 0, 40);
        scalePercent = clamp(scalePercent, 50, 150);
        cornerRadius = clamp(cornerRadius, 0, 4);
        backgroundOpacity = clamp(backgroundOpacity, 0, 100);
        backgroundColor &= 0xFFFFFF;
        accentColor &= 0xFFFFFF;
        selectionMs = clamp(selectionMs, 0, 500);
        selectedScalePercent = clamp(selectedScalePercent, 100, 150);
        hideDelaySeconds = clamp(hideDelaySeconds, 1, 30);
        hideAnimMs = clamp(hideAnimMs, 0, 1000);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private void copyFrom(SmoothHotbarConfig other) {
        enabled = other.enabled;
        design = other.design;
        bottomOffset = other.bottomOffset;
        scalePercent = other.scalePercent;
        cornerRadius = other.cornerRadius;
        backgroundOpacity = other.backgroundOpacity;
        backgroundColor = other.backgroundColor;
        slotBackgrounds = other.slotBackgrounds;
        accentColor = other.accentColor;
        easing = other.easing;
        selectionMs = other.selectionMs;
        selectedScalePercent = other.selectedScalePercent;
        wrapPortal = other.wrapPortal;
        smoothCooldowns = other.smoothCooldowns;
        autoHide = other.autoHide;
        hideDelaySeconds = other.hideDelaySeconds;
        hideAnimMs = other.hideAnimMs;
        hideWholeHud = other.hideWholeHud;
        showOnDamage = other.showOnDamage;
        showOnUse = other.showOnUse;
    }
}
