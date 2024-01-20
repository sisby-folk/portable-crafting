package folk.sisby.portable_crafting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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

	public static final Identifier S2C_SCREENS_ENABLED = new Identifier(ID, "s2c_screens_enabled");
	public static final Identifier C2S_OPEN_PORTABLE_CRAFTING = new Identifier(ID, "c2s_open_portable_crafting");

	public static boolean canUse(PlayerEntity player) {
		TagKey<Item> tag = SCREEN_TYPES.getOrDefault(player.currentScreenHandler.getClass(), null);
		return (tag != null) && (player.getInventory().contains(tag)
				|| player.currentScreenHandler.getCursorStack().isIn(tag)
				|| player.currentScreenHandler.slots.stream().anyMatch(s -> s.getStack().isIn(tag)));
	}

	public static boolean openPortableCrafting(PlayerEntity player, ItemStack stack, boolean dry) {
		Optional<TagKey<Item>> tag = SCREEN_FACTORIES.keySet().stream().filter(t -> CONFIG.screensEnabled.get(t.id().toString())).filter(stack::isIn).findFirst();
		if (tag.isPresent() && (player == null || tag.get() != SCREEN_TYPES.getOrDefault(player.currentScreenHandler.getClass(), null)))
		{
			if (!dry && player instanceof ServerPlayerEntity spe) spe.openHandledScreen(SCREEN_FACTORIES.get(tag.get()));
			return true;
		}
		return false;
	}

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(C2S_OPEN_PORTABLE_CRAFTING, (server, player, handler, buf, sender) -> {
			Item item = Item.byRawId(buf.readVarInt());
            server.execute(() -> {
                if (player.getInventory().containsAny(Set.of(item))) openPortableCrafting(player, item.getDefaultStack(), false);
            });
        });
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeCollection(CONFIG.screensEnabled.keySet().stream().filter(CONFIG.screensEnabled::get).toList(), PacketByteBuf::writeString);
			sender.sendPacket(S2C_SCREENS_ENABLED, buf);
		}));

		registerCraftingScreen(true, TagKey.of(Registry.ITEM_KEY, new Identifier("c", "crafting_tables")), CraftingScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new CraftingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.crafting")));
		registerCraftingScreen(true, TagKey.of(Registry.ITEM_KEY, new Identifier("c", "smithing_tables")), SmithingScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new SmithingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.upgrade")));
		registerCraftingScreen(true, TagKey.of(Registry.ITEM_KEY, new Identifier("c", "grindstones")), GrindstoneScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new GrindstoneScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.grindstone_title")));
		registerCraftingScreen(true, TagKey.of(Registry.ITEM_KEY, new Identifier("c", "cartography_tables")), CartographyTableScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new CartographyTableScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.cartography_table")));
		registerCraftingScreen(true, TagKey.of(Registry.ITEM_KEY, new Identifier("c", "looms")), LoomScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new LoomScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.loom")));
		registerCraftingScreen(true, TagKey.of(Registry.ITEM_KEY, new Identifier("c", "stonecutters")), StonecutterScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new StonecutterScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.stonecutter")));
		registerCraftingScreen(false, TagKey.of(Registry.ITEM_KEY, new Identifier("c", "anvils")), AnvilScreenHandler.class, new SimpleNamedScreenHandlerFactory((i, inv, p) -> new AnvilScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), Text.translatable("container.repair")));

		LOGGER.info("[Portable Crafting] Initialised!");
	}

	public void registerCraftingScreen(boolean enabled, TagKey<Item> tag, Class<? extends ScreenHandler> handler, NamedScreenHandlerFactory factory) {
		SCREEN_TYPES.put(handler, tag);
		SCREEN_FACTORIES.put(tag, factory);
		CONFIG.screensEnabled.putIfAbsent(tag.id().toString(), enabled);
	}
}
