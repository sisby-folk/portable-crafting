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
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Predicate;

public class PortableCraftingTabProvider extends UniqueItemTabProvider {
	PortableCraftingTabProvider() {
		matches.put(new Identifier(PortableCrafting.ID, "crafting_tables"),
			e -> ClientPlayNetworking.canSend(PortableCrafting.C2S_OPEN_PORTABLE_CRAFTING)
				&& PortableCraftingClient.openPortableCrafting(e.getDefaultStack(), true)
		);
	}

	@Override
	public Tab createTab(ItemStack stack, int slot) {
		return new PortableCraftingTab(stack, slot, preclusions);
	}

	public static void register() {
		TabProviders.register(new Identifier(PortableCrafting.ID, "item_portable_crafting"), new PortableCraftingTabProvider());
		TabManager.tabGuessers.put(new Identifier(PortableCrafting.ID, "hotkey_portable_crafting"), (screen, tabs) -> {
			TagKey<Item> tag = PortableCrafting.SCREEN_TYPES.getOrDefault(screen.getScreenHandler().getClass(), null);
			if (tag != null) {
				for (Tab tab : tabs) {
					if (tab instanceof ItemTab it && it.stack.isIn(tag) || tab instanceof BlockTab bt && bt.block.asItem().getDefaultStack().isIn(tag)) {
						return tab;
					}
				}
			}
			return null;
		});
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
