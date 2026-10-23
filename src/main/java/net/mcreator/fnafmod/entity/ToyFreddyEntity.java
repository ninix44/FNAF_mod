package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class ToyFreddyEntity extends AnimatronicEntity {
	public ToyFreddyEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "toyfreddy", "textures/entities/toyfreddy.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(40.0, 0.23, 6.0);
	}
}
