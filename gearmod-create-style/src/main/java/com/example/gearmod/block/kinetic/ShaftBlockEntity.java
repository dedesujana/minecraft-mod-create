package com.example.gearmod.block.kinetic;

import com.example.gearmod.GearMod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/** A plain connector: takes no special action beyond the shared propagation logic. */
public class ShaftBlockEntity extends KineticBlockEntity {
    public ShaftBlockEntity(BlockPos pos, BlockState state) {
        super(GearMod.SHAFT_BE.get(), pos, state);
    }
}
