package eu.kaufko.paladin_spells.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BedrockSkinEffect extends MobEffect {

    public static final String DAMAGE_REDUCTION_KEY = "bedrock_skin_reduction";

    public BedrockSkinEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);

        addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "4d4bbdf5-f6be-4a57-94c9-d3ac4ae7d157",
                -1.0D,
                MobEffect.AttributeModifierOperation.ADD_MULTIPLIED_TOTAL
        );
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        entity.setDeltaMovement(0, entity.getDeltaMovement().y(), 0);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(
            LivingEntity entity,
            net.minecraft.world.entity.ai.attributes.AttributeMap attributes,
            int amplifier
    ) {
        super.removeAttributeModifiers(entity, attributes, amplifier);

        entity.getPersistentData().remove(DAMAGE_REDUCTION_KEY);
    }
}
