package folk.sisby.portable_crafting.screens;

import folk.sisby.portable_crafting.compat.inventory_tabs.PortableCraftingScreenTabCompat;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.text.Text;

import static folk.sisby.portable_crafting.screens.PortableCraftingScreenHandler.PORTABLE_CRAFTING_HANDLER_TYPE;

public class PortableCraftingScreen extends CraftingScreen {
	static {
		HandledScreens.register(PORTABLE_CRAFTING_HANDLER_TYPE, PortableCraftingScreen::new);
	}

	public static boolean TABS_COMPAT = false;

	public PortableCraftingScreen(CraftingScreenHandler craftingScreenHandler, PlayerInventory playerInventory, Text text) {
		super(craftingScreenHandler, playerInventory, text);
	}

	public static void touch() {
	}

	@Override
	protected void init() {
		super.init();
		if (TABS_COMPAT) PortableCraftingScreenTabCompat.screenInit();
	}
}
