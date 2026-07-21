package eu.kaufko.paladin_spells.spells;

import eu.kaufko.paladin_spells.PaladinSpells;
import eu.kaufko.paladin_spells.capabilities.magic.ImpulseCastData;
import eu.kaufko.paladin_spells.registry.PaladinSoundRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class RamSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "ram");

    public RamSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 15;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(10)
            .build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float distance = getSpellPower(spellLevel, caster);
        float damage = getRamDamage(spellLevel, caster);

        return List.of(
                Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(distance, 1)),
                Component.translatable("ui.irons_spellbooks.damage",Utils.stringTruncation(damage, 1))
        );
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.hasImpulse = true;
        float multiplier = (3 + getSpellPower(spellLevel, entity)) / 12f;
        float damage = getRamDamage(spellLevel, entity);

        Vec3 forward = entity.getLookAngle();

        if (playerMagicData.getAdditionalCastData() instanceof RamDirectionOverrideCastData) {
            if (Utils.random.nextBoolean())
                forward = forward.yRot(90);
            else
                forward = forward.yRot(-90);
        }

        //Create Dashing Movement Impulse
        var vec = forward.multiply(3, 0, 3).normalize().add(0, 0, 0).scale(multiplier);
        if (entity.onGround()) {
            entity.setPos(entity.position().add(0, 1.5, 0));
            vec.add(0, 0.25, 0);
        }
        playerMagicData.setAdditionalCastData(new ImpulseCastData((float) vec.x, (float) vec.y, (float) vec.z, true));
        //entity.setDeltaMovement(entity.getDeltaMovement().add(vec));
        entity.setDeltaMovement(new Vec3(
                Mth.lerp(.75f, entity.getDeltaMovement().x, vec.x),
                Mth.lerp(.75f, entity.getDeltaMovement().y, vec.y),
                Mth.lerp(.75f, entity.getDeltaMovement().z, vec.z)
        ));
        entity.hurtMarked = true;

        AABB chargeBox = entity.getBoundingBox()
                .expandTowards(vec.x, vec.y, vec.z)
                .inflate(1.5D);

        List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, chargeBox,
                                                                       target -> target != entity && target.isAlive()
        );

        for (LivingEntity target : targets) {
            target.hurt(entity.damageSources().mobAttack(entity), damage);
            target.knockback(1.5f, forward.x, forward.z);
        }
    }
    
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        if (castData instanceof ImpulseCastData bdcd) {
            entity.hasImpulse = bdcd.hasImpulse;
            entity.setDeltaMovement(entity.getDeltaMovement().add(bdcd.x, bdcd.y, bdcd.z));
        }
        super.onClientCast(level, spellLevel, entity, castData);
    }

    private float getRamDamage(int spellLevel, LivingEntity caster) {
        float armor = caster.getArmorValue();
        float spellPower = getSpellPower(spellLevel, caster);

        return (armor * 1.25f) + spellPower + (spellLevel * 2f);
    }

    @Override
    public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public DefaultConfig getDefaultConfig() { return defaultConfig; }

    @Override
    public CastType getCastType() { return CastType.INSTANT; }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(PaladinSoundRegistry.RAM.get());
    }

    public ICastDataSerializable getEmptyCastData() { return new ImpulseCastData(); }

    public static class RamDirectionOverrideCastData implements ICastData {
        @Override
        public void reset() {}
    }
}
