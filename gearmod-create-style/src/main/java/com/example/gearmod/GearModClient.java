package com.example.gearmod;

import com.example.gearmod.client.GearGeoRenderer;
import com.example.gearmod.client.RotatingGearRenderer;
import com.example.gearmod.client.ShaftRenderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = GearMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = GearMod.MODID, value = Dist.CLIENT)
public class GearModClient {

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Wire each block entity to its renderer
        event.registerBlockEntityRenderer(GearMod.ROTATING_GEAR_BE.get(), RotatingGearRenderer::new);
        event.registerBlockEntityRenderer(GearMod.SHAFT_BE.get(), ShaftRenderer::new);
        event.registerBlockEntityRenderer(GearMod.GEAR_BE.get(), GearGeoRenderer::new);
    }
}
