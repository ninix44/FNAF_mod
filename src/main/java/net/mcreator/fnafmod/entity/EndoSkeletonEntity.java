package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class EndoSkeletonEntity extends AnimatronicEntity {
	public EndoSkeletonEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "endo01", "textures/entities/endo01.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(35.0, 0.22, 8.0);
	}
}
