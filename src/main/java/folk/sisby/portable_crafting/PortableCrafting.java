package folk.sisby.portable_crafting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
public class PortableCrafting implements ModInitializer {
	public static final String ID = "portable_crafting";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static final TagKey<Item> CRAFTING_TABLES = TagKey.of(Registry.ITEM_KEY, new Identifier(ID, "crafting_tables"));
	public static final Identifier C2S_OPEN_PORTABLE_CRAFTING = new Identifier(ID, "c2s_open_portable_crafting");

	public static boolean canUse(@Nullable PlayerEntity player) {
		return player != null && player.getInventory().contains(CRAFTING_TABLES);
	}

	public static void openCrafting(PlayerEntity player) {
		if (player instanceof ServerPlayerEntity spe) {
			spe.closeHandledScreen();
			spe.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, inv, p) -> new CraftingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.crafting")));
		}
	}

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(C2S_OPEN_PORTABLE_CRAFTING, (server, player, handler, buf, sender) -> server.execute(() -> {
			if (canUse(player)) openCrafting(player);
		}));
		LOGGER.info("[Portable Crafting] Initialised!");
	}
}
