package folk.sisby.portable_crafting_standalone.screens;

import folk.sisby.portable_crafting_standalone.compat.inventory_tabs.PortableCraftingScreenTabCompat;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.text.Text;

public class PortableCraftingScreen extends CraftingScreen {
	public static boolean TABS_COMPAT = false;

	public PortableCraftingScreen(CraftingScreenHandler craftingScreenHandler, PlayerInventory playerInventory, Text text) {
		super(craftingScreenHandler, playerInventory, text);
	}

	@Override
	protected void init() {
		super.init();
		if (TABS_COMPAT) PortableCraftingScreenTabCompat.screenInit();
	}
}
