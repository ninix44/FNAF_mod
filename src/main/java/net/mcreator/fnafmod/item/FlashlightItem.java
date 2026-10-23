package net.mcreator.fnafmod.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class FlashlightItem extends Item {
	public FlashlightItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			boolean wasOn = isOn(stack);
			CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean("on", !wasOn));
			player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.8f, wasOn ? 0.9f : 1.2f);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
		if (isOn(stack) && entity instanceof LivingEntity living && (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)) {
			living.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, false, false, false));
		}
	}

	public static boolean isOn(ItemStack stack) {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		if (data == null) return false;
		return data.copyTag().getBooleanOr("on", false);
	}
}
