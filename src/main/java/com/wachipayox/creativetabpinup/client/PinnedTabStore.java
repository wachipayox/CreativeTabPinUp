package com.wachipayox.creativetabpinup.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wachipayox.creativetabpinup.CreativeTabPinUp;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;

public final class PinnedTabStore {
    public static final int MAX_PINNED_TABS = 8;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final List<ResourceLocation> PINNED_TABS = new ArrayList<>();
    private static boolean loaded;
    private static boolean cleaned;

    private PinnedTabStore() {
    }

    public static void load() {
        if (loaded) {
            return;
        }

        loaded = true;
        Path file = configFile();
        if (!Files.exists(file)) {
            return;
        }

        try {
            JsonObject root = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonArray tabs = root.getAsJsonArray("pinnedTabs");
            if (tabs == null) {
                return;
            }

            for (JsonElement element : tabs) {
                ResourceLocation id = ResourceLocation.tryParse(element.getAsString());
                if (id != null && !PINNED_TABS.contains(id) && PINNED_TABS.size() < MAX_PINNED_TABS) {
                    PINNED_TABS.add(id);
                }
            }
        } catch (Exception ignored) {
            PINNED_TABS.clear();
        }
    }

    public static void cleanupInvalidTabs() {
        ensureLoaded();
        if (cleaned) {
            return;
        }

        cleaned = true;
        if (PINNED_TABS.removeIf(id -> CreativeModeTabRegistry.getTab(id) == null)) {
            save();
        }
    }

    public static List<CreativeModeTab> getPinnedTabs() {
        ensureLoaded();
        return PINNED_TABS.stream()
                .map(CreativeModeTabRegistry::getTab)
                .filter(tab -> tab != null && tab.shouldDisplay())
                .toList();
    }

    public static boolean isPinned(CreativeModeTab tab) {
        ensureLoaded();
        ResourceLocation id = CreativeModeTabRegistry.getName(tab);
        return id != null && PINNED_TABS.contains(id);
    }

    public static boolean toggle(CreativeModeTab tab) {
        ensureLoaded();
        ResourceLocation id = CreativeModeTabRegistry.getName(tab);
        if (id == null) {
            return true;
        }

        if (PINNED_TABS.remove(id)) {
            save();
            return true;
        }

        if (PINNED_TABS.size() >= MAX_PINNED_TABS) {
            return false;
        }

        PINNED_TABS.add(id);
        save();
        return true;
    }

    public static void unpin(CreativeModeTab tab) {
        ensureLoaded();
        ResourceLocation id = CreativeModeTabRegistry.getName(tab);
        if (id != null && PINNED_TABS.remove(id)) {
            save();
        }
    }

    private static void ensureLoaded() {
        if (!loaded) {
            load();
        }
    }

    private static void save() {
        JsonArray tabs = new JsonArray();
        PINNED_TABS.forEach(id -> tabs.add(id.toString()));

        JsonObject root = new JsonObject();
        root.add("pinnedTabs", tabs);

        Path file = configFile();
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    private static Path configFile() {
        return FMLPaths.CONFIGDIR.get().resolve(CreativeTabPinUp.MOD_ID + ".json");
    }
}
