package com.wachipayox.creativetabpinup.compat.filterstamp;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.neoforged.fml.ModList;

public final class FilterStampCompat {
    private static final String MOD_ID = "filter_stamp";

    private FilterStampCompat() {
    }

    public static Layout getLayout(CreativeModeInventoryScreen screen) {
        if (!ModList.get().isLoaded(MOD_ID)) {
            return Layout.HIDDEN;
        }
        return LoadedFilterStampCompat.getLayout(screen);
    }

    public enum Mode {
        HIDDEN,
        COMPACT,
        SELECTOR
    }

    public record Layout(Mode mode, Rect2i area) {
        static final Layout HIDDEN = new Layout(Mode.HIDDEN, new Rect2i(0, 0, 0, 0));

        public boolean hidesPinnedTabs() {
            return mode == Mode.SELECTOR;
        }

        public boolean hasCompactDrawer() {
            return mode == Mode.COMPACT;
        }
    }
}
