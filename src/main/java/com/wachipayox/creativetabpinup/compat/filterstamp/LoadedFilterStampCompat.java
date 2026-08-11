package com.wachipayox.creativetabpinup.compat.filterstamp;

import com.wachi.filterstamp.client.gui.inventory.FilterStampInventoryOverlay;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;

final class LoadedFilterStampCompat {
    private static final int COMPACT_PANEL_WIDTH = 40;

    private LoadedFilterStampCompat() {
    }

    static FilterStampCompat.Layout getLayout(CreativeModeInventoryScreen screen) {
        return FilterStampInventoryOverlay.getWidget(screen)
                .map(widget -> classify(widget.getExtraArea()))
                .orElse(FilterStampCompat.Layout.HIDDEN);
    }

    private static FilterStampCompat.Layout classify(Rect2i area) {
        if (area.getWidth() <= 0 || area.getHeight() <= 0) {
            return FilterStampCompat.Layout.HIDDEN;
        }

        FilterStampCompat.Mode mode = area.getWidth() > COMPACT_PANEL_WIDTH
                ? FilterStampCompat.Mode.SELECTOR
                : FilterStampCompat.Mode.COMPACT;
        return new FilterStampCompat.Layout(mode, area);
    }
}
