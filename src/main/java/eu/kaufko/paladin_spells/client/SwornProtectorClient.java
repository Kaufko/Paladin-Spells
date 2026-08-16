package eu.kaufko.paladin_spells.client;

import eu.kaufko.paladin_spells.spells.SwornProtectorSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SwornProtectorClient {
    public static float getRedirect(SwornProtectorSpell spell, int spellLevel) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player == null) return 0f;
        return spell.getRedirectPercentage(spellLevel, SwornProtectorSpell.MAX_LEVEL, player);
    }
}

