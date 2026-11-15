package mix.smoothhotbar.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public final class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        FabricLoader loader = FabricLoader.getInstance();
        if (!loader.isModLoaded("cloth-config") && !loader.isModLoaded("cloth-config2")) {
            return parent -> null;
        }
        return ConfigScreen.factory();
    }
}
