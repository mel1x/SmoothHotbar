package mix.smoothhotbar;

import net.minecraft.entity.player.PlayerInventory;

final class SlotAccess {
    private SlotAccess() {
    }

    static int selected(PlayerInventory inventory) {
        return inventory.getSelectedSlot();
    }
}
