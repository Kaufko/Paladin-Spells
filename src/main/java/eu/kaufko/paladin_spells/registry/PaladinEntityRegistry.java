package eu.kaufko.paladin_spells.registry;

import eu.kaufko.paladin_spells.PaladinSpells;
import eu.kaufko.paladin_spells.entity.spells.BedrockSkin.BedrockSkinEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PaladinEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PaladinSpells.MODID);

    public static final RegistryObject<EntityType<BedrockSkinEntity>> BEDROCK_SKIN_ENTITY =
            ENTITY_TYPES.register("bedrock_skin_entity", () ->
                    EntityType.Builder.<BedrockSkinEntity>of(BedrockSkinEntity::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f)
                            .noSummon()
                            .fireImmune()
                            .build("bedrock_skin_entity")

            );
}