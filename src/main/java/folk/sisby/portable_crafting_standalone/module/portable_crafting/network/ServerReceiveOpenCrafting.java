package folk.sisby.portable_crafting_standalone.module.portable_crafting.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import folk.sisby.portable_crafting_standalone.module.portable_crafting.PortableCrafting;
import folk.sisby.portable_crafting_standalone.network.Id;
import folk.sisby.portable_crafting_standalone.network.ServerReceiver;

@Id("portable_crafting_standalone:open_crafting")
public class ServerReceiveOpenCrafting extends ServerReceiver {
    @Override
    public void handle(MinecraftServer server, ServerPlayerEntity player, PacketByteBuf buffer) {
        server.execute(() -> {
            if (player == null || !player.getInventory().contains(new ItemStack(Blocks.CRAFTING_TABLE))) return;
            PortableCrafting.triggerUsedCraftingTable(player);
            PortableCrafting.openContainer(player);
        });
    }
}
