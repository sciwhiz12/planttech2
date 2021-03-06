package net.kaneka.planttech2.container;


import net.kaneka.planttech2.tileentity.machine.baseclasses.EnergyTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BaseContainer extends Container
{
	protected final EnergyTileEntity tileentity;
	protected final IIntArray field_array;
	
	public BaseContainer(int id, ContainerType<?> type, PlayerInventory player, EnergyTileEntity tileentity, int slots)
	{
		super(type, id);
		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 9; x++)
			{
				addSlot(new Slot(player, x + y * 9 + 9, 23 + x * 18, 106 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++)
		{
			addSlot(new Slot(player, x, 23 + x * 18, 164));
		}

		this.tileentity = tileentity;
		field_array = tileentity.getIntArray();
		trackIntArray(field_array);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		/*
		for (IContainerListener listener : listeners)
		{
			for (int i = 0; i < tileentity.getAmountFields(); i++)
			{
				if (fields[i] != tileentity.getField(i))
				{
					fields[i] = tileentity.getField(i);
					listener.sendWindowProperty(this, i, fields[i]);
				}
			}
		}
		*/
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return tileentity.isUsableByPlayer(playerIn);
	}
	
	public EnergyTileEntity getTE()
	{
		return tileentity; 
	}
	
	public int getValue(int id)
	{
		return field_array.get(id); 
	}
	
	public static class SlotItemHandlerWithInfo extends SlotItemHandler
	{
		private String usage; 
		
		public SlotItemHandlerWithInfo(IItemHandler itemHandler, int index, int xPosition, int yPosition, String usage)
		{
			super(itemHandler, index, xPosition, yPosition);
			this.usage = usage; 
		}
		
		public String getUsageString()
		{
			return usage; 
		}
		
	}
}


