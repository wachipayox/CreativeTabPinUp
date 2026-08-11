package com.wachipayox.creativetabpinup.compat.jei;

import com.wachipayox.creativetabpinup.client.CreativeTabPinRenderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.ClientHooks;

public final class CreativeTabPinUpJeiHandler<T extends AbstractContainerMenu>
        implements IGuiContainerHandler<EffectRenderingInventoryScreen<T>> {
    private static final int COMPACT_EFFECT_WIDTH = 32;

    @Override
    public List<Rect2i> getGuiExtraAreas(EffectRenderingInventoryScreen<T> screen) {
        List<Rect2i> areas = new ArrayList<>();
        int rightSideOffset = 0;

        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            areas.addAll(CreativeTabPinRenderer.getRightPinnedTabExclusionAreas(creativeScreen));
            rightSideOffset = CreativeTabPinRenderer.getRightSideReservedWidth(creativeScreen);
        }

        addEffectAreas(screen, areas, rightSideOffset);
        return areas;
    }

    private static void addEffectAreas(
            EffectRenderingInventoryScreen<?> screen,
            List<Rect2i> areas,
            int horizontalOffset
    ) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        Collection<MobEffectInstance> activeEffects = player.getActiveEffects();
        if (activeEffects.isEmpty()) {
            return;
        }

        int effectSpacing = 33;
        if (activeEffects.size() > 5) {
            effectSpacing = 132 / (activeEffects.size() - 1);
        }

        int x = screen.getGuiLeft() + screen.getXSize() + 2 + horizontalOffset;
        int y = screen.getGuiTop();
        List<MobEffectInstance> visibleEffects = activeEffects.stream()
                .filter(ClientHooks::shouldRenderEffect)
                .sorted()
                .toList();

        for (MobEffectInstance ignored : visibleEffects) {
            areas.add(new Rect2i(x, y, COMPACT_EFFECT_WIDTH, effectSpacing));
            y += effectSpacing;
        }
    }
}
