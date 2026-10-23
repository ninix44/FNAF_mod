package net.mcreator.fnafmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.mcreator.fnafmod.block.ArcadeMachineBlock;
import net.mcreator.fnafmod.block.CheckerTileBlock;
import net.mcreator.fnafmod.block.DoorButtonBlock;
import net.mcreator.fnafmod.block.DoorLightBlock;
import net.mcreator.fnafmod.block.FluorescentLightBlock;
import net.mcreator.fnafmod.block.OfficeDoorBlock;
import net.mcreator.fnafmod.block.PlushieBlock;
import net.mcreator.fnafmod.entity.BalloonBoyEntity;
import net.mcreator.fnafmod.entity.BonnieBunnyEntity;
import net.mcreator.fnafmod.entity.ChicaChickenEntity;
import net.mcreator.fnafmod.entity.Endo02Entity;
import net.mcreator.fnafmod.entity.EndoSkeletonEntity;
import net.mcreator.fnafmod.entity.FoxyPirateEntity;
import net.mcreator.fnafmod.entity.FreddyFazbearEntity;
import net.mcreator.fnafmod.entity.GoldenFreddyEntity;
import net.mcreator.fnafmod.entity.MangleEntity;
import net.mcreator.fnafmod.entity.PuppetEntity;
import net.mcreator.fnafmod.entity.SpringBonnieEntity;
import net.mcreator.fnafmod.entity.SpringtrapEntity;
import net.mcreator.fnafmod.entity.ToyBonnieEntity;
import net.mcreator.fnafmod.entity.ToyChicaEntity;
import net.mcreator.fnafmod.entity.ToyFreddyEntity;
import net.mcreator.fnafmod.entity.WitheredBonnieEntity;
import net.mcreator.fnafmod.entity.WitheredChicaEntity;
import net.mcreator.fnafmod.entity.WitheredFoxyEntity;
import net.mcreator.fnafmod.entity.WitheredFreddyEntity;
import net.mcreator.fnafmod.item.CameraMonitorItem;
import net.mcreator.fnafmod.item.FlashlightItem;
import net.mcreator.fnafmod.item.FreddyMaskItem;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FnafMod implements ModInitializer {
	public static final String MOD_ID = "fnaf_mod";

	public static final SoundEvent ANIMATRONIC_STEP = registerSound("animatronic_step");
	public static final SoundEvent DOOR_CLOSE = registerSound("door_close");
	public static final SoundEvent GOLDEN_FREDDY_SCREAM = registerSound("golden_freddy_scram");
	public static final SoundEvent NOSE_HONK = registerSound("nosehonk");
	public static final SoundEvent FNAF1_SCREAM = registerSound("fnaf1_scream");
	public static final SoundEvent TOREADOR_MARCH = registerSound("toreador_march");
	public static final SoundEvent FREDBEAR_LAUGH = registerSound("fredbearlaugh");
	public static final SoundEvent CAMERA_BLIP = registerSound("camerablip");
	public static final SoundEvent CAMERA_UP = registerSound("cameraup");
	public static final SoundEvent CAMERA_DOWN = registerSound("cameradown");

	public static final ResourceKey<EntityType<?>> FREDDY_KEY = entityKey("freddy_fazbear");
	public static final EntityType<FreddyFazbearEntity> FREDDY_FAZBEAR = Registry.register(BuiltInRegistries.ENTITY_TYPE, FREDDY_KEY,
		EntityType.Builder.of(FreddyFazbearEntity::new, MobCategory.MONSTER).sized(0.7f, 2.2f).build(FREDDY_KEY));

	public static final ResourceKey<EntityType<?>> BONNIE_KEY = entityKey("bonnie_bunny");
	public static final EntityType<BonnieBunnyEntity> BONNIE_BUNNY = Registry.register(BuiltInRegistries.ENTITY_TYPE, BONNIE_KEY,
		EntityType.Builder.of(BonnieBunnyEntity::new, MobCategory.MONSTER).sized(0.7f, 2.2f).build(BONNIE_KEY));

	public static final ResourceKey<EntityType<?>> CHICA_KEY = entityKey("chica_chicken");
	public static final EntityType<ChicaChickenEntity> CHICA_CHICKEN = Registry.register(BuiltInRegistries.ENTITY_TYPE, CHICA_KEY,
		EntityType.Builder.of(ChicaChickenEntity::new, MobCategory.MONSTER).sized(0.7f, 2.1f).build(CHICA_KEY));

	public static final ResourceKey<EntityType<?>> FOXY_KEY = entityKey("foxy_pirate");
	public static final EntityType<FoxyPirateEntity> FOXY_PIRATE = Registry.register(BuiltInRegistries.ENTITY_TYPE, FOXY_KEY,
		EntityType.Builder.of(FoxyPirateEntity::new, MobCategory.MONSTER).sized(0.7f, 2.1f).build(FOXY_KEY));

	public static final ResourceKey<EntityType<?>> GOLDEN_FREDDY_KEY = entityKey("golden_freddy");
	public static final EntityType<GoldenFreddyEntity> GOLDEN_FREDDY = Registry.register(BuiltInRegistries.ENTITY_TYPE, GOLDEN_FREDDY_KEY,
		EntityType.Builder.of(GoldenFreddyEntity::new, MobCategory.MONSTER).sized(0.8f, 1.6f).build(GOLDEN_FREDDY_KEY));

	public static final ResourceKey<EntityType<?>> ENDO_KEY = entityKey("endo_skeleton");
	public static final EntityType<EndoSkeletonEntity> ENDO_SKELETON = Registry.register(BuiltInRegistries.ENTITY_TYPE, ENDO_KEY,
		EntityType.Builder.of(EndoSkeletonEntity::new, MobCategory.MONSTER).sized(0.6f, 2.0f).build(ENDO_KEY));

	public static final ResourceKey<EntityType<?>> PUPPET_KEY = entityKey("puppet");
	public static final EntityType<PuppetEntity> PUPPET = Registry.register(BuiltInRegistries.ENTITY_TYPE, PUPPET_KEY,
		EntityType.Builder.of(PuppetEntity::new, MobCategory.MONSTER).sized(0.6f, 2.4f).build(PUPPET_KEY));

	public static final ResourceKey<EntityType<?>> SPRINGTRAP_KEY = entityKey("springtrap");
	public static final EntityType<SpringtrapEntity> SPRINGTRAP = Registry.register(BuiltInRegistries.ENTITY_TYPE, SPRINGTRAP_KEY,
		EntityType.Builder.of(SpringtrapEntity::new, MobCategory.MONSTER).sized(0.7f, 2.2f).build(SPRINGTRAP_KEY));

	public static final ResourceKey<EntityType<?>> SPRING_BONNIE_KEY = entityKey("spring_bonnie");
	public static final EntityType<SpringBonnieEntity> SPRING_BONNIE = Registry.register(BuiltInRegistries.ENTITY_TYPE, SPRING_BONNIE_KEY,
		EntityType.Builder.of(SpringBonnieEntity::new, MobCategory.MONSTER).sized(0.7f, 2.2f).build(SPRING_BONNIE_KEY));

	public static final ResourceKey<EntityType<?>> TOY_FREDDY_KEY = entityKey("toy_freddy");
	public static final EntityType<ToyFreddyEntity> TOY_FREDDY = Registry.register(BuiltInRegistries.ENTITY_TYPE, TOY_FREDDY_KEY,
		EntityType.Builder.of(ToyFreddyEntity::new, MobCategory.MONSTER).sized(0.7f, 2.2f).build(TOY_FREDDY_KEY));

	public static final ResourceKey<EntityType<?>> TOY_BONNIE_KEY = entityKey("toy_bonnie");
	public static final EntityType<ToyBonnieEntity> TOY_BONNIE = Registry.register(BuiltInRegistries.ENTITY_TYPE, TOY_BONNIE_KEY,
		EntityType.Builder.of(ToyBonnieEntity::new, MobCategory.MONSTER).sized(0.7f, 2.2f).build(TOY_BONNIE_KEY));

	public static final ResourceKey<EntityType<?>> TOY_CHICA_KEY = entityKey("toy_chica");
	public static final EntityType<ToyChicaEntity> TOY_CHICA = Registry.register(BuiltInRegistries.ENTITY_TYPE, TOY_CHICA_KEY,
		EntityType.Builder.of(ToyChicaEntity::new, MobCategory.MONSTER).sized(0.7f, 2.1f).build(TOY_CHICA_KEY));

	public static final ResourceKey<EntityType<?>> MANGLE_KEY = entityKey("mangle");
	public static final EntityType<MangleEntity> MANGLE = Registry.register(BuiltInRegistries.ENTITY_TYPE, MANGLE_KEY,
		EntityType.Builder.of(MangleEntity::new, MobCategory.MONSTER).sized(0.7f, 2.1f).build(MANGLE_KEY));

	public static final ResourceKey<EntityType<?>> BALLOON_BOY_KEY = entityKey("balloon_boy");
	public static final EntityType<BalloonBoyEntity> BALLOON_BOY = Registry.register(BuiltInRegistries.ENTITY_TYPE, BALLOON_BOY_KEY,
		EntityType.Builder.of(BalloonBoyEntity::new, MobCategory.MONSTER).sized(0.5f, 1.4f).build(BALLOON_BOY_KEY));

	public static final ResourceKey<EntityType<?>> WITHERED_FREDDY_KEY = entityKey("withered_freddy");
	public static final EntityType<WitheredFreddyEntity> WITHERED_FREDDY = Registry.register(BuiltInRegistries.ENTITY_TYPE, WITHERED_FREDDY_KEY,
		EntityType.Builder.of(WitheredFreddyEntity::new, MobCategory.MONSTER).sized(0.75f, 2.3f).build(WITHERED_FREDDY_KEY));

	public static final ResourceKey<EntityType<?>> WITHERED_BONNIE_KEY = entityKey("withered_bonnie");
	public static final EntityType<WitheredBonnieEntity> WITHERED_BONNIE = Registry.register(BuiltInRegistries.ENTITY_TYPE, WITHERED_BONNIE_KEY,
		EntityType.Builder.of(WitheredBonnieEntity::new, MobCategory.MONSTER).sized(0.75f, 2.3f).build(WITHERED_BONNIE_KEY));

	public static final ResourceKey<EntityType<?>> WITHERED_CHICA_KEY = entityKey("withered_chica");
	public static final EntityType<WitheredChicaEntity> WITHERED_CHICA = Registry.register(BuiltInRegistries.ENTITY_TYPE, WITHERED_CHICA_KEY,
		EntityType.Builder.of(WitheredChicaEntity::new, MobCategory.MONSTER).sized(0.75f, 2.2f).build(WITHERED_CHICA_KEY));

	public static final ResourceKey<EntityType<?>> WITHERED_FOXY_KEY = entityKey("withered_foxy");
	public static final EntityType<WitheredFoxyEntity> WITHERED_FOXY = Registry.register(BuiltInRegistries.ENTITY_TYPE, WITHERED_FOXY_KEY,
		EntityType.Builder.of(WitheredFoxyEntity::new, MobCategory.MONSTER).sized(0.75f, 2.2f).build(WITHERED_FOXY_KEY));

	public static final ResourceKey<EntityType<?>> ENDO_02_KEY = entityKey("endo_02");
	public static final EntityType<Endo02Entity> ENDO_02 = Registry.register(BuiltInRegistries.ENTITY_TYPE, ENDO_02_KEY,
		EntityType.Builder.of(Endo02Entity::new, MobCategory.MONSTER).sized(0.65f, 2.1f).build(ENDO_02_KEY));

	public static final Item FREDDY_SPAWN_EGG = registerEgg("freddy_fazbear_spawn_egg", FREDDY_FAZBEAR);
	public static final Item BONNIE_SPAWN_EGG = registerEgg("bonnie_bunny_spawn_egg", BONNIE_BUNNY);
	public static final Item CHICA_SPAWN_EGG = registerEgg("chica_chicken_spawn_egg", CHICA_CHICKEN);
	public static final Item FOXY_SPAWN_EGG = registerEgg("foxy_pirate_spawn_egg", FOXY_PIRATE);
	public static final Item GOLDEN_FREDDY_SPAWN_EGG = registerEgg("golden_freddy_spawn_egg", GOLDEN_FREDDY);
	public static final Item ENDO_SPAWN_EGG = registerEgg("endo_skeleton_spawn_egg", ENDO_SKELETON);
	public static final Item PUPPET_SPAWN_EGG = registerEgg("puppet_spawn_egg", PUPPET);
	public static final Item SPRINGTRAP_SPAWN_EGG = registerEgg("springtrap_spawn_egg", SPRINGTRAP);
	public static final Item SPRING_BONNIE_SPAWN_EGG = registerEgg("spring_bonnie_spawn_egg", SPRING_BONNIE);
	public static final Item TOY_FREDDY_SPAWN_EGG = registerEgg("toy_freddy_spawn_egg", TOY_FREDDY);
	public static final Item TOY_BONNIE_SPAWN_EGG = registerEgg("toy_bonnie_spawn_egg", TOY_BONNIE);
	public static final Item TOY_CHICA_SPAWN_EGG = registerEgg("toy_chica_spawn_egg", TOY_CHICA);
	public static final Item MANGLE_SPAWN_EGG = registerEgg("mangle_spawn_egg", MANGLE);
	public static final Item BALLOON_BOY_SPAWN_EGG = registerEgg("balloon_boy_spawn_egg", BALLOON_BOY);
	public static final Item WITHERED_FREDDY_SPAWN_EGG = registerEgg("withered_freddy_spawn_egg", WITHERED_FREDDY);
	public static final Item WITHERED_BONNIE_SPAWN_EGG = registerEgg("withered_bonnie_spawn_egg", WITHERED_BONNIE);
	public static final Item WITHERED_CHICA_SPAWN_EGG = registerEgg("withered_chica_spawn_egg", WITHERED_CHICA);
	public static final Item WITHERED_FOXY_SPAWN_EGG = registerEgg("withered_foxy_spawn_egg", WITHERED_FOXY);
	public static final Item ENDO_02_SPAWN_EGG = registerEgg("endo_02_spawn_egg", ENDO_02);

	public static final Item FLASHLIGHT = registerItem("flash_light", FlashlightItem::new, new Item.Properties().stacksTo(1));
	public static final Item FREDDY_MASK = registerItem("freddy_mask", FreddyMaskItem::new, new Item.Properties().stacksTo(1)
		.component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
			.setCameraOverlay(id("screens/freddymaskoverlay"))
			.setEquipSound(SoundEvents.ARMOR_EQUIP_GENERIC)
			.build()));
	public static final Item GUARD_HAT = registerItem("guard_hat_helmet", Item::new, new Item.Properties().stacksTo(1)
		.component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
			.setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
			.build()));
	public static final Item CAMERA_MONITOR = registerItem("camera_monitor", CameraMonitorItem::new, new Item.Properties().stacksTo(1));
	public static final Item PIZZA_SLICE = registerItem("pizza_slice", Item::new, new Item.Properties().food(
		new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).build(),
		Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
			new MobEffectInstance(MobEffects.REGENERATION, 120, 0)))).build()));
	public static final Item PIZZA_BOX_CLOSED = registerItem("pizza_box_closed", Item::new, new Item.Properties().stacksTo(16));
	public static final Item EMPTY_PIZZA_BOX = registerItem("empty_pizza_box", Item::new, new Item.Properties().stacksTo(16));
	public static final Item FAZ_COIN = registerItem("faz_coin", Item::new, new Item.Properties().stacksTo(64).rarity(Rarity.RARE));
	public static final Item CUPCAKE = registerItem("cupcake", Item::new, new Item.Properties().stacksTo(16));
	public static final Item MICROPHONE = registerItem("microphone", Item::new, new Item.Properties().stacksTo(1));
	public static final Item BATTERY = registerItem("battery", Item::new, new Item.Properties().stacksTo(64));
	public static final Item PIZZA_CUTTER = registerItem("pizza_cutter", Item::new, new Item.Properties().stacksTo(1));

	public static final Block OFFICE_DOOR = registerBlock("office_door", OfficeDoorBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(4.0f, 8.0f).noOcclusion());
	public static final Block DOOR_BUTTON = registerBlock("door_button", DoorButtonBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(1.5f).noOcclusion());
	public static final Block DOOR_LIGHT = registerBlock("door_light", DoorLightBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(1.5f).noOcclusion()
			.lightLevel(state -> state.getValue(DoorLightBlock.LIT) ? 15 : 0));
	public static final Block FLUORESCENT_LIGHT = registerBlock("fluorescent_light", FluorescentLightBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.GLASS).strength(0.8f).lightLevel(state -> 15));
	public static final Block CHECKER_TILES = registerBlock("black_white_tile", CheckerTileBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(2.0f, 6.0f));
	public static final Block WALL_TILES = registerBlock("white_black_wall_tile", CheckerTileBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(2.0f, 6.0f));
	public static final Block RANCID_TILES = registerBlock("rancid_tiles", CheckerTileBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(2.0f, 6.0f));
	public static final Block ARCADE_FREDDY = registerBlock("arcade_machine_freddy", ArcadeMachineBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(3.0f).noOcclusion());
	public static final Block ARCADE_BONNIE = registerBlock("arcade_machine_bonnie", ArcadeMachineBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(3.0f).noOcclusion());
	public static final Block ARCADE_CHICA = registerBlock("arcade_machine_chica", ArcadeMachineBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(3.0f).noOcclusion());
	public static final Block ARCADE_FOXY = registerBlock("arcade_machine_foxy", ArcadeMachineBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(3.0f).noOcclusion());

	public static final Block FREDDY_PLUSHIE = registerBlock("freddy_plushie", PlushieBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(0.5f).noOcclusion());
	public static final Block BONNIE_PLUSHIE = registerBlock("bonnie_plushie", PlushieBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(0.5f).noOcclusion());
	public static final Block CHICA_PLUSHIE = registerBlock("chica_plushie", PlushieBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(0.5f).noOcclusion());
	public static final Block FOXY_PLUSHIE = registerBlock("foxy_plushie", PlushieBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(0.5f).noOcclusion());
	public static final Block GOLDEN_FREDDY_PLUSHIE = registerBlock("golden_freddy_plushie", PlushieBlock::new,
		BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(0.5f).noOcclusion());

	public static final CreativeModeTab CHARACTERS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("characters"),
		FabricCreativeModeTab.builder().title(Component.translatable("itemGroup.fnaf_mod.characters"))
			.icon(() -> new ItemStack(FREDDY_SPAWN_EGG))
			.displayItems((parameters, output) -> {
				output.accept(FREDDY_SPAWN_EGG);
				output.accept(BONNIE_SPAWN_EGG);
				output.accept(CHICA_SPAWN_EGG);
				output.accept(FOXY_SPAWN_EGG);
				output.accept(GOLDEN_FREDDY_SPAWN_EGG);
				output.accept(ENDO_SPAWN_EGG);
				output.accept(PUPPET_SPAWN_EGG);
				output.accept(SPRINGTRAP_SPAWN_EGG);
				output.accept(SPRING_BONNIE_SPAWN_EGG);
				output.accept(TOY_FREDDY_SPAWN_EGG);
				output.accept(TOY_BONNIE_SPAWN_EGG);
				output.accept(TOY_CHICA_SPAWN_EGG);
				output.accept(MANGLE_SPAWN_EGG);
				output.accept(BALLOON_BOY_SPAWN_EGG);
				output.accept(WITHERED_FREDDY_SPAWN_EGG);
				output.accept(WITHERED_BONNIE_SPAWN_EGG);
				output.accept(WITHERED_CHICA_SPAWN_EGG);
				output.accept(WITHERED_FOXY_SPAWN_EGG);
				output.accept(ENDO_02_SPAWN_EGG);
				output.accept(FREDDY_MASK);
				output.accept(MICROPHONE);
				output.accept(CUPCAKE);
			}).build());

	public static final CreativeModeTab BLOCKS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("blocks"),
		FabricCreativeModeTab.builder().title(Component.translatable("itemGroup.fnaf_mod.blocks"))
			.icon(() -> new ItemStack(OFFICE_DOOR))
			.displayItems((parameters, output) -> {
				output.accept(OFFICE_DOOR);
				output.accept(DOOR_BUTTON);
				output.accept(DOOR_LIGHT);
				output.accept(FLUORESCENT_LIGHT);
				output.accept(CHECKER_TILES);
				output.accept(WALL_TILES);
				output.accept(RANCID_TILES);
				output.accept(FREDDY_PLUSHIE);
				output.accept(BONNIE_PLUSHIE);
				output.accept(CHICA_PLUSHIE);
				output.accept(FOXY_PLUSHIE);
				output.accept(GOLDEN_FREDDY_PLUSHIE);
				output.accept(ARCADE_FREDDY);
				output.accept(ARCADE_BONNIE);
				output.accept(ARCADE_CHICA);
				output.accept(ARCADE_FOXY);
			}).build());

	public static final CreativeModeTab ITEMS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("items"),
		FabricCreativeModeTab.builder().title(Component.translatable("itemGroup.fnaf_mod.items"))
			.icon(() -> new ItemStack(FLASHLIGHT))
			.displayItems((parameters, output) -> {
				output.accept(FLASHLIGHT);
				output.accept(GUARD_HAT);
				output.accept(CAMERA_MONITOR);
				output.accept(PIZZA_CUTTER);
				output.accept(PIZZA_SLICE);
				output.accept(PIZZA_BOX_CLOSED);
				output.accept(EMPTY_PIZZA_BOX);
				output.accept(BATTERY);
				output.accept(FAZ_COIN);
			}).build());

	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(FREDDY_FAZBEAR, FreddyFazbearEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(BONNIE_BUNNY, BonnieBunnyEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(CHICA_CHICKEN, ChicaChickenEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FOXY_PIRATE, FoxyPirateEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(GOLDEN_FREDDY, GoldenFreddyEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(ENDO_SKELETON, EndoSkeletonEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(PUPPET, PuppetEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(SPRINGTRAP, SpringtrapEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(SPRING_BONNIE, SpringBonnieEntity.createAttributes());

		FabricDefaultAttributeRegistry.register(TOY_FREDDY, ToyFreddyEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(TOY_BONNIE, ToyBonnieEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(TOY_CHICA, ToyChicaEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(MANGLE, MangleEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(BALLOON_BOY, BalloonBoyEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(WITHERED_FREDDY, WitheredFreddyEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(WITHERED_BONNIE, WitheredBonnieEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(WITHERED_CHICA, WitheredChicaEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(WITHERED_FOXY, WitheredFoxyEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(ENDO_02, Endo02Entity.createAttributes());
	}

	private static SoundEvent registerSound(String name) {
		Identifier id = id(name);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}

	private static ResourceKey<EntityType<?>> entityKey(String name) {
		return ResourceKey.create(Registries.ENTITY_TYPE, id(name));
	}

	private static Item registerEgg(String name, EntityType<?> entityType) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id(name));
		return Registry.register(BuiltInRegistries.ITEM, key,
			new SpawnEggItem(new Item.Properties().spawnEgg(entityType).setId(key)));
	}

	private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id(name));
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
	}

	private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id(name));
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id(name));
		Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(properties.setId(blockKey)));
		Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey)));
		return block;
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
