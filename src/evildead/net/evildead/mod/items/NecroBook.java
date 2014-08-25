package net.evildead.mod.items;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.evildead.mod.gui.GuiScreenNecroBook;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NecroBook extends Item{

	
	
	
	public NecroBook() {
		super();
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister){
		this.itemIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5));		// 5 characters "item."
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player){
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		player.openGui(EvilDead.instance, EvilDead.guiIDNecro, world, 0, 0, 0);
		FMLClientHandler.instance().displayGuiScreen(player, new GuiScreenNecroBook(player, stack));
		return stack;
	}
}
