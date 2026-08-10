package com.wachipayox.creativetabpinup.client;

import com.mojang.math.Axis;
import com.wachipayox.creativetabpinup.mixin.CreativeModeInventoryScreenAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.CreativeTabsScreenPage;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;

public final class CreativeTabPinRenderer {
    private static final int SIDE_TAB_WIDTH = 32;
    private static final int SIDE_TAB_HEIGHT = 26;
    private static final int SIDE_TAB_VISIBLE_WIDTH = 28;
    private static final int SIDE_TAB_SPACING = 27;
    private static final int PIN_SIZE = 8;

    private CreativeTabPinRenderer() {
    }

    public static void render(CreativeModeInventoryScreen screen, GuiGraphics graphics, int mouseX, int mouseY) {
        PinnedTabStore.cleanupInvalidTabs();

        List<CreativeModeTab> pinnedTabs = PinnedTabStore.getPinnedTabs();
        for (int i = 0; i < pinnedTabs.size(); i++) {
            renderPinnedTab(screen, graphics, pinnedTabs.get(i), i, mouseX, mouseY);
        }

        for (CreativeModeTab tab : screen.getCurrentPage().getVisibleTabs()) {
            Bounds body = normalTabBody(screen, tab);
            if (body.contains(mouseX, mouseY)) {
                Bounds pin = normalPinBounds(screen, tab);
                drawPinIcon(graphics, pin.x(), pin.y(), PinnedTabStore.isPinned(tab));
                break;
            }
        }
    }

    public static ClickTarget findPinnedTarget(CreativeModeInventoryScreen screen, double mouseX, double mouseY) {
        List<CreativeModeTab> pinnedTabs = PinnedTabStore.getPinnedTabs();
        for (int i = 0; i < pinnedTabs.size(); i++) {
            CreativeModeTab tab = pinnedTabs.get(i);
            Bounds body = pinnedTabBody(screen, i);
            if (body.contains(mouseX, mouseY)) {
                return new ClickTarget(tab, pinnedPinBounds(screen, tab, i).contains(mouseX, mouseY));
            }
        }
        return null;
    }

    public static CreativeModeTab findNormalPinTarget(CreativeModeInventoryScreen screen, double mouseX, double mouseY) {
        for (CreativeModeTab tab : screen.getCurrentPage().getVisibleTabs()) {
            Bounds body = normalTabBody(screen, tab);
            Bounds pin = normalPinBounds(screen, tab);
            if (body.contains(mouseX, mouseY) && pin.contains(mouseX, mouseY)) {
                return tab;
            }
        }
        return null;
    }

    private static void renderPinnedTab(
            CreativeModeInventoryScreen screen,
            GuiGraphics graphics,
            CreativeModeTab tab,
            int index,
            int mouseX,
            int mouseY
    ) {
        boolean left = index < 4;
        Bounds body = pinnedTabBody(screen, index);
        int drawX = left ? body.x() : body.x() - 4;
        int drawY = body.y();

        CreativeTabsScreenPage page = pageFor(screen, tab);
        boolean top = page.isTop(tab);
        boolean positiveRotation = positiveRotation(left, top);
        boolean selected = CreativeModeInventoryScreenAccessor.creativetabpinup$getSelectedTab() == tab;
        ResourceLocation sprite = tabSprite(page, tab, selected);

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 200.0F);
        drawRotatedBackground(graphics, sprite, drawX, drawY, positiveRotation);
        ItemStack icon = tab.getIconItem();
        graphics.renderItem(icon, drawX + 8, drawY + 5);
        graphics.renderItemDecorations(Minecraft.getInstance().font, icon, drawX + 8, drawY + 5);
        graphics.pose().popPose();

