package mix.smoothhotbar;

import net.minecraft.text.Text;

public final class Lang {
    private Lang() {
    }

    public static Text text(String key) {
        return Text.translatable(key);
    }
}
