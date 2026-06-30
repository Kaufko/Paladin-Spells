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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AutoSpellConfig
public class BulwarkSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(PaladinSpells.MODID, "bulwark");

    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("b1c2d3e4-f5a6-7890-bcde-f12345678901");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float multiplier = getMultiplier(spellLevel, caster);
        float duration = getDuration(spellLevel);

        return List.of(
                Component.translatable("ui.paladin_spells.bulwark_multiplier", Utils.stringTruncation(multiplier, 1)),
                Component.translatable("ui.paladin_spells.bulwark_duration", Utils.stringTruncation(duration, 1))
        );
    }

    public BulwarkSpell() {
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
        // CRITICAL: Only run on server
        if (level.isClientSide) {
            return;
        }
        doBulwark(level, spellLevel, entity);
    }

    public void onCast(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        // CRITICAL: Only run on server
        if (level.isClientSide) {
            return;
        }
        doBulwark(level, spellLevel, entity);
    }

    private void doBulwark(Level level, int spellLevel, LivingEntity entity) {
        if (entity == null) {
            return;
        }

        float multiplier = getMultiplier(spellLevel, entity);
        float duration = getDuration(spellLevel);

        AttributeInstance armorAttribute = entity.getAttribute(Attributes.ARMOR);

        if (armorAttribute == null) {
            return;
        }

        // Remove existing modifier FIRST
        armorAttribute.removeModifier(ARMOR_MODIFIER_UUID);

        // Calculate bonus armor
        double currentArmor = armorAttribute.getValue();
        double bonusArmor = currentArmor * (multiplier - 1);

        PaladinSpells.LOGGER.info("Bulwark - Current Armor: {}, Multiplier: {}, Bonus: {}", currentArmor, multiplier, bonusArmor);

        if (bonusArmor > 0) {
            AttributeModifier armorModifier = new AttributeModifier(
                    ARMOR_MODIFIER_UUID,
                    "bulwark_armor_boost",
                    bonusArmor,
                    AttributeModifier.Operation.ADDITION
            );
            armorAttribute.addPermanentModifier(armorModifier);

            PaladinSpells.LOGGER.info("Bulwark - Applied +{} armor", (int)bonusArmor);
        }

        if (entity instanceof Player player) {
            int armorBoost = (int)bonusArmor;
            player.displayClientMessage(
                    Component.literal("§6🛡 Bulwark! §e+" + armorBoost + " §6armor! (" + String.format("%.1f", multiplier) + "x)"),
                    true
            );
        }
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }

    private float getMultiplier(int spellLevel, LivingEntity caster) {
        // Base: 1.1x at level 1, +0.1x per level
        float baseMultiplier = 1.1f + (spellLevel - 1) * 0.1f;

        // Get Holy Spell Power - FIXED: use getSpellPower correctly
        float holyPower = getSpellPower(spellLevel, caster);
        PaladinSpells.LOGGER.info("Holy Power: {}", holyPower);

        // Each point of Holy Power adds 1% to the multiplier
        float holyBonus = 1.0f + (holyPower / 100f);
        float finalMultiplier = baseMultiplier * holyBonus;

        PaladinSpells.LOGGER.info("Multiplier - Base: {}, Holy: {}, Final: {}", baseMultiplier, holyPower, finalMultiplier);

        return finalMultiplier;
    }

    private float getDuration(int spellLevel) {
        return 5 + (spellLevel - 1) * 5;
    }

    // CRITICAL: Clean up modifier when player logs out
    @Mod.EventBusSubscriber(modid = PaladinSpells.MODID)
    public static class BulwarkEventHandler {
        @SubscribeEvent
        public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            Player player = event.getEntity();
            if (player == null) return;

            AttributeInstance armorAttribute = player.getAttribute(Attributes.ARMOR);
            if (armorAttribute != null) {
                armorAttribute.removeModifier(ARMOR_MODIFIER_UUID);
                PaladinSpells.LOGGER.info("Bulwark - Removed armor modifier on logout for {}", player.getName().getString());
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            Player player = event.getEntity();
            if (player == null) return;

            // Clean up any leftover modifier when player logs in
            AttributeInstance armorAttribute = player.getAttribute(Attributes.ARMOR);
            if (armorAttribute != null) {
                armorAttribute.removeModifier(ARMOR_MODIFIER_UUID);
                PaladinSpells.LOGGER.info("Bulwark - Cleaned up armor modifier on login for {}", player.getName().getString());
            }
        }
    }
}