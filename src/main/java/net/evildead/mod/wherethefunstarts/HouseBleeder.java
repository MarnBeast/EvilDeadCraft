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
	
	private boolean foundHouses = false;
	
	private Traverser traverser;
	private ArrayList<ChunkCoordinates> validAirInThisRoom;			// used by checkFacingBlockSidesForValidAir
	private ArrayList<ArrayList<ChunkCoordinates>> roomsToFlood = new ArrayList<ArrayList<ChunkCoordinates>>();
	
	private static final int MAXHOUSEAIRBLOCKS = 150;
	
	

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
	
	
	
	public void dripBlood() {
		if(!foundHouses) return;		// just in case we run into a race condition.
		
		for(ArrayList<ChunkCoordinates> airToReplace : roomsToFlood) {
			for(ChunkCoordinates loc : airToReplace) {
				if(world.getBlock(loc.posX, loc.posY, loc.posZ) == Blocks.air &&
					World.doesBlockHaveSolidTopSurface(world, loc.posX, loc.posY + 1, loc.posZ)) {
					
					world.setBlock(loc.posX, loc.posY, loc.posZ, EvilDead.blockBloodDripper);
				}
			}
		}
	}
	
	public void setToAir() {
		for(int x = origin.posX - radius; x < origin.posX + radius; x++) {
        	for(int z = origin.posZ - radius; z < origin.posZ + radius; z++) {
        		for(int y = origin.posY - radius; y < origin.posZ + radius; y++) {
        			if(world.getBlock(x, y, z) == EvilDead.blockTestMarker ||
        				world.getBlock(x, y, z) == EvilDead.blockBloodDripper) {
        				world.setBlock(x, y, z, Blocks.air);
        			}
        		}
        	}
		}
	}
	
	
	/**
	 * Finds the houses based on enclosed structures (a player cannot exit) with a door and remembers
	 * them for reference when flooding.
	 */
	public void findHouses() {
		
		foundHouses = false;
		roomsToFlood.clear();
		
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
		
		for(ArrayList<ChunkCoordinates> airToReplace : roomsToFlood) {
			for(ChunkCoordinates loc : airToReplace) {
				//world.setBlock(loc.posX, loc.posY, loc.posZ, EvilDead.blockTestMarker);
			}
		}
		foundHouses = true;
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
				roomsToFlood.add(validAirInThisRoom);
			}
		}

		// do both in front of and behind the door. Who knows, could be a room!

		validAirInThisRoom = new ArrayList<ChunkCoordinates>();
		traverser.turnAround();
		if(traverser.facingValidAir()) {
			boolean roomFound = checkFacingBlockSidesForValidAir(traverser);
			if(roomFound) {
				roomsToFlood.add(validAirInThisRoom);
			}
		}
	}
	
	
	// 
	/**
	 * Recursively move through the room checking for valid air.
	 * @param traverser
	 * @return False when the maximum amount of house air blocks has been reached and we should stop checking.
	 */
	private boolean checkFacingBlockSidesForValidAir(Traverser traverser) {
		if(validAirInThisRoom.size() >= MAXHOUSEAIRBLOCKS) 
			return false;
		
		CoorDir origStraight = traverser.getStraightDir();
		CoorDir origUp = traverser.getUpDir();
		
		traverser.moveStraight();		// move to facing block
		
		ChunkCoordinates loc = traverser.getLocation();
		validAirInThisRoom.add(loc);
		
		traverser.turnDown();						// Down
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){
			
			if(!checkFacingBlockSidesForValidAir(traverser))
				return false;
		}
	
		traverser.turnUp(); 
		if(traverser.getStraightDir() != origStraight || traverser.getUpDir() != origUp) {
			System.out.println("BUG FOUND \"Down\"- oS=" + origStraight + " nS=" + traverser.getStraightDir() + ", oU=" + origUp + " nU=" + traverser.getUpDir() +
					"\n" + traverser.getLocation().posX);
			//traverser.setStraightDir(origStraight);
			//traverser.setUpDir(origUp);
		}
//		else{
//			System.out.println("NO BUG");
//		}
		traverser.turnRight();						// Right
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){

			if(!checkFacingBlockSidesForValidAir(traverser))
				return false;
		}
	
		traverser.turnLeft();						// Straight
		if(traverser.getStraightDir() != origStraight || traverser.getUpDir() != origUp) {
			System.out.println("BUG FOUND \"Right\"- oS=" + origStraight + " nS=" + traverser.getStraightDir() + ", oU=" + origUp + " nU=" + traverser.getUpDir() +
					"\n" + traverser.getLocation().posX);
			traverser.setStraightDir(origStraight);
			traverser.setUpDir(origUp);
		}
//		else{
//			System.out.println("NO BUG");
//		}
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){

			if(!checkFacingBlockSidesForValidAir(traverser))
				return false;
		}

		if(traverser.getStraightDir() != origStraight || traverser.getUpDir() != origUp) {
			System.out.println("BUG FOUND \"Straight\"- oS=" + origStraight + " nS=" + traverser.getStraightDir() + ", oU=" + origUp + " nU=" + traverser.getUpDir() +
					"\n" + traverser.getLocation().posX);
			traverser.setStraightDir(origStraight);
			traverser.setUpDir(origUp);
		}
//		else{
//			System.out.println("NO BUG");
//		}
		traverser.turnLeft();						// Left
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){

			if(!checkFacingBlockSidesForValidAir(traverser))
				return false;
		}
	
		traverser.turnRight();
		if(traverser.getStraightDir() != origStraight || traverser.getUpDir() != origUp) {
			System.out.println("BUG FOUND \"Left\"- oS=" + origStraight + " nS=" + traverser.getStraightDir() + ", oU=" + origUp + " nU=" + traverser.getUpDir() +
					"\n" + traverser.getLocation().posX);
			traverser.setStraightDir(origStraight);
			traverser.setUpDir(origUp);
		}
//		else{
//			System.out.println("NO BUG");
//		}
		traverser.turnUp();							// Up
		if(	traverser.facingValidAir() &&
				!chunkCoordinatesSaved(traverser.getBlockFacingCoords())){

			if(!checkFacingBlockSidesForValidAir(traverser))
				return false;
		}
	
		traverser.turnDown(); traverser.moveBack();		// (back is taken care of via recursive return)
		if(traverser.getStraightDir() != origStraight || traverser.getUpDir() != origUp) {
			System.out.println("BUG FOUND \"Up\"- oS=" + origStraight + " nS=" + traverser.getStraightDir() + ", oU=" + origUp + " nU=" + traverser.getUpDir() +
					"\n" + traverser.getLocation().posX);
			traverser.setStraightDir(origStraight);
			traverser.setUpDir(origUp);
		}
//		else{
//			System.out.println("NO BUG");
//		}
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
