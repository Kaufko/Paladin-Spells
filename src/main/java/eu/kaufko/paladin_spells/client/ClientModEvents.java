package eu.kaufko.paladin_spells.client;

import eu.kaufko.paladin_spells.entity.spells.BedrockSkin.BedrockSkinRenderer;
import eu.kaufko.paladin_spells.registry.PaladinEntityRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "paladin_spells", value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                PaladinEntityRegistry.BEDROCK_SKIN_ENTITY.get(),
                BedrockSkinRenderer::new
        );
    }
}