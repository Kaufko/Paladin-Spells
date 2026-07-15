package eu.kaufko.paladin_spells.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SwornProtectorEffect extends MobEffect {
    public SwornProtectorEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }
    @Override
    public void onEffectRemoved(LivingEntity entity, int amplifier) {
        super.onEffectRemoved(entity, amplifier);
    
        entity.getPersistentData().remove("sworn_protector_redirect");
        entity.getPersistentData().remove("sworn_protector_range");
    }
}
