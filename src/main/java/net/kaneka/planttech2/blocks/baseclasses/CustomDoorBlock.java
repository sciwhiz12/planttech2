package net.kaneka.planttech2.blocks.baseclasses;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CustomDoorBlock extends BaseBlock
{

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
	protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);

	public CustomDoorBlock(Block.Properties builder, String name, ItemGroup group, boolean hasItem)
	{
		super(builder, name, group, hasItem, true);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(OPEN, Boolean.valueOf(false)).with(HINGE, DoorHingeSide.LEFT)
		        .with(POWERED, Boolean.valueOf(false)).with(HALF, DoubleBlockHalf.LOWER));
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		Direction enumfacing = state.get(FACING);
		boolean flag = !state.get(OPEN);
		boolean flag1 = state.get(HINGE) == DoorHingeSide.RIGHT;
		switch (enumfacing)
		{
		case EAST:
		default:
			return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
		case SOUTH:
			return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
		case WEST:
			return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
		case NORTH:
			return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
		}
	}

	@SuppressWarnings("deprecation")
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		DoubleBlockHalf doubleblockhalf = stateIn.get(HALF);
		if (facing.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (facing == Direction.UP))
		{
			return facingState.getBlock() == this && facingState.get(HALF) != doubleblockhalf
			        ? stateIn.with(FACING, facingState.get(FACING)).with(OPEN, facingState.get(OPEN)).with(HINGE, facingState.get(HINGE)).with(POWERED, facingState.get(POWERED))
			        : Blocks.AIR.getDefaultState();
		} else
		{
			return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState()
			        : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}
	}

	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
	}

	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		DoubleBlockHalf doubleblockhalf = state.get(HALF);
		BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		if (blockstate.getBlock() == this && blockstate.get(HALF) != doubleblockhalf)
		{
			worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
			worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
			ItemStack itemstack = player.getHeldItemMainhand();
			if (!worldIn.isRemote && !player.isCreative())
			{
				Block.spawnDrops(state, worldIn, pos, (TileEntity) null, player, itemstack);
				Block.spawnDrops(blockstate, worldIn, blockpos, (TileEntity) null, player, itemstack);
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
	{
		switch (type)
		{
		case LAND:
			return state.get(OPEN);
		case WATER:
			return false;
		case AIR:
			return state.get(OPEN);
		default:
			return false;
		}
	}

	public boolean isFullCube(BlockState state)
	{
		return false;
	}

	private int getCloseSound()
	{
		return this.material == Material.IRON ? 1011 : 1012;
	}

	private int getOpenSound()
	{
		return this.material == Material.IRON ? 1005 : 1006;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockPos blockpos = context.getPos();
		if (blockpos.getY() < 255 && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context))
		{
			World world = context.getWorld();
			boolean flag = world.isBlockPowered(blockpos) || world.isBlockPowered(blockpos.up());
			return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing()).with(HINGE, this.getHingeSide(context)).with(POWERED, Boolean.valueOf(flag))
			        .with(OPEN, Boolean.valueOf(flag)).with(HALF, DoubleBlockHalf.LOWER);
		} else
		{
			return null;
		}
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		worldIn.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	private DoorHingeSide getHingeSide(BlockItemUseContext p_208073_1_)
	{
		IBlockReader iblockreader = p_208073_1_.getWorld();
		BlockPos blockpos = p_208073_1_.getPos();
		Direction direction = p_208073_1_.getPlacementHorizontalFacing();
		BlockPos blockpos1 = blockpos.up();
		Direction direction1 = direction.rotateYCCW();
		BlockPos blockpos2 = blockpos.offset(direction1);
		BlockState blockstate = iblockreader.getBlockState(blockpos2);
		BlockPos blockpos3 = blockpos1.offset(direction1);
		BlockState blockstate1 = iblockreader.getBlockState(blockpos3);
		Direction direction2 = direction.rotateY();
		BlockPos blockpos4 = blockpos.offset(direction2);
		BlockState blockstate2 = iblockreader.getBlockState(blockpos4);
		BlockPos blockpos5 = blockpos1.offset(direction2);
		BlockState blockstate3 = iblockreader.getBlockState(blockpos5);
		int i = (isOpaque(blockstate.getCollisionShape(iblockreader, blockpos2)) ? -1 : 0) + (isOpaque(blockstate1.getCollisionShape(iblockreader, blockpos3)) ? -1 : 0)
		        + (isOpaque(blockstate2.getCollisionShape(iblockreader, blockpos4)) ? 1 : 0) + (isOpaque(blockstate3.getCollisionShape(iblockreader, blockpos5)) ? 1 : 0);
		boolean flag = blockstate.getBlock() == this && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
		boolean flag1 = blockstate2.getBlock() == this && blockstate2.get(HALF) == DoubleBlockHalf.LOWER;
		if ((!flag || flag1) && i <= 0)
		{
			if ((!flag1 || flag) && i >= 0)
			{
				int j = direction.getXOffset();
				int k = direction.getZOffset();
				Vector3d vec3d = p_208073_1_.getHitVec();
				double d0 = vec3d.x - (double) blockpos.getX();
				double d1 = vec3d.z - (double) blockpos.getZ();
				return (j >= 0 || !(d1 < 0.5D)) && (j <= 0 || !(d1 > 0.5D)) && (k >= 0 || !(d0 > 0.5D)) && (k <= 0 || !(d0 < 0.5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
			} else
			{
				return DoorHingeSide.LEFT;
			}
		} else
		{
			return DoorHingeSide.RIGHT;
		}
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if (this.material == Material.IRON)
		{
			return ActionResultType.FAIL;
		} else
		{
			state = state.func_235896_a_(OPEN);
			worldIn.setBlockState(pos, state, 10);
			worldIn.playEvent(player, state.get(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
			return ActionResultType.SUCCESS;
		}
	}

	public void toggleDoor(World worldIn, BlockPos pos, boolean open)
	{
		BlockState BlockState = worldIn.getBlockState(pos);
		if (BlockState.getBlock() == this && BlockState.get(OPEN) != open)
		{
			worldIn.setBlockState(pos, BlockState.with(OPEN, Boolean.valueOf(open)), 10);
			this.playSound(worldIn, pos, open);
		}
	}

	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.offset(state.get(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		if (blockIn != this && flag != state.get(POWERED))
		{
			if (flag != state.get(OPEN))
			{
				this.playSound(worldIn, pos, flag);
			}
			worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag)).with(OPEN, Boolean.valueOf(flag)), 2);
		}
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		BlockPos blockpos = pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		if (state.get(HALF) == DoubleBlockHalf.LOWER)
			return Block.hasEnoughSolidSide(worldIn, blockpos, Direction.UP);
		return blockstate.getBlock() == this;
	}

	private void playSound(World world, BlockPos pos, boolean flag)
	{
		world.playEvent((PlayerEntity) null, flag ? this.getOpenSound() : this.getCloseSound(), pos, 0);
	}

	public PushReaction getPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}

	public BlockState rotate(BlockState state, Rotation rot)
	{
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return mirrorIn == Mirror.NONE ? state : state.rotate(mirrorIn.toRotation(state.get(FACING))).func_235896_a_(HINGE);
	}

	@OnlyIn(Dist.CLIENT)
	public long getPositionRandom(BlockState state, BlockPos pos)
	{
		return MathHelper.getCoordinateRandom(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(HALF, FACING, OPEN, HINGE, POWERED);
	}
}