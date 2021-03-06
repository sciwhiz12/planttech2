package net.kaneka.planttech2.blocks;

import net.kaneka.planttech2.blocks.baseclasses.BaseBlock;
import net.kaneka.planttech2.items.CropSeedItem;
import net.kaneka.planttech2.utilities.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class CropBarsBlock extends BaseBlock
{
	public CropBarsBlock()
	{
		super(Block.Properties.create(Material.WOOD).doesNotBlockMovement(), "cropbars", ModCreativeTabs.groupmain, true, true);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) 
    {
    	ItemStack possibleSeedStack = player.inventory.getCurrentItem();
    	if (CropSeedItem.plant(world, pos, possibleSeedStack))
    	{
			if (!world.isRemote && !player.isCreative())
				possibleSeedStack.shrink(1);
			return ActionResultType.SUCCESS;
    	}
    	return ActionResultType.PASS;
    }

	@Override
	public BlockRenderType getRenderType(BlockState iBlockState)
	{
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
	{
		return true; 
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
	{
		return world.getBlockState(pos.down()).isSolid();
	}
}
