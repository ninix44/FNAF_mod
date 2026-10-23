package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class BalloonBoyEntity extends AnimatronicEntity {
	public BalloonBoyEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "balloonboy", "textures/entities/bb.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(25.0, 0.22, 2.0);
	}
}
