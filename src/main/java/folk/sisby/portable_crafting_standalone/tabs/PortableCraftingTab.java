package folk.sisby.portable_crafting_standalone.tabs;

import com.kqp.inventorytabs.tabs.tab.Tab;
import folk.sisby.portable_crafting_standalone.module.portable_crafting.PortableCraftingClient;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PortableCraftingTab extends Tab {

	protected PortableCraftingTab() {
		super(new ItemStack(Blocks.CRAFTING_TABLE));
	}

	@Override
	public void open() {
		PortableCraftingClient.openCraftingTable();
	}

	@Override
	public boolean shouldBeRemoved() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		return (player == null || !player.getInventory().contains(new ItemStack(Blocks.CRAFTING_TABLE)));
	}

	@Override
	public Text getHoverText() {
		return new TranslatableText("container.portable_crafting_standalone.portable_crafting_table");
	}
}
