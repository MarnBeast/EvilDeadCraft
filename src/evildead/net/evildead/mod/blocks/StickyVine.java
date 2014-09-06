package net.evildead.mod.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;

public class StickyVine extends BlockWeb{

	public StickyVine(){
		super();
		
		this.setHardness(1.0F);
		this.setResistance(3.0F);   
		this.setStepSound(soundTypeGrass);
		this.setCreativeTab(getCreativeTabToDisplayOn().tabDecorations);
		this.setLightOpacity(8);
		this.setLightLevel(0.0f);
	}


//	@SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister iconRegister){
//		this.blockIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5));
//	}

    
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i1, int i2){
		return Blocks.vine.getIcon(i1, i2);
	}
	

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }


    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return p_149720_1_.getBiomeGenForCoords(p_149720_2_, p_149720_4_).getBiomeFoliageColor(p_149720_2_, p_149720_3_, p_149720_4_);
    }
}
