package com.example.gearmod.client;

import com.example.gearmod.block.kinetic.GearBlockEntity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GearGeoRenderer extends GeoBlockRenderer<GearBlockEntity> {
    public GearGeoRenderer(BlockEntityRendererProvider.Context context) {
        super(new GearGeoModel());
    }
}
