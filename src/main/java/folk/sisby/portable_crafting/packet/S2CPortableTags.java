package folk.sisby.portable_crafting.packet;

import com.mojang.serialization.Codec;
import folk.sisby.portable_crafting.PortableCrafting;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;

public record S2CPortableTags(List<TagKey<Item>> tags) implements CustomPayload {
	public static final CustomPayload.Id<S2CPortableTags> ID = new CustomPayload.Id<>(Identifier.of(PortableCrafting.ID, "s2c_portable_tags"));
	public static final PacketCodec<RegistryByteBuf, S2CPortableTags> CODEC = PacketCodec.tuple(PacketCodecs.codec(Codec.list(TagKey.codec(RegistryKeys.ITEM))), S2CPortableTags::tags, S2CPortableTags::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
