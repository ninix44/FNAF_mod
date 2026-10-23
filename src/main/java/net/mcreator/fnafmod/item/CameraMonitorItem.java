package net.mcreator.fnafmod.item;

import net.mcreator.fnafmod.FnafMod;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class CameraMonitorItem extends Item {
	public CameraMonitorItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!level.isClientSide()) {
			level.playSound(null, player.getX(), player.getY(), player.getZ(), FnafMod.CAMERA_UP, SoundSource.PLAYERS, 0.9f, 1.0f);
			player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100, 0, false, false, true));
			player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 100, 1, false, false, false));
		}
		return InteractionResult.SUCCESS;
	}
}
