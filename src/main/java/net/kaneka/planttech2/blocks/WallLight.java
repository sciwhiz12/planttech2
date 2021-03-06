package net.kaneka.planttech2.blocks;

import net.kaneka.planttech2.blocks.baseclasses.FacingWallLightBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WallLight extends FacingWallLightBase
{
    public static final VoxelShape NEGATIVE_Z = Block.makeCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 12.0D, 4.0D);
    public static final VoxelShape POSITIVE_Z = Block.makeCuboidShape(10.0, 6.0, 16.0, 6.0, 12.0, 12.0);
    public static final VoxelShape NEGATIVE_X = Block.makeCuboidShape(0.0, 6.0, 10.0, 4.0, 12.0, 6.0);
    public static final VoxelShape POSITIVE_X = Block.makeCuboidShape(16.0, 6.0, 6.0, 12.0, 12.0, 10.0);

    public static final VoxelShape NEGATIVE_Z_BROKE = Block.makeCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 12.0D, 1.0D);
    public static final VoxelShape POSITIVE_Z_BROKE = Block.makeCuboidShape(10.0, 6.0, 16.0, 6.0, 12.0, 15.0);
    public static final VoxelShape NEGATIVE_X_BROKE = Block.makeCuboidShape(0.0, 6.0, 10.0, 1.0, 12.0, 6.0);
    public static final VoxelShape POSITIVE_X_BROKE = Block.makeCuboidShape(16.0, 6.0, 6.0, 15.0, 12.0, 10.0);

    public WallLight(Properties property, String name, ItemGroup group, boolean hasItem)
    {
        super(property, name, group, hasItem);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        boolean broke = state.get(LIGHT_STATUS) == 0;
        switch (state.get(HORIZONTAL_FACING))
        {
            case NORTH:
            default:
                return broke ? NEGATIVE_Z_BROKE : NEGATIVE_Z;
            case SOUTH:
                return broke ? POSITIVE_Z_BROKE : POSITIVE_Z;
            case WEST:
                return broke ? NEGATIVE_X_BROKE : NEGATIVE_X;
            case EAST:
                return broke ? POSITIVE_X_BROKE : POSITIVE_X;
        }
    }

    @Override
    public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity)
    {
        return state.get(LIGHT_STATUS) == 0 ? SoundType.STONE : super.getSoundType(state, world, pos, entity);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        super.onBlockHarvested(worldIn, pos, state, player);
    }
}
