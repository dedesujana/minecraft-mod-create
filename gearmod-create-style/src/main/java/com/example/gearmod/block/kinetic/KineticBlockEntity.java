package com.example.gearmod.block.kinetic;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared base for every block that participates in the "kinetic network": hand crank (power
 * source), shaft (transmits rotation in a line), and gear (transmits rotation + looks the part).
 *
 * <p>The model is intentionally simple, closer in spirit to Create than an exact copy of it:
 * every tick, a non-source block sets its own speed to the loudest (largest magnitude) speed
 * among its 6 neighbours. The hand crank is the only speed source, and only "pushes" speed for
 * a limited time after being cranked. This means gears/shafts only spin when connected, directly
 * or through a chain of other kinetic blocks, to an active crank - same basic idea as Create's
 * kinetic power, without stress/capacity mechanics.
 *
 * <p>Rendering never needs a client-side tick: {@link #getRenderAngle(float)} extrapolates the
 * current visual angle from the last server-synced rotation/speed and the client's game time,
 * so smooth animation keeps working between syncs without any network chatter.
 */
public abstract class KineticBlockEntity extends BlockEntity {

    protected float speed = 0f;      // degrees per tick, authoritative value from the server
    protected float rotation = 0f;   // accumulated rotation (degrees) at the moment of last sync
    private long syncedGameTime = 0L; // client-side: level.getGameTime() at the moment 'rotation' was valid

    protected KineticBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /** Hand crank overrides this to true: sources don't take speed from neighbours. */
    protected boolean isSource() {
        return false;
    }

    /** Only meaningful for sources (e.g. hand crank); decides this tick's own speed. */
    protected void tickSource(Level level, BlockPos pos, BlockState state) {
    }

    public float getSpeed() {
        return speed;
    }

    protected void setSpeed(float newSpeed) {
        if (Math.abs(this.speed - newSpeed) > 0.001f) {
            this.speed = newSpeed;
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            }
        }
    }

    /** Angle in degrees to render this tick, smoothly extrapolated on the client. */
    public float getRenderAngle(float partialTick) {
        if (level == null) {
            return rotation;
        }
        float elapsedTicks = (level.getGameTime() - syncedGameTime) + partialTick;
        return (rotation + elapsedTicks * speed) % 360f;
    }

    /** Common ticker used by every kinetic block's {@code getTicker}. Server-side only. */
    public static void tick(Level level, BlockPos pos, BlockState state, KineticBlockEntity be) {
        be.rotation = (be.rotation + be.speed) % 360f;

        if (be.isSource()) {
            be.tickSource(level, pos, state);
            return;
        }

        float loudest = 0f;
        for (Direction dir : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(dir));
            if (neighbor instanceof KineticBlockEntity kinetic && kinetic != be) {
                loudest = Math.max(loudest, Math.abs(kinetic.speed));
            }
        }
        be.setSpeed(loudest);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("Speed", speed);
        tag.putFloat("Rotation", rotation);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        speed = tag.getFloat("Speed");
        rotation = tag.getFloat("Rotation");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadAdditional(tag, registries);
        if (level != null) {
            this.syncedGameTime = level.getGameTime();
        }
    }
}
