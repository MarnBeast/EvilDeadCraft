package net.evildead.wherethefunstarts;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;
import net.evildead.mod.EvilDead;
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
import net.minecraftforge.common.MinecraftForge;

public class Summoner {
	
	private Minecraft mc;
	private SummonerEventHandler sEvent = new SummonerEventHandler();
	private ChunkCoordinates origin;
	
	private static final int RADIUS = 25;	// radius of summon event

	public Summoner(Minecraft mc){
		this.mc = mc;
	}

	public void begin() {
		
		/* Say Incantation */
		String incantation = "Kanda… Es-trada… Montos… Kanda…";

		IChatComponent chat = new ChatComponentText("<" + mc.thePlayer.getDisplayName() + "> ");
		
		IChatComponent kandaChat = new ChatComponentText(incantation);
		kandaChat.setChatStyle(new ChatStyle().setItalic(true).setColor(EnumChatFormatting.GRAY));
		
		chat.appendSibling(kandaChat);

		this.mc.thePlayer.addChatMessage(chat);
        this.mc.displayGuiScreen((GuiScreen)null);
        
        /* Start Playing Invocation Music */
        origin = mc.thePlayer.getPlayerCoordinates();
        mc.thePlayer.playSound(EvilDead.modid + ":invocation1", 3.0f, 1.0f);
        
        //mc.theWorld.setRainStrength(0.2f);
        
        
        
		//mc.theWorld.playAuxSFXAtEntity((EntityPlayer)null, 1005, pos.posX, pos.posY, pos.posZ, Item.getIdFromItem(EvilDead.itemKnowbyRecord));
        FMLCommonHandler.instance().bus().register(sEvent);
        //MinecraftForge.EVENT_BUS.register(sEvent);
        
	}
	
	public class SummonerEventHandler{
		int count;
		
		@SubscribeEvent
		public void checkUpdate(TickEvent.ServerTickEvent event) {
			mc.thePlayer.addChatMessage(new ChatComponentText("Tick: " + count++));
			
			
			
		}
		
		@SubscribeEvent
		public void throwDoors(TickEvent.ServerTickEvent event) {
			if(count == 1950){
				
				/* Start Thunderstorm */
				mc.theWorld.setRainStrength(1.0f);
				mc.theWorld.setThunderStrength(1.0f);

		        /* Play Invocation2 */
		        mc.thePlayer.playSound(EvilDead.modid + ":invocation2", 3.0f, 1.0f);
		        
		        for(int x = origin.posX - RADIUS; x < origin.posX + RADIUS; x++) {
		        	for(int y = origin.posY - RADIUS; y < origin.posY + RADIUS; y++) {
		        		for(int z = origin.posZ - RADIUS; z < origin.posZ + RADIUS; z++) {
				        	
		        			Block block = mc.theWorld.getBlock(x, y, z);
		        			if(block instanceof BlockDoor)
		        			{
		        				BlockDoor door = (BlockDoor) block;

		        				// only change the bottom door: 
		        				// http://hydra-media.cursecdn.com/minecraft.gamepedia.com/f/fd/12w06a-doors-idx.png
		        				if(mc.theWorld.getBlock(x, y+1, z) instanceof BlockDoor){	
		        					int direction = mc.theWorld.getBlockMetadata(x, y, z);
		        					if(direction == 3) direction = 7;
		        					else if(direction == 0) direction = 4;
		        					else if(direction == 1) direction = 5;
		        					else if(direction == 2) direction = 6;
		        					mc.theWorld.setBlockMetadataWithNotify(x, y, z, direction, 3);
		        					
		        				}
		        			}
				        }
			        }
		        }
		        
			}
		}
	}

}
