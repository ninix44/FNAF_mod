package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class Endo02Entity extends AnimatronicEntity {
	public Endo02Entity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "endo02", "textures/entities/endo02.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(40.0, 0.24, 6.0);
	}
}
