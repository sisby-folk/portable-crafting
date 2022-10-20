package folk.sisby.portable_crafting_standalone.mixin.compat.inventorytabs;

import com.kqp.inventorytabs.tabs.TabManager;
import folk.sisby.portable_crafting_standalone.PortableCraftingStandalone;
import folk.sisby.portable_crafting_standalone.module.portable_crafting.PortableCraftingScreenHandler;
import folk.sisby.portable_crafting_standalone.tabs.PortableCraftingTab;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreen.class)
public abstract class CraftingScreenTabMixin extends HandledScreen<PortableCraftingScreenHandler> {

	public CraftingScreenTabMixin(PortableCraftingScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "init", at = @At("TAIL"))
	public void onInit(CallbackInfo ci) {
			TabManager tabManager = TabManager.getInstance();
			 if (this.title == PortableCraftingStandalone.LABEL) {
				tabManager.tabs.stream().filter(tab -> tab instanceof PortableCraftingTab).findFirst().ifPresent(tabManager::onOpenTab);
			}
	}
}
