package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class ToyBonnieEntity extends AnimatronicEntity {
	public ToyBonnieEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "toybonnie", "textures/entities/toybonnie.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(35.0, 0.25, 5.0);
	}
}
