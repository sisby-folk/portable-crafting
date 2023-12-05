package folk.sisby.portable_crafting.mixin;

import folk.sisby.portable_crafting.PortableCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.CraftingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingScreenHandler.class)
public class MixinCraftingScreenHandler {
	@Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
	public void allowUsingCraftingTables(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (PortableCrafting.canUse(player)) {
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
}
