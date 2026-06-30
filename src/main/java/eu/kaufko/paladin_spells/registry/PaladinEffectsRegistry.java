package eu.kaufko.paladin_spells.registry;
import eu.kaufko.paladin_spells.effects.BedrockSkinEffect;
import eu.kaufko.paladin_spells.effects.BulwarkEffect;
import eu.kaufko.paladin_spells.effects.SwornProtectorEffect;
import eu.kaufko.paladin_spells.effects.TauntEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static eu.kaufko.paladin_spells.PaladinSpells.MODID;

public class PaladinEffectsRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);

    public static final RegistryObject<MobEffect> TAUNT_EFFECT =
            MOB_EFFECTS.register("taunt", TauntEffect::new);
    public static final RegistryObject<MobEffect> BULWARK_EFFECT =
            MOB_EFFECTS.register("bulwark", BulwarkEffect::new);
    public static final RegistryObject<MobEffect> SWORN_PROTECTOR_EFFECT =
            MOB_EFFECTS.register("sworn_protector", SwornProtectorEffect::new);
    public static final RegistryObject<MobEffect> BEDROCK_SKIN_EFFECT =
            MOB_EFFECTS.register("bedrock_skin", BedrockSkinEffect::new);


}
