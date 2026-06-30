package eu.kaufko.paladin_spells.spells;

import eu.kaufko.paladin_spells.PaladinSpells;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class TauntSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "taunt");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float range = getRange(spellLevel, caster);
        float duration = getDuration(spellLevel);

        return List.of(
                Component.translatable("ui.paladin_spells.taunt_range", Utils.stringTruncation(range, 1)),
                Component.translatable("ui.paladin_spells.taunt_duration", Utils.stringTruncation(duration, 1))
        );
    }

    public TauntSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 10;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(PaladinSoundRegistry.TAUNT.get());
    }


    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        doTaunt(level, spellLevel, entity, playerMagicData);
    }

    public void onCast(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        doTaunt(level, spellLevel, entity, playerMagicData);
    }

    private void doTaunt(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (level.isClientSide) {
            return;
        }

        float range = getRange(spellLevel, entity);
        float duration = getDuration(spellLevel);

        PaladinSpells.LOGGER.info("Range: {}, Duration: {}", range, duration);

        AABB aabb = entity.getBoundingBox().inflate(range);
        List<Mob> nearbyMobs = level.getEntitiesOfClass(Mob.class, aabb,
                mob -> mob.isAlive() &&
                        !mob.isDeadOrDying() &&
                        mob.distanceTo(entity) <= range &&
                        mob != entity);

        PaladinSpells.LOGGER.info("Found {} mobs to taunt", nearbyMobs.size());

        int tauntedCount = 0;
        for (Mob mob : nearbyMobs) {
            mob.setTarget(entity);
            mob.setLastHurtByMob(entity);

            if (entity instanceof Player player) {
                mob.setLastHurtByPlayer(player);
            }
            MobEffectInstance tauntEffect = new MobEffectInstance(
                    PaladinSpells.TAUNT_EFFECT.get(),
                    (int)(duration * 20), // Duration in ticks
                    0,
                    false,
                    true
            );
            mob.addEffect(tauntEffect);

            mob.getPersistentData().putUUID("taunt_target_uuid", entity.getUUID());

            if (mob.getAttribute(Attributes.FOLLOW_RANGE) != null) {
                mob.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(range * 2);
            }

            MobEffectInstance glowing = new MobEffectInstance(
                    MobEffects.GLOWING,
                    (int)(duration * 20),
                    0,
                    false,
                    true
            );
            mob.addEffect(glowing);

            level.addParticle(
                    ParticleTypes.ANGRY_VILLAGER,
                    mob.getX(), mob.getY() + 1, mob.getZ(),
                    0, 0.1, 0
            );

            tauntedCount++;
            PaladinSpells.LOGGER.info("Taunted: {}", mob.getName().getString());
        }

        // Chat message
        if (entity instanceof Player player) {
            player.displayClientMessage(
                    Component.literal("§6⚔ Taunted §e" + tauntedCount + " §6enemies!"),
                    true
            );
        }

        PaladinSpells.LOGGER.info("Total taunted: {}", tauntedCount);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }

    private float getRange(int spellLevel, LivingEntity caster) {
        float spellPower = getSpellPower(spellLevel, caster);
        return Math.max(10, 10 + spellPower * 2);
    }

    private float getDuration(int spellLevel) {
        return 5 + (spellLevel - 1) * 12.78f;
    }
}