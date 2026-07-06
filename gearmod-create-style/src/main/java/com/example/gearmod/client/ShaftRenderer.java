package com.example.gearmod.client;

import com.example.gearmod.block.kinetic.ShaftBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/** Spins the shaft's baked model based on the kinetic network's current speed (0 = idle). */
public class ShaftRenderer implements BlockEntityRenderer<ShaftBlockEntity> {

    private final BlockRenderDispatcher blockRenderer;

    public ShaftRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(ShaftBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                        MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        BlockState state = blockEntity.getBlockState();
        BakedModel model = blockRenderer.getBlockModel(state);
        float angle = blockEntity.getRenderAngle(partialTick);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(new org.joml.Quaternionf().rotateY((float) Math.toRadians(angle)));
        poseStack.translate(-0.5, -0.5, -0.5);

        blockRenderer.getModelRenderer().tesselateBlock(
                level, model, state, blockEntity.getBlockPos(), poseStack,
                bufferSource.getBuffer(RenderType.cutout()), false, RandomSource.create(),
                state.getSeed(blockEntity.getBlockPos()), packedOverlay
        );

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(ShaftBlockEntity blockEntity) {
        return false;
    }
}
