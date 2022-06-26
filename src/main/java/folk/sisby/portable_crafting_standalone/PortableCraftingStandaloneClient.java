package folk.sisby.portable_crafting_standalone;

import folk.sisby.portable_crafting_standalone.module.portable_crafting.PortableCraftingClient;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortableCraftingStandaloneClient implements ClientModInitializer {
	public static final Logger CLIENT_LOGGER = LoggerFactory.getLogger("Portable Crafting Standalone [CLIENT]");

	@Override
	public void onInitializeClient(ModContainer mod) {
		CLIENT_LOGGER.info("Initializing {}!", mod.metadata().name());

		PortableCraftingClient.runWhenEnabled();
	}
}
