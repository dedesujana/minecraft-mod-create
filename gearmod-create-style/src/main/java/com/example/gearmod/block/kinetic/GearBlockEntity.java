package com.example.gearmod.block.kinetic;

import com.example.gearmod.GearMod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;

/**
 * The showpiece block: a toothed gear rendered with GeckoLib. It participates in the same
 * kinetic network as the shaft/hand crank (see {@link KineticBlockEntity}), and its rotation is
 * driven directly in code (see {@code GearGeoModel#setCustomAnimations}) rather than through a
 * keyframe animation - the network's current speed IS the animation.
 */
public class GearBlockEntity extends KineticBlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public GearBlockEntity(BlockPos pos, BlockState state) {
        super(GearMod.GEAR_BE.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Intentionally empty: the gear's spin is driven directly from kinetic speed in
        // GearGeoModel#setCustomAnimations, not through a keyframe AnimationController.
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
