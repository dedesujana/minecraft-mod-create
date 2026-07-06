package com.example.gearmod;

import org.slf4j.Logger;

import com.example.gearmod.block.RotatingGearBlock;
import com.example.gearmod.block.RotatingGearBlockEntity;
import com.example.gearmod.block.kinetic.GearBlock;
import com.example.gearmod.block.kinetic.GearBlockEntity;
import com.example.gearmod.block.kinetic.HandCrankBlock;
import com.example.gearmod.block.kinetic.HandCrankBlockEntity;
import com.example.gearmod.block.kinetic.ShaftBlock;
import com.example.gearmod.block.kinetic.ShaftBlockEntity;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GearMod.MODID)
public class GearMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "gearmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // Deferred registers, one per registry type, all under the "gearmod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // The animated rotating gear block, id "gearmod:rotating_gear"
    public static final DeferredBlock<RotatingGearBlock> ROTATING_GEAR = BLOCKS.register("rotating_gear",
            () -> new RotatingGearBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    // The BlockItem for the rotating gear block
    public static final DeferredItem<BlockItem> ROTATING_GEAR_ITEM =
            ITEMS.registerSimpleBlockItem("rotating_gear", ROTATING_GEAR);

    // The block entity type that powers the rotating gear's animation
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RotatingGearBlockEntity>> ROTATING_GEAR_BE =
            BLOCK_ENTITY_TYPES.register("rotating_gear",
                    () -> BlockEntityType.Builder.of(RotatingGearBlockEntity::new, ROTATING_GEAR.get()).build(null));

    // --- Kinetic network blocks (Create-mod-style: crank -> shaft -> gear) ---

    // Hand crank: right-click to power the network for a few seconds
    public static final DeferredBlock<HandCrankBlock> HAND_CRANK = BLOCKS.register("hand_crank",
            () -> new HandCrankBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0f)
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> HAND_CRANK_ITEM =
            ITEMS.registerSimpleBlockItem("hand_crank", HAND_CRANK);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HandCrankBlockEntity>> HAND_CRANK_BE =
            BLOCK_ENTITY_TYPES.register("hand_crank",
                    () -> BlockEntityType.Builder.of(HandCrankBlockEntity::new, HAND_CRANK.get()).build(null));

    // Shaft: connects kinetic blocks together and visually spins when powered
    public static final DeferredBlock<ShaftBlock> SHAFT = BLOCKS.register("shaft",
            () -> new ShaftBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> SHAFT_ITEM =
            ITEMS.registerSimpleBlockItem("shaft", SHAFT);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShaftBlockEntity>> SHAFT_BE =
            BLOCK_ENTITY_TYPES.register("shaft",
                    () -> BlockEntityType.Builder.of(ShaftBlockEntity::new, SHAFT.get()).build(null));

    // Gear: the GeckoLib-animated toothed gear, driven by the kinetic network's speed
    public static final DeferredBlock<GearBlock> GEAR = BLOCKS.register("gear",
            () -> new GearBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> GEAR_ITEM =
            ITEMS.registerSimpleBlockItem("gear", GEAR);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GearBlockEntity>> GEAR_BE =
            BLOCK_ENTITY_TYPES.register("gear",
                    () -> BlockEntityType.Builder.of(GearBlockEntity::new, GEAR.get()).build(null));

    // A dedicated creative tab for this mod's items
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GEAR_TAB = CREATIVE_MODE_TABS.register("gear_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gearmod"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> GEAR_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ROTATING_GEAR_ITEM.get());
                        output.accept(HAND_CRANK_ITEM.get());
                        output.accept(SHAFT_ITEM.get());
                        output.accept(GEAR_ITEM.get());
                    }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public GearMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the deferred registers to the mod event bus so their entries get registered
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        LOGGER.info("GearMod loaded, rotating_gear block registered");
    }
}
