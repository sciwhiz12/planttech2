package net.kaneka.planttech2.gui;

import net.kaneka.planttech2.PlantTechMain;
import net.kaneka.planttech2.container.DNARemoverContainer;
import net.kaneka.planttech2.tileentity.machine.DNARemoverTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DNARemoverScreen extends BaseContainerScreen<DNARemoverContainer>
{ 
	private static final ResourceLocation TEXTURES = new ResourceLocation(PlantTechMain.MODID + ":textures/gui/container/dna_remover.png");
	private static final ResourceLocation TEXTURES_COLORBLIND = new ResourceLocation(PlantTechMain.MODID + ":textures/gui/container_colorblind/dna_remover.png");
	
	
	public DNARemoverScreen(DNARemoverContainer container, PlayerInventory player, ITextComponent name)
    {
    	super(container, player, name);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int l = this.getCookProgressScaled(32);
		blit(this.guiLeft + 62, this.guiTop + 50, 0, 202, l, 12);
		
		int k = this.getEnergyStoredScaled(55);
		blit(this.guiLeft + 149, this.guiTop + 28 + (55 - k), 208, 55 - k, 16, 0 + k);
	}
	
	private int getCookProgressScaled(int pixels)
	{
		int i = container.getValue(2);
		return i != 0 ? i * pixels / ((DNARemoverTileEntity) this.te).ticksPerItem() : 0;
	}
	
	@Override
	protected ResourceLocation getBackgroundTexture()
	{
		return TEXTURES;
	}


	@Override
	protected ResourceLocation getBackgroundTextureColorblind()
	{
		return TEXTURES_COLORBLIND;
	}
	
	@Override
	protected String getGuideEntryString()
	{
		return "dna_remover";
	}
}
