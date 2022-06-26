package folk.sisby.portable_crafting_standalone.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import folk.sisby.portable_crafting_standalone.helper.LogHelper;

import java.util.Optional;

/**
 * A server message received on the client.
 * Annotate a ServerSender with the same ID.
 */
@SuppressWarnings("unused")
public abstract class ClientReceiver {
    protected Identifier id; // cached message ID
    protected int warnings = 0;

    public ClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(id(), this::handleInternal);
    }

    /**
     * Cache and fetch the message ID from the annotation.
     */
    protected Identifier id() {
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

    protected void handleInternal(MinecraftClient client, ClientPlayNetworkHandler listener, PacketByteBuf buffer, PacketSender sender) {
        debug("Received message `" + id + "` from server.");

        try {
            handle(client, buffer);
        } catch (Exception e) {
            if (warnings < 10) {
                debug("Exception when handling message from client: " + e.getMessage());
                warnings++;
            }
        }
    }

    /**
     * Handle the message reading from the buffer and then executing on the client.
     * If exceptions are thrown here then they are caught by handleInternal.
     */
    public abstract void handle(MinecraftClient client, PacketByteBuf buffer);

    /**
     * Convenience method to read a wrapped optional compound tag from a buffer.
     */
    public Optional<NbtCompound> getNbtCompound(PacketByteBuf buffer) {
        return Optional.ofNullable(buffer.readNbt());
    }
}
