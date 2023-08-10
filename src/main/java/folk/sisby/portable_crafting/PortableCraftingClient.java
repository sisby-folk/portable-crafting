package folk.sisby.portable_crafting;

import com.mojang.blaze3d.platform.InputUtil;
import folk.sisby.portable_crafting.screens.PortableCraftingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBind;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static folk.sisby.portable_crafting.PortableCrafting.C2S_OPEN_PORTABLE_CRAFTING;

@SuppressWarnings("deprecation")
public class PortableCraftingClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Portable Crafting Client");
	public static KeyBind keyBinding;

	public static void openCraftingTable() {
		ClientPlayNetworking.send(C2S_OPEN_PORTABLE_CRAFTING, PacketByteBufs.empty());
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("[Portable Crafting Client] Initializing!");

		PortableCraftingScreen.touch();

		if (FabricLoader.getInstance().isModLoaded("inventorytabs")) {
			PortableCraftingScreen.TABS_COMPAT = true;
		}

		PortableCraftingClient.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBind(
				"key.portable_crafting.open_crafting_table",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_V,
				"key.categories.inventory"
		));

		ClientTickEvents.END_CLIENT_TICK.register(level -> {
			if (keyBinding == null || level == null) return;
			while (keyBinding.wasPressed()) {
				openCraftingTable();
			}
		});
	}
}
