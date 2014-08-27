package net.evildead.mod.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.evildead.mod.EvilDead;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiScreenRubbingPaper extends GuiScreen {

	private static final ResourceLocation rubbingGuiTextures = new ResourceLocation(EvilDead.modid + ":" + "textures/gui/rubbing.png");

    private final ItemStack bookObj;
    /** Update ticks since the gui was opened */
    private int updateCount;
    private int bookImageWidth = 206;
    private int bookImageHeight = 200;
    
    
	public GuiScreenRubbingPaper(EntityPlayer player, ItemStack stack) {
		this.bookObj = stack;
	}
	
	 /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.updateCount;
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
    }
    
    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        byte localHeight = 8;
        int localWidth = (this.width - this.bookImageWidth + 20) / 2;
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(rubbingGuiTextures);
        this.drawTexturedModalRect(localWidth, localHeight, 0, 0, this.bookImageWidth, this.bookImageHeight);

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
