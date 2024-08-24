package folk.sisby.portable_crafting;

import folk.sisby.portable_crafting.packet.C2SOpenPortable;
import folk.sisby.portable_crafting.packet.S2CPortableTags;
import folk.sisby.portable_crafting.tabs.PortableCraftingTabProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.TagKey;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class PortableCraftingClient implements ClientModInitializer {
	public static final Set<Item> SERVER_PORTABLE_WORKSTATIONS = new HashSet<>();
	public static final Set<TagKey<Item>> SERVER_PORTABLE_WORKSTATION_TAGS = new HashSet<>();
	public static KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.portable_crafting.open_crafting_table",
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_V,
		"key.categories.inventory"
	));
	public static boolean CHANGING_SCREENS = false;

	public static boolean openPortableCrafting(ItemStack stack, boolean dry) {
		if (C2SOpenPortable.canSend() && (SERVER_PORTABLE_WORKSTATIONS.stream().anyMatch(stack::isOf) || SERVER_PORTABLE_WORKSTATION_TAGS.stream().anyMatch(stack::isIn))) {
			if (!dry) new C2SOpenPortable(stack.getItem()).send();
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
		ClientPlayNetworking.registerGlobalReceiver(S2CPortableTags.ID, ((packet, context) -> {
			SERVER_PORTABLE_WORKSTATIONS.clear();
			SERVER_PORTABLE_WORKSTATION_TAGS.clear();
			SERVER_PORTABLE_WORKSTATIONS.addAll(packet.items());
			SERVER_PORTABLE_WORKSTATION_TAGS.addAll(packet.tags());
		}));

		if (FabricLoader.getInstance().isModLoaded("inventory_tabs")) {
			PortableCraftingTabProvider.register();
		}
		PortableCrafting.LOGGER.info("[Portable Crafting Client] Initialised!");
	}
}
