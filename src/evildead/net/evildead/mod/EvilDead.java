package net.evildead.mod;

import net.evildead.common.EDProxyCommon;
import net.evildead.mod.blocks.BloodyVine;
import net.evildead.mod.blocks.DarkAir;
import net.evildead.mod.blocks.StickyVine;
import net.evildead.mod.items.MusicDisc;
import net.evildead.mod.items.NecroBook;
import net.evildead.mod.items.RubbingPaper;
import net.evildead.mod.network.EDMessage;
import net.evildead.mod.worldgen.EvilDeadWorldGen;
import net.evildead.mod.worldgen.structure.EvilCabinFirst;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;


@Mod(modid = EvilDead.modid, version = EvilDead.version)
public class EvilDead {

	public static final String modid = "EvilDead";
	public static final String version = "Ash v0.1";
	
	
	@Instance(modid)
	public static EvilDead instance;
	
	@SidedProxy(clientSide = "net.evildead.common.EDProxyCommon", serverSide = "net.evildead.common.EDProxyCommon")
    public static EDProxyCommon proxy;
	
	public static SimpleNetworkWrapper network;
	
	EvilDeadWorldGen eventWorldGen = new EvilDeadWorldGen();
	
	public static Item itemNecroBook;
	public static Item itemRubbingPaper;
	public static Item itemKnowbyRecord;

	public static Block blockDarkAir;
	public static Block blockStickyVine;
	public static Block blockBloodyVine;
	
	public static final int guiIDNecro = 1;
	public static final int guiIDRubbing = 2;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent){
		
		// Message channel - for sending the summoner trigger to the server.
		network = NetworkRegistry.INSTANCE.newSimpleChannel("EvilDead");
		network.registerMessage(EDMessage.Handler.class, EDMessage.class, 0, Side.SERVER);
		network.registerMessage(EDMessage.Handler.class, EDMessage.class, 1, Side.CLIENT);
		
		// GUI - for my custom Necronomicon GUI
		NetworkRegistry.INSTANCE.registerGuiHandler(EvilDead.instance, proxy);
		
		// Items
		itemKnowbyRecord = new MusicDisc("Knowby").setUnlocalizedName("record_Knowby");
		GameRegistry.registerItem(itemKnowbyRecord, "record_Knowby");
		
		// Blocks
		blockDarkAir = new DarkAir().setBlockName("DarkAir");
		GameRegistry.registerBlock(blockDarkAir, "DarkAir");
		blockStickyVine = new StickyVine().setBlockName("StickyVine");
		GameRegistry.registerBlock(blockStickyVine, "StickyVine");
		blockBloodyVine = new BloodyVine().setBlockName("BloodyVine");
		GameRegistry.registerBlock(blockBloodyVine, "BloodyVine");
		
		// Spawn
		GameRegistry.registerWorldGenerator(eventWorldGen, 0);

		MapGenStructureIO.registerStructure(EvilCabinFirst.class, "EvilCabin");
		
		// Books
		itemNecroBook = new NecroBook().setUnlocalizedName("Necronomicon");
		GameRegistry.registerItem(itemNecroBook, "Necronomicon");
		itemRubbingPaper = new RubbingPaper().setUnlocalizedName("RubbingPaper");
		GameRegistry.registerItem(itemRubbingPaper, "RubbingPaper");
		
		
		
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
		
		// Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(itemRubbingPaper), 
				new Object[]{itemNecroBook, Items.paper, new ItemStack(Items.coal,1,1)});	// charcoal
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){
		
	}
	
}
