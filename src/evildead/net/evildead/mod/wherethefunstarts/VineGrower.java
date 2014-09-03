package net.evildead.mod.wherethefunstarts;

import net.evildead.mod.EvilDead;
import net.evildead.mod.blocks.DarkAir;
import net.evildead.mod.blocks.StickyVine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class VineGrower {
	
	private EntityPlayer summoner;
	private ChunkCoordinates origin;
	private int radius;
	
	private static final int VINERINGWIDTH = 20;		// depth of vine ring
	private static final int VINECHANCE = 5;			// 1 in VINECHANCE chance
	private static final int VINECYCLESONLEVEL = 1;		// how many times to cycle on a level before moving to the next level
	private static final int VINETICKSPERLEVEL = 1;		// 10 seconds per level
	
	private static final double VINETICKSPERCYCLE = (double)VINETICKSPERLEVEL / VINECYCLESONLEVEL;
	private static final int VINEXZDIVIDE = 1;//(int) (Math.sqrt(VINETICKSPERCYCLE) > 0 ? VINETICKSPERCYCLE : 1);
	private static final int VINETICKSPERDIVIDEXZ = (int)((double)VINETICKSPERLEVEL / (VINECYCLESONLEVEL * VINEXZDIVIDE * VINEXZDIVIDE));
	private int vineticks = radius * VINETICKSPERLEVEL;

	private int vinesLevel = 100;
	private int xDivide = 0;
	private int zDivide = 0;
	private int vinesLevelTime = 0;
	
	public int getTotalGrowTicks(){
		return vineticks;
	}
	
	public int getCurrentVinesLevel(){
		return vinesLevel;
	}
	
	
	/**
	 * Manages gradual vine growth over a number of ticks.
	 * @param summoner Player who summoned the event.
	 * @param origin The summon events epicenter.
	 * @param radius The radius of the vines perimeter. 
	 * That is, the distance from the origin to the middle of a side or half the width of the square,
	 * not the classical square radius of center to vertices.
	 */
	public VineGrower(EntityPlayer summoner, ChunkCoordinates origin, int radius){
		this.summoner = summoner;
		this.origin = origin;
		this.radius = radius;
		this.vineticks = radius * VINETICKSPERLEVEL;
	}
	
	/**
	 * Called each tick to grow the vines. Don't need to modulus the ticks, I do that here.
	 * @param count
	 */
	public void grow(int count) {
		  
		int modval = VINETICKSPERDIVIDEXZ; // vinesLevel / (RADIUS / VINEAVGTICKSPERLEVEL);  // We want to "grow" below ground much faster, since we want to get to the surface growth quickly. 
		int mod = (modval != 0) ? count % modval : 0;				// Plus, this may make the vines look like they are slowing to a stop above ground.
		if(mod == 0 && vinesLevel < radius) {
			for(int x = origin.posX - radius + xDivide; x < origin.posX + radius; x+=VINEXZDIVIDE) {
	        	for(int z = origin.posZ - radius + zDivide; z < origin.posZ + radius; z+=VINEXZDIVIDE) {
	    			
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
		
		if(	((x < origin.posX - radius + VINERINGWIDTH) ||
    			(x > origin.posX + radius - VINERINGWIDTH)) ||
    			((z < origin.posZ - radius + VINERINGWIDTH) ||
    			(z > origin.posZ + radius - VINERINGWIDTH))) {

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

	/**
	 * Called each tick to shrink the vines. Don't need to modulus the ticks, I do that here.
	 * @param count
	 */
	public void shrink(int count) {
		  
		int modval = VINETICKSPERDIVIDEXZ; // vinesLevel / (RADIUS / VINEAVGTICKSPERLEVEL);  // We want to "grow" below ground much faster, since we want to get to the surface growth quickly. 
		int mod = (modval != 0) ? count % modval : 0;				// Plus, this may make the vines look like they are slowing to a stop above ground.
		if(mod == 0 && vinesLevel >= 0){
			
			World world = summoner.worldObj;
			for(int x = origin.posX - radius + xDivide; x < origin.posX + radius; x+=VINEXZDIVIDE) {
	        	for(int z = origin.posZ - radius + zDivide; z < origin.posZ + radius; z+=VINEXZDIVIDE) {
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
		
		if(	((x < origin.posX - radius + VINERINGWIDTH) ||
		(x > origin.posX + radius - VINERINGWIDTH)) ||
		((z < origin.posZ - radius + VINERINGWIDTH) ||
		(z > origin.posZ + radius - VINERINGWIDTH))) {
		
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

	
	/**
	 * For debugging. Does an instant clear of all vines in the area.
	 */
	public void resetVines() {
		int rad = radius * 2;

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
	

	
	public void growInstantly() {
		summoner.addChatMessage(new ChatComponentText("Resetting vines"));
		for(int x = origin.posX - radius; x < origin.posX + radius; x++) {
        	for(int y = origin.posY - radius; y < origin.posY + radius; y++) {
        		for(int z = origin.posZ - radius; z < origin.posZ + radius; z++) {

        			this.setVine(x, y, z);
        		}
        	}
		}
		summoner.addChatMessage(new ChatComponentText("Resetting vines complete"));
	}
	
	/**
	 * Called each tick to grow the vines. Don't need to modulus the ticks, I do that here.
	 * @param count
	 */
	public void simpleGrow(int count) {
		  
		int modval = VINETICKSPERLEVEL; 
		int mod = (modval != 0) ? count % modval : 0;
		if(mod == 0 && vinesLevel < radius) {
			for(int x = origin.posX - radius + xDivide; x < origin.posX + radius; x+=VINEXZDIVIDE) {
	        	for(int z = origin.posZ - radius + zDivide; z < origin.posZ + radius; z+=VINEXZDIVIDE) {
	    			
	        		int y = origin.posY + vinesLevel;
	        		setVine(x,y,z);
	    			
	        		y = origin.posY - vinesLevel;
	        		setVine(x,y,z);
		        }
	        }
			
			vinesLevel++;
			summoner.addChatMessage(new ChatComponentText("Vine level: " + vinesLevel + " - Cycle: " + vinesLevelTime));
			//summoner.addChatMessage(new ChatComponentText("xdiv: " + xDivide + " - zdiv: " + zDivide));
		}
	}
	
	/**
	 * Called each tick to grow the vines. Don't need to modulus the ticks, I do that here.
	 * @param count
	 */
	public void simpleShrink(int count) {
		  
		int modval = VINETICKSPERLEVEL; 
		int mod = (modval != 0) ? count % modval : 0;
		if(mod == 0 && vinesLevel >= 0){
			for(int x = origin.posX - radius + xDivide; x < origin.posX + radius; x+=VINEXZDIVIDE) {
	        	for(int z = origin.posZ - radius + zDivide; z < origin.posZ + radius; z+=VINEXZDIVIDE) {
	    			
	        		int y = origin.posY + vinesLevel;
	        		removeVine(x,y,z);
	    			
	        		y = origin.posY - vinesLevel;
	        		removeVine(x,y,z);
		        }
	        }
			
			vinesLevel--;
			summoner.addChatMessage(new ChatComponentText("Vine level: " + vinesLevel + " - Cycle: " + vinesLevelTime));
			//summoner.addChatMessage(new ChatComponentText("xdiv: " + xDivide + " - zdiv: " + zDivide));
		}
	}

}
