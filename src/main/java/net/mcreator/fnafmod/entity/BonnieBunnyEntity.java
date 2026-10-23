package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class BonnieBunnyEntity extends AnimatronicEntity {
	public BonnieBunnyEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "bonnie", "textures/entities/bonnie_the_bunny.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(50.0, 0.25, 10.0);
	}
}
