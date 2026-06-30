package eu.kaufko.paladin_spells.registry;

import eu.kaufko.paladin_spells.PaladinSpells;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class PaladinDamageTypeRegistry {
        public static final ResourceKey<DamageType> REDIRECT = ResourceKey.create(
                Registries.DAMAGE_TYPE,
                ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "redirect")
        );
}
