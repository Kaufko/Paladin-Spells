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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class SwornProtectorSpell extends AbstractSpell {

    private static final ResourceLocation SPELL_ID =
            ResourceLocation.fromNamespaceAndPath(
                    PaladinSpells.MODID,
                    "sworn_protector"
            );

    public static final int MAX_LEVEL = 10;

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float range = getRange(spellLevel, caster);
        float duration = getDuration(spellLevel, caster);
        float redirectPercentage = getRedirectPercentage(spellLevel, MAX_LEVEL, caster);

        return List.of(
                Component.translatable(
                        "ui.paladin_spells.sworn_protector.redirect_percentage",
                        Utils.stringTruncation(redirectPercentage * 100, 1)
                ),
                Component.translatable(
                        "ui.irons_spellbooks.radius",
                        Utils.stringTruncation(range, 1)
                ),
                Component.translatable(
                        "ui.irons_spellbooks.duration",
                        Utils.stringTruncation(duration, 1)
                )
        );
    }

    public static float getRangeStatic(int spellLevel, float spellPower) {
        return 10 + spellPower * 2;
    }

    public static float getRedirectPercentageStatic(int spellLevel, int maxLevel, float armor, float spellPower) {
        float normalizedLevel = (spellLevel - 1f) / (maxLevel - 1f);

        float scaledValue = (float) Math.pow(
                normalizedLevel,
                0.3f / (1 + 0.1f * spellPower)
        );

        float armorBonus = 0.20f * armor / (armor + 100.0f);

        return Math.min(
                1.0f,
                0.20f + scaledValue * 0.60f + armorBonus
        );
    }

    private float getRange(int spellLevel, LivingEntity caster) {
        return getRangeStatic(
                spellLevel,
                getSpellPower(spellLevel, caster)
        );
    }

    private float getDuration(int spellLevel, LivingEntity caster) {
        return 5 + getSpellPower(spellLevel, caster) * 10;
    }

    public float getRedirectPercentage(int spellLevel, int maxSpellLevel, LivingEntity caster) {
        return getRedirectPercentageStatic(
                spellLevel,
                maxSpellLevel,
                caster.getArmorValue(),
                getSpellPower(spellLevel, caster)
        );
    }

    public SwornProtectorSpell() {
        manaCostPerLevel = 15;
        baseSpellPower = 1;
        spellPowerPerLevel = 0;
        castTime = 0;
        baseManaCost = 30;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(MAX_LEVEL)
            .setCooldownSeconds(25)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return SPELL_ID;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (level.isClientSide) {
            return;
        }

        int durationTicks = (int) (getDuration(spellLevel, entity) * 20);
        float redirectPercentage = getRedirectPercentage(spellLevel, MAX_LEVEL, entity);
        float range = getRange(spellLevel, entity);
        
        entity.getPersistentData().putFloat("sworn_protector_redirect", redirectPercentage);
        entity.getPersistentData().putFloat("sworn_protector_range", range);
        
        entity.addEffect(
                new MobEffectInstance(
                        PaladinEffectsRegistry.SWORN_PROTECTOR_EFFECT.get(),
                        durationTicks,
                        spellLevel - 1
                )
        );
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }
}
