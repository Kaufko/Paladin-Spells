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

@Mod.EventBusSubscriber(modid = PaladinSpells.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SwornProtectorEvent {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Player attacker)) {
            return;
        }
        if (event.getSource().is(PaladinDamageTypeRegistry.REDIRECT)) {
            return;
        }

        LivingEntity victim = event.getEntity();
        if (!victim instanceof Player) { //maybe add friended mobs / pets??
            return;
        }

        double radius = 8.0;
        float redirectPercent = 0.25f;

        var protectors = victim.level().getEntitiesOfClass(
                LivingEntity.class,
                victim.getBoundingBox().inflate(radius),
                e -> e.hasEffect(PaladinEffectsRegistry.SWORN_PROTECTOR_EFFECT.get()) && e != victim);

        if (protectors.isEmpty()) {
            return;
        }

        LivingEntity protector = protectors.get(0);
        if (protector == attacker) {
            return;
        }

        float originalDamage = event.getAmount();
        float redirected = originalDamage * redirectPercent;
        event.setAmount(originalDamage - redirected);

        Holder<DamageType> holder = protector.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(PaladinDamageTypeRegistry.REDIRECT);

        PaladinSpells.LOGGER.info("{} protected {} for {} damage", protector.getName().getString(), victim.getName().getString(), redirected);
        protector.hurt(new DamageSource(holder, victim), redirected);
    }
}
