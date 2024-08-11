package folk.sisby.portable_crafting;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueMap;

import java.util.Map;

public class PortableCraftingConfig extends WrappedConfig {
	@Comment("Items / screens to register for portable crafting")
	@Comment("Item ID - Screen Handler Type")
	@Comment("Items must be block items")
	@Comment("Blocks must actually open the specified screen handler type on use when placed, or this won't work")
	public final Map<String, String> blockItemScreens = ValueMap.builder("")
		.put("minecraft:crafting_table", "minecraft:crafting")
		.put("minecraft:smithing_table", "minecraft:smithing")
		.put("minecraft:grindstone", "minecraft:grindstone")
		.put("minecraft:cartography_table", "minecraft:cartography_table")
		.put("minecraft:loom", "minecraft:loom")
		.put("minecraft:stonecutter", "minecraft:stonecutter")
		.put("minecraft:anvil", "minecraft:anvil")
		.build();

	@Comment("Tags to accept for the items registered above")
	@Comment("Adding a Tag replaces the default by-ID item matching functionality")
	@Comment("Item Tag - Item ID")
	public final Map<String, String> blockItemTags = ValueMap.builder("")
		.put("c:player_workstations/crafting_tables", "minecraft:crafting_table")
		.build();
}
