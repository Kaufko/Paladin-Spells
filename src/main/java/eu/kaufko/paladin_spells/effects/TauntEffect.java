package eu.kaufko.paladin_spells.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.UUID;

public class TauntEffect extends MobEffect {
    private static final String TAUNT_TARGET_UUID = "taunt_target_uuid";

    public TauntEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0000);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Mob mob)) {
            return;
        }

        if (!mob.getPersistentData().contains(TAUNT_TARGET_UUID)) {
            return;
        }

        UUID targetUuid = mob.getPersistentData().getUUID(TAUNT_TARGET_UUID);
        if (!(mob.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Entity targetEntity = serverLevel.getEntity(targetUuid);

        if (!(targetEntity instanceof LivingEntity targetLiving)) {
            mob.getPersistentData().remove(TAUNT_TARGET_UUID);
            return;
        }
        if (entity.getRandom().nextFloat() < 0.2F) {
            serverLevel.sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    entity.getX() + (entity.getRandom().nextDouble() - 0.5),
                    entity.getY() + entity.getRandom().nextDouble(),
                    entity.getZ() + (entity.getRandom().nextDouble() - 0.5),
                    1,
                    0, 0.15, 0,
                    0.0
            );
        }


        mob.setTarget(targetLiving);
        mob.setAggressive(true);

        if (targetLiving instanceof net.minecraft.world.entity.player.Player player) {
            mob.setLastHurtByPlayer(player);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }


}
