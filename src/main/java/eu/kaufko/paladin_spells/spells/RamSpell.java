package eu.kaufko.paladin_spells.spells;

import eu.kaufko.paladin_spells.PaladinSpells;
import eu.kaufko.paladin_spells.registry.PaladinSoundRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class RamSpell extends AbstractSpell {

    private final ResourceLocation spellId =
            ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "ram");

    public RamSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 25;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(10)
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
        return Optional.of(PaladinSoundRegistry.BULWARK.get());
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float distance = getRamDistance(spellLevel, caster);
        float damage = getRamDamage(spellLevel, caster);

        return List.of(
                Component.translatable(
                        "ui.paladin_spells.ram.distance",
                        Utils.stringTruncation(distance, 1)
                ),
                Component.translatable(
                        "ui.paladin_spells.ram.damage",
                        Utils.stringTruncation(damage, 1)
                )
        );
    }

    @Override
    public void onCast(Level level,
                       int spellLevel,
                       LivingEntity entity,
                       CastSource castSource,
                       MagicData playerMagicData) {

        if (level.isClientSide || entity == null) {
            return;
        }

        doRam(spellLevel, entity);
    }

    private void doRam(int spellLevel, LivingEntity entity) {
        float distance = getRamDistance(spellLevel, entity);
        float damage = getRamDamage(spellLevel, entity);

        Vec3 look = entity.getLookAngle().normalize();

        entity.setDeltaMovement(
                look.x * distance,
                Math.max(0.2D, look.y * 0.15D),
                look.z * distance
        );

        entity.hurtMarked = true;

        AABB chargeBox = entity.getBoundingBox()
                .expandTowards(
                        look.x * distance,
                        look.y * distance,
                        look.z * distance
                )
                .inflate(1.5D);

        List<LivingEntity> targets = entity.level().getEntitiesOfClass(
                LivingEntity.class,
                chargeBox,
                target -> target != entity && target.isAlive()
        );

        for (LivingEntity target : targets) {

            target.hurt(
                    entity.damageSources().mobAttack(entity),
                    damage
            );

            target.knockback(
                    1.5F,
                    -look.x,
                    -look.z
            );
        }

        if (entity instanceof Player player) {
            player.displayClientMessage(
                    Component.literal(
                            "§6 Ram! §e"
                                    + String.format("%.1f", damage)
                                    + " damage"
                    ),
                    true
            );
        }
    }

    private float getRamDistance(int spellLevel, LivingEntity caster) {
        float spellPower = getSpellPower(spellLevel, caster);
        return 2f + spellLevel + (spellPower * 0.1f);
    }

    private float getRamDamage(int spellLevel, LivingEntity caster) {
        float armor = caster.getArmorValue();
        float spellPower = getSpellPower(spellLevel, caster);

        return (armor * 1.25f)
                + spellPower
                + (spellLevel * 2f);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}
