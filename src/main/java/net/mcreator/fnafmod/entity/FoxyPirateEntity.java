package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class FoxyPirateEntity extends AnimatronicEntity {
	public FoxyPirateEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "foxypirate", "textures/entities/foxy.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(40.0, 0.35, 14.0);
	}
}
