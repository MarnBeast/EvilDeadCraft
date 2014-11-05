package net.evildead.mod.worldgen;

import java.util.Random;

import net.evildead.mod.worldgen.structure.EvilCabin;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class EvilDeadWorldGen implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

		switch(world.provider.dimensionId){
		// Surface
		case 0:
			generateSurface(world, random, chunkX*16, chunkZ*16);
			
		// Nether
		case -1:
			generateNether(world, random, chunkX*16, chunkZ*16);
			
		// End
		case 1:
			generateEnd(world, random, chunkX*16, chunkZ*16);
		
		}
	}

	private void generateSurface(World world, Random random, int x, int z) {
		// this.addOreSpawn(EvilDead.oreWhatever, world, random, x=blockXPos, z=blockZPos, maxX, maxZ, maxVeinSize, chanceToSpawn, minY, maxY);
		
		int Xcoord = x + random.nextInt(16);
		int Ycoord = 10 + random.nextInt(128);
		int Zcoord = z + random.nextInt(16);
		BiomeGenBase biomegenbase = world.getWorldChunkManager().getBiomeGenAt(Xcoord + 16, Zcoord + 16);
	
		if(
				biomegenbase == BiomeGenBase.forest ||
				biomegenbase == BiomeGenBase.taiga ||
				biomegenbase == BiomeGenBase.swampland ||
				biomegenbase == BiomeGenBase.taiga ||
				biomegenbase == BiomeGenBase.forestHills ||
				biomegenbase == BiomeGenBase.taigaHills ||
				biomegenbase == BiomeGenBase.jungle ||
				biomegenbase == BiomeGenBase.jungleHills ||
				biomegenbase == BiomeGenBase.roofedForest ||
				biomegenbase == BiomeGenBase.coldTaiga ||
				biomegenbase == BiomeGenBase.coldTaigaHills ||
				biomegenbase == BiomeGenBase.megaTaiga ||
				biomegenbase == BiomeGenBase.megaTaigaHills
			)
		{
			(new EvilCabin()).generate(world,random,Xcoord,Ycoord,Zcoord);
		}
	}

	private void generateNether(World world, Random random, int x, int z) {

	}

	private void generateEnd(World world, Random random, int x, int z) {

	}
	
	
//	private void addOreSpawn(Block block, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize, int chanceToSpawn, int minY, int maxY){
//		for(int i = 0; i < chanceToSpawn; i++){
//			int posX = blockXPos + random.nextInt(maxX);
//			int posY = minY + random.nextInt(maxY - minY);
//			int posZ = blockZPos + random.nextInt(maxZ);
//			
//			(new WorldGenMinable(block, maxVeinSize)).generate(world, random, posX, posY, posZ);
//			
//		}
//	}

}
