package folk.sisby.portable_crafting;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import org.lwjgl.glfw.GLFW;

import static folk.sisby.portable_crafting.PortableCrafting.C2S_OPEN_PORTABLE_CRAFTING;

@SuppressWarnings("deprecation")
public class PortableCraftingClient implements ClientModInitializer {
	public static KeyBind keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBind(
		"key.portable_crafting.open_crafting_table",
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_V,
		"key.categories.inventory"
	));

	public static void openCraftingTable() {
		if (ClientPlayNetworking.canSend(C2S_OPEN_PORTABLE_CRAFTING) && PortableCrafting.canUse(MinecraftClient.getInstance().player)) {
			ClientPlayNetworking.send(C2S_OPEN_PORTABLE_CRAFTING, PacketByteBufs.empty());
		}
	}

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(world -> {
			if (keyBinding == null || world == null) return;
			while (keyBinding.wasPressed()) {
				openCraftingTable();
			}
		});
		PortableCrafting.LOGGER.info("[Portable Crafting Client] Initialised!");
	}
}
