package net.kaneka.planttech2.gui;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.kaneka.planttech2.container.TeleporterContainer;
import net.kaneka.planttech2.gui.buttons.CustomButton;
import net.kaneka.planttech2.items.TeleporterItem;
import net.kaneka.planttech2.items.upgradeable.BaseUpgradeableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.energy.IEnergyStorage;

public class TeleporterScreen extends ContainerScreen<TeleporterContainer>
{
	protected static final ResourceLocation BACKGROUND = new ResourceLocation("planttech2:textures/gui/teleporter.png");
	protected final PlayerInventory player;
	protected int guiLeft;
	protected int guiTop;
	protected ItemStack stack = ItemStack.EMPTY; 
    protected int invsize; 
    protected IEnergyStorage energystorage; 

	
	public TeleporterScreen(TeleporterContainer container, PlayerInventory player, ITextComponent name)
    {
    	super(container, player, name);
    	this.player = player; 
    	stack = container.getStack(); 
		energystorage = BaseUpgradeableItem.getEnergyCap(stack); 
    }

	@Override
	public void init()
	{
		super.init();
		this.xSize = 441; 
        this.ySize = 197; 
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		if (Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof TeleporterItem)
		{
			stack = Minecraft.getInstance().player.getHeldItemMainhand();
		}
		else
		{
			this.onClose();
		}
		
		int buttonwidth = 300; 
		
		addButton(new CustomButton(1, this.guiLeft + 28, this.guiTop + 10 + 0 * 22, buttonwidth, 20, "TEST", (button) -> 
		{
			TeleporterScreen.this.buttonClicked(0);
		})); 
		addButton(new CustomButton(2, this.guiLeft + 28, this.guiTop + 10 + 1 * 22, buttonwidth, 20, "TEST", (button) -> 
		{
			TeleporterScreen.this.buttonClicked(1);
		})); 
		addButton(new CustomButton(3, this.guiLeft + 28, this.guiTop + 10 + 2 * 22, buttonwidth, 20, "TEST", (button) -> 
		{
			TeleporterScreen.this.buttonClicked(2);
		})); 
		addButton(new CustomButton(4, this.guiLeft + 28, this.guiTop + 10 + 3 * 22, buttonwidth, 20, "TEST", (button) -> 
		{
			TeleporterScreen.this.buttonClicked(3);
		})); 
		addButton(new CustomButton(5, this.guiLeft + 28, this.guiTop + 10 + 4 * 22, buttonwidth, 20, "TEST", (button) -> 
		{
			TeleporterScreen.this.buttonClicked(4);
		})); 
		addButton(new CustomButton(6, this.guiLeft + 28, this.guiTop + 10 + 5 * 22, buttonwidth, 20, "TEST", (button) -> 
		{
			TeleporterScreen.this.buttonClicked(5);
		})); 
		addButton(new CustomButton(7, this.guiLeft + 28, this.guiTop + 10 + 6 * 22, buttonwidth, 20, "TEST", (button) -> 
		{
			TeleporterScreen.this.buttonClicked(6);
		})); 
		addButton(new CustomButton(8, this.guiLeft + 28, this.guiTop + 10 + 7 * 22, buttonwidth, 20, "TEST", (button) -> 
		{
			TeleporterScreen.this.buttonClicked(7);
		})); 
		
	}

	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(mStack);
//		drawGuiContainerBackgroundLayer(mStack, partialTicks, mouseX, mouseY);
		this.drawButtons(mStack, mouseX, mouseY, partialTicks);
		this.drawTooltips(mStack, mouseX, mouseY);
        this.func_230459_a_(mStack, mouseX, mouseY);
	}
	
	private void buttonClicked(int id)
	{
		
	}
	
	
	
	private void drawButtons(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
	{
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		for (int i = 0; i < this.buttons.size(); ++i)
		{
			this.buttons.get(i).render(mStack, mouseX, mouseY, partialTicks);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		minecraft.getTextureManager().bindTexture(BACKGROUND);
		blit(mStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize, 512, 512);

		int k = this.getEnergyStoredScaled(157);
		blit(mStack, this.guiLeft + 396, this.guiTop + 23 + (157 - k), 441, 157 - k, 16, k, 512, 512);
	}
	
	protected void drawTooltips(MatrixStack mStack, int mouseX, int mouseY)
	{
		if(energystorage != null)
		{
			drawTooltip(mStack, energystorage.getEnergyStored() + "/" + energystorage.getMaxEnergyStored(), mouseX, mouseY, 162, 28, 16, 74);
		}
	}
	
	public void drawTooltip(MatrixStack mStack, String lines, int mouseX, int mouseY, int posX, int posY, int width, int height)
	{
		posX += this.guiLeft;
		posY += this.guiTop; 
        if (mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height)
			renderComponentHoverEffect(mStack, null, mouseX, mouseY);
//            renderComponentHoverEffect(mStack, new StringTextComponent(lines), mouseX, mouseY);
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

	

	

	
}
