# Creative Tab Pin-Up

Creative Tab Pin-Up is a client-side NeoForge mod for Minecraft 1.21.1 that lets you pin creative inventory tabs as persistent shortcuts.

## Features

- Pin up to 8 creative tabs without reordering or removing them from the normal creative inventory.
- The first 4 pinned tabs appear on the left side of the creative menu and the next 4 on the right.
- Pinned tabs stay visible from the inventory tab and from every NeoForge creative tab page.
- Pinned tabs keep their normal tab look with the background rotated for the side layout while the item icon stays upright.
- Hover any normal creative tab to reveal a pin button in its corner.
- Hover a pinned tab to reveal the crossed pin button used to unpin it.
- Selecting a pinned tab does not change the creative tab page you are currently viewing.
- Pins are stored globally on the client in `config/creativetabpinup.json`, so they persist across worlds and restarts.
- JEI and Filter Stamp integrations are optional and only activate when those mods are present.
- No server installation is required.

## Filter Stamp compatibility

When Filter Stamp is installed, pinned tabs avoid its compact creative-inventory drawer dynamically. Only pinned tabs whose rows intersect the visible stamp drawer move out of the way, using closed detached-tab sprites. Opening Filter Stamp's large selector hides the pinned shortcuts until the selector is closed. Fully hiding Filter Stamp restores the normal pinned-tab layout.

## Installation

1. Install NeoForge 21.1.226 or newer for Minecraft 1.21.1.
2. Put the Creative Tab Pin-Up jar in the client's `mods` folder.
3. Launch the game and open the creative inventory.

## Development

Build with Java 21:

```bash
./gradlew build
```

The development runtime includes the pinned Filter Stamp revision used by the compatibility layer. The built Creative Tab Pin-Up jar does not bundle Filter Stamp.

The built jar is written to `build/libs`.
