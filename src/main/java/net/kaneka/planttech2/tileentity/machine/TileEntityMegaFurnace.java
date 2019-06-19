package net.kaneka.planttech2.tileentity.machine;

import net.kaneka.planttech2.container.ContainerMegaFurnace;
import net.kaneka.planttech2.recipes.ModRecipeTypes;
import net.kaneka.planttech2.registries.ModTileEntities;
import net.kaneka.planttech2.tileentity.machine.baseclasses.TileEntityEnergyInventory;
import net.kaneka.planttech2.utilities.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class TileEntityMegaFurnace extends TileEntityEnergyInventory
{
    public int[] ticksPassed = new int[6];
    boolean isSmelting;

    public TileEntityMegaFurnace()
    {
	super(ModTileEntities.MEGAFURNACE_TE, 10000, 13);
    }

    @Override
    public void doUpdate()
    {
	isSmelting = false;
	for (int i = 0; i < 6; i++)
	{
	    if (this.energystorage.getEnergyStored() > this.getEnergyPerTickPerItem() || true)
	    {
		if (this.canSmelt(i))
		{
		    isSmelting = true;
		    ticksPassed[i]++;
		    if (ticksPassed[i] >= this.getTicksPerItem())
		    {
			this.smeltItem(i);
			ticksPassed[i] = 0;
		    }
		}
		else if (ticksPassed[i] > 0)
		{
		    ticksPassed[i] = 0;
		}
	    }
	    else
	    {
		if (!this.canSmelt(i) && ticksPassed[i] > 0)
		{
		    ticksPassed[i] = 0;
		}
		break;
	    }
	}
	if (isSmelting)
	{
	    //this.energystorage.extractEnergy(getEnergyPerTickPerItem(), false);
	}
    }

    private boolean canSmelt(int slot)
    {
	ItemStack itemstack = itemhandler.getStackInSlot(slot);
	if (itemstack.isEmpty())
	{
	    return false;
	}
	else
	{
		//RecipeWrapper wrapper = new RecipeWrapper(itemhandler, 1, 1);
	    ItemStack output = new ItemStack(Items.COAL);
	    if (output.isEmpty())
	    {
		return false;
	    }
	    else
	    {
		ItemStack outputslot = itemhandler.getStackInSlot(slot + 6);
		if (outputslot.isEmpty())
		{
		    return true;
		}
		else if (!output.isItemEqual(outputslot))
		{
		    return false;
		}
		else if (outputslot.getCount() + output.getCount() <= 64 && outputslot.getCount() + output.getCount() <= outputslot.getMaxStackSize())
		{
		    return true;
		}
		else
		{
		    return outputslot.getCount() + output.getCount() <= output.getMaxStackSize();
		}

	    }
	}
    }

    public void smeltItem(int slot)
    {
	if (this.canSmelt(slot))
	{
	    ItemStack itemstack = this.itemhandler.getStackInSlot(slot);
	    ItemStack itemstack1 = new ItemStack(Items.COAL);
	    ItemStack itemstack2 = this.itemhandler.getStackInSlot(slot + 6);

	    if (itemstack2.isEmpty())
	    {
		this.itemhandler.setStackInSlot(slot + 6, itemstack1.copy());
	    }
	    else if (itemstack2.getItem() == itemstack1.getItem())
	    {
		itemstack2.grow(itemstack1.getCount());
	    }
	    itemstack.shrink(1);
	}
    }

    public int getEnergyPerTickPerItem()
    {
	return 4 + (getUpgradeTier(12, Constants.SPEEDUPGRADE_TYPE) * 4);
    }

    public int getTicksPerItem()
    {
	return 200 - (getUpgradeTier(12, Constants.SPEEDUPGRADE_TYPE) * 35);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
	for (int i = 0; i < 6; i++)
	{
	    compound.putInt("cooktime_" + i, ticksPassed[i]);
	}
	super.write(compound);
	return compound;
    }

    @Override
    public void read(CompoundNBT compound)
    {
	for (int i = 0; i < 6; i++)
	{
	    this.ticksPassed[i] = compound.getInt("cooktime_" + i);
	}
	super.read(compound);
    }

    @Override
    public String getNameString()
    {
	return "megafurnace";
    }

    @Override
    public int getField(int id)
    {
	switch (id)
	{
	case 0:
	case 1:
	    return super.getField(id);
	case 2:
	    return this.ticksPassed[0];
	case 3:
	    return this.ticksPassed[1];
	case 4:
	    return this.ticksPassed[2];
	case 5:
	    return this.ticksPassed[3];
	case 6:
	    return this.ticksPassed[4];
	case 7:
	    return this.ticksPassed[5];
	default:
	    return 0;
	}
    }

    @Override
    public void setField(int id, int value)
    {
	switch (id)
	{
	case 0:
	case 1:
	    super.setField(id, value);
	    break;
	case 2:
	    this.ticksPassed[0] = value;
	    break;
	case 3:
	    this.ticksPassed[1] = value;
	    break;
	case 4:
	    this.ticksPassed[2] = value;
	    break;
	case 5:
	    this.ticksPassed[3] = value;
	    break;
	case 6:
	    this.ticksPassed[4] = value;
	    break;
	case 7:
	    this.ticksPassed[5] = value;
	    break;
	}
    }

    @Override
    public int getAmountFields()
    {
	return 8;
    }
    
    @Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity player)
	{
		return new ContainerMegaFurnace(id, inv, this);
	}

}
