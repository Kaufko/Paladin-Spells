package eu.kaufko.paladin_spells.entity.spells.BedrockSkin;

import eu.kaufko.paladin_spells.entity.spells.BedrockSkin.BedrockSkinEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BedrockSkinRenderer extends EntityRenderer<BedrockSkinEntity> {
    public BedrockSkinRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(BedrockSkinEntity entity) {
        return null; // never used, we don't render anything
    }

    @Override
    public boolean shouldRender(BedrockSkinEntity entity, net.minecraft.client.renderer.culling.Frustum frustum, double x, double y, double z) {
        return false; // skip rendering entirely
    }
}