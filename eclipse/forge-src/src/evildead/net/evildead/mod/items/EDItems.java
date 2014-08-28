package net.evildead.mod.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class EDItems extends Item{

	public EDItems() {
		this.setCreativeTab(getCreativeTab().tabMisc);	
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister){
		this.itemIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5));		// 5 characters "item."
	}
}
