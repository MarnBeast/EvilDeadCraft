package net.evildead.mod.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;

public class KnowbyRecord extends ItemRecord{

	public KnowbyRecord(String recordName) {
		super(recordName);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.maxStackSize = 1;
		
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister){
		this.itemIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5));		// 5 characters "item."
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_)
    {
        p_77624_3_.add(this.getUnlocalizedName().substring(5));
    }
	
	public String getSoundFile()
    {
            return "/assets/evildead/audio/KnowbyRecord.ogg";
    }
}
