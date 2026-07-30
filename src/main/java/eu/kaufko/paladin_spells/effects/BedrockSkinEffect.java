package eu.kaufko.paladin_spells.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class BedrockSkinEffect extends MobEffect {

    public static final String DAMAGE_REDUCTION_KEY = "bedrock_skin_reduction";

    public BedrockSkinEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        entity.getPersistentData().remove(DAMAGE_REDUCTION_KEY);
    }
}