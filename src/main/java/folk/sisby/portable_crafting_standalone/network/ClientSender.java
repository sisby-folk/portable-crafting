package folk.sisby.portable_crafting_standalone.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * A message sent from a client to the server.
 */
@SuppressWarnings("unused")
public abstract class ClientSender {
    protected Identifier id; // cached message ID

    /**
     * Send an empty message to the server.
     * Typically this is used to request the server send some specific data.
     */
    public void send() {
        send(null);
    }

    /**
     * Send message to server with packet data.
     */
    public void send(@Nullable Consumer<PacketByteBuf> callback) {
        var id = id();
        var buffer = new PacketByteBuf(Unpooled.buffer());

        if (callback != null) {
            callback.accept(buffer);
        }

        ClientPlayNetworking.send(id, buffer);
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
}
