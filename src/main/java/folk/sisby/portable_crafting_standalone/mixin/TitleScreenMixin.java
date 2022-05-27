package folk.sisby.portable_crafting_standalone.mixin;

import folk.sisby.portable_crafting_standalone.PortableCraftingStandalone;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "init", at = @At("TAIL"))
	public void onInit(CallbackInfo ci) {
		//PortableCraftingStandalone.LOGGER.info("This line is printed by an example mod mixin!");
	}
}
