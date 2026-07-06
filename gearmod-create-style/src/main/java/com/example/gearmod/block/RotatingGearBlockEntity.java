package com.example.gearmod.block;

import com.example.gearmod.GearMod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Holds no persistent state - the spin animation is purely time-based and computed
 * every frame in the renderer, so nothing needs to be saved or synced here.
 */
public class RotatingGearBlockEntity extends BlockEntity {
    public RotatingGearBlockEntity(BlockPos pos, BlockState state) {
        super(GearMod.ROTATING_GEAR_BE.get(), pos, state);
    }
}
