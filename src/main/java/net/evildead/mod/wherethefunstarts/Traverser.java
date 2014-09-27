package net.evildead.mod.wherethefunstarts;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class Traverser{

	public enum CoorDir { Xp, Xn, Yp, Yn, Zp, Zn };
	
	private ChunkCoordinates location;
	private CoorDir upIs;
	private CoorDir straightIs;
	private World world;

	public Traverser(World world, ChunkCoordinates startLoc, CoorDir straightIs) {
		this(world, startLoc, straightIs, CoorDir.Yp);
	}
	public Traverser(World world, ChunkCoordinates startLoc, CoorDir straightIs, CoorDir upIs) {
		this.world = world;
		this.location = startLoc;
		this.straightIs = straightIs;
		this.upIs = upIs;
	}
	
	public ChunkCoordinates getLocation() { return location; }
	public CoorDir getUpDir() { return upIs; }
	public CoorDir getStraightDir() { return straightIs; }
	public void setUpDir(CoorDir up) { upIs = up; }
	public void setStraightDir(CoorDir straight) { straightIs = straight; }


	public ChunkCoordinates getBlockFacingCoords() {
		switch(straightIs) {
			case Xp: return new ChunkCoordinates(location.posX + 1, location.posY, location.posZ);
			case Xn: return new ChunkCoordinates(location.posX - 1, location.posY, location.posZ);
			case Yp: return new ChunkCoordinates(location.posX, location.posY + 1, location.posZ);
			case Yn: return new ChunkCoordinates(location.posX, location.posY - 1, location.posZ);
			case Zp: return new ChunkCoordinates(location.posX, location.posY, location.posZ + 1);
			case Zn: return new ChunkCoordinates(location.posX, location.posY, location.posZ - 1);
			default: return null;
		}
	}
	
	boolean facingValidAir() {
		ChunkCoordinates facingCoords = getBlockFacingCoords();
		Block facing = world.getBlock(facingCoords.posX, facingCoords.posY, facingCoords.posZ);
		if(facing != Blocks.air) return false;
		
		// facing up or down, only need 1 block space to pass through
		if(straightIs == CoorDir.Yp || straightIs == CoorDir.Yn)  return true; 
		else {
			// get above block.
			Block facing2 = world.getBlock(facingCoords.posX, facingCoords.posY + 1, facingCoords.posZ);
			
			if(facing2 == Blocks.air) {	
				// above is air. Does it match above us? As in, could we walk into it?	
				Block aboveUs = world.getBlock(location.posX, location.posY + 1, location.posZ);
				if(aboveUs == Blocks.air || aboveUs == Blocks.wooden_door) return true;
			}
			
			// get below block.
			facing2 = world.getBlock(facingCoords.posX, facingCoords.posY - 1, facingCoords.posZ);	
			if(facing2 == Blocks.air) {	
				// below is air. Does it match below us? As in, could we walk into it?
				Block belowUs = world.getBlock(location.posX, location.posY - 1, location.posZ);
				if(belowUs == Blocks.air || belowUs == Blocks.wooden_door) return true;
			}
		}
		return false;
	}

	void turnDown() {
		
		CoorDir newUpIs = straightIs;
		CoorDir newStraightIs = negCoorDir(upIs);
		
		upIs = newUpIs;
		straightIs = newStraightIs;
	}

	void turnUp() {
		
		CoorDir newUpIs = negCoorDir(straightIs);
		CoorDir newStraightIs = upIs;
		
		upIs = newUpIs;
		straightIs = newStraightIs;
	}
	void turnRight() {
		
		CoorDir newStraightIs = straightIs;
		
		switch(straightIs) {
			case Xp:
				switch(upIs) {
					case Yp: newStraightIs = CoorDir.Zp; break;
					case Yn: newStraightIs = CoorDir.Zn; break;
					case Zn: newStraightIs = CoorDir.Yp; break;
					case Zp: newStraightIs = CoorDir.Yn; break;
					default: newStraightIs = null;
				} break;
			case Yp:
				switch(upIs) {
					case Xp: newStraightIs = CoorDir.Zn; break;
					case Xn: newStraightIs = CoorDir.Zp; break;
					case Zn: newStraightIs = CoorDir.Yn; break;
					case Zp: newStraightIs = CoorDir.Yp; break;
					default: newStraightIs = null;
				} break;
			case Zp:
				switch(upIs) {
					case Xp: newStraightIs = CoorDir.Yp; break;
					case Xn: newStraightIs = CoorDir.Yn; break;
					case Yn: newStraightIs = CoorDir.Xp; break;
					case Yp: newStraightIs = CoorDir.Xn; break;
					default: newStraightIs = null;
				} break;
			case Xn:
				switch(upIs) {
					case Yp: newStraightIs = CoorDir.Zn; break;
					case Yn: newStraightIs = CoorDir.Zp; break;
					case Zn: newStraightIs = CoorDir.Yn; break;
					case Zp: newStraightIs = CoorDir.Yp; break;
					default: newStraightIs = null;
				} break;
			case Yn:
				switch(upIs) {
					case Xp: newStraightIs = CoorDir.Zp; break;
					case Xn: newStraightIs = CoorDir.Zn; break;
					case Zn: newStraightIs = CoorDir.Yp; break;
					case Zp: newStraightIs = CoorDir.Yn; break;
					default: newStraightIs = null;
				} break;
			case Zn:
				switch(upIs) {
					case Xp: newStraightIs = CoorDir.Yn; break;
					case Xn: newStraightIs = CoorDir.Yp; break;
					case Yn: newStraightIs = CoorDir.Xn; break;
					case Yp: newStraightIs = CoorDir.Xp; break;
					default: newStraightIs = null;
				} break;
		}

		straightIs = newStraightIs;
	}

	void turnLeft() {
		turnRight();
		straightIs = negCoorDir(straightIs);
	}

	void turnAround() {
		straightIs = negCoorDir(straightIs);
	}

	void moveStraight() {
		location = new ChunkCoordinates(location.posX, location.posY, location.posZ);
		switch(straightIs) {
			case Xp: location.posX++; break;
			case Xn: location.posX--; break;
			case Yp: location.posY++; break;
			case Yn: location.posY--; break;
			case Zp: location.posZ++; break;
			case Zn: location.posZ--; break;
			default: break;
		}
	}

   void moveBack() {
	   location = new ChunkCoordinates(location.posX, location.posY, location.posZ);
	   switch(straightIs) {
			case Xp: location.posX--; break;
			case Xn: location.posX++; break;
			case Yp: location.posY--; break;
			case Yn: location.posY++; break;
			case Zp: location.posZ--; break;
			case Zn: location.posZ++; break;
			default: break;
		}
	}

	private CoorDir negCoorDir(CoorDir dir) {
		switch(dir) {
			case Xp: return CoorDir.Xn;
			case Xn: return CoorDir.Xp;
			case Yp: return CoorDir.Yn;
			case Yn: return CoorDir.Yp;
			case Zp: return CoorDir.Zn;
			case Zn: return CoorDir.Zp;
			default: return null;
		}
	}
}
