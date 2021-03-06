package net.kaneka.planttech2.gui;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.kaneka.planttech2.PlantTechMain;
import net.kaneka.planttech2.container.ItemUpgradeableContainer;
import net.kaneka.planttech2.items.upgradeable.BaseUpgradeableItem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemUpgradeableScreen extends ContainerScreen<ItemUpgradeableContainer>
{
	protected static final Map<Integer, ResourceLocation> BACKGROUND = new HashMap<Integer, ResourceLocation>()	{
		private static final long serialVersionUID = 1L; {
			put(10, new ResourceLocation(PlantTechMain.MODID + ":textures/gui/container/itemupgradeable_10.png"));
	    }
	};
	protected final PlayerInventory player;
    protected ItemStack stack; 
    protected int invsize; 
    protected IEnergyStorage energystorage; 

    public ItemUpgradeableScreen(ItemUpgradeableContainer container, PlayerInventory inv, ITextComponent name)
    {
    	super(container, inv, name); 
    	this.player = inv; 
    	stack = container.getStack(); 
    	this.invsize = BaseUpgradeableItem.getInventorySize(stack); 
		energystorage = BaseUpgradeableItem.getEnergyCap(stack); 
    }
	
	@Override
	public void init()
    {
        super.init();
        this.xSize = 205; 
        this.ySize = 202;     
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }
	
	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
	{
			this.renderBackground(mStack);
			super.render(mStack, mouseX, mouseY, partialTicks);
			this.drawTooltips(mStack, mouseX, mouseY);
	        this.func_230459_a_(mStack, mouseX, mouseY);
	}

	protected void drawTooltips(MatrixStack mStack, int mouseX, int mouseY)
	{
		if(energystorage != null)
			drawTooltip(mStack, energystorage.getEnergyStored() + "/" + energystorage.getMaxEnergyStored(), mouseX, mouseY, 162, 28, 16, 74);
	}
	
	public void drawTooltip(MatrixStack mStack, String lines, int mouseX, int mouseY, int posX, int posY, int width, int height)
	{
		posX += this.guiLeft;
		posY += this.guiTop;
        if (mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height)
			renderComponentHoverEffect(mStack, null, mouseX, mouseY);
//            renderComponentHoverEffect(mStack, new StringTextComponent(lines), mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack mStack, float p_230450_2_, int p_230450_3_, int p_230450_4_)
	{
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		minecraft.getTextureManager().bindTexture(BACKGROUND.get(invsize));
		blit(mStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		int k = this.getEnergyStoredScaled(55);
		blit(mStack, this.guiLeft + 149, this.guiTop + 28 + (55 - k), 208, 55 - k, 16, 0 + k);
	}

	protected int getEnergyStoredScaled(int pixels)
	{
		
		if(energystorage != null)
		{
			int i = energystorage.getEnergyStored();
			int j = energystorage.getMaxEnergyStored();
			return i != 0 && j != 0 ? i * pixels / j : 0; 
		}
		return 0; 
	}

	@Override
	protected boolean itemStackMoved(int keyCode, int scanCode)
	{
		return false;
	}
}
