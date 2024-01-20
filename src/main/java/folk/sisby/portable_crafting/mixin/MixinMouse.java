package folk.sisby.portable_crafting.mixin;

import folk.sisby.portable_crafting.PortableCraftingClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse {
	@Inject(method = "lockCursor", at = @At("HEAD"), cancellable = true)
	public void keepCursorWhenChangingTabs(CallbackInfo ci) {
		if (PortableCraftingClient.CHANGING_SCREENS) {
			PortableCraftingClient.CHANGING_SCREENS = false;
			ci.cancel();
		}
	}
}

