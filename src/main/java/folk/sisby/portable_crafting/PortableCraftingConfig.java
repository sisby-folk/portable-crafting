package folk.sisby.portable_crafting;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueMap;

import java.util.Map;

public class PortableCraftingConfig extends WrappedConfig {
	public final Map<String, Boolean> screensEnabled = ValueMap.builder(true).build();
}
