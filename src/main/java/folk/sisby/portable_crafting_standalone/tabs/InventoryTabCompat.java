package folk.sisby.portable_crafting_standalone.tabs;

import com.kqp.inventorytabs.api.TabProviderRegistry;
import folk.sisby.portable_crafting_standalone.PortableCraftingStandalone;

public class InventoryTabCompat {
	public static void init() {
		TabProviderRegistry.register(PortableCraftingStandalone.id("portablecraftingtab"), new PortableCraftingTabProvider());
	}
}
