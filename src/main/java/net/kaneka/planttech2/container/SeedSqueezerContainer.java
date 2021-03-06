package net.kaneka.planttech2.container;

import net.kaneka.planttech2.container.BaseContainer.SlotItemHandlerWithInfo;
import net.kaneka.planttech2.items.CropSeedItem;
import net.kaneka.planttech2.registries.ModContainers;
import net.kaneka.planttech2.tileentity.machine.SeedSqueezerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SeedSqueezerContainer extends BaseContainer
{
	public SeedSqueezerContainer(int id, PlayerInventory inv)
	{
		this(id, inv, new SeedSqueezerTileEntity()); 
	}
	
	public SeedSqueezerContainer(int id, PlayerInventory player, SeedSqueezerTileEntity tileentity) 
	{
		super(id, ModContainers.SEEDQUEEZER, player, tileentity, 16);
		IItemHandler handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);
		
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 3; x++)
			{
				this.addSlot(new SlotItemHandlerWithInfo(handler, x + y * 3, 59 + x * 18, 28 + y * 18, "slot.seedsqueezer.input"));
			}
		}
		
		this.addSlot(new NoAccessSlot(handler, 9, 122, 46, "slot.seedsqueezer.squeeze"));
		this.addSlot(new SlotItemHandlerWithInfo(handler, 10, 97, 85, "slot.util.speedupgrade"));
		this.addSlot(new SlotItemHandlerWithInfo(handler, tileentity.getFluidInSlot(), 23, 38, "slot.util.fluidin"));
		this.addSlot(new SlotItemHandlerWithInfo(handler, tileentity.getFluidOutSlot(), 23, 57, "slot.util.fluidout"));
		this.addSlot(new SlotItemHandlerWithInfo(handler, tileentity.getEnergyInSlot(), 167, 38, "slot.util.energyin"));
		this.addSlot(new SlotItemHandlerWithInfo(handler, tileentity.getEnergyOutSlot(), 167, 57, "slot.util.energyout"));
		this.addSlot(new SlotItemHandlerWithInfo(handler, tileentity.getKnowledgeChipSlot(), 12, 9, "slot.util.knowledgechip"));
		
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) 
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
				if(!this.mergeItemStack(stack1, 36, 45, false) && stack1.getItem() instanceof CropSeedItem) 
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

class NoAccessSlot extends SlotItemHandlerWithInfo
{

	public NoAccessSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, String usage)
	{
		super(itemHandler, index, xPosition, yPosition, usage);
	}
	
	@Override
	public boolean canTakeStack(PlayerEntity playerIn)
	{
		return false; 
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
	    return false; 
	}
	
}
