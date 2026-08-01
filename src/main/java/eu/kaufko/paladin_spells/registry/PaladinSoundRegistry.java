package eu.kaufko.paladin_spells.registry;

import eu.kaufko.paladin_spells.PaladinSpells;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PaladinSoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PaladinSpells.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BULWARK =
            SOUND_EVENTS.register("bulwark",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "bulwark")));

    public static final DeferredHolder<SoundEvent, SoundEvent> TAUNT =
            SOUND_EVENTS.register("taunt",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "taunt")));

    public static final DeferredHolder<SoundEvent, SoundEvent> RAM =
            SOUND_EVENTS.register("ram",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "ram")));
}