package net.evildead.mod;

import net.evildead.mod.blocks.BloodyVine;
import net.evildead.mod.blocks.StickyVine;
import net.evildead.mod.items.KnowbyRecord;
import net.evildead.mod.items.NecroBook;
import net.evildead.mod.worldgen.EvilDeadWorldGen;
import net.evildead.mod.worldgen.structure.EvilCabinFirst;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;


@Mod(modid = EvilDead.modid, version = EvilDead.version)
public class EvilDead {

	public static final String modid = "EvilDead";
	public static final String version = "Ash v0.1";
	
	EvilDeadWorldGen eventWorldGen = new EvilDeadWorldGen();
	
	public static Item itemNecroBook;
	public static Item itemKnowbyRecord;
	
	public static Block blockStickyVine;
	public static Block blockBloodyVine;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent){
		
		// Items
		itemNecroBook = new NecroBook().setUnlocalizedName("Necronomicon");
		GameRegistry.registerItem(itemNecroBook, "Necronomicon");
		itemKnowbyRecord = new KnowbyRecord("KnowbyRecord").setUnlocalizedName("KnowbyRecord");
		GameRegistry.registerItem(itemKnowbyRecord, "KnowbyRecord");
		
		// Blocks
		blockStickyVine = new StickyVine(Material.vine).setBlockName("StickyVine");
		GameRegistry.registerBlock(blockStickyVine, "StickyVine");
		blockBloodyVine = new BloodyVine(Material.vine).setBlockName("BloodyVine");
		GameRegistry.registerBlock(blockBloodyVine, "BloodyVine");
		
		// Spawn
		GameRegistry.registerWorldGenerator(eventWorldGen, 0);

		MapGenStructureIO.registerStructure(EvilCabinFirst.class, "EvilCabin");
		
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
		
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){
		
	}
}
