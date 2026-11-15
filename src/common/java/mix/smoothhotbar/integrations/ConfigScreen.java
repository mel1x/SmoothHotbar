package mix.smoothhotbar.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import mix.smoothhotbar.Design;
import mix.smoothhotbar.Easing;
import mix.smoothhotbar.SmoothHotbarConfig;
import mix.smoothhotbar.Lang;

import java.util.Locale;
import java.util.function.Consumer;

final class ConfigScreen {
    private static final String OPTION = "smoothhotbar.option.";

    private ConfigScreen() {
    }

    static ConfigScreenFactory<?> factory() {
        return parent -> {
            SmoothHotbarConfig cfg = SmoothHotbarConfig.INSTANCE;
            SmoothHotbarConfig def = new SmoothHotbarConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Lang.text("smoothhotbar.config.title"))
                .setSavingRunnable(cfg::save);
            ConfigEntryBuilder eb = builder.entryBuilder();

            ConfigCategory general = builder.getOrCreateCategory(Lang.text("smoothhotbar.config.category.general"));
            general.addEntry(toggle(eb, "enabled", cfg.enabled, def.enabled, v -> cfg.enabled = v));
            general.addEntry(eb.startEnumSelector(Lang.text(OPTION + "design"), Design.class, cfg.design)
                .setDefaultValue(def.design)
                .setEnumNameProvider(value -> Lang.text("smoothhotbar.design." + value.name().toLowerCase(Locale.ROOT)))
                .setTooltip(Lang.text(OPTION + "design.tooltip"))
                .setSaveConsumer(v -> cfg.design = v)
                .build());
            general.addEntry(slider(eb, "bottom_offset", cfg.bottomOffset, def.bottomOffset, 0, 40, v -> cfg.bottomOffset = v));
            general.addEntry(slider(eb, "scale", cfg.scalePercent, def.scalePercent, 50, 150, v -> cfg.scalePercent = v));

            ConfigCategory custom = builder.getOrCreateCategory(Lang.text("smoothhotbar.config.category.custom"));
            custom.addEntry(slider(eb, "corner_radius", cfg.cornerRadius, def.cornerRadius, 0, 4, v -> cfg.cornerRadius = v));
            custom.addEntry(slider(eb, "background_opacity", cfg.backgroundOpacity, def.backgroundOpacity, 0, 100, v -> cfg.backgroundOpacity = v));
            custom.addEntry(color(eb, "background_color", cfg.backgroundColor, def.backgroundColor, v -> cfg.backgroundColor = v));
            custom.addEntry(toggle(eb, "slot_backgrounds", cfg.slotBackgrounds, def.slotBackgrounds, v -> cfg.slotBackgrounds = v));
            custom.addEntry(color(eb, "accent_color", cfg.accentColor, def.accentColor, v -> cfg.accentColor = v));

            ConfigCategory animation = builder.getOrCreateCategory(Lang.text("smoothhotbar.config.category.animation"));
            animation.addEntry(eb.startEnumSelector(Lang.text(OPTION + "easing"), Easing.class, cfg.easing)
                .setDefaultValue(def.easing)
                .setEnumNameProvider(value -> Lang.text("smoothhotbar.easing." + value.name().toLowerCase(Locale.ROOT)))
                .setTooltip(Lang.text(OPTION + "easing.tooltip"))
                .setSaveConsumer(v -> cfg.easing = v)
                .build());
            animation.addEntry(slider(eb, "selection_ms", cfg.selectionMs, def.selectionMs, 0, 500, v -> cfg.selectionMs = v));
            animation.addEntry(slider(eb, "selected_scale", cfg.selectedScalePercent, def.selectedScalePercent, 100, 150, v -> cfg.selectedScalePercent = v));
            animation.addEntry(toggle(eb, "wrap_portal", cfg.wrapPortal, def.wrapPortal, v -> cfg.wrapPortal = v));
            animation.addEntry(toggle(eb, "smooth_cooldowns", cfg.smoothCooldowns, def.smoothCooldowns, v -> cfg.smoothCooldowns = v));

            ConfigCategory hide = builder.getOrCreateCategory(Lang.text("smoothhotbar.config.category.auto_hide"));
            hide.addEntry(toggle(eb, "auto_hide", cfg.autoHide, def.autoHide, v -> cfg.autoHide = v));
            hide.addEntry(slider(eb, "hide_delay", cfg.hideDelaySeconds, def.hideDelaySeconds, 1, 30, v -> cfg.hideDelaySeconds = v));
            hide.addEntry(slider(eb, "hide_anim_ms", cfg.hideAnimMs, def.hideAnimMs, 0, 1000, v -> cfg.hideAnimMs = v));
            hide.addEntry(toggle(eb, "hide_whole_hud", cfg.hideWholeHud, def.hideWholeHud, v -> cfg.hideWholeHud = v));
            hide.addEntry(toggle(eb, "show_on_damage", cfg.showOnDamage, def.showOnDamage, v -> cfg.showOnDamage = v));
            hide.addEntry(toggle(eb, "show_on_use", cfg.showOnUse, def.showOnUse, v -> cfg.showOnUse = v));

            return builder.build();
        };
    }

    private static AbstractConfigListEntry<?> toggle(ConfigEntryBuilder eb, String key, boolean value, boolean def,
                                                     Consumer<Boolean> save) {
        return eb.startBooleanToggle(Lang.text(OPTION + key), value)
            .setDefaultValue(def)
            .setTooltip(Lang.text(OPTION + key + ".tooltip"))
            .setSaveConsumer(save)
            .build();
    }

    private static AbstractConfigListEntry<?> slider(ConfigEntryBuilder eb, String key, int value, int def,
                                                     int min, int max, Consumer<Integer> save) {
        return eb.startIntSlider(Lang.text(OPTION + key), value, min, max)
            .setDefaultValue(def)
            .setTooltip(Lang.text(OPTION + key + ".tooltip"))
            .setSaveConsumer(save)
            .build();
    }

    private static AbstractConfigListEntry<?> color(ConfigEntryBuilder eb, String key, int value, int def,
                                                    Consumer<Integer> save) {
        return eb.startColorField(Lang.text(OPTION + key), value)
            .setDefaultValue(def)
            .setTooltip(Lang.text(OPTION + key + ".tooltip"))
            .setSaveConsumer(save)
            .build();
    }
}
