package folk.sisby.portable_crafting.packet;

import folk.sisby.portable_crafting.PortableCrafting;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public record C2SOpenPortable(Item item) implements CustomPayload {
	public static final Id<C2SOpenPortable> ID = new Id<>(Identifier.of(PortableCrafting.ID, "c2s_open_portable"));
	public static final PacketCodec<RegistryByteBuf, C2SOpenPortable> CODEC = PacketCodec.tuple(PacketCodecs.registryValue(RegistryKeys.ITEM), C2SOpenPortable::item, C2SOpenPortable::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
