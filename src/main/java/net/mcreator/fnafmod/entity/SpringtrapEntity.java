package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class SpringtrapEntity extends AnimatronicEntity {
	public SpringtrapEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "springtrap", "textures/entities/springtrap.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(60.0, 0.26, 15.0);
	}
}
