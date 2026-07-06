package com.example.gearmod.block;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A block whose visible model is drawn entirely by {@link com.example.gearmod.client.RotatingGearRenderer},
 * which spins it continuously - similar in spirit to Create mod's kinetic blocks (shafts, cogs, etc).
 */
public class RotatingGearBlock extends BaseEntityBlock {
    public static final MapCodec<RotatingGearBlock> CODEC = simpleCodec(RotatingGearBlock::new);

    public RotatingGearBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    // ENTITYBLOCK_ANIMATED tells vanilla to skip the normal static block model renderer
    // and rely entirely on the block entity renderer to draw the block every frame.
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RotatingGearBlockEntity(pos, state);
    }
}
