package folk.sisby.portable_crafting.screens;

import folk.sisby.portable_crafting.PortableCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

public class PortableCraftingScreenHandler extends CraftingScreenHandler {
	public static final ScreenHandlerType<PortableCraftingScreenHandler> PORTABLE_CRAFTING_HANDLER_TYPE = Registry.register(Registry.SCREEN_HANDLER, PortableCrafting.ID, new ScreenHandlerType<>(PortableCraftingScreenHandler::new));
	public static final Text LABEL = Text.translatable("container.portable_crafting.portable_crafting_table");


	public PortableCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(syncId, playerInventory);
	}

	public PortableCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(syncId, playerInventory, context);
		this.type = PORTABLE_CRAFTING_HANDLER_TYPE;
	}

	public static void touch() {
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return PortableCrafting.is_allowed(player);
	}
}
