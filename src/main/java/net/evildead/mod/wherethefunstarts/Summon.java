package net.evildead.mod.wherethefunstarts;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.evildead.mod.blocks.DarkAir;
import net.evildead.mod.blocks.StickyVine;
import net.evildead.mod.network.EDMessage;
import net.evildead.mod.util.EDReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class Summon {
	
	private EntityPlayer summoner;
	private SummonerEventHandler sEvent = new SummonerEventHandler();
	private ChunkCoordinates origin;
	
	private static final int RADIUS = 100;
	private static final int DEEPENORIGIN = 10;

	private VineGrower vines;
	private HouseBleeder bleeder;
	
	public Summon(EntityPlayer summoner){
		this.summoner = summoner;
	}

	
	public void begin() {

		// This class does setblock stuff and is designed to be operated by the server;
		if(!summoner.worldObj.isRemote)
		{
			/* Initialize */
	        origin = summoner.getPlayerCoordinates();
	        origin.posY = (origin.posY < RADIUS / 5 && origin.posY > (-RADIUS) / 5) ? 		// grow out of the ground.
	        		-DEEPENORIGIN : origin.posY - DEEPENORIGIN;
	        
			/* Say Incantation */
			String incantation = "Kanda� Es-trada� Montos� Kanda�";
	
			IChatComponent chat = new ChatComponentText("<" + summoner.getDisplayName() + "> ");
			
			IChatComponent kandaChat = new ChatComponentText(incantation);
			kandaChat.setChatStyle(new ChatStyle().setItalic(true).setColor(EnumChatFormatting.GRAY));
			chat.appendSibling(kandaChat);
			
			summoner.addChatMessage(chat);
			
	        
	        /* Start Playing Invocation Music */
	        SendSoundToAllAround(EDReference.Sounds.INVOCATION1, 
	        		origin.posX, origin.posY, origin.posZ,
	        		10f, 1f);
	        
	        vines = new VineGrower(summoner, origin, RADIUS);
	        bleeder = new HouseBleeder(summoner, origin, RADIUS);
	        
	        FMLCommonHandler.instance().bus().register(sEvent);
		}
	}
	
	private void SendSoundToAllAround(String soundName, int x, int y, int z, float volume, float pitch) {
		String DEL = EDReference.MessageFlags.SOUND_DELIMITER;
		EvilDead.network.sendToAllAround(
				new EDMessage(
						EDReference.MessageFlags.SOUND_MESSAGE + DEL +
						soundName + DEL +
						volume + DEL +
						pitch + DEL +
						x + DEL +
						y + DEL +
						z + DEL), 
				new TargetPoint(
						summoner.dimension, 
						origin.posX, 
						origin.posY,
						origin.posZ, 
						RADIUS));
	}
	
	private void SendSoundToAllAround(String soundName, float volume, float pitch) {
		String DEL = EDReference.MessageFlags.SOUND_DELIMITER;
		EvilDead.network.sendToAllAround(
				new EDMessage(
						EDReference.MessageFlags.SOUND_MESSAGE + DEL +
						soundName + DEL +
						volume + DEL +
						pitch + DEL), 
				new TargetPoint(
						summoner.dimension, 
						origin.posX, 
						origin.posY,
						origin.posZ, 
						RADIUS));
	}
	
	

	public class SummonerEventHandler{
		int count;
		
		@SubscribeEvent
		public void onTick(TickEvent.ServerTickEvent event){
			//printTick(count);
			if(count == 0) bleeder.begin();//findHouses();//traverserTest();//.floodHouses();
			//if(count == 200) bleeder.findHouses();//traverserTest();//.floodHouses();
			//if(count == 400) bleeder.dripBlood();	// 10 seconds later, drip blood.
			//if(count == 0) vines.resetVines();
			//if(count > 600 && count < 1200) vines.simpleGrow(count);  		// 30 seconds
			//if(count > 1200 && count < 1800) vines.simpleShrink(count);    // 60 seconds
			//if(count < vines.getTotalGrowTicks()) vines.grow(count); //growVines(count);
			//if(count == 1950) throwDoors();
			//if(count > vines.getTotalGrowTicks()+400 && count < 2*vines.getTotalGrowTicks() + 400) vines.shrink(count);
			
			count++;
		}
	}
	
	/*
	 * EVENT TRIGGERED METHODS
	 */
	
	private void printTick(int count) {
			int mod = count % 10;
			if(mod == 0){
				summoner.addChatMessage(new ChatComponentText("Tick: " + count));
			}
			count++;
	}
	

	
	private void throwDoors() {
		
		/* Start Thunderstorm */
		summoner.worldObj.getWorldInfo().setRaining(true);
		summoner.worldObj.setRainStrength(1.0f);
		summoner.worldObj.getWorldInfo().setThundering(true);
		summoner.worldObj.setThunderStrength(1.0f);

        /* Play Invocation2 */
		SendSoundToAllAround(EDReference.Sounds.INVOCATION2, 
        		origin.posX, origin.posY, origin.posZ,
        		10f, 1f);
        
		/* Throw Doors */
        for(int x = origin.posX - RADIUS; x < origin.posX + RADIUS; x++) {
        	for(int y = origin.posY - RADIUS; y < origin.posY + RADIUS; y++) {
        		for(int z = origin.posZ - RADIUS; z < origin.posZ + RADIUS; z++) {
		        	
        			Block block = summoner.worldObj.getBlock(x, y, z);
        			if(block instanceof BlockDoor)
        			{
        				BlockDoor door = (BlockDoor) block;

        				// only change the bottom door: 
        				// http://hydra-media.cursecdn.com/minecraft.gamepedia.com/f/fd/12w06a-doors-idx.png
//        				if(summoner.worldObj.getBlock(x, y+1, z) instanceof BlockDoor){	
//        					int direction = summoner.worldObj.getBlockMetadata(x, y, z);
//        					if(direction == 3) direction = 7;
//        					else if(direction == 0) direction = 4;
//        					else if(direction == 1) direction = 5;
//        					else if(direction == 2) direction = 6;
//        					summoner.worldObj.setBlockMetadataWithNotify(x, y, z, direction, 3);
//        					
//        				}
        				
        				door.onBlockActivated(summoner.worldObj, x, y, z, summoner, 0, x, y, z);
        			}
		        }
	        }
        }
	}
	
	
	private void spawnEntities(int count) {
		
	}
	
	

}
