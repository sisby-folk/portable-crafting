package folk.sisby.portable_crafting.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import folk.sisby.portable_crafting.PortableCrafting;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;canUse(Lnet/minecraft/entity/player/PlayerEntity;)Z"))
	private boolean applyCanUse(boolean original)
	{
		return original || PortableCrafting.canUse((PlayerEntity) (Object) this);
	}
}
