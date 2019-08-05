package net.kaneka.planttech2.registries;

import net.kaneka.planttech2.world.structure.tech.TechVillagePieces;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class ModStructurePieceTypes
{
	public static IStructurePieceType TECHVILLAGE = IStructurePieceType.register(TechVillagePieces.TechVillage::new, "techvillage");
}