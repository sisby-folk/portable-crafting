package folk.sisby.portable_crafting.mixin;

import folk.sisby.portable_crafting.PortableCrafting;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {
	@Shadow
	public ServerPlayerEntity player;

	@Inject(method = "onClickSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;disableSyncing()V"), cancellable = true)
	private void openViaClick(ClickSlotC2SPacket packet, CallbackInfo ci) {
		ScreenHandler handler = player.currentScreenHandler;
		if (packet.getActionType() == SlotActionType.PICKUP && packet.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && handler.isValid(packet.getSlot()) && handler.getCursorStack().isEmpty()) {
			ItemStack stack = handler.slots.get(packet.getSlot()).getStack();
			if (stack.getCount() == 1) {
				if (PortableCrafting.openPortableCrafting(player, stack, true)) {
					if (!ServerPlayNetworking.canSend(player, PortableCrafting.S2C_SCREENS_ENABLED)) {
						handler.nextRevision();
						handler.updateToClient();
					}
					PortableCrafting.openPortableCrafting(player, stack, false);
					ci.cancel();
				}
			}
		}
	}
}
