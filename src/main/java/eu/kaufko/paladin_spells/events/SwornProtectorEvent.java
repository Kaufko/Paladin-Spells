package eu.kaufko.paladin_spells.events;

import eu.kaufko.paladin_spells.PaladinSpells;
import eu.kaufko.paladin_spells.registry.PaladinDamageTypeRegistry;
import eu.kaufko.paladin_spells.registry.PaladinEffectsRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;

@Mod.EventBusSubscriber(modid = PaladinSpells.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SwornProtectorEvent {

    private static final String REDIRECT_KEY = "sworn_protector_redirect";
    private static final String RANGE_KEY = "sworn_protector_range";

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (event.getSource().is(PaladinDamageTypeRegistry.REDIRECT)) {
            return;
        }

        LivingEntity victim = event.getEntity();

        if (!(victim instanceof Player)) {
            return;
        }

        var protectors = victim.level().getEntitiesOfClass(
                LivingEntity.class,
                victim.getBoundingBox().inflate(64),
                entity -> entity != victim
                        && entity.hasEffect(PaladinEffectsRegistry.SWORN_PROTECTOR_EFFECT.get())
        );

        if (protectors.isEmpty()) {
            return;
        }

        LivingEntity protector = protectors.stream()
                .filter(entity -> entity.getPersistentData().contains(RANGE_KEY))
                .filter(entity -> entity.distanceTo(victim) <= entity.getPersistentData().getFloat(RANGE_KEY))
                .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(victim)))
                .orElse(null);

        if (protector == null) {
            return;
        }

        var attacker = event.getSource().getEntity();

        if (protector == attacker) {
            return;
        }

        float redirectPercent = protector.getPersistentData().getFloat(REDIRECT_KEY);

        float originalDamage = event.getAmount();
        float redirected = originalDamage * redirectPercent;

        event.setAmount(originalDamage - redirected);

        Holder<DamageType> holder = protector.level()
                .registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(PaladinDamageTypeRegistry.REDIRECT);

        protector.hurt(new DamageSource(holder, victim), redirected);

        PaladinSpells.LOGGER.info(
                "{} protected {} for {} damage",
                protector.getName().getString(),
                victim.getName().getString(),
                redirected
        );
    }
}
