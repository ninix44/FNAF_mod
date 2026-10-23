package net.mcreator.fnafmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class FreddyFazbearEntity extends AnimatronicEntity {
	public FreddyFazbearEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level, "freddyfazbear", "textures/entities/freddyfazbeartexture.png");
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimatronicAttributes(50.0, 0.23, 12.0);
	}
}
