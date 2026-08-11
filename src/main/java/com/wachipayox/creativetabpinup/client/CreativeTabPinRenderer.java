package com.wachipayox.creativetabpinup.client;

import com.mojang.math.Axis;
import com.wachipayox.creativetabpinup.mixin.CreativeModeInventoryScreenAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.CreativeTabsScreenPage;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;

public final class CreativeTabPinRenderer {
    private static final int SIDE_TAB_WIDTH = 32;
    private static final int SIDE_TAB_HEIGHT = 26;
    private static final int SIDE_TAB_VISIBLE_WIDTH = 28;
    private static final int SIDE_TAB_SPACING = 27;
    private static final int PIN_WIDTH = 13;
    private static final int PIN_HEIGHT = 15;
    private static final int NORMAL_PIN_GAP = -3;
    private static final int SIDE_PIN_GAP = 2;
    private static final int RIGHT_SIDE_RESERVED_WIDTH = SIDE_TAB_VISIBLE_WIDTH + SIDE_PIN_GAP + PIN_WIDTH;

    private CreativeTabPinRenderer() {
    }

    public static int getRightPinnedTabCount() {
        return Mth.clamp(PinnedTabStore.getPinnedTabs().size() - 4, 0, 4);
    }

    public static int getRightSideReservedWidth() {
        return getRightPinnedTabCount() > 0 ? RIGHT_SIDE_RESERVED_WIDTH : 0;
    }

    public static List<Rect2i> getRightPinnedTabExclusionAreas(CreativeModeInventoryScreen screen) {
        int rightPinnedTabs = getRightPinnedTabCount();
        List<Rect2i> areas = new ArrayList<>(rightPinnedTabs);

        for (int i = 0; i < rightPinnedTabs; i++) {
            Bounds body = pinnedTabBody(screen, 4 + i);
            areas.add(new Rect2i(body.x(), body.y(), RIGHT_SIDE_RESERVED_WIDTH, body.height()));
        }

        return areas;
    }

    public static void render(CreativeModeInventoryScreen screen, GuiGraphics graphics, int mouseX, int mouseY) {
        PinnedTabStore.cleanupInvalidTabs();

        List<CreativeModeTab> pinnedTabs = PinnedTabStore.getPinnedTabs();
        for (int i = 0; i < pinnedTabs.size(); i++) {
            renderPinnedTab(screen, graphics, pinnedTabs.get(i), i, mouseX, mouseY);
        }

        for (CreativeModeTab tab : screen.getCurrentPage().getVisibleTabs()) {
            if (tab.equals(CreativeModeTabs.searchTab())) continue;

            Bounds body = normalTabBody(screen, tab);
            Bounds pin = normalPinBounds(screen, tab);
            if (body.contains(mouseX, mouseY) || pin.contains(mouseX, mouseY)) {
                drawPinIcon(graphics, pin.x(), pin.y(), PinnedTabStore.isPinned(tab));
                break;
            }
        }
    }

    public static ClickTarget findPinnedTarget(CreativeModeInventoryScreen screen, double mouseX, double mouseY) {
        List<CreativeModeTab> pinnedTabs = PinnedTabStore.getPinnedTabs();
        for (int i = 0; i < pinnedTabs.size(); i++) {
            CreativeModeTab tab = pinnedTabs.get(i);
            if (pinnedPinBounds(screen, i).contains(mouseX, mouseY)) {
                return new ClickTarget(tab, true);
            }
            if (pinnedTabBody(screen, i).contains(mouseX, mouseY)) {
                return new ClickTarget(tab, false);
            }
        }
        return null;
    }

