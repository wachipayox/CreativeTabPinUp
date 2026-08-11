package com.wachipayox.creativetabpinup.client;

import com.wachipayox.creativetabpinup.CreativeTabPinUp;
import com.wachipayox.creativetabpinup.mixin.CreativeModeInventoryScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = CreativeTabPinUp.MOD_ID, value = Dist.CLIENT)
public final class CreativeTabPinEvents {
    private CreativeTabPinEvents() {
    }

    @SubscribeEvent
    public static void onRender(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof CreativeModeInventoryScreen screen) {
            CreativeTabPinRenderer.render(screen, event.getGuiGraphics(), event.getMouseX(), event.getMouseY());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderInventoryMobEffects(ScreenEvent.RenderInventoryMobEffects event) {
        if (event.getScreen() instanceof CreativeModeInventoryScreen screen) {
            event.addHorizontalOffset(CreativeTabPinRenderer.getRightSideReservedWidth(screen));
        }
    }

    @SubscribeEvent
    public static void onMousePressed(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != 0 || !(event.getScreen() instanceof CreativeModeInventoryScreen screen)) {
            return;
        }

        CreativeTabPinRenderer.ClickTarget pinnedTarget = CreativeTabPinRenderer.findPinnedTarget(
                screen,
                event.getMouseX(),
                event.getMouseY()
        );

        if (pinnedTarget != null) {
            if (pinnedTarget.pinButton()) {
                PinnedTabStore.unpin(pinnedTarget.tab());
            } else {
                ((CreativeModeInventoryScreenAccessor) screen).creativetabpinup$selectTab(pinnedTarget.tab());
            }
            event.setCanceled(true);
            return;
        }

        CreativeModeTab normalTab = CreativeTabPinRenderer.findNormalPinTarget(
                screen,
                event.getMouseX(),
                event.getMouseY()
        );

        if (normalTab != null) {
            if (!PinnedTabStore.toggle(normalTab)) {
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.creativetabpinup.limit", PinnedTabStore.MAX_PINNED_TABS),
                        false
                );
            }
            event.setCanceled(true);
        }
    }
}
