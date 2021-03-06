package net.kaneka.planttech2.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.kaneka.planttech2.PlantTechMain;
import net.kaneka.planttech2.enums.EnumTemperature;
import net.kaneka.planttech2.enums.EnumTraitsInt;
import net.kaneka.planttech2.hashmaps.HashMapCropTraits;
import net.kaneka.planttech2.items.AdvancedAnalyserItem;
import net.kaneka.planttech2.items.AnalyserItem;
import net.kaneka.planttech2.items.CropRemover;
import net.kaneka.planttech2.registries.ModBlocks;
import net.kaneka.planttech2.tileentity.CropsTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.context.BlockPosContext;

public class CropBaseBlock extends ContainerBlock
{
	public static final IntegerProperty GROWSTATE = IntegerProperty.create("growstate", 0, 7);
	private String entryName;

	public CropBaseBlock(String entryName)
	{
		super(Block.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(0.5f));
		this.entryName = entryName;
		setRegistryName(entryName + "_crop");
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn)
	{
		return new CropsTileEntity();
	}

	public void updateCrop(World world, BlockPos pos, HashMapCropTraits traits)
	{
		BlockState state = world.getBlockState(pos);
		int growstate = state.get(GROWSTATE);
		if (growstate < 7)
				world.setBlockState(pos, state.with(GROWSTATE, growstate + 1));
		else
		{
			List<BlockPos> neighborpos = getNeighborBlockPosRandom(pos);
			for (BlockPos blockpos : neighborpos)
			{
				if (world.getBlockState(blockpos).getBlock() == ModBlocks.CROPBARS)
				{
					List<BlockPos> cropbarneighbors = getNeighborBlockPosRandomExeptOne(blockpos, pos);
					for (BlockPos possiblePartner : cropbarneighbors)
					{
						if (world.getBlockState(possiblePartner).getBlock() instanceof CropBaseBlock)
						{
							if (world.getTileEntity(possiblePartner) instanceof CropsTileEntity)
							{
								HashMapCropTraits partnertraits = ((CropsTileEntity) world.getTileEntity(possiblePartner)).getTraits();
								world.setBlockState(blockpos, getDefaultState());
								if (world.getTileEntity(blockpos) instanceof CropsTileEntity && world.getTileEntity(pos) instanceof CropsTileEntity)
								{
									((CropsTileEntity) world.getTileEntity(blockpos))
									        .setTraits(((CropsTileEntity) world.getTileEntity(pos)).getTraits().calculateNewTraits(partnertraits));
									break;
								}
							}
						}
					}
					if (!(world.getBlockState(blockpos).getBlock() instanceof CropBaseBlock))
					{
						world.setBlockState(blockpos, getDefaultState());
						if (world.getTileEntity(blockpos) instanceof CropsTileEntity && world.getTileEntity(pos) instanceof CropsTileEntity)
						{
							((CropsTileEntity) world.getTileEntity(blockpos)).setTraits(((CropsTileEntity) world.getTileEntity(pos)).getTraits().copy());
						}
					}
					break;
				}
			}
		}
	}

	public void updateCreative(World world, BlockPos pos)
	{
		BlockState state = world.getBlockState(pos);
		int growstate = state.get(GROWSTATE).intValue();
		if (growstate < 7)
		{
			world.setBlockState(pos, state.with(GROWSTATE, 7));
		}
	}

	private List<BlockPos> getNeighborBlockPosRandom(BlockPos pos)
	{
		List<BlockPos> neighbors = new ArrayList<BlockPos>();
		neighbors.add(pos.north());
		neighbors.add(pos.east());
		neighbors.add(pos.south());
		neighbors.add(pos.west());
		Collections.shuffle(neighbors);
		return neighbors;
	}

	private List<BlockPos> getNeighborBlockPosRandomExeptOne(BlockPos pos, BlockPos exept)
	{
		List<BlockPos> neighbors = getNeighborBlockPosRandom(pos);
		neighbors.remove(exept);
		return neighbors;
	}

	private boolean canGrow(World world, BlockPos pos, HashMapCropTraits traits)
	{
		if (!enoughLight(world, pos, traits.getTrait(EnumTraitsInt.LIGHTSENSITIVITY)))
			return false;
		if (!enoughWater(world, pos, traits.getTrait(EnumTraitsInt.WATERSENSITIVITY)))
			return false;
		if (!rightSoil(world, pos, traits.getType()))
			return false;
		if (!rightTemperature(world, pos, traits.getType(), traits.getTrait(EnumTraitsInt.TEMPERATURETOLERANCE)))
			return false;
		return true;
	}

