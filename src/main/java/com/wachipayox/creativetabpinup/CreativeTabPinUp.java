package com.wachipayox.creativetabpinup;

import com.wachipayox.creativetabpinup.client.PinnedTabStore;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = CreativeTabPinUp.MOD_ID, dist = Dist.CLIENT)
public final class CreativeTabPinUp {
    public static final String MOD_ID = "creativetabpinup";

    public CreativeTabPinUp() {
        PinnedTabStore.load();
    }
}
