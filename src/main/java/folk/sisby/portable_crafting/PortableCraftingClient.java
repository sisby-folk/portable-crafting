package folk.sisby.portable_crafting;

import folk.sisby.portable_crafting.tabs.PortableCraftingTabProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

import static folk.sisby.portable_crafting.PortableCrafting.C2S_OPEN_PORTABLE_CRAFTING;

public class PortableCraftingClient implements ClientModInitializer {
	public static KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.portable_crafting.open_crafting_table",
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_V,
		"key.categories.inventory"
	));

	public static final Set<TagKey<Item>> SERVER_SCREENS_ENABLED = new HashSet<>();
	public static boolean CHANGING_SCREENS = false;

	public static boolean openPortableCrafting(ItemStack stack, boolean dry) {
		if (ClientPlayNetworking.canSend(C2S_OPEN_PORTABLE_CRAFTING) && SERVER_SCREENS_ENABLED.stream().anyMatch(stack::isIn)) {
			if (!dry) ClientPlayNetworking.send(C2S_OPEN_PORTABLE_CRAFTING, PacketByteBufs.create().writeVarInt(Item.getRawId(stack.getItem())));
			return true;
		}
		return false;
	}

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(world -> {
			if (keyBinding == null || world == null) return;
			while (keyBinding.wasPressed()) {
				openPortableCrafting(Items.CRAFTING_TABLE.getDefaultStack().copy(), false);
			}
		});
		ClientPlayNetworking.registerGlobalReceiver(PortableCrafting.S2C_SCREENS_ENABLED, ((client, handler, buf, responseSender) -> {
			SERVER_SCREENS_ENABLED.clear();
			SERVER_SCREENS_ENABLED.addAll(buf.readList(b -> TagKey.of(Registry.ITEM_KEY, new Identifier(b.readString()))));
		}));

		if (FabricLoader.getInstance().isModLoaded("inventory-tabs")) {
			PortableCraftingTabProvider.register();
		}
		PortableCrafting.LOGGER.info("[Portable Crafting Client] Initialised!");
	}
}
