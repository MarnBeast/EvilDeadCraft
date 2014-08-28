package net.evildead.mod;

import net.evildead.mod.items.EDItems;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;


@Mod(modid = EvilDead.modid, version = EvilDead.version)
public class EvilDead {

	public static final String modid = "The Evil Dead";
	public static final String version = "Ash v0.1";
	
	public static Item itemNecroBook;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent){
		
		itemNecroBook = new EDItems().setUnlocalizedName("Necronomicon");
		GameRegistry.registerItem(itemNecroBook, "Necronomicon");
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
		
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){
		
	}
}
