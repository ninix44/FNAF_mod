package net.mcreator.fnafmod.entity;

import net.mcreator.fnafmod.FnafMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GoldenFreddyEntity extends AnimatronicEntity {
	public GoldenFreddyEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "freddyfazbear", "textures/entities/real_golden_freddy.png");
		setAnimationState("sit");
	}

	@Override
	public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float amount) {
		boolean damaged = super.hurtServer(serverLevel, damageSource, amount);
		if (damaged && !level().isClientSide()) {
			level().playSound(null, getX(), getY(), getZ(), FnafMod.GOLDEN_FREDDY_SCREAM, SoundSource.HOSTILE, 1.5f, 1.0f);
			this.discard();
		}
		return damaged;
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide() && tickCount % 20 == 0) {
			Player player = level().getNearestPlayer(this, 3.5);
			if (player != null && !player.isCreative() && !player.isSpectator()) {
				level().playSound(null, getX(), getY(), getZ(), FnafMod.GOLDEN_FREDDY_SCREAM, SoundSource.HOSTILE, 1.5f, 1.0f);
				this.discard();
			}
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(100.0, 0.0, 20.0);
	}
}
