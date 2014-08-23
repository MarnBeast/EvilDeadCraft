package net.evildead.mod.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class StickyVine extends BlockWeb{

	public StickyVine(Material material){
		super();
		
		this.setHardness(1.0F);
		this.setResistance(3.0F);
		this.setStepSound(soundTypeGrass);
		this.setCreativeTab(getCreativeTabToDisplayOn().tabDecorations);
		this.setLightOpacity(8);
	}


	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){
		this.blockIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5));
	}
}
