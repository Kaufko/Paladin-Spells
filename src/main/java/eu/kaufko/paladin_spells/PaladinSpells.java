package eu.kaufko.paladin_spells;

import com.mojang.logging.LogUtils;
import eu.kaufko.paladin_spells.registry.PaladinEffectsRegistry;
import eu.kaufko.paladin_spells.registry.PaladinEntityRegistry;
import eu.kaufko.paladin_spells.registry.PaladinSoundRegistry;
import eu.kaufko.paladin_spells.registry.PaladinSpellRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(PaladinSpells.MODID)
public class PaladinSpells {
    public static final String MODID = "paladin_spells";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PaladinSpells(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        PaladinSpellRegistry.register(modEventBus);
        PaladinSoundRegistry.SOUND_EVENTS.register(modEventBus);
        PaladinEffectsRegistry.MOB_EFFECTS.register(modEventBus);
        PaladinEntityRegistry.ENTITY_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}