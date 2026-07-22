package eu.kaufko.paladin_spells.spells;

import eu.kaufko.paladin_spells.PaladinSpells;
import eu.kaufko.paladin_spells.registry.PaladinEffectsRegistry;
import eu.kaufko.paladin_spells.registry.PaladinSoundRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;


public class TauntSpell extends AbstractSpell {
    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "taunt");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float range = getRange(spellLevel, caster);
        float duration = getDuration(spellLevel);

        return List.of(
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(range, 1)),
                Component.translatable("ui.irons_spellbooks.duration", Utils.stringTruncation(duration, 1))
        );
    }

    public TauntSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
        this.castTime = 0;
        this.baseManaCost = 30;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(20)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(PaladinSoundRegistry.TAUNT.get());
    }


    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (level.isClientSide) {
            return;
        }
        float range = getRange(spellLevel, entity);
        float duration = getDuration(spellLevel);

        AABB aabb = entity.getBoundingBox().inflate(range);
        List<Mob> nearbyMobs = level.getEntitiesOfClass(Mob.class, aabb,
                mob -> mob.isAlive() &&
                        !mob.isDeadOrDying() &&
                        mob.distanceTo(entity) <= range &&
                        mob != entity);


        int tauntedCount = 0;
        for (Mob mob : nearbyMobs) {
            if(mob instanceof Enemy) {
                MobEffectInstance tauntEffect = new MobEffectInstance(
                        PaladinEffectsRegistry.TAUNT_EFFECT.get(),
                        (int) (duration * 20), // Duration in ticks
                        0,
                        false,
                        false
                );
                mob.addEffect(tauntEffect);

                mob.getPersistentData().putUUID("taunt_target_uuid", entity.getUUID());

                level.addParticle(
                        ParticleTypes.ANGRY_VILLAGER,
                        mob.getX(), mob.getY() + 1.5, mob.getZ(),
                        0, 0.1, 0
                );

                tauntedCount++;
            }
        }
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.TOUCH_GROUND_ANIMATION;
    }

    private float getRange(int spellLevel, LivingEntity caster) {
        float spellPower = getSpellPower(spellLevel, caster);
        return 10 + spellPower * 2;
    }

    private float getDuration(int spellLevel) {
        return 5 + getSpellPower
    }
}
