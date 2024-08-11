<!--suppress HtmlDeprecatedTag, XmlDeprecatedElement -->
<center>
  <img src="https://cdn.modrinth.com/data/qmVRzDCY/images/cbe8676bda07c29d468b5d7a78b7f4e04bb4ea55.gif" alt="portable crafting preview"/><br/>
  Open crafting screens from your inventory.<br/>
  Works server-side, with extra polish when installed on the client.<br/>
  Requires <a href="https://modrinth.com/mod/connector">Connector</a> and <a href="https://modrinth.com/mod/forgified-fabric-api">FFAPI</a> on (neo)forge.<br/>
</center>

---

### Server Features

 - Open workstation blocks by right-clicking them in the inventory (or using them from the hotbar)
 - Supports crafting, smithing, stonecutting, grinding, looms, cartography tables, and anvils by default
 - Supports all vanilla and most modded non-container workstations via the config (see below)

### Mixed-Side Features
 - Opening workstations is seamless, with no ping-dependent "ghost pickup" while the screen is loading
 - Quickly open your crafting table via a hotkey (Default: `v`)
 - Swap to portable workstations using [Inventory Tabs](https://modrinth.com/mod/inventory-tabs)
> ![tabs preview](https://cdn.modrinth.com/data/qmVRzDCY/images/bba889be1d361cb9464db547c216367223c93707.png)

## Modpack Configuration

You add and change which workstations are portable via `config/portable_crafting.toml`.<br/>
Blocks _must_ open the specified screen handler on use, or this will not work.

```toml
[blockItemScreens]
	"minecraft:crafting_table" = "minecraft:crafting"
	"...block_item" = "...screen_handler"

[blockItemTags]
	"c:player_workstations/crafting_tables" = "minecraft:crafting_table"
	"...item_tag" = "...block_item_from_above"
```

Stateless modded blocks that follow the same conventions as vanilla's will work. Others may crash, so test as you go.<br/>
The config is synchronised to the client when mixed-side to display tabs and apply seam-hiding changes.

## Afterword

All mods are built on the work of many others.

This mod specifically is inspired by a similar feature from Svenhjol's Charm - one of the few implementations of portable crafting grids that didn't introduce new items at the time.

We made this mod for [Tinkerer's Quilt](https://modrinth.com/modpack/tinkerers-quilt) - our modpack about ease of play and vanilla content.

---

<center>
<b>Tinkerer's:</b> <a href="https://modrinth.com/modpack/tinkerers-quilt">Quilt</a> - <a href="https://modrinth.com/mod/tinkerers-smithing">Smithing</a> - <a href="https://modrinth.com/mod/origins-minus">Origins</a> - <a href="https://modrinth.com/mod/tinkerers-statures">Statures</a> - <a href="https://modrinth.com/mod/picohud">HUD</a><br/>
<b>Loveletters:</b> <a href="https://modrinth.com/mod/inventory-tabs">Tabs</a> - <a href="https://modrinth.com/mod/antique-atlas-4">Atlas</a> - <i>Portable Crafting</i> - <a href="https://modrinth.com/mod/drogstyle">Drogstyle</a><br/>
<b>Others:</b> <a href="https://modrinth.com/mod/switchy">Switchy</a> - <a href="https://modrinth.com/mod/crunchy-crunchy-advancements">Crunchy</a> - <a href="https://modrinth.com/mod/starcaller">Starcaller</a><br/>
</center>
