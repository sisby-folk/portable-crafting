package folk.sisby.portable_crafting_standalone.module.portable_crafting;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.CraftingScreenHandler;

public class PortableCraftingScreenHandler extends CraftingScreenHandler {

    public PortableCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getInventory().contains(new ItemStack(Blocks.CRAFTING_TABLE));
    }
}
