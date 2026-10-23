package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class ToyChicaEntity extends AnimatronicEntity {
	public ToyChicaEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "toychica", "textures/entities/toychica.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(35.0, 0.24, 5.0);
	}
}
