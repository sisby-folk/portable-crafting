package folk.sisby.portable_crafting.mixin;

import folk.sisby.portable_crafting.PortableCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class MixinItemStack {
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	public void allowUsingCraftingTables(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		ItemStack self = (ItemStack) (Object) this;
		if (self.isIn(PortableCrafting.CRAFTING_TABLES)) {
			PortableCrafting.openCrafting(player);
			cir.setReturnValue(TypedActionResult.success(self, false));
			cir.cancel();
		}
	}
}
