package net.mcreator.fnafmod.entity;

import net.mcreator.fnafmod.FnafMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class AnimatronicEntity extends Monster {
	private static final EntityDataAccessor<String> ANIMATION_STATE = SynchedEntityData.defineId(AnimatronicEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<String> TEXTURE_VARIANT = SynchedEntityData.defineId(AnimatronicEntity.class, EntityDataSerializers.STRING);

	private final String modelKey;
	private final String defaultTexture;

	protected AnimatronicEntity(EntityType<? extends Monster> entityType, Level level, String modelKey, String defaultTexture) {
		super(entityType, level);
		this.modelKey = modelKey;
		this.defaultTexture = defaultTexture != null ? defaultTexture : "";
		if (getTextureVariant().isEmpty() && !this.defaultTexture.isEmpty()) {
			setTextureVariant(this.defaultTexture);
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ANIMATION_STATE, "idle");
		builder.define(TEXTURE_VARIANT, "");
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.25, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::shouldTargetPlayer));
	}

	protected boolean shouldTargetPlayer(LivingEntity target, ServerLevel serverLevel) {
		if (target instanceof Player player) {
			ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
			if (head.is(FnafMod.FREDDY_MASK)) {
				return false;
			}
			return level().isDarkOutside() || getTarget() == player;
		}
		return false;
	}

	public String modelKey() {
		return modelKey;
	}

	public String getAnimationState() {
		String s = this.entityData.get(ANIMATION_STATE);
		return s != null ? s : "idle";
	}

	public void setAnimationState(String state) {
		this.entityData.set(ANIMATION_STATE, state != null ? state : "idle");
	}

	public String getTextureVariant() {
		String v = this.entityData.get(TEXTURE_VARIANT);
		if ((v == null || v.isEmpty()) && defaultTexture != null && !defaultTexture.isEmpty()) {
			return defaultTexture;
		}
		return v != null ? v : "";
	}

	public void setTextureVariant(String variant) {
		this.entityData.set(TEXTURE_VARIANT, variant != null ? variant : "");
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(FnafMod.ANIMATRONIC_STEP, 0.2f, 1.0f);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.WOOL_HIT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ANVIL_DESTROY;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(FnafMod.PIZZA_SLICE)) {
			if (!level().isClientSide()) {
				if (!player.getAbilities().instabuild) stack.shrink(1);
				this.heal(10.0f);
				this.playSound(SoundEvents.GENERIC_EAT.value(), 1.0f, 1.0f);
			}
			return InteractionResult.SUCCESS;
		}

		if (!level().isClientSide()) {
			level().playSound(null, getX(), getY(), getZ(), FnafMod.NOSE_HONK, SoundSource.NEUTRAL, 0.8f, 1.0f);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putString("TextureVariant", getTextureVariant());
		output.putString("AnimationState", getAnimationState());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		setTextureVariant(input.getStringOr("TextureVariant", defaultTexture != null ? defaultTexture : ""));
		setAnimationState(input.getStringOr("AnimationState", "idle"));
	}

	public static AttributeSupplier.Builder createAnimatronicAttributes(double maxHealth, double movementSpeed, double attackDamage) {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, maxHealth)
			.add(Attributes.MOVEMENT_SPEED, movementSpeed)
			.add(Attributes.ATTACK_DAMAGE, attackDamage)
			.add(Attributes.FOLLOW_RANGE, 24.0)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
	}
}
