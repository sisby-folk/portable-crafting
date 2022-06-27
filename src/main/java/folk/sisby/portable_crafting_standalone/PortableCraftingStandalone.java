package folk.sisby.portable_crafting_standalone;

import folk.sisby.portable_crafting_standalone.module.portable_crafting.PortableCrafting;
import folk.sisby.portable_crafting_standalone.tabs.InventoryTabCompat;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortableCraftingStandalone implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Portable Crafting Standalone");

	public static final String ID = "portablecraftingstandalone";

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Initializing {}!", mod.metadata().name());

		if (QuiltLoader.isModLoaded("inventorytabs")) {
			InventoryTabCompat.init();
		}

		PortableCrafting.runWhenEnabled();

		// TODO: - Make it pick the right tab when opening using the hotkey
		// 		 - Make it update the tabs while its open so there's no sillyness
	}
}
