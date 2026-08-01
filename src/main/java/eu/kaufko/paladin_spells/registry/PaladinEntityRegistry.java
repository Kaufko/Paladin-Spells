package eu.kaufko.paladin_spells.registry;

import eu.kaufko.paladin_spells.PaladinSpells;
import eu.kaufko.paladin_spells.entity.spells.BedrockSkin.BedrockSkinEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PaladinEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PaladinSpells.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<BedrockSkinEntity>> BEDROCK_SKIN_ENTITY =
            ENTITY_TYPES.register("bedrock_skin_entity", () ->
                    EntityType.Builder.<BedrockSkinEntity>of(BedrockSkinEntity::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f)
                            .noSummon()
                            .fireImmune()
                            .build("bedrock_skin_entity")
            );
}