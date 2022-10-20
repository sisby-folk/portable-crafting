package folk.sisby.portable_crafting.compat.inventory_tabs;

import com.kqp.inventorytabs.api.TabProviderRegistry;
import com.kqp.inventorytabs.tabs.tab.Tab;
import folk.sisby.portable_crafting.PortableCrafting;
import folk.sisby.portable_crafting.PortableCraftingClient;
import folk.sisby.portable_crafting.screens.PortableCraftingScreen;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PortableCraftingTab extends Tab {
	public static final Identifier ID = new Identifier(PortableCrafting.ID, "portable_crafting_tab");

	static {
		TabProviderRegistry.register(ID, new PortableCraftingTabProvider());
	}

	protected PortableCraftingTab() {
		super(new ItemStack(Blocks.CRAFTING_TABLE));
	}

	public static void touch() {
	}

	@Override
	public void open() {
		PortableCraftingClient.openCraftingTable();
	}

	@Override
	public boolean shouldBeRemoved() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		return (player == null || !PortableCrafting.is_allowed(player));
	}

	@Override
	public Text getHoverText() {
		return PortableCraftingScreen.LABEL;
	}
}
