package folk.sisby.portable_crafting.compat.inventory_tabs;

import com.kqp.inventorytabs.tabs.TabManager;

public class PortableCraftingScreenTabCompat {
	public static void screenInit() {
		if (!TabManager.getInstance().screenOpenedViaTab()) {
			TabManager.getInstance().tabs.stream().filter(tab -> tab instanceof PortableCraftingTab).findFirst().ifPresent(TabManager.getInstance()::onOpenTab);
		}
	}

}
