package folk.sisby.portable_crafting_standalone.module.portable_crafting;

import folk.sisby.portable_crafting_standalone.module.portable_crafting.network.ServerReceiveOpenCrafting;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;

public class PortableCrafting {
    private static final Text LABEL = new TranslatableText("container.portable_crafting_standalone.portable_crafting_table");

    public static ServerReceiveOpenCrafting SERVER_RECEIVE_OPEN_CRAFTING;

    public static boolean enableKeybind = true;

    public static void runWhenEnabled() {
        SERVER_RECEIVE_OPEN_CRAFTING = new ServerReceiveOpenCrafting();
    }

    public static void openContainer(ServerPlayerEntity player) {
        player.closeHandledScreen();
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, inv, p) -> new PortableCraftingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), LABEL));
    }

    public static void triggerUsedCraftingTable(ServerPlayerEntity player) {
		// There was an advancement here, but I didn't care for it.
    }
}
