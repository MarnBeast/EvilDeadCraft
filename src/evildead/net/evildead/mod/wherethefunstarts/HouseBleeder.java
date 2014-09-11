package net.evildead.mod.wherethefunstarts;

import java.util.ArrayList;

import net.evildead.mod.EvilDead;
import net.evildead.mod.wherethefunstarts.Traverser.CoorDir;
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
	
	private Traverser traverser;
	private ArrayList<ChunkCoordinates> validAirInThisRoom;
	private ArrayList<ChunkCoordinates> airToReplace = new ArrayList<ChunkCoordinates>();
	
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
        				checkForHouse(new ChunkCoordinates(x,y,z), doorMeta);
        			}
            	}
        	}
		}
		
		for(ChunkCoordinates loc : airToReplace) {
			world.setBlock(loc.posX, loc.posY, loc.posZ, EvilDead.blockTestMarker);
		}
	}
	
	
	private void checkForHouse(ChunkCoordinates doorLoc, int doorMeta) {
		CoorDir dir = null;
		if(doorMeta == 1 || doorMeta == 5 || doorMeta == 3 || doorMeta == 7) {			// North or South
			dir = CoorDir.Zp;
		}
		else if(doorMeta == 2 || doorMeta == 6 || doorMeta == 0 || doorMeta == 4) {		// East or West
			dir = CoorDir.Xp;
		}
		else return;	// this is a door topper. we're already checking with the bottom, so ignore.
		
		validAirInThisRoom = new ArrayList<ChunkCoordinates>();
		traverser = new Traverser(world, doorLoc, dir);
		if(traverser.facingValidAir()) {										// If the air in front of the door is air, check the rest of the room
			boolean roomFound = checkFacingBlockSidesForValidAir(traverser);	// A complete room was mapped.
			if(roomFound) {
				airToReplace.addAll(validAirInThisRoom);
			}
		}

		// do both in front of and behind the door. Who knows, could be a room!
		validAirInThisRoom.clear();
		traverser.turnAround();
		if(traverser.facingValidAir()) {
			boolean roomFound = checkFacingBlockSidesForValidAir(traverser);
			if(roomFound) {
				airToReplace.addAll(validAirInThisRoom);
			}
		}
	}
	
	
	// Recursively move through the room checking for valid air.
	private boolean checkFacingBlockSidesForValidAir(Traverser traverser) {
		if(validAirInThisRoom.size() >= MAXHOUSEAIRBLOCKS) 
			return false;
		
		boolean firstPass = validAirInThisRoom.size() < 1;
		
		traverser.moveStraight();		// move to facing block
//		if(traverser.getStraightDir() == CoorDir.Yp) traverser.turnDown();		// get back on the xz plane
//		else if(traverser.getStraightDir() == CoorDir.Yn) traverser.turnUp();
		
		ChunkCoordinates loc = traverser.getLocation();
		validAirInThisRoom.add(loc);
		
		traverser.turnDown();						// Down
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){
			
			if(!checkFacingBlockSidesForValidAir(traverser))
				return false;
		}
	
		traverser.turnUp(); 
		traverser.turnRight();	// Right
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){

			if(!checkFacingBlockSidesForValidAir(traverser)) 
				return false;
		}
	
		traverser.turnLeft();						// Straight
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){

			if(!checkFacingBlockSidesForValidAir(traverser)) 
				return false;
		}
	
		traverser.turnLeft();						// Left
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){

			if(!checkFacingBlockSidesForValidAir(traverser)) 
				return false;
		}
	
		traverser.turnRight();
		traverser.turnUp();	// Up
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){

			if(!checkFacingBlockSidesForValidAir(traverser)) 
				return false;
		}
	
		traverser.turnDown(); traverser.moveBack();		// (back is taken care of via recursive return)
		return true;
	}

	
	private boolean chunkCoordinatesSaved(ChunkCoordinates coord) {
		for(ChunkCoordinates savedCoord : validAirInThisRoom) {
			if(	coord.posX == savedCoord.posX &&
				coord.posY == savedCoord.posY &&
				coord.posZ == savedCoord.posZ) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	

	
	
	public void traverserTest() {
		Traverser t = new Traverser(world, origin, CoorDir.Zp);
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		
		t.turnRight();
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		
		t.turnLeft();
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		
		t.turnDown();
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		
		t.turnAround();
		t.moveStraight();
		t.moveStraight();
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		
		t.turnDown();
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		
		t.turnUp();
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		
		t.turnDown();
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		
		t.turnRight();
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		
		t.turnLeft();
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
		t.moveStraight();
		world.setBlock(t.getLocation().posX, t.getLocation().posY, t.getLocation().posZ, EvilDead.blockTestMarker);
	}
}
