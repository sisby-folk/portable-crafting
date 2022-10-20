package folk.sisby.portable_crafting_standalone.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * A message sent from the server to a client or all connected clients.
 */
@SuppressWarnings("unused")
public abstract class ServerSender {
    protected Identifier id; // cached message ID

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

    protected boolean showDebugMessages() {
        return true;
    }

    /**
     * Send an empty message to a player client.
     * Typically this is used to request that the client perform a specific action.
     */
    public void send(ServerPlayerEntity player) {
        send(player, null);
    }

    /**
     * Send message with packet data to a player client.
     */
    public void send(ServerPlayerEntity player, @Nullable Consumer<PacketByteBuf> callback) {
        var id = id();
        var buffer = new PacketByteBuf(Unpooled.buffer());

        if (callback != null) {
            callback.accept(buffer);
        }

        ServerPlayNetworking.send(player, id, buffer);
    }

    /**
     * Send an empty message to all connected player clients.
     */
    public void sendToAll(MinecraftServer server) {
        sendToAll(server, null);
    }

    /**
     * Send message with packet data to all connected player clients.
     */
    public void sendToAll(MinecraftServer server, @Nullable Consumer<PacketByteBuf> callback) {
        var playerList = server.getPlayerManager();
        playerList.getPlayerList().forEach(player -> send(player, callback));
    }
}
