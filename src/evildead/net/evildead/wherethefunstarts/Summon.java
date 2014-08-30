package net.evildead.wherethefunstarts;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.evildead.network.EDMessage;
import net.evildead.util.EDReference;
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
	
	private static final int RADIUS = 25;	// radius of summon event

	
	public Summon(EntityPlayer summoner){
		this.summoner = summoner;
	}

	
	public void begin() {

		// This class does set block stuff and is designed to be operated by the server;
		if(!summoner.worldObj.isRemote)
		{
			/* Initialize */
	        origin = summoner.getPlayerCoordinates();
	        
	        
			/* Say Incantation */
			String incantation = "Kanda… Es-trada… Montos… Kanda…";
	
			IChatComponent chat = new ChatComponentText("<" + summoner.getDisplayName() + "> ");
			
			IChatComponent kandaChat = new ChatComponentText(incantation);
			kandaChat.setChatStyle(new ChatStyle().setItalic(true).setColor(EnumChatFormatting.GRAY));
			chat.appendSibling(kandaChat);
			
			summoner.addChatMessage(chat);
			
	        
	        /* Start Playing Invocation Music */
	        SendSoundToAllAround(EDReference.Sounds.INVOCATION1);
	        
	        
	        FMLCommonHandler.instance().bus().register(sEvent);
		}
	}
	
	private void SendSoundToAllAround(String soundName) {
		EvilDead.network.sendToAllAround(
				new EDMessage(
						EDReference.MessageFlags.SOUND_MESSAGE + 
						soundName), 
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
		public void printTick(TickEvent.ServerTickEvent event) {
			//if(count % 10 == 0)
				summoner.addChatMessage(new ChatComponentText("Tick: " + count++));
		}
		
		@SubscribeEvent
		public void throwDoors(TickEvent.ServerTickEvent event) {
			if(count == 1950){
				
				/* Start Thunderstorm */
				summoner.worldObj.getWorldInfo().setRaining(true);
				summoner.worldObj.setRainStrength(1.0f);
				summoner.worldObj.getWorldInfo().setThundering(true);
				summoner.worldObj.setThunderStrength(1.0f);

		        /* Play Invocation2 */
				SendSoundToAllAround(EDReference.Sounds.INVOCATION2);
		        
		        for(int x = origin.posX - RADIUS; x < origin.posX + RADIUS; x++) {
		        	for(int y = origin.posY - RADIUS; y < origin.posY + RADIUS; y++) {
		        		for(int z = origin.posZ - RADIUS; z < origin.posZ + RADIUS; z++) {
				        	
		        			Block block = summoner.worldObj.getBlock(x, y, z);
		        			if(block instanceof BlockDoor)
		        			{
		        				BlockDoor door = (BlockDoor) block;

		        				// only change the bottom door: 
		        				// http://hydra-media.cursecdn.com/minecraft.gamepedia.com/f/fd/12w06a-doors-idx.png
		        				if(summoner.worldObj.getBlock(x, y+1, z) instanceof BlockDoor){	
		        					int direction = summoner.worldObj.getBlockMetadata(x, y, z);
		        					if(direction == 3) direction = 7;
		        					else if(direction == 0) direction = 4;
		        					else if(direction == 1) direction = 5;
		        					else if(direction == 2) direction = 6;
		        					summoner.worldObj.setBlockMetadataWithNotify(x, y, z, direction, 3);
		        					
		        				}
		        			}
				        }
			        }
		        }
		        
			}
		}
	}

}