        if (body.contains(mouseX, mouseY)) {
            Bounds pin = pinnedPinBounds(screen, tab, index);
            drawPinIcon(graphics, pin.x(), pin.y(), true);
            graphics.renderTooltip(Minecraft.getInstance().font, tab.getDisplayName(), mouseX, mouseY);
        }
    }

    private static void drawRotatedBackground(
            GuiGraphics graphics,
            ResourceLocation sprite,
            int x,
            int y,
            boolean positiveRotation
    ) {
        graphics.pose().pushPose();
        if (positiveRotation) {
            graphics.pose().translate(x + SIDE_TAB_WIDTH, y, 0.0F);
            graphics.pose().mulPose(Axis.ZP.rotationDegrees(90.0F));
        } else {
            graphics.pose().translate(x, y + SIDE_TAB_HEIGHT, 0.0F);
            graphics.pose().mulPose(Axis.ZP.rotationDegrees(-90.0F));
        }
        graphics.blitSprite(sprite, 0, 0, 26, 32);
        graphics.pose().popPose();
    }

    private static boolean positiveRotation(boolean left, boolean top) {
        return left != top;
    }

    private static ResourceLocation tabSprite(CreativeTabsScreenPage page, CreativeModeTab tab, boolean selected) {
        String row = page.isTop(tab) ? "top" : "bottom";
        String state = selected ? "selected" : "unselected";
        int column = Mth.clamp(page.getColumn(tab), 0, 6) + 1;
        return ResourceLocation.withDefaultNamespace(
                "container/creative_inventory/tab_" + row + "_" + state + "_" + column
        );
    }

    private static CreativeTabsScreenPage pageFor(CreativeModeInventoryScreen screen, CreativeModeTab tab) {
        CreativeTabsScreenPage currentPage = screen.getCurrentPage();
        if (currentPage.getVisibleTabs().contains(tab)) {
            return currentPage;
        }

        List<CreativeModeTab> tabs = CreativeModeTabRegistry.getSortedCreativeModeTabs().stream()
                .filter(CreativeModeTab::hasAnyItems)
                .toList();

        for (int i = 0; i < tabs.size(); i += 10) {
            CreativeTabsScreenPage page = new CreativeTabsScreenPage(
                    new ArrayList<>(tabs.subList(i, Math.min(i + 10, tabs.size())))
            );
            if (page.getVisibleTabs().contains(tab)) {
                return page;
            }
        }

        return currentPage;
    }

    private static Bounds normalTabBody(CreativeModeInventoryScreen screen, CreativeModeTab tab) {
        CreativeTabsScreenPage page = screen.getCurrentPage();
        int column = page.getColumn(tab);
        int x = 27 * column;
        if (tab.isAlignedRight()) {
            x = screen.getXSize() - 27 * (7 - column) + 1;
        }

        int y = page.isTop(tab) ? -32 : screen.getYSize();
        return new Bounds(screen.getGuiLeft() + x, screen.getGuiTop() + y, 26, 32);
    }

    private static Bounds normalPinBounds(CreativeModeInventoryScreen screen, CreativeModeTab tab) {
        Bounds body = normalTabBody(screen, tab);
        int y = screen.getCurrentPage().isTop(tab) ? body.y() + 6 : body.y() + 2;
        return new Bounds(body.x() + 17, y, PIN_SIZE, PIN_SIZE);
    }

    private static Bounds pinnedTabBody(CreativeModeInventoryScreen screen, int index) {
        boolean left = index < 4;
        int sideIndex = index % 4;
        int totalHeight = SIDE_TAB_HEIGHT + 3 * SIDE_TAB_SPACING;
        int y = screen.getGuiTop() + (screen.getYSize() - totalHeight) / 2 + sideIndex * SIDE_TAB_SPACING;
        int x = left
                ? screen.getGuiLeft() - SIDE_TAB_VISIBLE_WIDTH
                : screen.getGuiLeft() + screen.getXSize();
        return new Bounds(x, y, SIDE_TAB_VISIBLE_WIDTH, SIDE_TAB_HEIGHT);
    }

    private static Bounds pinnedPinBounds(CreativeModeInventoryScreen screen, CreativeModeTab tab, int index) {
        Bounds body = pinnedTabBody(screen, index);
        boolean positiveRotation = positiveRotation(index < 4, pageFor(screen, tab).isTop(tab));
        if (positiveRotation) {
            return new Bounds(
                    body.x() + body.width() - PIN_SIZE - 3,
                    body.y() + body.height() - PIN_SIZE - 3,
                    PIN_SIZE,
                    PIN_SIZE
            );
        }
        return new Bounds(body.x() + 3, body.y() + 3, PIN_SIZE, PIN_SIZE);
    }

    private static void drawPinIcon(GuiGraphics graphics, int x, int y, boolean crossed) {
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 400.0F);

        int outline = 0xFF202020;
        int fill = 0xFFF0F0F0;
        graphics.fill(x + 2, y + 1, x + 6, y + 2, outline);
        graphics.fill(x + 1, y + 2, x + 7, y + 3, outline);
        graphics.fill(x + 3, y + 3, x + 5, y + 6, outline);
        graphics.fill(x + 2, y + 5, x + 6, y + 6, outline);
        graphics.fill(x + 3, y + 6, x + 4, y + 8, outline);
        graphics.fill(x + 3, y + 2, x + 5, y + 5, fill);

        if (crossed) {
            int cross = 0xFFFF5555;
            for (int i = 0; i < 7; i++) {
                graphics.fill(x + i, y + i, x + i + 2, y + i + 2, cross);
            }
        }

        graphics.pose().popPose();
    }

    public record ClickTarget(CreativeModeTab tab, boolean pinButton) {
    }

    private record Bounds(int x, int y, int width, int height) {
        private boolean contains(double mouseX, double mouseY) {
            return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        }
    }
}
