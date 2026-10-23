package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class WitheredChicaEntity extends AnimatronicEntity {
	public WitheredChicaEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "withered-chica", "textures/entities/withered-chica.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(50.0, 0.22, 7.0);
	}
}
