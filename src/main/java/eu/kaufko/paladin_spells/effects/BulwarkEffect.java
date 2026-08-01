package eu.kaufko.paladin_spells.effects;

import eu.kaufko.paladin_spells.PaladinSpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class BulwarkEffect extends MobEffect {

    public BulwarkEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
        this.addAttributeModifier(
                Attributes.ARMOR,
                ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "bulwark_armor"),
                0.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}