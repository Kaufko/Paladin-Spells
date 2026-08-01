package eu.kaufko.paladin_spells.effects;

import eu.kaufko.paladin_spells.PaladinSpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class BulwarkEffect extends MobEffect {
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("b1c2d3e4-f5a6-7890-bcde-f12345678901");

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