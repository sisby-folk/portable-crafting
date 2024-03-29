package folk.sisby.portable_crafting.mixin;

import folk.sisby.portable_crafting.PortableCraftingClient;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayInteractionManager {
	@Shadow @Final private ClientPlayNetworkHandler networkHandler;

	@Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
	private void clickSlotOpenPortable(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
		ScreenHandler handler = player.currentScreenHandler;
		if (actionType == SlotActionType.PICKUP && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && handler.isValid(slotId) && handler.getCursorStack().isEmpty()) {
			ItemStack stack = handler.slots.get(slotId).getStack();
			if (stack.getCount() == 1) {
				if (PortableCraftingClient.openPortableCrafting(stack, true)) {
					if (handler != player.playerScreenHandler) PortableCraftingClient.CHANGING_SCREENS = true;

					networkHandler.sendPacket(new ClickSlotC2SPacket(
						handler.syncId,
						handler.getRevision(),
						slotId,
						button,
						actionType,
						handler.getCursorStack().copy(),
						new Int2ObjectOpenHashMap<>(Map.of(slotId, handler.getSlot(slotId).getStack().copy()))
					));

					MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

					ci.cancel();
				}
			}
		}
	}
}
