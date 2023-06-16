package folk.sisby.portable_crafting;

import folk.sisby.portable_crafting.compat.inventory_tabs.PortableCraftingTab;
import folk.sisby.portable_crafting.screens.PortableCraftingScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortableCrafting implements ModInitializer {
	public static final String ID = "portable_crafting";
	public static final Logger LOGGER = LoggerFactory.getLogger("Portable Crafting");

	public static final TagKey<Item> CRAFTING_TABLES = TagKey.of(RegistryKeys.ITEM, new Identifier(ID, "crafting_tables"));
	public static final Identifier C2S_OPEN_PORTABLE_CRAFTING = new Identifier(ID, "c2s_open_portable_crafting");

	public static boolean is_allowed(PlayerEntity player) {
		return player.getInventory().contains(CRAFTING_TABLES); // If tag in inventory
	}

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Portable Crafting Initializing!");

		if (QuiltLoader.isModLoaded("inventorytabs")) {
			PortableCraftingTab.touch();
		}

		PortableCraftingScreenHandler.touch();

		ServerPlayNetworking.registerGlobalReceiver(C2S_OPEN_PORTABLE_CRAFTING, (server, player, handler, buf, sender) -> server.execute(() -> {
			if (PortableCrafting.is_allowed(player)) {
				player.closeHandledScreen();
				player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, inv, p) -> new PortableCraftingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), PortableCraftingScreenHandler.LABEL));
			}
		}));
	}
}
