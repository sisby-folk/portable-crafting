package folk.sisby.portable_crafting_standalone.module.portable_crafting;

import com.mojang.blaze3d.platform.InputConstants;
import folk.sisby.portable_crafting_standalone.PortableCraftingStandalone;
import folk.sisby.portable_crafting_standalone.module.portable_crafting.network.ClientSendOpenCrafting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public class PortableCraftingClient{
    public static KeyMapping keyBinding;

    public static ClientSendOpenCrafting CLIENT_SEND_OPEN_CRAFTING;

    public static void runWhenEnabled() {
        CLIENT_SEND_OPEN_CRAFTING = new ClientSendOpenCrafting();

        if (PortableCrafting.enableKeybind) {
            keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.portable_crafting_standalone.open_crafting_table",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.inventory"
            ));

            ClientTickEvents.END_WORLD_TICK.register(level -> {
                if (keyBinding == null || level == null) return;
                while (keyBinding.consumeClick()) {
                    openCraftingTable();
                }
            });
        }
    }

    private boolean hasCrafting(Player player) {
        return true;
		// Below isn't correctly implemented in charm itself. Tag-based conception of "crafting table" should be implemented later.
		// return player.getInventory().contains(PortableCraftingStandalone.CRAFTING_TABLES);
    }

    private static void openCraftingTable() {
        CLIENT_SEND_OPEN_CRAFTING.send();
    }
}
