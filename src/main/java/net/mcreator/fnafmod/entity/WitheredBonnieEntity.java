package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class WitheredBonnieEntity extends AnimatronicEntity {
	public WitheredBonnieEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "withered-bonnie", "textures/entities/withered-bonnie.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(50.0, 0.22, 7.0);
	}
}
