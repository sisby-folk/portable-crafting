package folk.sisby.portable_crafting;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueMap;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PortableCraftingConfig extends WrappedConfig {
	public final Map<String, Boolean> screensEnabled = ValueMap.builder(true).build();

	public List<TagKey<Item>> getPortableTags() {
		return screensEnabled.keySet().stream().filter(screensEnabled::get).map(Identifier::tryParse).filter(Objects::nonNull).map(i ->  TagKey.of(RegistryKeys.ITEM, i)).toList();
	}
}
