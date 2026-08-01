package eu.kaufko.paladin_spells.effects;

import eu.kaufko.paladin_spells.PaladinSpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BedrockSkinEffect extends MobEffect {

    public static final String DAMAGE_REDUCTION_KEY = "bedrock_skin_reduction";

    public BedrockSkinEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
        this.addAttributeModifier(
                Attributes.ARMOR,
                ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "bedrock_skin_armor"),
                10.0, // value
                AttributeModifier.Operation.ADD_VALUE // or ADD_MULTIPLIED_BASE / ADD_MULTIPLIED_TOTAL
        );
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return true;
    }
}