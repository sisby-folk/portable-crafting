package folk.sisby.portable_crafting;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueMap;

import java.util.Map;

public class PortableCraftingConfig extends WrappedConfig {
	public final Map<String, String> blockScreens = ValueMap.builder("")
		.put("minecraft:crafting_table", "minecraft:crafting")
		.put("minecraft:smithing_table", "minecraft:smithing")
		.put("minecraft:grindstone", "minecraft:grindstone")
		.put("minecraft:cartography_table", "minecraft:cartography_table")
		.put("minecraft:loom", "minecraft:loom")
		.put("minecraft:stonecutter", "minecraft:stonecutter")
		.put("minecraft:anvil", "minecraft:anvil")
		.build();
}
