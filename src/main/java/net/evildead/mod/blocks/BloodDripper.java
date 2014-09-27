package net.evildead.mod.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.evildead.mod.particle.EntityDropBloodParticleFX;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BloodDripper extends Block{
	
	public BloodDripper()
	{
		super(Material.vine);
		this.setCreativeTab(getCreativeTabToDisplayOn().tabDecorations);
		this.setTickRandomly(true);
	}
    
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){
		this.blockIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5));
	}
	
	
	/**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }
    
    /**
     * Returns whether this block is collideable based on the arguments passed in n@param par1 block metaData n@param
     * par2 whether the player right-clicked while holding a boat
     */
    public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_)
    {
        return false;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        double d5;
        double d6;
        double d7;


        if (random.nextInt(10) == 0)
        {
            d5 = (double)((float)x + random.nextFloat());
            d6 = (double)y + 0.95D;
            d7 = (double)((float)z + random.nextFloat());

            //world.spawnParticle("dripWater", d5, d6, d7, 0.0D, 0.0D, 0.0D);
        	
        	EntityDropBloodParticleFX suspended = new EntityDropBloodParticleFX(world, d5, d6, d7);
        	Minecraft.getMinecraft().effectRenderer.addEffect(suspended);
        	
        	System.out.println("DRIP");
        }
    }
}
