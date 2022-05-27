package folk.sisby.portable_crafting_standalone.module.portable_crafting;

import folk.sisby.portable_crafting_standalone.module.portable_crafting.network.ServerReceiveOpenCrafting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class PortableCrafting {
    private static final Component LABEL = new TranslatableComponent("container.portable_crafting_standalone.portable_crafting_table");

    public static ServerReceiveOpenCrafting SERVER_RECEIVE_OPEN_CRAFTING;

    public static boolean enableKeybind = true;

    public static void runWhenEnabled() {
        SERVER_RECEIVE_OPEN_CRAFTING = new ServerReceiveOpenCrafting();
    }

    public static void openContainer(ServerPlayer player) {
        player.closeContainer();
        player.openMenu(new SimpleMenuProvider((i, inv, p) -> new PortableCraftingMenu(i, inv, ContainerLevelAccess.create(p.level, p.blockPosition())), LABEL));
    }

    public static void triggerUsedCraftingTable(ServerPlayer player) {

    }
}
