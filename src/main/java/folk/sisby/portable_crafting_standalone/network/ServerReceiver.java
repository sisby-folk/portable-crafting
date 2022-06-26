package folk.sisby.portable_crafting_standalone.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import folk.sisby.portable_crafting_standalone.helper.LogHelper;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * A client message received on the server.
 * Annotate a ClientSender with the same ID.
 */
@SuppressWarnings("unused")
public abstract class ServerReceiver {
    private int warnings = 0;
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

    protected void debug(String message) {
        if (showDebugMessages()) {
            LogHelper.debug(getClass(), message);
        }
    }

    protected boolean showDebugMessages() {
        return true;
    }

    private void handleInternal(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler listener, PacketByteBuf buffer, PacketSender sender) {
        var id = id();
        debug("Received message `" + id + "` from client " + player.getUuid() + ".");

        try {
            handle(server, player, buffer);
        } catch (Exception e) {
            if (warnings < 10) {
                debug("Exception when handling message from server: " + e.getMessage());
                warnings++;
            }
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
