package folk.sisby.portable_crafting.tabs;

import folk.sisby.inventory_tabs.TabManager;
import folk.sisby.inventory_tabs.TabProviders;
import folk.sisby.inventory_tabs.providers.UniqueItemTabProvider;
import folk.sisby.inventory_tabs.tabs.BlockTab;
import folk.sisby.inventory_tabs.tabs.ItemTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import folk.sisby.portable_crafting.PortableCrafting;
import folk.sisby.portable_crafting.PortableCraftingClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@SuppressWarnings("deprecation")
public class PortableCraftingTabProvider extends UniqueItemTabProvider {
	PortableCraftingTabProvider() {
		matches.put(new Identifier(PortableCrafting.ID, "crafting_tables"),
			e -> ClientPlayNetworking.canSend(PortableCrafting.C2S_OPEN_PORTABLE_CRAFTING) // Tag reload is per-server, so this works.
				&& e.getDefaultStack().isIn(PortableCrafting.CRAFTING_TABLES)
		);
	}

	@Override
	public Tab createTab(ItemStack stack, int slot) {
		return new PortableCraftingTab(stack, slot);
	}

	public static void register() {
		TabProviders.register(new Identifier(PortableCrafting.ID, "item_portable_crafting"), new PortableCraftingTabProvider());
		TabManager.tabGuessers.put(new Identifier(PortableCrafting.ID, "hotkey_portable_crafting"), (screen, tabs) -> {
			if (screen.getClass() == CraftingScreen.class) {
				for (Tab tab : tabs) {
					if (tab instanceof ItemTab it && it.stack.isIn(PortableCrafting.CRAFTING_TABLES) || tab instanceof BlockTab bt && bt.block.asItem().getDefaultStack().isIn(PortableCrafting.CRAFTING_TABLES)) {
						return tab;
					}
				}
			}
			return null;
		});
	}

	public static class PortableCraftingTab extends ItemTab {
		public PortableCraftingTab(ItemStack stack, int slot) {
			super(stack, slot, true);
		}

		@Override
		public boolean open() {
			return PortableCraftingClient.openCraftingTable();
		}
	}
}