    public static CreativeModeTab findNormalPinTarget(CreativeModeInventoryScreen screen, double mouseX, double mouseY) {
        for (CreativeModeTab tab : screen.getCurrentPage().getVisibleTabs()) {
            if (tab.equals(CreativeModeTabs.searchTab())) continue;
            if (normalPinBounds(screen, tab).contains(mouseX, mouseY)) {
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
        Bounds pin = pinnedPinBounds(screen, index);
        int drawX = left ? body.x() : body.x() - 4;
        int drawY = body.y();

        CreativeTabsScreenPage page = pageFor(screen, tab);
        boolean selected = CreativeModeInventoryScreenAccessor.creativetabpinup$getSelectedTab() == tab;
        ResourceLocation sprite = tabSprite(page, tab, selected);

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 200.0F);
        drawClippedRotatedBackground(screen, graphics, sprite, drawX, drawY, left);
        ItemStack icon = tab.getIconItem();
        graphics.renderItem(icon, drawX + 8, drawY + 5);
        graphics.renderItemDecorations(Minecraft.getInstance().font, icon, drawX + 8, drawY + 5);
        graphics.pose().popPose();

        boolean bodyHovered = body.contains(mouseX, mouseY);
        if (bodyHovered || pin.contains(mouseX, mouseY)) {
            drawPinIcon(graphics, pin.x(), pin.y(), true);
        }
        if (bodyHovered) {
            graphics.renderTooltip(Minecraft.getInstance().font, tab.getDisplayName(), mouseX, mouseY);
        }
    }

    private static void drawClippedRotatedBackground(
            CreativeModeInventoryScreen screen,
            GuiGraphics graphics,
            ResourceLocation sprite,
            int x,
            int y,
            boolean left
    ) {
        int panelLeft = screen.getGuiLeft();
        int panelRight = panelLeft + screen.getXSize();
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        if (left) {
            graphics.enableScissor(0, 0, panelLeft, screenHeight);
        } else {
            graphics.enableScissor(panelRight, 0, screenWidth, screenHeight);
        }

        drawRotatedBackground(graphics, sprite, x, y, !left);
        graphics.disableScissor();
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

    private static ResourceLocation tabSprite(CreativeTabsScreenPage page, CreativeModeTab tab, boolean selected) {
        String state = selected ? "selected" : "unselected";
        int column = Mth.clamp(page.getColumn(tab), 0, 6) + 1;
        return ResourceLocation.withDefaultNamespace(
                "container/creative_inventory/tab_top_" + state + "_" + column
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

        CreativeTabsScreenPage page = screen.getCurrentPage();

        boolean top = page.isTop(tab);
        boolean supertop = !tab.isAlignedRight()
                && top
                && !page.getVisibleTabs().isEmpty()
                && page.getVisibleTabs().getFirst() == tab;

        int x = body.x() + (body.width() - PIN_WIDTH) / 2;
        int y = screen.getCurrentPage().isTop(tab)
                ? body.y() - NORMAL_PIN_GAP - PIN_HEIGHT
                : body.y() + body.height() + NORMAL_PIN_GAP;

        if (supertop) {
            x -= 22;
            y += 27;
        }

        return new Bounds(x, y, PIN_WIDTH, PIN_HEIGHT);
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

    private static Bounds pinnedPinBounds(CreativeModeInventoryScreen screen, int index) {
        Bounds body = pinnedTabBody(screen, index);
        boolean left = index < 4;
        int x = left
                ? body.x() - SIDE_PIN_GAP - PIN_WIDTH
                : body.x() + body.width() + SIDE_PIN_GAP;
        int y = body.y() + (body.height() - PIN_HEIGHT) / 2;
        return new Bounds(x, y, PIN_WIDTH, PIN_HEIGHT);
    }

    private static void drawPinIcon(GuiGraphics graphics, int x, int y, boolean crossed) {
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 400.0F);

        int outline = 0xFF1B1B1B;
        int shadow = 0xFF777777;
        int fill = 0xFFD8D8D8;
        int highlight = 0xFFFFFFFF;

        graphics.fill(x + 3, y, x + 10, y + 1, outline);
        graphics.fill(x + 2, y + 1, x + 11, y + 3, outline);
        graphics.fill(x + 1, y + 3, x + 12, y + 5, outline);
        graphics.fill(x + 4, y + 5, x + 9, y + 8, outline);
        graphics.fill(x + 1, y + 8, x + 12, y + 10, outline);
        graphics.fill(x + 5, y + 10, x + 8, y + 13, outline);
        graphics.fill(x + 6, y + 13, x + 7, y + 15, outline);

        graphics.fill(x + 3, y + 1, x + 10, y + 2, shadow);
        graphics.fill(x + 2, y + 3, x + 11, y + 4, fill);
        graphics.fill(x + 3, y + 3, x + 5, y + 4, highlight);
        graphics.fill(x + 5, y + 5, x + 8, y + 8, fill);
        graphics.fill(x + 5, y + 5, x + 6, y + 7, highlight);
        graphics.fill(x + 2, y + 8, x + 11, y + 9, fill);
        graphics.fill(x + 6, y + 10, x + 7, y + 13, fill);

        if (crossed) {
            int cross = 0xFFFF4A4A;
            for (int i = 0; i < 12; i++) {
                graphics.fill(x + i, y + i + 1, x + i + 2, y + i + 3, cross);
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
