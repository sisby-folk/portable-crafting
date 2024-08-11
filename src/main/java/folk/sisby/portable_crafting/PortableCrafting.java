package folk.sisby.portable_crafting;

import folk.sisby.portable_crafting.packet.C2SOpenPortable;
import folk.sisby.portable_crafting.packet.S2CPortableTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
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
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> sender.sendPacket(new S2CPortableTags(CONFIG.getPortableTags()))));

		registerCraftingScreen(true, TagKey.of(RegistryKeys.ITEM, Identifier.tryParse("c:player_workstations/crafting_tables")), CraftingScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new CraftingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.crafting")));
		registerCraftingScreen(true, TagKey.of(RegistryKeys.ITEM, Identifier.tryParse("c:player_workstations/smithing_tables")), SmithingScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new SmithingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.upgrade")));
		registerCraftingScreen(true, TagKey.of(RegistryKeys.ITEM, Identifier.tryParse("c:player_workstations/grindstones")), GrindstoneScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new GrindstoneScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.grindstone_title")));
		registerCraftingScreen(true, TagKey.of(RegistryKeys.ITEM, Identifier.tryParse("c:player_workstations/cartography_tables")), CartographyTableScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new CartographyTableScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.cartography_table")));
		registerCraftingScreen(true, TagKey.of(RegistryKeys.ITEM, Identifier.tryParse("c:player_workstations/looms")), LoomScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new LoomScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.loom")));
		registerCraftingScreen(true, TagKey.of(RegistryKeys.ITEM, Identifier.tryParse("c:player_workstations/stonecutters")), StonecutterScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new StonecutterScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.stonecutter")));
		registerCraftingScreen(false, TagKey.of(RegistryKeys.ITEM, Identifier.tryParse("c:player_workstations/anvils")), AnvilScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new AnvilScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.repair")));

		LOGGER.info("[Portable Crafting] Initialised!");
	}

	public void registerCraftingScreen(boolean enabled, TagKey<Item> tag, Class<? extends ScreenHandler> handler, NamedScreenHandlerFactory factory) {
		SCREEN_TYPES.put(handler, tag);
		SCREEN_FACTORIES.put(tag, factory);
		CONFIG.screensEnabled.putIfAbsent(tag.id().toString(), enabled);
	}
}
