package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class ChicaChickenEntity extends AnimatronicEntity {
	public ChicaChickenEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "chica", "textures/entities/chica.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(45.0, 0.22, 9.0);
	}
}
