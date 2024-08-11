package folk.sisby.portable_crafting;

import folk.sisby.portable_crafting.mixin.AbstractBlockAccessor;
import folk.sisby.portable_crafting.packet.C2SOpenPortable;
import folk.sisby.portable_crafting.packet.S2CPortableTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PortableCrafting implements ModInitializer {
	public static final String ID = "portable_crafting";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final PortableCraftingConfig CONFIG = PortableCraftingConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", "portable_crafting", PortableCraftingConfig.class);
	public static final Map<Item, NamedScreenHandlerFactory> ITEM_FACTORIES = new HashMap<>();
	public static final Map<ScreenHandlerType<?>, Item> TYPE_ITEMS = new HashMap<>();
	public static final Map<TagKey<Item>, Item> TAG_ITEMS = new HashMap<>();

	public static boolean CHANGING_SCREENS;

	public static Identifier id(String path) {
		return Identifier.of(ID, path);
	}

	public static boolean canUse(PlayerEntity player) {
		Item item = TYPE_ITEMS.getOrDefault(getType(player.currentScreenHandler), null);
		if (item == null) return false;
		Set<TagKey<Item>> tags = new HashSet<>();
		TAG_ITEMS.entrySet().stream().filter(e -> e.getValue() == item).map(Map.Entry::getKey).forEach(tags::add);
		if (tags.isEmpty()) {
			return player.getInventory().containsAny(Set.of(item))
				|| player.currentScreenHandler.getCursorStack().isOf(item)
				|| player.currentScreenHandler.slots.stream().anyMatch(s -> s.getStack().isOf(item));
		} else {
			return tags.stream().anyMatch(t -> player.getInventory().contains(t))
				|| tags.stream().anyMatch(t -> player.currentScreenHandler.getCursorStack().isIn(t))
				|| tags.stream().anyMatch(t -> player.currentScreenHandler.slots.stream().anyMatch(s -> s.getStack().isIn(t)));
		}
	}

	public static ScreenHandlerType<?> getType(ScreenHandler handler) {
		try {
			return handler.getType();
		} catch (UnsupportedOperationException ignored) {
			return null;
		}
	}

	public static boolean openPortableCrafting(PlayerEntity player, ItemStack stack, boolean dry) {
		Item item = ITEM_FACTORIES.containsKey(stack.getItem()) && !TAG_ITEMS.containsValue(stack.getItem()) ? stack.getItem() : TAG_ITEMS.entrySet().stream().filter(e -> stack.isIn(e.getKey())).map(Map.Entry::getValue).findFirst().orElse(null);
		if (item != null) {
			if (!dry && player instanceof ServerPlayerEntity spe && item != TYPE_ITEMS.getOrDefault(getType(player.currentScreenHandler), null)) {
				CHANGING_SCREENS = true;
				spe.openHandledScreen(ITEM_FACTORIES.get(item));
				CHANGING_SCREENS = false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(C2SOpenPortable.ID, (server, player, handler, buf, sender) -> {
			C2SOpenPortable packet = C2SOpenPortable.fromBuf(buf);
			server.execute(() -> {
				if (player.getInventory().containsAny(Set.of(packet.item()))) openPortableCrafting(player, packet.item().getDefaultStack(), false);
			});
		});
		CONFIG.blockItemScreens.forEach((blockId, handlerId) -> {
			Item item = Registry.ITEM.get(Identifier.tryParse(blockId));
			if (item == Items.AIR || !(item instanceof BlockItem blockItem)) {
				LOGGER.warn("[Portable Crafting] Block item '{}' is invalid! Skipping.", blockId);
				return;
			}
			ScreenHandlerType<?> handler = Registry.SCREEN_HANDLER.get(Identifier.tryParse(handlerId));
			if (handler == null) {
				LOGGER.warn("[Portable Crafting] Screen handler '{}' is invalid! Skipping.", handlerId);
				return;
			}
			register(blockItem, handler);
		});
		CONFIG.blockItemTags.forEach((itemTag, blockId) -> {
			Item item = Registry.ITEM.get(Identifier.tryParse(blockId));
			if (item == Items.AIR || !(item instanceof BlockItem blockItem)) {
				LOGGER.warn("[Portable Crafting] Tag block item '{}' is invalid! Skipping.", blockId);
				return;
			}
			if (!ITEM_FACTORIES.containsKey(item)) {
				LOGGER.warn("[Portable Crafting] Block item '{}' isn't registered in blockItemScreens! Skipping.", blockId);
				return;
			}
			Identifier tagId = Identifier.tryParse(itemTag);
			if (tagId == null) {
				LOGGER.warn("[Portable Crafting] Tag '{}' is invalid! Skipping.", itemTag);
				return;
			}
			TAG_ITEMS.put(TagKey.of(Registry.ITEM_KEY, tagId), blockItem);
		});
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> new S2CPortableTags(new ArrayList<>(ITEM_FACTORIES.keySet().stream().filter(i -> !TAG_ITEMS.containsValue(i)).toList()), new ArrayList<>(TAG_ITEMS.keySet())).send(handler.getPlayer())));
		LOGGER.info("[Portable Crafting] Initialised!");
	}

	public void register(BlockItem item, ScreenHandlerType<?> handler) {
		Block block = item.getBlock();
		NamedScreenHandlerFactory factory = new SimpleNamedScreenHandlerFactory((i, inv, p) ->
			((AbstractBlockAccessor) block).callCreateScreenHandlerFactory(null, p.getWorld(), p.getBlockPos()).createMenu(i, inv, p),
			((AbstractBlockAccessor) block).callCreateScreenHandlerFactory(null, null, null).getDisplayName()
		);
		TYPE_ITEMS.put(handler, item);
		ITEM_FACTORIES.put(item, factory);
	}
}
