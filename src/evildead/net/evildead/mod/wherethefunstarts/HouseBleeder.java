package net.evildead.mod.wherethefunstarts;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class HouseBleeder {

	private EntityPlayer summoner;
	private World world;
	private ChunkCoordinates origin;
	private int radius;
	
	private static final int MAXHOUSEAIRBLOCKS = 100;

	/**
	 * Finds houses within the radius and fills them with blood!
	 * @param summoner Player who summoned the event.
	 * @param origin The summon events epicenter.
	 * @param radius The radius of the event arena. 
	 * That is, the distance from the origin to the middle of a side or half the width of the square,
	 * not the classical square radius of center to vertices.
	 */
	public HouseBleeder(EntityPlayer summoner, ChunkCoordinates origin, int radius){
		this.summoner = summoner;
		this.world = summoner.worldObj;
		this.origin = origin;
		this.radius = radius;
	}
	
	public void floodHouses() {
		
		for(int x = origin.posX - radius; x < origin.posX + radius; x++) {
        	for(int z = origin.posZ - radius; z < origin.posZ + radius; z++) {
        		for(int y = origin.posY - radius; y < origin.posZ + radius; y++) {
        			Block block = world.getBlock(x, y, z);
        			if(block == Blocks.wooden_door) {

        				int doorMeta = world.getBlockMetadata(x, y, z);
        				// north (-z, m 1/5) 
        				// south (+z, m 3/7) 
        				// east  (+x, m 2/6) 
        				// west  (-x, m 0/4)
        				boolean blockFound = false;
        				if(doorMeta == 1 || doorMeta == 5) {			// North	(door is facing north, look south for house first)
        					for(int hx = x; !blockFound; hx++) {
        						Block hblock = world.getBlock(hx, y, z);
        					}
        				}
        			}
            	}
        	}
		}
	}
	
	private void checkForHouse(int x, int y, int z, int doorMeta) {
		boolean blockFound = false;
		
		// hmm, actually, it doesn't matter what direction we are facing, we need to check both.'
		// What if it's a room in a house? Still want to flood it.
		
		
		
		if(doorMeta == 1 || doorMeta == 5) {			// North	(door is facing north, look south for house first)
			for(int hx = x; !blockFound; hx++) {
				for(int hy = y; !blockFound; hy++) {
					for(int hz = z; !blockFound; hz++) {
						
					}
				}
				Block hblock = world.getBlock(hx, y, z);
			}
		}
	}
}
