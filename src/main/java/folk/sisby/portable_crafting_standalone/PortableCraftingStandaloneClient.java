package folk.sisby.portable_crafting_standalone;

import com.mojang.blaze3d.platform.InputUtil;
import folk.sisby.portable_crafting_standalone.screens.PortableCraftingScreen;
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

import static folk.sisby.portable_crafting_standalone.PortableCraftingStandalone.ID_OPEN_CRAFTING_TABLE;

public class PortableCraftingStandaloneClient implements ClientModInitializer {
	public static final Logger CLIENT_LOGGER = LoggerFactory.getLogger("Portable Crafting Standalone [CLIENT]");
	public static KeyBind keyBinding;

	public static final boolean enableKeybind = true;

	public static void openCraftingTable() {
		ClientPlayNetworking.send(ID_OPEN_CRAFTING_TABLE, PacketByteBufs.empty());
	}

	@Override
	public void onInitializeClient(ModContainer mod) {
		CLIENT_LOGGER.info("Initializing {}!", mod.metadata().name());

		if (QuiltLoader.isModLoaded("inventorytabs")) {
			PortableCraftingScreen.TABS_COMPAT = true;
		}

		if (enableKeybind) {
			keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBind(
					"key.portable_crafting_standalone.open_crafting_table",
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
}
