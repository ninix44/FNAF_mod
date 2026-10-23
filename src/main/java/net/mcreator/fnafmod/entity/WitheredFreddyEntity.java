package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class WitheredFreddyEntity extends AnimatronicEntity {
	public WitheredFreddyEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "withered-freddy", "textures/entities/withered-freddy.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(55.0, 0.21, 8.0);
	}
}
