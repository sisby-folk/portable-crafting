package folk.sisby.portable_crafting_standalone.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.Optional;

/**
 * A client message received on the server.
 * Annotate a ClientSender with the same ID.
 */
@SuppressWarnings("unused")
public abstract class ServerReceiver {
    private Identifier id; // cached message ID

    public ServerReceiver() {
        var id = id();
        ServerPlayNetworking.registerGlobalReceiver(id, this::handleInternal);
    }

    /**
     * Cache and fetch the message ID from the annotation.
     */
    private Identifier id() {
        if (id == null) {
            if (getClass().isAnnotationPresent(Id.class)) {
                var annotation = getClass().getAnnotation(Id.class);
                id = new Identifier(annotation.value());
            } else {
                throw new IllegalStateException("Missing ID for `" + getClass() + "`");
            }
        }

        return id;
    }

    private void handleInternal(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler listener, PacketByteBuf buffer, PacketSender sender) {
        var id = id();

        try {
            handle(server, player, buffer);
        } catch (Exception ignored) {
        }
    }
    /**
     * Handle the message reading from the buffer and then executing on the client.
     * If exceptions are thrown here then they are caught by handleInternal.
     */
    public abstract void handle(MinecraftServer server, ServerPlayerEntity player, PacketByteBuf buffer);

    /**
     * Convenience method to read a wrapped optional compound tag from a buffer.
     */
    public Optional<NbtCompound> getNbtCompound(PacketByteBuf buffer) {
        return Optional.ofNullable(buffer.readNbt());
    }
}
