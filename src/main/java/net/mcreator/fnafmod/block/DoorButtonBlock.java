package net.mcreator.fnafmod.block;

import com.mojang.serialization.MapCodec;
import net.mcreator.fnafmod.FnafMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class DoorButtonBlock extends HorizontalDirectionalBlock {
	public static final MapCodec<DoorButtonBlock> CODEC = simpleCodec(DoorButtonBlock::new);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public DoorButtonBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(POWERED, false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		boolean powered = !state.getValue(POWERED);
		level.setBlock(pos, state.setValue(POWERED, powered), 3);
		level.playSound(null, pos, FnafMod.DOOR_CLOSE, SoundSource.BLOCKS, 0.8f, powered ? 1.2f : 0.9f);

		for (BlockPos target : BlockPos.betweenClosed(pos.offset(-3, -2, -3), pos.offset(3, 2, 3))) {
			BlockState targetState = level.getBlockState(target);
			if (targetState.is(FnafMod.OFFICE_DOOR)) {
				level.setBlock(target, targetState.setValue(OfficeDoorBlock.OPEN, !powered), 3);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	protected boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(POWERED) ? 15 : 0;
	}
}
