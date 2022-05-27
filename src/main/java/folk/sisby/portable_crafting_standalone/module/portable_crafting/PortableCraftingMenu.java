package folk.sisby.portable_crafting_standalone.module.portable_crafting;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;

public class PortableCraftingMenu extends CraftingMenu {
    public PortableCraftingMenu(int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(syncId, playerInventory, context);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
