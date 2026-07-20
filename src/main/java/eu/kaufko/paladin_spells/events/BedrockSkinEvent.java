package eu.kaufko.paladin_spells.events;

import eu.kaufko.paladin_spells.effects.BedrockSkinEffect;
import eu.kaufko.paladin_spells.registry.PaladinEffectsRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BedrockSkinEvents {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {

        LivingEntity entity = event.getEntity();

        if (!entity.hasEffect(PaladinEffectsRegistry.BEDROCK_SKIN_EFFECT.get())) {
            return;
        }

        float reduction = entity.getPersistentData()
                .getFloat(BedrockSkinEffect.DAMAGE_REDUCTION_KEY);

        float reducedDamage = event.getAmount() * (1.0f - reduction);

        event.setAmount(reducedDamage);
    }
}
