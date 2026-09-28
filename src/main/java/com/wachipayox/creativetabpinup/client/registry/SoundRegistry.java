package com.wachipayox.creativetabpinup.client.registry;


import com.wachipayox.creativetabpinup.CreativeTabPinUp;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> soundRegistry =
            DeferredRegister.create(
                    BuiltInRegistries.SOUND_EVENT,
                    CreativeTabPinUp.MOD_ID
            );

    public static final DeferredHolder<SoundEvent, SoundEvent>
            PIN = soundRegistry.register(
            "pin",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CreativeTabPinUp.MOD_ID, "pin"))
    ),
            UNPIN = soundRegistry.register(
            "unpin",
            () -> SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(CreativeTabPinUp.MOD_ID, "unpin")
            )
    );
}
