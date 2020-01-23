package net.kaneka.planttech2.dimensions.planttopia;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;

public class PlantTopiaBiomeProvider extends BiomeProvider
{
	public static final List<Biome> SPAWNS = Lists.newArrayList(Biomes.PLAINS);
	private static final Set<Biome> BIOMES = ImmutableSet.of(Biomes.PLAINS, Biomes.BIRCH_FOREST); 

	protected PlantTopiaBiomeProvider()
	{
		super(BIOMES);
	}
	
	@Override
	public List<Biome> getBiomesToSpawnIn()
	{
		return SPAWNS;
	}

	@Override
	public Biome getNoiseBiome(int x, int y, int z)
	{
		return Biomes.PLAINS;
	}
	
	

	
}
