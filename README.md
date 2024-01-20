<!--suppress HtmlDeprecatedTag, XmlDeprecatedElement -->
<center>
  <img src="https://cdn.modrinth.com/data/qmVRzDCY/images/cbe8676bda07c29d468b5d7a78b7f4e04bb4ea55.gif" alt="portable crafting preview"/><br/>
  Open crafting screens from your inventory.<br/>
  Requires <a href="https://modrinth.com/mod/connector">Connector</a> and <a href="https://modrinth.com/mod/forgified-fabric-api">FFAPI</a> on forge.<br/>
  Works well with <a href="https://modrinth.com/mod/inventory-tabs">Inventory Tabs</a> and <a href="https://modrinth.com/mod/emi">EMI</a>.
</center>

---

Portable Crafting operates primarily on the server-side. It allows you to:

 - Right-click crafting stations in the inventory to open them.
   - This includes crafting tables, smithing tables, stonecutters, grindstones, looms, and cartography tables.
   - Alternatively, you can use crafting stations without targeting a block from the hand.
 - Freely toggle each station via the config on the server side.
 - Quick-open a crafting table from anywhere in your inventory using a hotkey (Default: `v`) when installed on the client.
 - Access crafting stations directly via tabs when [Inventory Tabs](https://modrinth.com/mod/inventory-tabs) is installed.

<center>
    <img src="https://cdn.modrinth.com/data/qmVRzDCY/images/12deabe833db2a8507bc798b662e7a54373e41e4.png" alt="portable crafting tab preview"/><br/>
</center>

### Addons

Additional crafting stations can be registered through `PortableCrafting.registerCraftingScreen()`, providing an item tag, screen handler type, and screen handler factory.

The modrinth maven can be used to set up portable crafting as a dependency.

## Afterword

All mods are built on the work of many others.

This mod specifically is inspired by a similar feature from Svenhjol's Charm - one of the few implementations of portable crafting grids that didn't introduce new items at the time.

We made this mod for [Tinkerer's Quilt](https://modrinth.com/modpack/tinkerers-quilt) - our modpack about ease of play and vanilla content.
