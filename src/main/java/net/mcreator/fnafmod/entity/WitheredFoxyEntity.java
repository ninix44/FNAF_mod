package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class WitheredFoxyEntity extends AnimatronicEntity {
	public WitheredFoxyEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "withered-foxy", "textures/entities/withered-foxy.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(45.0, 0.28, 8.0);
	}
}
