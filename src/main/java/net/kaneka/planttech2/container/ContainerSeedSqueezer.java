package net.kaneka.planttech2.container;

import net.kaneka.planttech2.PlantTechMain;
import net.kaneka.planttech2.items.ItemCropSeed;
import net.kaneka.planttech2.tileentity.machine.TileEntitySeedSqueezer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSeedSqueezer extends ContainerBase
{
	
	public ContainerSeedSqueezer(InventoryPlayer player, TileEntitySeedSqueezer tileentity) 
	{
		super(player, tileentity, 11);
		IItemHandler handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);
		
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 3; x++)
			{
				this.addSlot(new SlotItemHandler(handler, x + y * 3, 24 + x * 18, 29 + y * 18));
			}
		}
		
		this.addSlot(new NoAccessSlot(handler, 9, 113, 47));
		this.addSlot(new SlotItemHandler(handler, 10, 88, 65));
		this.addSlot(new SlotItemHandler(handler, 11, 138, 47));
		
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) 
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot)this.inventorySlots.get(index);
		if(slot != null && slot.getHasStack()) 
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if(index > 37)
			{
				if(!this.mergeItemStack(stack1, 0, 35, true)) return ItemStack.EMPTY;
				slot.onSlotChange(stack1, stack);
			}
			else if(index < 36)
			{
				if(!this.mergeItemStack(stack1, 36, 45, false) && stack1.getItem() instanceof ItemCropSeed) 
				{
					return ItemStack.EMPTY;
				}
				else if(index >= 0 && index < 27)
				{
					if(!this.mergeItemStack(stack1, 27, 35, false)) return ItemStack.EMPTY;
				}
				else if(index >= 27 && index < 36 && !this.mergeItemStack(stack1, 0, 26, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(!this.mergeItemStack(stack1, 0, 35, false)) 
			{
				return ItemStack.EMPTY;
			}
			
			
			if(stack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();

			}
			if(stack1.getCount() == stack.getCount()) return ItemStack.EMPTY;
			slot.onTake(playerIn, stack1);
		}
		return stack;
	}
}

class NoAccessSlot extends SlotItemHandler
{

	public NoAccessSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
	{
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return false; 
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
	    return false; 
	}
	
}
