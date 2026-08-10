package com.wachipayox.creativetabpinup.mixin;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreativeModeInventoryScreen.class)
public interface CreativeModeInventoryScreenAccessor {
    @Invoker("selectTab")
    void creativetabpinup$selectTab(CreativeModeTab tab);

    @Accessor("selectedTab")
    static CreativeModeTab creativetabpinup$getSelectedTab() {
        throw new AssertionError();
    }
}
