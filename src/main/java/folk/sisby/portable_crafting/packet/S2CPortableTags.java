package folk.sisby.portable_crafting.packet;

import folk.sisby.portable_crafting.PortableCrafting;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;

import java.util.List;

public record S2CPortableTags(List<RegistryKey<Item>> items, List<TagKey<Item>> tags) implements CustomPayload {
	public static final CustomPayload.Id<S2CPortableTags> ID = new CustomPayload.Id<>(PortableCrafting.id("s2c_portable_tags"));
	public static final PacketCodec<PacketByteBuf, S2CPortableTags> CODEC = PacketCodec.tuple(
		RegistryKey.createPacketCodec(RegistryKeys.ITEM).collect(PacketCodecs.toList()), S2CPortableTags::items,
		PacketCodecs.codec(TagKey.codec(RegistryKeys.ITEM)).collect(PacketCodecs.toList()), S2CPortableTags::tags,
		S2CPortableTags::new
	);

	public static boolean canSend(ServerConfigurationNetworkHandler handler) {
		return ServerConfigurationNetworking.canSend(handler, ID);
	}

	public void send(ServerConfigurationNetworkHandler handler) {
		if (canSend(handler)) ServerConfigurationNetworking.send(handler, this);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
