package folk.sisby.portable_crafting.screens;

import folk.sisby.portable_crafting.compat.inventory_tabs.PortableCraftingScreenTabCompat;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.text.Text;

public class PortableCraftingScreen extends CraftingScreen {
	public static final Text LABEL = Text.translatable("container.portable_crafting.portable_crafting_table");

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
