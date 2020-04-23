package net.kaneka.planttech2;

import net.kaneka.planttech2.configuration.PlantTech2Configuration;
import net.kaneka.planttech2.datapack.reloadlistener.ReloadListenerCropListEntryConfiguration;
import net.kaneka.planttech2.events.ClientEvents;
import net.kaneka.planttech2.events.PlayerEvents;
import net.kaneka.planttech2.handlers.CapabilityHandler;
import net.kaneka.planttech2.handlers.LootTableHandler;
import net.kaneka.planttech2.librarys.CropList;
import net.kaneka.planttech2.packets.PlantTech2PacketHandler;
import net.kaneka.planttech2.proxy.ClientProxy;
import net.kaneka.planttech2.proxy.IProxy;
import net.kaneka.planttech2.proxy.ServerProxy;
import net.kaneka.planttech2.recipes.ModRecipeTypes;
import net.kaneka.planttech2.registries.ModBiomes;
import net.kaneka.planttech2.registries.ModBlocks;
import net.kaneka.planttech2.registries.ModCommands;
import net.kaneka.planttech2.registries.ModRenderer;
import net.kaneka.planttech2.registries.ModScreens;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("planttech2")
public class PlantTechMain
{
	public static final String MODID = "planttech2";

	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static PlantTechMain instance;
	
	public static CropList croplist = new CropList();

	public PlantTechMain()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PlantTech2Configuration.SERVER, "planttech2-server.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PlantTech2Configuration.CLIENT, "planttech2-client.toml");
		
		PlantTech2Configuration.loadConfig(PlantTech2Configuration.CLIENT, FMLPaths.CONFIGDIR.get().resolve("planttech2-client.toml").toString());
		PlantTech2Configuration.loadConfig(PlantTech2Configuration.SERVER, FMLPaths.CONFIGDIR.get().resolve("planttech2-server.toml").toString());

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEvents::registerColorBlock);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEvents::registerColorItem);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEvents::onWorldStart);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEvents::onFogRenderDensity);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEvents::onFogRenderColour);
			//FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEvents::textureStitchEvent);
		});

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		MinecraftForge.EVENT_BUS.addListener(this::onServerAboutToStarting);
		MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
		MinecraftForge.EVENT_BUS.addListener(PlayerEvents::playerConnect);
		MinecraftForge.EVENT_BUS.addListener(PlayerEvents::attachCapability);
		MinecraftForge.EVENT_BUS.addListener(PlayerEvents::playerTicking);
		MinecraftForge.EVENT_BUS.addListener(PlayerEvents::onPlayerClone);
		MinecraftForge.EVENT_BUS.addListener(PlayerEvents::onPlayerChangedDimension);
		MinecraftForge.EVENT_BUS.addListener(PlayerEvents::onPlayerRespawn);
	}

	private void onServerAboutToStarting(FMLServerAboutToStartEvent event)
	{
		event.getServer().getResourceManager().addReloadListener(new ReloadListenerCropListEntryConfiguration());
	}
	
	private void onServerStarting(final FMLServerStartingEvent event)
	{
		ModCommands.registerAll(event.getCommandDispatcher(), event.getServer().isDedicatedServer());
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		new ModRecipeTypes();  
		//new ModStructurePieceTypes(); 
		CapabilityHandler.registerAll(); 
		PlantTech2PacketHandler.register();
		PlantTechMain.croplist.configuratePlanttechEntries();
		LootTableHandler.register();
		
	}

	private void doClientStuff(final FMLClientSetupEvent event)
	{
		ModRenderer.registerEntityRenderer();
		ModScreens.registerGUI();
		RenderType rendertype = RenderType.getCutoutMipped();
		for(Block block: ModBlocks.CROPS.values())
		{
			RenderTypeLookup.setRenderLayer(block, rendertype);
		}
		RenderTypeLookup.setRenderLayer(ModBlocks.CROPBARS, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DARK_CRYSTAL_GLASSPANE_END, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DARK_CRYSTAL_GLASSPANE_CROSS, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DARK_CRYSTAL_GLASSPANE_MIDDLE, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DARK_CRYSTAL_DOOR, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.WHITE_CRYSTAL_GLASSPANE_END, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.WHITE_CRYSTAL_GLASSPANE_CROSS, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.WHITE_CRYSTAL_GLASSPANE_MIDDLE, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.WHITE_CRYSTAL_DOOR, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.CARVER, rendertype);
		
		
		RenderTypeLookup.setRenderLayer(ModBlocks.MACHINESHELL_IRON_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.MACHINESHELL_PLANTIUM_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.MACHINESHELL_IRON, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.MACHINESHELL_PLANTIUM, rendertype);
		
		RenderTypeLookup.setRenderLayer(ModBlocks.MACHINEBULBREPROCESSOR_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.SEEDSQUEEZER_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.CHIPALYZER_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.COMPRESSOR_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DNA_CLEANER_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DNA_COMBINER_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DNA_EXTRACTOR_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DNA_REMOVER_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.IDENTIFIER_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.INFUSER_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.MEGAFURNACE_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.PLANTFARM_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.SEEDCONSTRUCTOR_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.SOLARGENERATOR_GROWING, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.ENERGY_SUPPLIER_GROWING, rendertype);

		RenderTypeLookup.setRenderLayer(ModBlocks.MACHINEBULBREPROCESSOR, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.SEEDSQUEEZER, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.CHIPALYZER, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.COMPRESSOR, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DNA_CLEANER, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DNA_COMBINER, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DNA_EXTRACTOR, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.DNA_REMOVER, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.IDENTIFIER, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.INFUSER, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.MEGAFURNACE, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.INFUSER, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.PLANTFARM, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.SEEDCONSTRUCTOR, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.SOLARGENERATOR, rendertype);
		RenderTypeLookup.setRenderLayer(ModBlocks.ENERGY_SUPPLIER, rendertype);

	}
}
