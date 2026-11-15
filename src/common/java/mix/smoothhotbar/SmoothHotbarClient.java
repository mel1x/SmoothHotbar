package mix.smoothhotbar;

import net.fabricmc.api.ClientModInitializer;

public final class SmoothHotbarClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SmoothHotbarConfig.INSTANCE.load();
    }
}
