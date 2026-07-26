package eu.kaufko.paladin_spells.client;

import eu.kaufko.paladin_spells.entity.spells.BedrockSkin.BedrockSkinRenderer;
import eu.kaufko.paladin_spells.registry.PaladinEntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "paladin_spells", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                PaladinEntityRegistry.BEDROCK_SKIN_ENTITY.get(),
                BedrockSkinRenderer::new
        );
    }
}