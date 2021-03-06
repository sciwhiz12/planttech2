package net.kaneka.planttech2.tileentity.machine;

import net.kaneka.planttech2.container.SeedconstructorContainer;
import net.kaneka.planttech2.enums.EnumTraitsInt;
import net.kaneka.planttech2.hashmaps.HashMapCropTraits;
import net.kaneka.planttech2.registries.ModItems;
import net.kaneka.planttech2.registries.ModTileEntities;
import net.kaneka.planttech2.tileentity.machine.baseclasses.EnergyInventoryFluidTileEntity;
import net.kaneka.planttech2.utilities.PlantTechConstants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;

public class SeedconstructorTileEntity extends EnergyInventoryFluidTileEntity
{
	private int ticksPassed = 0;
	protected final IIntArray field_array = new IIntArray()
	{
		public int get(int index)
		{
			switch (index)
			{
			case 0:
				return SeedconstructorTileEntity.this.energystorage.getEnergyStored();
			case 1:
				return SeedconstructorTileEntity.this.energystorage.getMaxEnergyStored();
			case 2:
			    return SeedconstructorTileEntity.this.BIOMASS_CAP.getCurrentStorage();
			case 3:
			    return SeedconstructorTileEntity.this.BIOMASS_CAP.getMaxStorage();
			case 4: 
				return SeedconstructorTileEntity.this.ticksPassed; 
				
			default:
				return 0;
			}
		}

		public void set(int index, int value)
		{
			switch (index)
			{
			case 0:
				SeedconstructorTileEntity.this.energystorage.setEnergyStored(value);
				break;
			case 1:
				SeedconstructorTileEntity.this.energystorage.setEnergyMaxStored(value);
				break;
			case 2:
				SeedconstructorTileEntity.this.BIOMASS_CAP.setCurrentStorage(value);
			    break; 
			case 3: 
				SeedconstructorTileEntity.this.BIOMASS_CAP.setMaxStorage(value);
				break;
			case 4: 
				SeedconstructorTileEntity.this.ticksPassed = value; 
				break; 
			}

		}

		public int size()
		{
			return 5;
		}
	};

	public SeedconstructorTileEntity()
	{
		super(ModTileEntities.SEEDCONSTRUCTOR_TE, 1000, 8, 5000, PlantTechConstants.MACHINETIER_SEEDCONSTRUCTOR);
	}

	@Override
	public void doUpdate()
	{
		if (energystorage.getEnergyStored() > energyPerTick())
		{
			ItemStack stack1 = itemhandler.getStackInSlot(0);
			ItemStack stack2 = itemhandler.getStackInSlot(1);

			if (!stack1.isEmpty() && stack2.isEmpty())
			{
				if (stack1.getItem() == ModItems.DNA_CONTAINER && stack1.hasTag() && BIOMASS_CAP.getCurrentStorage() >= fluidPerItem())
				{
					if (ticksPassed < ticksPerItem())
					{
						ticksPassed++;
						energystorage.extractEnergy(energyPerTick(), false);
					} else
					{
						ticksPassed = 0;
						energystorage.extractEnergy(energyPerTick(), false);
						CompoundNBT nbt = stack1.getTag();
						HashMapCropTraits traits = new HashMapCropTraits();
						traits.setAnalysed(true);
						if (nbt.contains("type"))
						{
							traits.setType(nbt.getString("type"));
						}
						for (String key : HashMapCropTraits.getTraitsKeyList())
						{
							if (nbt.contains(key))
							{
								if (!key.equals("type"))
								{
									traits.setTrait(EnumTraitsInt.getByName(key), nbt.getInt(key));
								}
							}
						}
						ItemStack stack = new ItemStack(ModItems.SEEDS.get(traits.getType()));
						itemhandler.setStackInSlot(1, traits.addToItemStack(stack));
						BIOMASS_CAP.extractBiomass(fluidPerItem());
						addKnowledge();
					}
				}
			}
		}
		doEnergyLoop();
		doFluidLoop();
	}
	
	@Override
	public IIntArray getIntArray()
	{
		return field_array;
	}

	public int fluidPerItem()
	{
		return 500;
	}

	public int energyPerTick()
	{
		return 4 + (getUpgradeTier(2, PlantTechConstants.SPEEDUPGRADE_TYPE) * 4);
	}

	public int ticksPerItem()
	{
		return 200 - (getUpgradeTier(2, PlantTechConstants.SPEEDUPGRADE_TYPE) * 35);
	}

	@Override
	public String getNameString()
	{
		return "seedconstructor";
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putInt("tickspassed", ticksPassed);
		super.write(compound);
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound)
	{
		this.ticksPassed = compound.getInt("tickspassed");
		super.read(state, compound);
	}

	@Override
	protected int getFluidInSlot()
	{
		return 3;
	}

	@Override
	protected int getFluidOutSlot()
	{
		return 4;
	}
	
	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity player)
	{
		return new SeedconstructorContainer(id, inv, this);
	}

	@Override
	public int getEnergyInSlot()
	{
		return 5;
	}

	@Override
	public int getEnergyOutSlot()
	{
		return 6;
	}

	@Override
	public int getKnowledgeChipSlot()
	{
		return 7;
	}

	@Override
	public int getKnowledgePerAction()
	{
		return 250;
	}

}
