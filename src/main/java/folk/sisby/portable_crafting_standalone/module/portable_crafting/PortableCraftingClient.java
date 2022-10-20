package folk.sisby.portable_crafting_standalone.module.portable_crafting;

import com.mojang.blaze3d.platform.InputUtil;
import folk.sisby.portable_crafting_standalone.PortableCraftingStandalone;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import static folk.sisby.portable_crafting_standalone.PortableCraftingStandalone.ID_OPEN_CRAFTING_TABLE;

public class PortableCraftingClient{
    public static KeyBind keyBinding;

    public static void runWhenEnabled() {
        if (PortableCraftingStandalone.enableKeybind) {
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

    public static void openCraftingTable() {
		ClientPlayNetworking.send(ID_OPEN_CRAFTING_TABLE, PacketByteBufs.empty());
    }
}
