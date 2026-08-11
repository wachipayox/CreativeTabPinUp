package com.wachipayox.creativetabpinup.compat.jei;

import com.wachipayox.creativetabpinup.CreativeTabPinUp;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IJeiFeatures;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public final class CreativeTabPinUpJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(CreativeTabPinUp.MOD_ID, "jei");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void configureJei(IJeiFeatures jeiFeatures) {
        jeiFeatures.disableInventoryEffectRendererGuiHandler();
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(
                EffectRenderingInventoryScreen.class,
                new CreativeTabPinUpJeiHandler<>()
        );
    }
}
