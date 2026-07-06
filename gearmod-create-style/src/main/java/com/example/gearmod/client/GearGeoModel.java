package com.example.gearmod.client;

import com.example.gearmod.GearMod;
import com.example.gearmod.block.kinetic.GearBlockEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

/**
 * Points GeckoLib at the gear's model/texture/animation files, and drives its rotation directly
 * from the kinetic network speed every frame (see {@link #setCustomAnimations}), rather than
 * through a keyframe animation - so the gear speeds up/slows down/stops exactly in sync with
 * whether it's connected to a powered shaft or hand crank.
 */
public class GearGeoModel extends GeoModel<GearBlockEntity> {

    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath(GearMod.MODID, "geo/gear.geo.json");
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GearMod.MODID, "textures/block/gear.png");
    private static final ResourceLocation ANIMATIONS =
            ResourceLocation.fromNamespaceAndPath(GearMod.MODID, "animations/gear.animation.json");

    @Override
    public ResourceLocation getModelResource(GearBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(GearBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(GearBlockEntity animatable) {
        return ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(GearBlockEntity animatable, long instanceId, AnimationState<GearBlockEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone bone = getAnimationProcessor().getBone("gear");
        if (bone != null) {
            float angleDegrees = animatable.getRenderAngle(animationState.getPartialTick());
            bone.setRotY((float) Math.toRadians(angleDegrees));
        }
    }
}
