package eu.kaufko.paladin_spells.events;

import eu.kaufko.paladin_spells.effects.BedrockSkinEffect;
import eu.kaufko.paladin_spells.entity.spells.BedrockSkin.BedrockSkinEntity;
import eu.kaufko.paladin_spells.registry.PaladinEffectsRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber
public class BedrockSkinEvent {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {

        LivingEntity entity = event.getEntity();

        if (!entity.hasEffect(PaladinEffectsRegistry.BEDROCK_SKIN_EFFECT)) {
            return;
        }

        float reduction = entity.getPersistentData()
                .getFloat(BedrockSkinEffect.DAMAGE_REDUCTION_KEY);

        float reducedDamage = event.getNewDamage() * (1.0f - reduction);

        event.setNewDamage(reducedDamage);
    }

    @SubscribeEvent
    public static void onDismount(EntityMountEvent event) {
        if (!event.isMounting() && event.getEntityBeingMounted() instanceof BedrockSkinEntity anchor) {
            if (!anchor.isRemoved() && anchor.tickCount <=  0) {
                event.setCanceled(true);
            }
        }
    }
}
