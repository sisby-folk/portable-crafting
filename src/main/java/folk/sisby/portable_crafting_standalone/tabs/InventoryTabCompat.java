package folk.sisby.portable_crafting_standalone.tabs;

import com.kqp.inventorytabs.api.TabProviderRegistry;
import folk.sisby.portable_crafting_standalone.PortableCraftingStandalone;
import folk.sisby.portable_crafting_standalone.module.portable_crafting.PortableCraftingScreenHandler;

public class InventoryTabCompat {
	public static void init() {
		TabProviderRegistry.register(PortableCraftingStandalone.id("portablecraftingtab"), new PortableCraftingTabProvider());
	}
}
