package net.evildead.mod.items;

import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.evildead.mod.gui.GuiScreenEDBook;
import net.evildead.mod.gui.GuiScreenRubbingPaper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class RubbingPaper extends Item{

	private boolean returnedNecroBook = false;
	
	public RubbingPaper() {
		this.setCreativeTab(getCreativeTab().tabMisc);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister){
		this.itemIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5));		// 5 characters "item."
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player){
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		player.openGui(EvilDead.instance, EvilDead.guiIDRubbing, world, 0, 0, 0);
		FMLClientHandler.instance().displayGuiScreen(player, new GuiScreenRubbingPaper(player, stack));
		return stack;
	}
	
	@Override
	 public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(this.getItemNameLocal());
	 }

	 public String getItemNameLocal(){
		 return StatCollector.translateToLocal(this.getUnlocalizedName() + ".desc");
	 }
}
