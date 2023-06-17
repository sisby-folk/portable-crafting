package folk.sisby.portable_crafting;

import com.mojang.blaze3d.platform.InputUtil;
import folk.sisby.portable_crafting.screens.PortableCraftingScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static folk.sisby.portable_crafting.PortableCrafting.C2S_OPEN_PORTABLE_CRAFTING;

public class PortableCraftingClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Portable Crafting Client");
	public static KeyBind keyBinding;

	public static void openCraftingTable() {
		ClientPlayNetworking.send(C2S_OPEN_PORTABLE_CRAFTING, PacketByteBufs.empty());
	}

	@Override
	public void onInitializeClient(ModContainer mod) {
		LOGGER.info("[Portable Crafting Client] Initializing!");

		PortableCraftingScreen.touch();

		if (QuiltLoader.isModLoaded("inventorytabs")) {
			PortableCraftingScreen.TABS_COMPAT = true;
		}

		PortableCraftingClient.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBind(
				"key.portable_crafting.open_crafting_table",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_V,
				"key.categories.inventory"
		));

		ClientTickEvents.END.register(level -> {
			if (keyBinding == null || level == null) return;
			while (keyBinding.wasPressed()) {
				openCraftingTable();
			}
		});
	}
}
