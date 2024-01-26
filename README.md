<!--suppress HtmlDeprecatedTag, XmlDeprecatedElement -->
<center>
  <img src="https://cdn.modrinth.com/data/qmVRzDCY/images/cbe8676bda07c29d468b5d7a78b7f4e04bb4ea55.gif" alt="portable crafting preview"/><br/>
  Open crafting screens from your inventory.<br/>
  Works server-side, with extra polish when installed on the client.<br/>
  Requires <a href="https://modrinth.com/mod/connector">Connector</a> and <a href="https://modrinth.com/mod/forgified-fabric-api">FFAPI</a> on forge.<br/>
</center>

---

Portable Crafting allows you to:

 - Open crafting stations by right-clicking them in the inventory (or using them from the hotbar)
   - Supports crafting, smithing, stonecutting, grinding, looms, cartography tables, and anvils (off by default)
 - Enable and disable portable crafting stations via the config (`config/portable-crafting.toml`)
 - Quickly open your crafting table via a hotkey (Default: `v`)

Crafting stations are also shown as tabs when installed alongside [Inventory Tabs](https://modrinth.com/mod/inventory-tabs).

![tabs preview](https://cdn.modrinth.com/data/qmVRzDCY/images/bba889be1d361cb9464db547c216367223c93707.png)

### Addons

```groovy
repositories {
    maven { url "https://api.modrinth.com/maven" }
}
dependencies {
    modCompileOnly "maven.modrinth:portable-crafting:2.2.0+1.19"
}
```

Additional crafting stations can be registered through `PortableCrafting.registerCraftingScreen()`, providing an item tag, screen handler type, and screen handler factory.

## Afterword

All mods are built on the work of many others.

This mod specifically is inspired by a similar feature from Svenhjol's Charm - one of the few implementations of portable crafting grids that didn't introduce new items at the time.

We made this mod for [Tinkerer's Quilt](https://modrinth.com/modpack/tinkerers-quilt) - our modpack about ease of play and vanilla content.
