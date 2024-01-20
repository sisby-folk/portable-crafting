package folk.sisby.portable_crafting.mixin;

import folk.sisby.portable_crafting.PortableCrafting;
import folk.sisby.portable_crafting.PortableCraftingClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class MixinScreenHandler {
	@Shadow
	@Nullable
	private ScreenHandlerSyncHandler syncHandler;
	@Shadow
	private ItemStack previousCursorStack;

	@Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
	public void openCraftingTable(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
		ScreenHandler self = (ScreenHandler) (Object) this;
		if (actionType == SlotActionType.PICKUP && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && slotIndex >= 0 && slotIndex < self.slots.size()) {
			ItemStack stack = self.slots.get(slotIndex).getStack();
			if (stack.getCount() == 1) {
				if (player.getWorld().isClient()) {
					if (PortableCraftingClient.openPortableCrafting(stack, true)) {
						// previousCursorStack = self.getCursorStack().copy();
						// if (syncHandler != null) syncHandler.updateCursorStack(self, this.previousCursorStack);
						PortableCraftingClient.CHANGING_SCREENS = true;
						PortableCrafting.openPortableCrafting(player, stack, false);
						ci.cancel();
					}
				} else {
					if (PortableCrafting.openPortableCrafting(player, stack, true)) {
						// previousCursorStack = self.getCursorStack().copy();
						// if (syncHandler != null) syncHandler.updateCursorStack(self, this.previousCursorStack);
						PortableCrafting.openPortableCrafting(player, stack, false);
						ci.cancel();
					}
				}
			}
		}
	}
}
