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
import java.util.UUID;

@AutoSpellConfig
public class SwornProtectorSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "sworn_protector");

    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("b1c2d3e4-f5a6-7890-bcde-f12345678901");
    
    private int baseDuration = 30;
    
    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float range = getRange(spellLevel, caster);
        float duration = getDuration(spellLevel);
        float redirectPercentage = getRedirectPercentage(spellLevel, caster);
        return List.of(
                Component.translatable("ui.paladin_spells.sworn_protector.redirect_percentage", Utils.stringTruncation(redirectPercentage * 100, 1)),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(range, 1)),
                Component.translatable("ui.irons_spellbooks.duration", Utils.stringTruncation(duration, 1))
        );
    }

    private float getRange(int spellLevel, LivingEntity caster) {
        float spellPower = getSpellPower(spellLevel, caster);
        return Math.max(10, 10 + spellPower * 2);
    }

    private float getDuration(int spellLevel, LivingEntity caster) {
        return 5 + getSpellPower(spellLevel, caster) * 12.78f;
    }

    public float getRedirectPercentage(int spellLevel, LivingEntity caster) {
        float normalizedLevel = (spellLevel - 1) / 9.0f;
        float scaledValue = (float) Math.pow(normalizedLevel, 0.3f / (1 + 0.1 * getSpellPower(spellLevel, caster));
        return 0.20f + scaledValue * 0.60f;
    }

    public SwornProtectorSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 30;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(25)
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (level.isClientSide) {
            return;
        }

        int durationTicks = (int) (getDuration(spellLevel) * 10 * 20);
        entity.addEffect(new MobEffectInstance(
                PaladinEffectsRegistry.SWORN_PROTECTOR_EFFECT.get(),
                durationTicks,
                0
        ));
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }
}
