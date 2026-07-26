package eu.kaufko.paladin_spells.events;

import eu.kaufko.paladin_spells.entity.spells.BedrockSkin.BedrockSkinEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "paladin_spells", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BedrockSkinEventClient {
    private static boolean wasRidingAnchor = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean isRidingAnchor = mc.player.getVehicle() instanceof BedrockSkinEntity;

        if (isRidingAnchor && !wasRidingAnchor) {
            mc.gui.setOverlayMessage(Component.empty(), false);
        }

        wasRidingAnchor = isRidingAnchor;
    }
}