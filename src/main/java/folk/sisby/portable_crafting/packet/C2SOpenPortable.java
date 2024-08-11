package folk.sisby.portable_crafting.packet;

import folk.sisby.portable_crafting.PortableCrafting;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public record C2SOpenPortable(Item item) {
	public static final Identifier ID = PortableCrafting.id("c2s_open_portable");

	public static C2SOpenPortable fromBuf(PacketByteBuf buf) {
		return new C2SOpenPortable(buf.readRegistryValue(Registry.ITEM));
	}

	private PacketByteBuf toBuf() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeRegistryValue(Registry.ITEM, item);
		return buf;
	}

	public static boolean canSend() {
		return ClientPlayNetworking.canSend(ID);
	}

	public void send() {
		ClientPlayNetworking.send(ID, toBuf());
	}
}
