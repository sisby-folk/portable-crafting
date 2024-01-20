package folk.sisby.portable_crafting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PortableCrafting implements ModInitializer {
	public static final String ID = "portable_crafting";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final PortableCraftingConfig CONFIG = PortableCraftingConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", "portable_crafting", PortableCraftingConfig.class);
	private static final Map<TagKey<Item>, NamedScreenHandlerFactory> SCREEN_FACTORIES = new HashMap<>();
	private static final Map<Class<? extends ScreenHandler>, TagKey<Item>> SCREEN_TYPES = new HashMap<>();

	public static final TagKey<Item> CRAFTING_TABLES = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "crafting_tables"));
	public static final Identifier C2S_OPEN_PORTABLE_CRAFTING = new Identifier(ID, "c2s_open_portable_crafting");

	public static boolean canUse(PlayerEntity player) {
		TagKey<Item> tag = SCREEN_TYPES.getOrDefault(player.currentScreenHandler.getClass(), null);
		return (tag != null) && (player.getInventory().contains(tag)
				|| player.currentScreenHandler.getCursorStack().isIn(tag)
				|| player.currentScreenHandler.slots.stream().anyMatch(s -> s.getStack().isIn(tag)));
	}

	public static boolean openCrafting(PlayerEntity player, ItemStack stack, boolean dry) {
		Optional<TagKey<Item>> tag = SCREEN_FACTORIES.keySet().stream().filter(t -> CONFIG.screensEnabled.get(t.id().toString())).filter(stack::isIn).findFirst();
		if (tag.isPresent() && tag.get() != SCREEN_TYPES.getOrDefault(player.currentScreenHandler.getClass(), null))
		{
			if (!dry && player instanceof ServerPlayerEntity spe) spe.openHandledScreen(SCREEN_FACTORIES.get(tag.get()));
			return true;
		}
		return false;
	}

	@Override
	public void onInitialize() {
		registerCraftingScreen(CRAFTING_TABLES, CraftingScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new CraftingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), new TranslatableText("container.crafting")));
		registerCraftingScreen(TagKey.of(Registry.ITEM_KEY, new Identifier("c", "smithing_tables")), SmithingScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new SmithingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), new TranslatableText("container.crafting")));
		ServerPlayNetworking.registerGlobalReceiver(C2S_OPEN_PORTABLE_CRAFTING, (server, player, handler, buf, sender) -> server.execute(() -> {
			if (player.getInventory().contains(CRAFTING_TABLES)) openCrafting(player, Items.CRAFTING_TABLE.getDefaultStack(), false);
		}));

		LOGGER.info("[Portable Crafting] Initialised!");
	}

	public void registerCraftingScreen(TagKey<Item> tag, Class<? extends ScreenHandler> handler, NamedScreenHandlerFactory factory) {
		SCREEN_TYPES.put(handler, tag);
		SCREEN_FACTORIES.put(tag, factory);
		CONFIG.screensEnabled.putIfAbsent(tag.id().toString(), true);
	}
}
