# Changelog

## 1.0.1

- Clipped pinned tab backgrounds at the creative window edge so they no longer render over the main inventory panel.
- Kept pinned tabs above active-effect overlays without changing Minecraft's effect rendering.
- Replaced the pin control with a larger vertical pixel-art pushpin and crossed unpin state based on the SVG source in `art/pin-icons.svg`.
- Moved pin controls outside their tabs: above top tabs, below bottom tabs, and outward from left/right pinned tabs.
- Kept pin controls visible and clickable while moving the cursor between a tab and its external pin button.

## 1.0.0

- Replaced the NeoForge MDK example identity with Creative Tab Pin-Up.
- Converted the mod to a client-only implementation.
- Added persistent client-wide creative tab pin storage.
- Added up to 8 pinned creative tab shortcuts, with 4 on each side of the creative inventory.
- Added rotated vanilla-style tab backgrounds with upright tab item icons.
- Added hover pin and unpin controls for normal and pinned tabs.
- Kept normal creative tab ordering, visibility, and NeoForge page behavior unchanged.
- Added English and Spanish limit feedback.
