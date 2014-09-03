package net.evildead.mod.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BloodyVine extends BlockWeb{

	public BloodyVine(){
		super();
		
		this.setHardness(2.0F);
		this.setResistance(4.0F);
		this.setStepSound(soundTypeGrass);
		this.setCreativeTab(getCreativeTabToDisplayOn().tabDecorations);
		
		this.setLightOpacity(5);
	}


	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){
		this.blockIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5));
	}
}
