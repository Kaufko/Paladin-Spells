package eu.kaufko.paladin_spells.registry;

import eu.kaufko.paladin_spells.effects.BedrockSkinEffect;
import eu.kaufko.paladin_spells.effects.BulwarkEffect;
import eu.kaufko.paladin_spells.effects.SwornProtectorEffect;
import eu.kaufko.paladin_spells.effects.TauntEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static eu.kaufko.paladin_spells.PaladinSpells.MODID;

public class PaladinEffectsRegistry {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID);

    public static final DeferredHolder<MobEffect, MobEffect> TAUNT_EFFECT =
            MOB_EFFECTS.register("taunt", TauntEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> BULWARK_EFFECT =
            MOB_EFFECTS.register("bulwark", BulwarkEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> SWORN_PROTECTOR_EFFECT =
            MOB_EFFECTS.register("sworn_protector", SwornProtectorEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> BEDROCK_SKIN_EFFECT =
            MOB_EFFECTS.register("bedrock_skin", BedrockSkinEffect::new);
}