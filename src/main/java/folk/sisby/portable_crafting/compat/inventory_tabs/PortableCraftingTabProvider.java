package folk.sisby.portable_crafting.compat.inventory_tabs;

import com.kqp.inventorytabs.tabs.provider.TabProvider;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public class PortableCraftingTabProvider implements TabProvider {

	@Override
	public void addAvailableTabs(ClientPlayerEntity player, List<Tab> tabs) {
		if (player.getInventory().contains(new ItemStack(Blocks.CRAFTING_TABLE))) {
			Tab tab = new PortableCraftingTab();
			if (tabs.stream().noneMatch(c -> c instanceof PortableCraftingTab)) {
				tabs.add(tab);
			}
		}
	}
}