	public String[] canGrowString(World world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		String[] messages = new String[10];
		if (te instanceof CropsTileEntity)
		{
			HashMapCropTraits traits = ((CropsTileEntity) te).getTraits();
			if (!enoughLight(world, pos, traits.getTrait(EnumTraitsInt.LIGHTSENSITIVITY)))
				messages[1] = "Not enough light";
			if (!enoughWater(world, pos, traits.getTrait(EnumTraitsInt.WATERSENSITIVITY)))
				messages[2] = "Not enough water";
			if (!rightSoil(world, pos, traits.getType()))
				messages[3] = "Not right soil";
			if (!rightTemperature(world, pos, traits.getType(), traits.getTrait(EnumTraitsInt.TEMPERATURETOLERANCE)))
				messages[4] = "Not right temperature";
		} else
		{
			messages[0] = "error";
		}
		return messages;
	}

	public boolean enoughLight(World world, BlockPos pos, int lightsensitivity)
	{
		if (!world.isAreaLoaded(pos, 1))// prevent loading unloaded chunks
		{
			return false;
		}
		if (world.getNeighborAwareLightSubtracted(pos, 0) >= (14 - lightsensitivity))
		{
			return true;
		}
		return false;
	}

	public boolean enoughWater(World world, BlockPos pos, int waterSensitivity)
	{
		for (BlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(((-1) * (waterSensitivity + 1)), 0, ((-1) * (waterSensitivity + 1))),
		        pos.add((waterSensitivity + 1), -1, (waterSensitivity + 1))))
		{
			if (world.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER )
			{
				return true;
			}
			
			if(world.getBlockState(blockpos$mutableblockpos).hasProperty(BlockStateProperties.WATERLOGGED))
			{
				if(world.getBlockState(blockpos$mutableblockpos).get(BlockStateProperties.WATERLOGGED))
				{
					return true; 
				}
			}
		}

		return false;
	}

	public boolean rightSoil(World world, BlockPos pos, String name)
	{
		ItemStack stack = PlantTechMain.croplist.getEntryByName(name).getSoil();
		if (stack.isEmpty())
		{
			return true;
		} else if (stack.getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) stack.getItem()).getBlock();
			BlockState state = world.getBlockState(pos.down());
			if ((state.getBlock() == block) || state.getBlock() == ModBlocks.UNIVERSAL_SOIL_INFUSED)
			{
				return true;
			}
			return false;
		}
		return false;

	}

	public boolean rightTemperature(World world, BlockPos pos, String name, int tolerance)
	{
		EnumTemperature temp = PlantTechMain.croplist.getEntryByName(name).getTemperature();
		return temp.inRange(world.getBiomeManager().getBiome(pos).getTemperature(pos), tolerance);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(GROWSTATE);
	}
	
	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid)
	{
		if (willHarvest && !player.isCreative())
		{
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
	}

	@Override
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack)
	{
		super.harvestBlock(world, player, pos, state, te, stack);
		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray)
	{
		int growstate = state.get(GROWSTATE).intValue();
		if (growstate > 6 && hand.equals(Hand.MAIN_HAND) && !worldIn.isRemote)
		{
			ItemStack holdItem = player.getHeldItem(Hand.MAIN_HAND);
			if (!holdItem.isEmpty())
			{
				if (holdItem.getItem() instanceof AnalyserItem || holdItem.getItem() instanceof AdvancedAnalyserItem || holdItem.getItem() instanceof CropRemover)
				{
					return super.onBlockActivated(state, worldIn, pos, player, hand, ray);
				}
			}
			NonNullList<ItemStack> drops = NonNullList.create();
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof CropsTileEntity)
			{
				((CropsTileEntity) te).dropsRemoveOneSeed(drops, growstate);
				for (ItemStack stack : drops)
				{
					spawnAsEntity(worldIn, pos, stack);
				}
				worldIn.setBlockState(pos, state.with(GROWSTATE, 0));
			}
		}
		return super.onBlockActivated(state, worldIn, pos, player, hand, ray);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		List<ItemStack> drops = Lists.newArrayList();
		int growstate = state.get(GROWSTATE);
		Vector3d vec3d = builder.get(LootParameters.field_237457_g_);
		if (vec3d != null)
		{
			BlockPos pos = new BlockPos(vec3d);
			TileEntity te = builder.getWorld().getTileEntity(pos);
			if (te instanceof CropsTileEntity)
			{
				((CropsTileEntity) te).addDrops(drops, growstate);
				drops.add(new ItemStack(ModBlocks.CROPBARS));
			}
		}
		return drops; 
	}

	/*----------------------RENDERING------------------*/

	@Override
	public BlockRenderType getRenderType(BlockState iBlockState)
	{
		return BlockRenderType.MODEL;
	}


	public String getEntryName()
	{
		return entryName;
	}

	public static boolean isOpaque(VoxelShape shape)
	{
		return true;
	}
	
	

	public static class ColorHandler implements IBlockColor
	{
		@Override
		public int getColor(BlockState state, IBlockDisplayReader blockDisplayReader, BlockPos pos, int tintindex)
		{
			if (tintindex == 0)
			{
				return PlantTechMain.croplist.getEntryByName(((CropBaseBlock) state.getBlock()).getEntryName()).getSeedColor();
			}
			return 0xFFFFFFFF;
		}

	}
}
