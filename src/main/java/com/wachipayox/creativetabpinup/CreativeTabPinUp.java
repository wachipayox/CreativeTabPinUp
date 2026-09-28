package com.wachipayox.creativetabpinup;

import com.wachipayox.creativetabpinup.client.PinnedTabStore;
import com.wachipayox.creativetabpinup.client.registry.SoundRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = CreativeTabPinUp.MOD_ID, dist = Dist.CLIENT)
public final class CreativeTabPinUp {
    public static final String MOD_ID = "creativetabpinup";

    public CreativeTabPinUp(IEventBus eventBus) {
        SoundRegistry.soundRegistry.register(eventBus);
        PinnedTabStore.load();
    }
}
