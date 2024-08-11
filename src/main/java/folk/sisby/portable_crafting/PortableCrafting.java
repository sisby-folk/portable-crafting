package folk.sisby.portable_crafting;

import folk.sisby.portable_crafting.mixin.AbstractBlockAccessor;
import folk.sisby.portable_crafting.packet.C2SOpenPortable;
import folk.sisby.portable_crafting.packet.S2CPortableTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PortableCrafting implements ModInitializer {
	public static final String ID = "portable_crafting";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final PortableCraftingConfig CONFIG = PortableCraftingConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", "portable_crafting", PortableCraftingConfig.class);
	public static final Map<Item, NamedScreenHandlerFactory> SCREEN_FACTORIES = new HashMap<>();
	public static final Map<ScreenHandlerType<?>, Item> SCREEN_TYPES = new HashMap<>();

	public static boolean CHANGING_SCREENS;

	public static Identifier id(String path) {
		return Identifier.of(ID, path);
	}

	public static boolean canUse(PlayerEntity player) {
		Item item = SCREEN_TYPES.getOrDefault(getType(player.currentScreenHandler), null);
		return (item != null) && (player.getInventory().containsAny(Set.of(item))
			|| player.currentScreenHandler.getCursorStack().isOf(item)
			|| player.currentScreenHandler.slots.stream().anyMatch(s -> s.getStack().isOf(item)));
	}

	public static ScreenHandlerType<?> getType(ScreenHandler handler) {
		try {
			return handler.getType();
		} catch (UnsupportedOperationException ignored) {
			return null;
		}
	}

	public static boolean openPortableCrafting(PlayerEntity player, ItemStack stack, boolean dry) {
		if (SCREEN_FACTORIES.containsKey(stack.getItem())) {
			if (!dry && player instanceof ServerPlayerEntity spe && stack.getItem() != SCREEN_TYPES.getOrDefault(getType(player.currentScreenHandler), null)) {
				CHANGING_SCREENS = true;
				spe.openHandledScreen(SCREEN_FACTORIES.get(stack.getItem()));
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
		CONFIG.blockScreens.forEach((blockId, handlerId) -> {
			Item item = Registries.ITEM.get(Identifier.tryParse(blockId));
			if (!(item instanceof BlockItem blockItem)) {
				LOGGER.warn("[Portable Crafting] Block item '{}' is invalid! Skipping.", blockId);
				return;
			}
			ScreenHandlerType<?> handler = Registries.SCREEN_HANDLER.get(Identifier.tryParse(handlerId));
			if (handler == null) {
				LOGGER.warn("[Portable Crafting] Screen handler '{}' is invalid! Skipping.", handlerId);
				return;
			}
			register(blockItem, handler);
		});
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> new S2CPortableTags(new ArrayList<>(SCREEN_FACTORIES.keySet())).send(handler.getPlayer())));
		LOGGER.info("[Portable Crafting] Initialised!");
	}

	public void register(BlockItem item, ScreenHandlerType<?> handler) {
		NamedScreenHandlerFactory factory = new SimpleNamedScreenHandlerFactory((i, inv, p) ->
			((AbstractBlockAccessor) item.getBlock()).callCreateScreenHandlerFactory(null, p.getWorld(), p.getBlockPos()).createMenu(i, inv, p),
			((AbstractBlockAccessor) item.getBlock()).callCreateScreenHandlerFactory(null, null, null).getDisplayName()
		);
		SCREEN_TYPES.put(handler, item);
		SCREEN_FACTORIES.put(item, factory);
	}
}
