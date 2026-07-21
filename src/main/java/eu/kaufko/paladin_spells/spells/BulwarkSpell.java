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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class BulwarkSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "bulwark"); //mark static?

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float multiplier = getArmorBonusPercent(spellLevel, caster);
        float duration = getDuration(spellLevel);

        return List.of(
                Component.translatable("ui.paladin_spells.bulwark.multiplier", Utils.stringTruncation(multiplier, 1)),
                Component.translatable("ui.irons_spellbooks.duration", Utils.stringTruncation(duration, 1))
        );
    }

    public BulwarkSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 15;
        this.spellPowerPerLevel = 5;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (level.isClientSide) {
            return;
        }
        if (entity == null) { //check what this is for and comment it
            return;
        }

        float bonusPercent = getArmorBonusPercent(spellLevel, entity);
        float duration = getDuration(spellLevel);

        int amplifier = Math.round(bonusPercent * 10f);

        int durationTicks = (int) (duration * 20);

        entity.addEffect(new MobEffectInstance(PaladinEffectsRegistry.BULWARK_EFFECT.get(), durationTicks, amplifier));
        //redo the scalinig it looks horrid and it propably is lmao
    }

    private float getArmorBonusPercent(int spellLevel, LivingEntity caster) {
        float spellPower = getSpellPower(spellLevel, caster);
        return spellPower;
    }

    private float getDuration(int spellLevel, LivingEntity caster) {
        return 5 + (spellLevel - 1) * 5 + getSpellPower(spellLevel, caster) * 3
    }

    @Override
    public CastType getCastType() { return CastType.INSTANT; }

    @Override
    public Optional<SoundEvent> getCastStartSound() { return Optional.of(PaladinSoundRegistry.BULWARK.get()); }

    @Override
    public AnimationHolder getCastStartAnimation() { return SpellAnimations.SELF_CAST_ANIMATION; }
    
    @Override
    public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public DefaultConfig getDefaultConfig() { return defaultConfig; }
}
