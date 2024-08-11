package folk.sisby.portable_crafting;

import com.mojang.datafixers.util.Function3;
import folk.sisby.portable_crafting.packet.C2SOpenPortable;
import folk.sisby.portable_crafting.packet.S2CPortableTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PortableCrafting implements ModInitializer {
	public static final String ID = "portable_crafting";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final PortableCraftingConfig CONFIG = PortableCraftingConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", "portable_crafting", PortableCraftingConfig.class);
	public static final Map<TagKey<Item>, NamedScreenHandlerFactory> SCREEN_FACTORIES = new HashMap<>();
	public static final Map<Class<? extends ScreenHandler>, TagKey<Item>> SCREEN_TYPES = new HashMap<>();

	public static boolean CHANGING_SCREENS;

	public static Identifier id(String path) {
		return Identifier.of(ID, path);
	}

	public static boolean canUse(PlayerEntity player) {
		TagKey<Item> tag = SCREEN_TYPES.getOrDefault(player.currentScreenHandler.getClass(), null);
		return (tag != null) && (player.getInventory().contains(tag)
			|| player.currentScreenHandler.getCursorStack().isIn(tag)
			|| player.currentScreenHandler.slots.stream().anyMatch(s -> s.getStack().isIn(tag)));
	}

	public static boolean openPortableCrafting(PlayerEntity player, ItemStack stack, boolean dry) {
		Optional<TagKey<Item>> tag = SCREEN_FACTORIES.keySet().stream().filter(t -> CONFIG.screensEnabled.get(t.id().toString())).filter(stack::isIn).findFirst();
		if (tag.isPresent()) {
			if (!dry && player instanceof ServerPlayerEntity spe && tag.get() != SCREEN_TYPES.getOrDefault(player.currentScreenHandler.getClass(), null)) {
				CHANGING_SCREENS = true;
				spe.openHandledScreen(SCREEN_FACTORIES.get(tag.get()));
				CHANGING_SCREENS = false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playS2C().register(S2CPortableTags.ID, S2CPortableTags.CODEC);
		PayloadTypeRegistry.playC2S().register(C2SOpenPortable.ID, C2SOpenPortable.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(C2SOpenPortable.ID, (packet, context) -> context.server().execute(() -> {
			if (context.player().getInventory().containsAny(Set.of(packet.item()))) openPortableCrafting(context.player(), packet.item().getDefaultStack(), false);
		}));
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> new S2CPortableTags(CONFIG.getPortableTags()).send(handler.getPlayer())));

		registerSimple(true, "c:player_workstations/crafting_tables", CraftingScreenHandler.class, CraftingScreenHandler::new, "container.crafting");
		registerSimple(true, "c:player_workstations/smithing_tables", SmithingScreenHandler.class, SmithingScreenHandler::new, "container.upgrade");
		registerSimple(true, "c:player_workstations/grindstones", GrindstoneScreenHandler.class, GrindstoneScreenHandler::new, "container.grindstone_title");
		registerSimple(true, "c:player_workstations/cartography_tables", CartographyTableScreenHandler.class, CartographyTableScreenHandler::new, "container.cartography_table");
		registerSimple(true, "c:player_workstations/looms", LoomScreenHandler.class, LoomScreenHandler::new, "container.loom");
		registerSimple(true, "c:player_workstations/stonecutters", StonecutterScreenHandler.class, StonecutterScreenHandler::new, "container.stonecutter");
		registerSimple(false, "c:player_workstations/anvils", AnvilScreenHandler.class, AnvilScreenHandler::new, "container.repair");

		LOGGER.info("[Portable Crafting] Initialised!");
	}

	public void registerSimple(boolean enabled, String tag, Class<? extends ScreenHandler> handler, Function3<Integer, PlayerInventory, ScreenHandlerContext, ScreenHandler> constructor, String translation) {
		register(enabled, TagKey.of(RegistryKeys.ITEM, Identifier.tryParse(tag)), handler, new SimpleNamedScreenHandlerFactory((i, inv, p) -> constructor.apply(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable(translation)));
	}

	public void register(boolean enabled, TagKey<Item> tag, Class<? extends ScreenHandler> handler, NamedScreenHandlerFactory factory) {
		SCREEN_TYPES.put(handler, tag);
		SCREEN_FACTORIES.put(tag, factory);
		CONFIG.screensEnabled.putIfAbsent(tag.id().toString(), enabled);
	}
}
