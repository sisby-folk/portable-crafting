package folk.sisby.portable_crafting.packet;

import folk.sisby.portable_crafting.PortableCrafting;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record S2CDummy() implements CustomPayload {
	public static final Id<S2CDummy> ID = new Id<>(PortableCrafting.id("s2c_dummy"));
	public static final PacketCodec<PacketByteBuf, S2CDummy> CODEC = PacketCodec.unit(new S2CDummy());

	public static boolean canSend(ServerPlayerEntity player) {
		return ServerPlayNetworking.canSend(player, ID);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
