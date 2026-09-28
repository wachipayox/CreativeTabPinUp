# Creative Tab Pin-Up

Creative Tab Pin-Up is a client-side NeoForge mod for Minecraft 1.21.1 that lets you pin creative inventory tabs as persistent shortcuts.

## Features

- Pin up to 8 creative tabs without reordering or removing them from the normal creative inventory.
- Hover any normal creative tab to reveal a pin button in its corner.
- Hover a pinned tab to reveal the crossed pin button used to unpin it.
- Pins are stored globally on the client in `config/creativetabpinup.json`, so they persist across worlds and restarts.
- JEI and Filter Stamp integrations
- Fully client side

## Filter Stamp compatibility

When Filter Stamp is installed, pinned tabs avoid its compact creative-inventory drawer dynamically. Only pinned tabs whose rows intersect the visible stamp drawer move out of the way, using closed detached-tab sprites. Opening Filter Stamp's large selector hides the pinned shortcuts until the selector is closed. Fully hiding Filter Stamp restores the normal pinned-tab layout.
