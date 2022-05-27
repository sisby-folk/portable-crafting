package folk.sisby.portable_crafting_standalone.module.portable_crafting.network;

import folk.sisby.portable_crafting_standalone.network.ClientSender;
import folk.sisby.portable_crafting_standalone.network.Id;

/**
 * Client sends empty message to ask server to open the crafting container.
 */
@Id("portable_crafting_standalone:open_crafting")
public class ClientSendOpenCrafting extends ClientSender { }
