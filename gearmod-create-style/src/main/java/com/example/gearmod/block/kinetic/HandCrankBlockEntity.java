package com.example.gearmod.block.kinetic;

import com.example.gearmod.GearMod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The only speed source in the kinetic network. Right-clicking it (see {@code HandCrankBlock})
 * spins it up to full speed for a limited time; while active it feeds that speed out to any
 * directly adjacent kinetic block (shaft/gear), which then propagates it onward.
 */
public class HandCrankBlockEntity extends KineticBlockEntity {

    private static final float ACTIVE_SPEED = 12f;         // degrees per tick while cranked
    private static final int ACTIVE_DURATION_TICKS = 100;  // 5 seconds per crank

    private int activeTicksRemaining = 0;

    public HandCrankBlockEntity(BlockPos pos, BlockState state) {
        super(GearMod.HAND_CRANK_BE.get(), pos, state);
    }

    @Override
    protected boolean isSource() {
        return true;
    }

    @Override
    protected void tickSource(Level level, BlockPos pos, BlockState state) {
        if (activeTicksRemaining > 0) {
            activeTicksRemaining--;
            setSpeed(ACTIVE_SPEED);
        } else {
            setSpeed(0f);
        }
    }

    /** Called from {@code HandCrankBlock} when a player right-clicks the crank. */
    public void crank() {
        this.activeTicksRemaining = ACTIVE_DURATION_TICKS;
        setChanged();
    }

    public boolean isActive() {
        return activeTicksRemaining > 0;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("ActiveTicks", activeTicksRemaining);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        activeTicksRemaining = tag.getInt("ActiveTicks");
    }
}
