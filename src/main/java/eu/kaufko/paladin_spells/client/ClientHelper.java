package eu.kaufko.paladin_spells.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class ClientHelper {
    @Nullable
    public static LivingEntity getLocalPlayer() {
        return Minecraft.getInstance().player;
    }
}
