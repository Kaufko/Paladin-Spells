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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.List;
import java.util.Optional;


public class SwornProtectorSpell extends AbstractSpell {

    private static final ResourceLocation SPELL_ID =
            ResourceLocation.fromNamespaceAndPath(
                    PaladinSpells.MODID,
                    "sworn_protector"
            );

    public static final int MAX_LEVEL = 10;

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float range = getRange(spellLevel);
        float duration = getDuration(spellLevel, caster);
        float redirectPercentage = caster == null ? -1f : getRedirectPercentage(spellLevel, MAX_LEVEL, caster);
        if(FMLEnvironment.dist.isClient() && redirectPercentage == -1f)
        {
            redirectPercentage = getRedirectPercentage(spellLevel, MAX_LEVEL, Minecraft.getInstance().player);
        } // a bit sketchy but it should work
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
                        "ui.irons_spellbooks.effect_length",
                        Utils.stringTruncation(duration, 1)
                )
        );
    }

    private float getRange(int spellPower) {
        return ( 10 + spellPower * 2) * 3;
    }

    private float getDuration(int spellLevel, LivingEntity caster) {
        return 15 + 20 * getSpellPower(spellLevel, caster) / 100;
    }

    public float getRedirectPercentage(int spellLevel, int maxSpellLevel, LivingEntity caster) {
        float normalizedLevel = (spellLevel - 1f) / (maxSpellLevel - 1f);

        float scaledValue = (float) Math.pow(
                normalizedLevel,
                0.6f / (1 + 0.1f * getSpellPower(spellLevel, caster))
        );

        float armorBonus = 0.20f * caster.getArmorValue() / (caster.getArmorValue() + 100.0f);

        return Math.min(
                1.0f,
                0.20f + scaledValue * 0.60f + armorBonus
        );
    }

    public SwornProtectorSpell() {
        manaCostPerLevel = 15;
        baseSpellPower = 10;
        spellPowerPerLevel = 5;
        castTime = 0;
        baseManaCost = 30;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(MAX_LEVEL)
            .setCooldownSeconds(35)
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
        return Optional.of(PaladinSoundRegistry.BULWARK.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (level.isClientSide) {
            return;
        }

        int durationTicks = (int) (getDuration(spellLevel, entity) * 20);
        float redirectPercentage = getRedirectPercentage(spellLevel, MAX_LEVEL, entity);
        float range = getRange(spellLevel);
        
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
