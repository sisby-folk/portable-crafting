package folk.sisby.portable_crafting_standalone.screens;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class PortableCraftingScreenHandler extends CraftingScreenHandler {
	public static final ScreenHandlerType<PortableCraftingScreenHandler> PORTABLE_CRAFTING_HANDLER_TYPE = Registry.register(Registry.SCREEN_HANDLER, "portable_crafting", new ScreenHandlerType<>(PortableCraftingScreenHandler::new));


	public PortableCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(syncId, playerInventory);
	}

	public PortableCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(syncId, playerInventory, context);
		this.type = PORTABLE_CRAFTING_HANDLER_TYPE;
	}

	public static void touch() {
		HandledScreens.register(PORTABLE_CRAFTING_HANDLER_TYPE, PortableCraftingScreen::new);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return player.getInventory().contains(new ItemStack(Blocks.CRAFTING_TABLE));
	}
}
