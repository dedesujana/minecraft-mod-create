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
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

    // ===== BASIC KINETIC BLOCKS =====
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

    // ===== ADDITIONAL GEAR BLOCKS =====
    public static final DeferredBlock<Block> SMALL_GEAR = BLOCKS.register("small_gear",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.5f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> SMALL_GEAR_ITEM = ITEMS.registerSimpleBlockItem("small_gear", SMALL_GEAR);

    public static final DeferredBlock<Block> LARGE_GEAR = BLOCKS.register("large_gear",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> LARGE_GEAR_ITEM = ITEMS.registerSimpleBlockItem("large_gear", LARGE_GEAR);

    public static final DeferredBlock<Block> BRASS_GEAR = BLOCKS.register("brass_gear",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> BRASS_GEAR_ITEM = ITEMS.registerSimpleBlockItem("brass_gear", BRASS_GEAR);

    public static final DeferredBlock<Block> COPPER_GEAR = BLOCKS.register("copper_gear",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.8f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> COPPER_GEAR_ITEM = ITEMS.registerSimpleBlockItem("copper_gear", COPPER_GEAR);

    // ===== SHAFT VARIANTS =====
    public static final DeferredBlock<Block> BRASS_SHAFT = BLOCKS.register("brass_shaft",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.8f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> BRASS_SHAFT_ITEM = ITEMS.registerSimpleBlockItem("brass_shaft", BRASS_SHAFT);

    public static final DeferredBlock<Block> COPPER_SHAFT = BLOCKS.register("copper_shaft",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.5f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> COPPER_SHAFT_ITEM = ITEMS.registerSimpleBlockItem("copper_shaft", COPPER_SHAFT);

    public static final DeferredBlock<Block> GOLDEN_SHAFT = BLOCKS.register("golden_shaft",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.3f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> GOLDEN_SHAFT_ITEM = ITEMS.registerSimpleBlockItem("golden_shaft", GOLDEN_SHAFT);

    // ===== MECHANICAL BLOCKS =====
    public static final DeferredBlock<Block> MECHANICAL_PRESS = BLOCKS.register("mechanical_press",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> MECHANICAL_PRESS_ITEM = ITEMS.registerSimpleBlockItem("mechanical_press", MECHANICAL_PRESS);

    public static final DeferredBlock<Block> MECHANICAL_MIXER = BLOCKS.register("mechanical_mixer",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> MECHANICAL_MIXER_ITEM = ITEMS.registerSimpleBlockItem("mechanical_mixer", MECHANICAL_MIXER);

    public static final DeferredBlock<Block> MECHANICAL_PUMP = BLOCKS.register("mechanical_pump",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> MECHANICAL_PUMP_ITEM = ITEMS.registerSimpleBlockItem("mechanical_pump", MECHANICAL_PUMP);

    public static final DeferredBlock<Block> MECHANICAL_SAW = BLOCKS.register("mechanical_saw",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> MECHANICAL_SAW_ITEM = ITEMS.registerSimpleBlockItem("mechanical_saw", MECHANICAL_SAW);

    public static final DeferredBlock<Block> MECHANICAL_DRILL = BLOCKS.register("mechanical_drill",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> MECHANICAL_DRILL_ITEM = ITEMS.registerSimpleBlockItem("mechanical_drill", MECHANICAL_DRILL);

    // ===== DECORATIVE BLOCKS =====
    public static final DeferredBlock<Block> BRASS_BLOCK = BLOCKS.register("brass_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> BRASS_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("brass_block", BRASS_BLOCK);

    public static final DeferredBlock<Block> COPPER_BLOCK = BLOCKS.register("copper_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.5f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> COPPER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("copper_block", COPPER_BLOCK);

    public static final DeferredBlock<Block> ANDESITE_BRICK = BLOCKS.register("andesite_brick",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> ANDESITE_BRICK_ITEM = ITEMS.registerSimpleBlockItem("andesite_brick", ANDESITE_BRICK);

    public static final DeferredBlock<Block> DIORITE_BRICK = BLOCKS.register("diorite_brick",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> DIORITE_BRICK_ITEM = ITEMS.registerSimpleBlockItem("diorite_brick", DIORITE_BRICK);

    public static final DeferredBlock<Block> GRANITE_BRICK = BLOCKS.register("granite_brick",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.2f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> GRANITE_BRICK_ITEM = ITEMS.registerSimpleBlockItem("granite_brick", GRANITE_BRICK);

    public static final DeferredBlock<Block> CHISELED_ANDESITE = BLOCKS.register("chiseled_andesite",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> CHISELED_ANDESITE_ITEM = ITEMS.registerSimpleBlockItem("chiseled_andesite", CHISELED_ANDESITE);

    // ===== CRAFT ITEMS - METALS =====
    public static final DeferredItem<Item> BRASS_INGOT = ITEMS.register("brass_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BRASS_DUST = ITEMS.register("brass_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ZINC_INGOT = ITEMS.register("zinc_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ZINC_DUST = ITEMS.register("zinc_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COPPER_INGOT = ITEMS.register("copper_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COPPER_DUST = ITEMS.register("copper_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> IRON_SHEET = ITEMS.register("iron_sheet",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COPPER_SHEET = ITEMS.register("copper_sheet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GOLDEN_SHEET = ITEMS.register("golden_sheet",
            () -> new Item(new Item.Properties()));

    // ===== CRAFT ITEMS - COMPONENTS =====
    public static final DeferredItem<Item> GEAR_CORE = ITEMS.register("gear_core",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEAR_SHAFT = ITEMS.register("gear_shaft",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ANDESITE_ALLOY = ITEMS.register("andesite_alloy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BRASS_ALLOY = ITEMS.register("brass_alloy",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> POLISHED_ROSE_QUARTZ = ITEMS.register("polished_rose_quartz",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHADOW_STEEL = ITEMS.register("shadow_steel",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CINDER_FLOUR = ITEMS.register("cinder_flour",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWDERED_OBSIDIAN = ITEMS.register("powdered_obsidian",
            () -> new Item(new Item.Properties()));

    // ===== CRAFT ITEMS - RARE =====
    public static final DeferredItem<Item> CHROMATIC_COMPOUND = ITEMS.register("chromatic_compound",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> REFINED_RADIANCE = ITEMS.register("refined_radiance",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CRUSHED_LAPIS = ITEMS.register("crushed_lapis",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRUSHED_DIAMOND = ITEMS.register("crushed_diamond",
            () -> new Item(new Item.Properties()));

    // ===== CRAFT ITEMS - TOOLS =====
    public static final DeferredItem<Item> WRENCH = ITEMS.register("wrench",
            () -> new Item(new Item.Properties().durability(64)));

    // A dedicated creative tab for this mod's items
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GEAR_TAB = CREATIVE_MODE_TABS.register("gear_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gearmod"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> GEAR_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Kinetic blocks
                        output.accept(ROTATING_GEAR_ITEM.get());
                        output.accept(HAND_CRANK_ITEM.get());
                        output.accept(SHAFT_ITEM.get());
                        output.accept(GEAR_ITEM.get());

                        // Additional gears
                        output.accept(SMALL_GEAR_ITEM.get());
                        output.accept(LARGE_GEAR_ITEM.get());
                        output.accept(BRASS_GEAR_ITEM.get());
                        output.accept(COPPER_GEAR_ITEM.get());

                        // Shaft variants
                        output.accept(BRASS_SHAFT_ITEM.get());
                        output.accept(COPPER_SHAFT_ITEM.get());
                        output.accept(GOLDEN_SHAFT_ITEM.get());

                        // Mechanical blocks
                        output.accept(MECHANICAL_PRESS_ITEM.get());
                        output.accept(MECHANICAL_MIXER_ITEM.get());
                        output.accept(MECHANICAL_PUMP_ITEM.get());
                        output.accept(MECHANICAL_SAW_ITEM.get());
                        output.accept(MECHANICAL_DRILL_ITEM.get());

                        // Decorative blocks
                        output.accept(BRASS_BLOCK_ITEM.get());
                        output.accept(COPPER_BLOCK_ITEM.get());
                        output.accept(ANDESITE_BRICK_ITEM.get());
                        output.accept(DIORITE_BRICK_ITEM.get());
                        output.accept(GRANITE_BRICK_ITEM.get());
                        output.accept(CHISELED_ANDESITE_ITEM.get());

                        // Craft items - metals
                        output.accept(BRASS_INGOT.get());
                        output.accept(BRASS_DUST.get());
                        output.accept(ZINC_INGOT.get());
                        output.accept(ZINC_DUST.get());
                        output.accept(COPPER_INGOT.get());
                        output.accept(COPPER_DUST.get());
                        output.accept(IRON_SHEET.get());
                        output.accept(COPPER_SHEET.get());
                        output.accept(GOLDEN_SHEET.get());

                        // Craft items - components
                        output.accept(GEAR_CORE.get());
                        output.accept(GEAR_SHAFT.get());
                        output.accept(ANDESITE_ALLOY.get());
                        output.accept(BRASS_ALLOY.get());
                        output.accept(POLISHED_ROSE_QUARTZ.get());
                        output.accept(SHADOW_STEEL.get());
                        output.accept(CINDER_FLOUR.get());
                        output.accept(POWDERED_OBSIDIAN.get());

                        // Rare items
                        output.accept(CHROMATIC_COMPOUND.get());
                        output.accept(REFINED_RADIANCE.get());
                        output.accept(CRUSHED_LAPIS.get());
                        output.accept(CRUSHED_DIAMOND.get());

                        // Tools
                        output.accept(WRENCH.get());
                    }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public GearMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the deferred registers to the mod event bus so their entries get registered
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        LOGGER.info("GearMod loaded with 50+ items and blocks!");
    }
}
