package folk.sisby.portable_crafting.tabs;

import folk.sisby.inventory_tabs.TabManager;
import folk.sisby.inventory_tabs.TabProviders;
import folk.sisby.inventory_tabs.providers.UniqueItemTabProvider;
import folk.sisby.inventory_tabs.tabs.BlockTab;
import folk.sisby.inventory_tabs.tabs.ItemTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import folk.sisby.portable_crafting.PortableCrafting;
import folk.sisby.portable_crafting.PortableCraftingClient;
import folk.sisby.portable_crafting.packet.C2SOpenPortable;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Predicate;

public class PortableCraftingTabProvider extends UniqueItemTabProvider {
	PortableCraftingTabProvider() {
		matches.put(PortableCrafting.id("crafting_tables"),
			e -> C2SOpenPortable.canSend()
				&& PortableCraftingClient.openPortableCrafting(e.getDefaultStack(), true)
		);
	}

	public static void register() {
		TabProviders.register(PortableCrafting.id("item_portable_crafting"), new PortableCraftingTabProvider());
		TabManager.tabGuessers.put(PortableCrafting.id("hotkey_portable_crafting"), (screen, tabs) -> {
			Item item = PortableCrafting.TYPE_ITEMS.getOrDefault(PortableCrafting.getType(screen.getScreenHandler()), null);
			if (item != null) {
				for (Tab tab : tabs) {
					if (tab instanceof ItemTab it && it.stack.isOf(item) || tab instanceof BlockTab bt && bt.block.asItem().getDefaultStack().isOf(item)) {
						return tab;
					}
				}
			}
			return null;
		});
	}

	@Override
	public Tab createTab(ItemStack stack, int slot) {
		return new PortableCraftingTab(stack, slot, preclusions);
	}

	public static class PortableCraftingTab extends ItemTab {
		public PortableCraftingTab(ItemStack stack, int slot, Map<Identifier, Predicate<ItemStack>> preclusions) {
			super(stack, slot, preclusions, true);
		}

		@Override
		public void open(ClientPlayerEntity player, ClientWorld world, ScreenHandler handler, ClientPlayerInteractionManager interactionManager) {
			PortableCraftingClient.openPortableCrafting(stack, false);
		}
	}
}
