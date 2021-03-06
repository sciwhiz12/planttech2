package net.kaneka.planttech2.recipes;

import net.kaneka.planttech2.PlantTechMain;
import net.kaneka.planttech2.recipes.recipeclasses.ChipalyzerRecipe;
import net.kaneka.planttech2.recipes.recipeclasses.CompressorRecipe;
import net.kaneka.planttech2.recipes.recipeclasses.InfuserRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class ModRecipeSerializers
{
	public static void registerAll(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
    	event.getRegistry().register(new CompressorRecipe.Serializer().setRegistryName(new ResourceLocation(PlantTechMain.MODID, "compressing"))); 
    	event.getRegistry().register(new InfuserRecipe.Serializer().setRegistryName(new ResourceLocation(PlantTechMain.MODID, "infusing"))); 
    	event.getRegistry().register(new ChipalyzerRecipe.Serializer().setRegistryName(new ResourceLocation(PlantTechMain.MODID, "chipalyzer"))); 
    }
}
