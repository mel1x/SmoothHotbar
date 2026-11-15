package mix.smoothhotbar;

import net.minecraft.network.chat.Component;

public final class Lang {
    private Lang() {
    }

    public static Component text(String key) {
        return Component.translatable(key);
    }
}
