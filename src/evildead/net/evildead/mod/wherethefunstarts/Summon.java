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
	
	private static final int RADIUS = 100;				// radius of summon event
	private static final int VINERINGWIDTH = 20;		// depth of vine ring
	private static final int VINECHANCE = 5;			// 1 in VINECHANCE chance
	private static final int VINECYCLESONLEVEL = 2;		// how many times to cycle on a level before moving to the next level
	private static final int VINETICKSPERLEVEL = 2;		// 10 seconds per level
	
	private static final double VINETICKSPERCYCLE = (double)VINETICKSPERLEVEL / VINECYCLESONLEVEL;
	private static final int VINEXZDIVIDE = 1;//(int) (Math.sqrt(VINETICKSPERCYCLE) > 0 ? VINETICKSPERCYCLE : 1);
	private static final int VINETICKSPERDIVIDEXZ = (int)((double)VINETICKSPERLEVEL / (VINECYCLESONLEVEL * VINEXZDIVIDE * VINEXZDIVIDE));
	private static final int VINETICKS = RADIUS * VINETICKSPERLEVEL;
	
	private static final int DEEPENORIGIN = 10;

	int vinesLevel = 0;
	int xDivide = 0;
	int zDivide = 0;
	int vinesLevelTime = 0;
	//int vinesMaxLevel = 150;

	
	public Summon(EntityPlayer summoner){
		this.summoner = summoner;
	}

	
	public void begin() {

		// This class does set block stuff and is designed to be operated by the server;
		if(!summoner.worldObj.isRemote)
		{
			/* Initialize */
	        origin = summoner.getPlayerCoordinates();
	        origin.posY = (origin.posY < RADIUS / 5 && origin.posY > (-RADIUS) / 5) ? 		// grow out of the ground.
	        		-DEEPENORIGIN : origin.posY - DEEPENORIGIN;
	        
			/* Say Incantation */
			String incantation = "Kanda… Es-trada… Montos… Kanda…";
	
			IChatComponent chat = new ChatComponentText("<" + summoner.getDisplayName() + "> ");
			
			IChatComponent kandaChat = new ChatComponentText(incantation);
			kandaChat.setChatStyle(new ChatStyle().setItalic(true).setColor(EnumChatFormatting.GRAY));
			chat.appendSibling(kandaChat);
			
			summoner.addChatMessage(chat);
			
	        
	        /* Start Playing Invocation Music */
	        SendSoundToAllAround(EDReference.Sounds.INVOCATION1, 
	        		origin.posX, origin.posY, origin.posZ,
	        		10f, 1f);
	        
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
			//if(count == 0) resetVines();
			if(count < VINETICKS) growVines(count); //growVines(count);
			//if(count == 1950) throwDoors();
			if(count > VINETICKS+400 && count < 2*VINETICKS + 400) shrinkVines(count);
			
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
	
	// for debugging
	private void resetVines() {
		int rad = RADIUS * 2;

		summoner.addChatMessage(new ChatComponentText("Resetting vines"));
		for(int x = origin.posX - rad; x < origin.posX + rad; x++) {
        	for(int y = origin.posY - rad; y < origin.posY + rad; y++) {
        		for(int z = origin.posZ - rad; z < origin.posZ + rad; z++) {
        			Block block = summoner.worldObj.getBlock(x, y, z);
        			if(block == EvilDead.blockStickyVine || block == EvilDead.blockDarkAir) {
        				summoner.worldObj.setBlockToAir(x, y, z);
        			}
        		}
        	}
		}
		summoner.addChatMessage(new ChatComponentText("Resetting vines complete"));
	}
	
	
	private void growVines(int count) {
		  
		int modval = VINETICKSPERDIVIDEXZ; // vinesLevel / (RADIUS / VINEAVGTICKSPERLEVEL);  // We want to "grow" below ground much faster, since we want to get to the surface growth quickly. 
		int mod = (modval != 0) ? count % modval : 0;				// Plus, this may make the vines look like they are slowing to a stop above ground.
		if(mod == 0 && vinesLevel < RADIUS) {
			for(int x = origin.posX - RADIUS + xDivide; x < origin.posX + RADIUS; x+=VINEXZDIVIDE) {
	        	for(int z = origin.posZ - RADIUS + zDivide; z < origin.posZ + RADIUS; z+=VINEXZDIVIDE) {
	    			
	        		int y = origin.posY + vinesLevel;
	        		setVine(x,y,z);
	    			
	        		y = origin.posY - vinesLevel;
	        		setVine(x,y,z);
		        }
	        }
			
			xDivide++;
			if(xDivide >= VINEXZDIVIDE){
				xDivide = 0;
				zDivide++;
			}
			if(zDivide >= VINEXZDIVIDE){
				zDivide = 0;
				vinesLevelTime++;
			}
			if(vinesLevelTime >= VINECYCLESONLEVEL) {
				vinesLevelTime = 0;
				vinesLevel++;
			}
			summoner.addChatMessage(new ChatComponentText("Vine level: " + vinesLevel + " - Cycle: " + vinesLevelTime));
			//summoner.addChatMessage(new ChatComponentText("xdiv: " + xDivide + " - zdiv: " + zDivide));
		}
	}
	
	private void setVine(int x, int y, int z){

		World world = summoner.worldObj;
		
		if(	((x < origin.posX - RADIUS + VINERINGWIDTH) ||
    			(x > origin.posX + RADIUS - VINERINGWIDTH)) ||
    			((z < origin.posZ - RADIUS + VINERINGWIDTH) ||
    			(z > origin.posZ + RADIUS - VINERINGWIDTH))) {

    			Block block = world.getBlock(x, y, z);
    			
        		if(world.isAirBlock(x, y, z) || block instanceof DarkAir) {
        			if( !world.isAirBlock(x+1, y, z) && !(world.getBlock(x+1, y, z) instanceof DarkAir) ||
    					!world.isAirBlock(x-1, y, z) && !(world.getBlock(x-1, y, z) instanceof DarkAir) ||
    					!world.isAirBlock(x, y+1, z) && !(world.getBlock(x, y+1, z) instanceof DarkAir) ||
    					!world.isAirBlock(x, y-1, z) && !(world.getBlock(x, y-1, z) instanceof DarkAir) ||
    					!world.isAirBlock(x, y, z+1) && !(world.getBlock(x, y, z+1) instanceof DarkAir) ||
    					!world.isAirBlock(x, y, z-1) && !(world.getBlock(x, y, z-1) instanceof DarkAir)) {
        				
        				int chance = world.rand.nextInt(VINECHANCE);
        				if(chance == 0) {
        					world.setBlock(x, y, z, EvilDead.blockStickyVine);
        				}
        				else {
        					world.setBlock(x, y, z, EvilDead.blockDarkAir);
        				}
        			}
        		}
    		}
	}

	private void shrinkVines(int count) {
		  
		int modval = VINETICKSPERDIVIDEXZ; // vinesLevel / (RADIUS / VINEAVGTICKSPERLEVEL);  // We want to "grow" below ground much faster, since we want to get to the surface growth quickly. 
		int mod = (modval != 0) ? count % modval : 0;				// Plus, this may make the vines look like they are slowing to a stop above ground.
		if(mod == 0 && vinesLevel >= 0){
			
			World world = summoner.worldObj;
			for(int x = origin.posX - RADIUS + xDivide; x < origin.posX + RADIUS; x+=VINEXZDIVIDE) {
	        	for(int z = origin.posZ - RADIUS + zDivide; z < origin.posZ + RADIUS; z+=VINEXZDIVIDE) {
	        		int y = origin.posY + vinesLevel;
	        		removeVine(x,y,z);
	    			
	        		y = origin.posY - vinesLevel;
	        		removeVine(x,y,z);
		        }
			}
				
			xDivide++;
			if(xDivide >= VINEXZDIVIDE){
				xDivide = 0;
				zDivide++;
			}
			if(zDivide >= VINEXZDIVIDE){
				zDivide = 0;
				vinesLevelTime++;
			}
			if(vinesLevelTime >= VINECYCLESONLEVEL) {
				vinesLevelTime = 0;
				vinesLevel--;
			}
			summoner.addChatMessage(new ChatComponentText("Vine level: " + vinesLevel + " - Cycle: " + vinesLevelTime));
			//summoner.addChatMessage(new ChatComponentText("xdiv: " + xDivide + " - zdiv: " + zDivide));
		}
	}
	
	private void removeVine(int x, int y, int z){

		World world = summoner.worldObj;
		
		if(	((x < origin.posX - RADIUS + VINERINGWIDTH) ||
		(x > origin.posX + RADIUS - VINERINGWIDTH)) ||
		((z < origin.posZ - RADIUS + VINERINGWIDTH) ||
		(z > origin.posZ + RADIUS - VINERINGWIDTH))) {
		
			Block block = world.getBlock(x, y, z);
			
    		if(block instanceof StickyVine || block instanceof DarkAir) {
				int chance = 0;
				if(vinesLevelTime < VINECYCLESONLEVEL-1) chance = world.rand.nextInt(VINECYCLESONLEVEL);
				if(chance == 0) {
					world.setBlockToAir(x, y, z);
				}
    		}
		}
	}
	
	
//	private void shrinVines(int count) {
//		int modval = vinesLevel / (RADIUS / VINETICKSPERLEVEL);  // We want to "grow" below ground much faster, since we want to get to the surface growth quickly. 
//		int mod = (modval != 0) ? count % modval : 0;				// Plus, this may make the vines look like they are slowing to a stop above ground.
//		if(mod == 0 && vinesLevel >= 0){
//			
//			World world = summoner.worldObj;
//			int y = origin.posY - RADIUS + vinesLevel;
//			
//			summoner.addChatMessage(new ChatComponentText("Vine y: " + y));
//			
//			for(int x = origin.posX - RADIUS; x < origin.posX + RADIUS; x++) {
//	        	for(int z = origin.posZ - RADIUS; z < origin.posZ + RADIUS; z++) {
//	        		
//	        		if(	((x < origin.posX - RADIUS + VINERINGWIDTH) ||
//	        			(x > origin.posX + RADIUS - VINERINGWIDTH)) ||
//	        			((z < origin.posZ - RADIUS + VINERINGWIDTH) ||
//	        			(z > origin.posZ + RADIUS - VINERINGWIDTH))) {
//	        			
//	        			Block block = world.getBlock(x, y, z);
//	        			
//		        		if(block instanceof StickyVine || block instanceof DarkAir) {
//	        				int chance = 0;
//	        				if(vinesLevelTime < VINECYCLESONLEVEL) chance = world.rand.nextInt(VINECYCLESONLEVEL);
//	        				if(chance == 0) {
//	        					world.setBlockToAir(x, y, z);
//	        				}
//		        		}
//	        		}
//		        }
//	        }
//			
//			vinesLevelTime++;
//			if(vinesLevelTime >= VINECYCLESONLEVEL) {
//				vinesLevelTime = 0;
//				vinesLevel--;
//			}
//		}
//	}
//	
	
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
