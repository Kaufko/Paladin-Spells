package eu.kaufko.paladin_spells.registry;

import eu.kaufko.paladin_spells.PaladinSpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PaladinSoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PaladinSpells.MODID);

    public static final RegistryObject<SoundEvent> BULWARK =
            SOUND_EVENTS.register("bulwark",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "bulwark")));

    public static final RegistryObject<SoundEvent> TAUNT =
            SOUND_EVENTS.register("taunt",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "taunt")));
}