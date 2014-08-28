package net.evildead.mod.items;

import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.evildead.mod.gui.GuiScreenEDBook;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class NecroBook extends Item{

	
	
	
	public NecroBook() { 
		this.setCreativeTab(getCreativeTab().tabMisc);
		this.setContainerItem(this);	// we don't want the necronomicon being consumed by the rubbing recipe.
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister){
		this.itemIcon = iconRegister.registerIcon(EvilDead.modid + ":" + this.getUnlocalizedName().substring(5));		// 5 characters "item."
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player){
		GuiScreenEDBook bookGui = new GuiScreenEDBook(player, stack)
			.withLeftBookTextures(new ResourceLocation(EvilDead.modid + ":" + "textures/gui/necroleft.png"))
			.withRightBookTextures(new ResourceLocation(EvilDead.modid + ":" + "textures/gui/necroright.png"))
			.withButtonTextures(new ResourceLocation(EvilDead.modid + ":" + "textures/gui/necroleft.png"))
			.withBookImageDimensions(150, 200);
		
		bookGui.addPage(new ResourceLocation(EvilDead.modid + ":" + "textures/gui/pages/necro0.png"))
			.addPage(new ResourceLocation(EvilDead.modid + ":" + "textures/gui/pages/necro1.png"))
			.addPage(new ResourceLocation(EvilDead.modid + ":" + "textures/gui/pages/necro2.png"))
			.addPage(new ResourceLocation(EvilDead.modid + ":" + "textures/gui/pages/necro3.png"))
			.addPage(new ResourceLocation(EvilDead.modid + ":" + "textures/gui/pages/necro4.png"))
			.addPage(new ResourceLocation(EvilDead.modid + ":" + "textures/gui/pages/necro3.png"));
		
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		player.openGui(EvilDead.instance, EvilDead.guiIDNecro, world, 0, 0, 0);
		FMLClientHandler.instance().displayGuiScreen(player, bookGui);
		return stack;
	}
	
	@Override
	 public EnumRarity getRarity(ItemStack itemStack){
		 return EnumRarity.rare;
	 }
	
	@Override
	 public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(this.getItemNameLocal());
	 }

	 public String getItemNameLocal(){
		 return StatCollector.translateToLocal(this.getUnlocalizedName() + ".desc");
	 }
	
}
