# Changelog

## 1.0.4

- Added optional Filter Stamp compatibility without making Filter Stamp a runtime requirement.
- Added Filter Stamp to the development runtime at the compatibility-tested repository revision.
- Dynamically move only pinned tabs that overlap Filter Stamp's compact drawer out of its way.
- Added detached closed-tab sprites for shifted pinned tabs, including a selected state based on the provided assets.
- Hide lateral pinned shortcuts while Filter Stamp's large selector is open and restore the normal layout when Filter Stamp is fully hidden.
- Disable right-side JEI/effect spacing for pinned shortcuts while Filter Stamp's large selector has those shortcuts hidden.

## 1.0.3

- Updated the mod icon and author metadata.
- Refined left pinned-tab positioning.
- Added subtle item-icon positioning differences for selected and unselected pinned tabs.

## 1.0.2

- Moved creative inventory effects 43 pixels to the right whenever at least one pinned tab is present on the right side.
- Added optional JEI integration through its GUI exclusion API.
- Reserved one precise 43x26 JEI exclusion area for each visible right-side pinned tab instead of reserving a full fixed column.
- Replaced JEI's built-in effect exclusion handler on supported JEI versions so the reserved effect area follows the shifted effect renderer.
- Preserved normal JEI effect exclusions on other inventory screens.

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
