package folk.sisby.portable_crafting_standalone;

import folk.sisby.portable_crafting_standalone.module.portable_crafting.PortableCrafting;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortableCraftingStandalone implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Portable Crafting Standalone");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Initializing {}!", mod.metadata().name());

		PortableCrafting.runWhenEnabled();
	}
}
