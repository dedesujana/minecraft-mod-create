package com.example.gearmod.client;

import com.example.gearmod.block.RotatingGearBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Draws the rotating_gear block's own baked model every frame, rotated around the Y axis
 * by an angle that increases with game time. This is the same general technique vanilla
 * uses for animated block-entity-rendered blocks (e.g. chests): the static block model is
 * suppressed (see RotatingGearBlock#getRenderShape) and this renderer draws it instead,
 * applying a rotation transform before tesselating.
 */
public class RotatingGearRenderer implements BlockEntityRenderer<RotatingGearBlockEntity> {

    // Degrees per tick. Raise/lower this to make the gear spin faster or slower.
    private static final float ROTATION_SPEED_DEG_PER_TICK = 6f;

    private final BlockRenderDispatcher blockRenderer;

    public RotatingGearRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(RotatingGearBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                        MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        BlockState state = blockEntity.getBlockState();
        BakedModel model = blockRenderer.getBlockModel(state);

        float time = level.getGameTime() + partialTick;
        float angle = (time * ROTATION_SPEED_DEG_PER_TICK) % 360f;

        poseStack.pushPose();
        // Rotate around the center of the block (0.5, 0.5, 0.5 in local space)
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(new org.joml.Quaternionf().rotateY((float) Math.toRadians(angle)));
        poseStack.translate(-0.5, -0.5, -0.5);

        blockRenderer.getModelRenderer().tesselateBlock(
                level,
                model,
                state,
                blockEntity.getBlockPos(),
                poseStack,
                bufferSource.getBuffer(RenderType.cutout()),
                false,
                RandomSource.create(),
                state.getSeed(blockEntity.getBlockPos()),
                packedOverlay
        );

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(RotatingGearBlockEntity blockEntity) {
        return false;
    }

    @Override
    public int getViewDistance() {
        return 64;
    }
}
