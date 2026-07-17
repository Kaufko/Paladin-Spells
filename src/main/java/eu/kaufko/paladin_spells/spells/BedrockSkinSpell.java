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
public class BedrockSkinSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "bedrock_skin");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float duration = getDuration(spellLevel, caster );
        float reduction = getDamageReduction(spellLevel, 10, caster);

        return List.of(
        Component.translatable(
                "ui.paladin_spells.bedrock_skin.reduction_percentage",
                Utils.stringTruncation(reduction * 100, 1)
        ),
        Component.translatable(
                "ui.irons_spellbooks.duration",
                Utils.stringTruncation(duration, 1)
        )
);
    }

    private float getDuration(int spellLevel, LivingEntity caster) {
        return 5 + getSpellPower(spellLevel, caster) * 5;
    }

    public BedrockSkinSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 2;
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
        doBedrockSkin(level, spellLevel, entity);
    }

    private static final String DAMAGE_REDUCTION_KEY = "bedrock_skin_reduction";

    private void doBedrockSkin(Level level, int spellLevel, LivingEntity entity) {
        int durationTicks = Math.round(getDuration(spellLevel, entity) * 20f);
    
        float reduction =
                getDamageReduction(
                        spellLevel,
                        defaultConfig.maxLevel,
                        entity
                );
    
        entity.getPersistentData().putFloat(
                DAMAGE_REDUCTION_KEY,
                reduction
        );
    
        entity.addEffect(
                new MobEffectInstance(
                        PaladinEffectsRegistry.BEDROCK_SKIN_EFFECT.get(),
                        durationTicks,
                        spellLevel - 1
                )
        );
    }


    @Override
    public AnimationHolder getCastStartAnimation() {
            return SpellAnimations.SELF_CAST_ANIMATION;
    }
    
    public float getDamageReduction(int spellLevel, int maxLevel, LivingEntity caster) {
        float normalizedLevel = (spellLevel - 1f) / (maxLevel - 1f);
        float spellPower = getSpellPower(spellLevel, caster);
        float scaledValue = (float) Math.pow(normalizedLevel,0.3f / (1 + 0.1f * spellPower));
    
        float armor = caster.getArmorValue();
        float armorBonus = 0.20f * armor / (armor + 100.0f);
    
        return Math.min(
                0.95f,
                0.10f + scaledValue * 0.5f + armorBonus
        );
    }
}
