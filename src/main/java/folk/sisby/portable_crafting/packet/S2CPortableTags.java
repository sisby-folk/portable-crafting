package folk.sisby.portable_crafting.packet;

import folk.sisby.portable_crafting.PortableCrafting;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public record S2CPortableTags(List<Item> items, List<TagKey<Item>> tags) {
	public static final Identifier ID = PortableCrafting.id("s2c_portable_tags");

	public static S2CPortableTags fromBuf(PacketByteBuf buf) {
		return new S2CPortableTags(buf.readList(b -> Item.byRawId(b.readVarInt())), buf.readList(b -> TagKey.of(Registry.ITEM_KEY, b.readIdentifier())));
	}

	private PacketByteBuf toBuf() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeCollection(items, (b, i) -> b.writeVarInt(Item.getRawId(i)));
		buf.writeCollection(tags, (b, t) -> b.writeIdentifier(t.id()));
		return buf;
	}

	public static boolean canSend(ServerPlayerEntity player) {
		return ServerPlayNetworking.canSend(player, ID);
	}

	public void send(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, ID, toBuf());
	}
}
