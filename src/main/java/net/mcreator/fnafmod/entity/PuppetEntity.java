package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class PuppetEntity extends AnimatronicEntity {
	public PuppetEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "puppet", "textures/entities/puppet.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(40.0, 0.28, 12.0);
	}
}
