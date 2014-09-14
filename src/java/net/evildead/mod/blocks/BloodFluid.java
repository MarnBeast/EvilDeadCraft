package net.evildead.mod.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.evildead.mod.particle.EntityBloodSplashFX;
import net.evildead.mod.particle.EntityDropBloodParticleFX;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.client.particle.EntitySuspendFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BloodFluid extends BlockFluidClassic {

	@SideOnly(Side.CLIENT)
    protected IIcon stillIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon flowingIcon;
    
	
	public BloodFluid(Fluid fluid, Material material) {
		super(fluid, material);
		this.lightOpacity = 5;
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
    
    @Override
    public IIcon getIcon(int side, int meta) {
            return (side == 0 || side == 1)? stillIcon : flowingIcon;
    }

	@Override
    @SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){
		this.stillIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5) + "Still");
		this.flowingIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5) + "Flowing");
	}
    
    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
            if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
            return super.canDisplace(world, x, y, z);
    }
    
    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
            if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
            return super.displaceIfPossible(world, x, y, z);
    }
    
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        int l;

        if (this.blockMaterial == Material.water)
        {
            if (random.nextInt(10) == 0)
            {
                l = world.getBlockMetadata(x, y, z);

                if (l <= 0 || l >= 8)
                {
                    world.spawnParticle("suspended", (double)((float)x + random.nextFloat()), (double)((float)y + random.nextFloat()), (double)((float)z + random.nextFloat()), 0.0D, 0.0D, 0.0D);
                	EntitySuspendFX suspended = new EntitySuspendFX(world, x, y, z, 0.0D, 0.0D, 0.0D);
                	suspended.setRBGColorF(0.8F, 0.4F, 0.4F);
                	Minecraft.getMinecraft().effectRenderer.addEffect(suspended);
                }
            }

            for (l = 0; l < 0; ++l)
            {
                int i1 = random.nextInt(4);
                int j1 = x;
                int k1 = z;

                if (i1 == 0)
                {
                    j1 = x - 1;
                }

                if (i1 == 1)
                {
                    ++j1;
                }

                if (i1 == 2)
                {
                    k1 = z - 1;
                }

                if (i1 == 3)
                {
                    ++k1;
                }

                if (world.getBlock(j1, y, k1).getMaterial() == Material.air && (world.getBlock(j1, y - 1, k1).getMaterial().blocksMovement() || world.getBlock(j1, y - 1, k1).getMaterial().isLiquid()))
                {
                    float f = 0.0625F;
                    double d0 = (double)((float)x + random.nextFloat());
                    double d1 = (double)((float)y + random.nextFloat());
                    double d2 = (double)((float)z + random.nextFloat());

                    if (i1 == 0)
                    {
                        d0 = (double)((float)x - f);
                    }

                    if (i1 == 1)
                    {
                        d0 = (double)((float)(x + 1) + f);
                    }

                    if (i1 == 2)
                    {
                        d2 = (double)((float)z - f);
                    }

                    if (i1 == 3)
                    {
                        d2 = (double)((float)(z + 1) + f);
                    }

                    double d3 = 0.0D;
                    double d4 = 0.0D;

                    if (i1 == 0)
                    {
                        d3 = (double)(-f);
                    }

                    if (i1 == 1)
                    {
                        d3 = (double)f;
                    }

                    if (i1 == 2)
                    {
                        d4 = (double)(-f);
                    }

                    if (i1 == 3)
                    {
                        d4 = (double)f;
                    }

                    //world.spawnParticle("splash", d0, d1, d2, d3, 0.0D, d4);
                    
                    EntityBloodSplashFX suspended = new EntityBloodSplashFX(world, x, y, z, 0.0D, 0.0D, 0.0D);
                	suspended.setRBGColorF(0.8F, 0.4F, 0.4F);
                	Minecraft.getMinecraft().effectRenderer.addEffect(suspended);
                }
            }
        }

        if (this.blockMaterial == Material.water && random.nextInt(64) == 0)
        {
            l = world.getBlockMetadata(x, y, z);

            if (l > 0 && l < 8)
            {
                world.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "liquid.water", random.nextFloat() * 0.25F + 0.75F, random.nextFloat() * 1.0F + 0.5F, false);
            }
        }

        double d5;
        double d6;
        double d7;


        if (random.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !world.getBlock(x, y - 2, z).getMaterial().blocksMovement())
        {
            d5 = (double)((float)x + random.nextFloat());
            d6 = (double)y - 1.05D;
            d7 = (double)((float)z + random.nextFloat());

            if (this.blockMaterial == Material.water)
            {
                //world.spawnParticle("dripWater", d5, d6, d7, 0.0D, 0.0D, 0.0D);
            	
            	EntityDropBloodParticleFX suspended = new EntityDropBloodParticleFX(world, d5, d6, d7);
            	Minecraft.getMinecraft().effectRenderer.addEffect(suspended);
            }
            else
            {
                world.spawnParticle("dripLava", d5, d6, d7, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    
}
