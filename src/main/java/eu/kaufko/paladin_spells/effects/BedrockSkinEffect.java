package eu.kaufko.paladin_spells.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class BedrockSkinEffect extends MobEffect {

    public static final String DAMAGE_REDUCTION_KEY = "bedrock_skin_reduction";

    private static final UUID SPEED_MODIFIER_UUID =
            UUID.fromString("4d4bbdf5-f6be-4a57-94c9-d3ac4ae7d157");

    public BedrockSkinEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);

        addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                SPEED_MODIFIER_UUID.toString(),
                -1.0D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.setDeltaMovement(0, entity.getDeltaMovement().y(), 0);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        entity.getPersistentData().remove(DAMAGE_REDUCTION_KEY);
    }
}