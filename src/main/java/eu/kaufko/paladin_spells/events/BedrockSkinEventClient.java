package eu.kaufko.paladin_spells.events;

import eu.kaufko.paladin_spells.entity.spells.BedrockSkin.BedrockSkinEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = "paladin_spells", value = Dist.CLIENT)
public class BedrockSkinEventClient {
    private static boolean wasRidingAnchor = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean isRidingAnchor = mc.player.getVehicle() instanceof BedrockSkinEntity;

        if (isRidingAnchor && !wasRidingAnchor) {
            mc.gui.setOverlayMessage(Component.empty(), false);
        }

        wasRidingAnchor = isRidingAnchor;
    }
}