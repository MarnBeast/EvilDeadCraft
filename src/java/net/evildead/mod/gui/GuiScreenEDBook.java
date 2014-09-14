package net.evildead.mod.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;


public class GuiScreenEDBook extends GuiScreen{

	private ResourceLocation bookLeftGuiTextures = null;
	private ResourceLocation bookRightGuiTextures = null;
	private ResourceLocation buttonGuiTextures = null;
	private ResourceLocation pageLeftGuiTextures = null;
	private ResourceLocation pageRightGuiTextures = null;

    private final ItemStack bookObj;
    /** Update ticks since the gui was opened */
    private int updateCount;
    private int bookImageWidth;//206;
    private int bookImageHeight;
    private int currPage;
    
    private ArrayList<ResourceLocation> pages = new ArrayList<ResourceLocation>();

    private GuiScreenEDBook.NextPageButton buttonNextPage;
    private GuiScreenEDBook.NextPageButton buttonPreviousPage;
    
    private boolean showPageNumbers = false;
    
    
	
	public GuiScreenEDBook(EntityPlayer player, ItemStack stack) {
        this.bookObj = stack;
	}
	

	/**
	 * Sets the texture for the right side of the book.
	 * 
	 * Don't forget to call withBookImageDimensions as well if needed!
	 * @param loc A ResourceLocation with the path to the texture.
	 * @return An instance of this GuiScreenEDBook object.
	 */
	public GuiScreenEDBook withLeftBookTextures(ResourceLocation loc)
	{
		bookLeftGuiTextures = loc;
		return this;
	}

	/**
	 * Sets the texture for the right side of the book.
	 * 
	 * Don't forget to call withBookImageDimensions as well if needed!
	 * @param loc A ResourceLocation with the path to the texture.
	 * @return An instance of this GuiScreenEDBook object.
	 */
	public GuiScreenEDBook withRightBookTextures(ResourceLocation loc)
	{
		bookRightGuiTextures = loc;
		return this;
	}
	
	public GuiScreenEDBook withBookImageDimensions(int width, int height)
	{
		bookImageWidth = width;
		bookImageHeight = height;
		return this;
	}

	public GuiScreenEDBook withButtonTextures(ResourceLocation loc)
	{
		buttonGuiTextures = loc;
		this.initButtons();
		return this;
	}
	
	public GuiScreenEDBook withPageNumbersDisplayed()
	{
		this.showPageNumbers = true;
		return this;
	}
	
	public GuiScreenEDBook withoutPageNumbersDisplayed()
	{
		this.showPageNumbers = false;
		return this;
	}
	
	public GuiScreenEDBook addPage(ResourceLocation pageTextures)
	{
		pages.add(pageTextures);
        this.updatePages();
		return this;
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
        
        initButtons();
        updatePages();
    }
    
    public void initButtons()
    {
        this.buttonList.clear();
        
        if(buttonGuiTextures != null)
        {
	        int xPos = this.width / 2;
	
	        this.buttonList.add(this.buttonNextPage = 
	        		new GuiScreenEDBook.NextPageButton(1, xPos + this.bookImageWidth - 50, 180, true)
	        		.withButtonTexture(buttonGuiTextures));
	        this.buttonList.add(this.buttonPreviousPage
	        		= new GuiScreenEDBook.NextPageButton(2, xPos - this.bookImageWidth + 24, 180, false)
	        		.withButtonTexture(buttonGuiTextures));
        }
        
        this.updateButtons();
    }
    
    
    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    private void updateButtons()
    {
        this.buttonNextPage.visible = this.currPage < this.pages.size() - 2;
        this.buttonPreviousPage.visible = this.currPage > 0;
    }
    
    private void updatePages()
    {
    	this.pageLeftGuiTextures = (pages.size() > currPage) ? pages.get(currPage) : null;
    	this.pageRightGuiTextures = (pages.size() > currPage + 1) ? pages.get(currPage + 1) : null;
    }
    
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                if (this.currPage < this.pages.size() - 1)
                {
                    this.currPage += 2;
                }
            }
            else if (button.id == 2)
            {
                if (this.currPage > 0)
                {
                    this.currPage -= 2;
                }
            }

            this.updateButtons();
            this.updatePages();
        }
    }
    
    
    
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        byte localHeight = 8;
        int localWidth = this.width / 2;
        
        if(bookRightGuiTextures != null) {
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        this.mc.getTextureManager().bindTexture(bookRightGuiTextures);
	        this.drawTexturedModalRect(localWidth, localHeight, 0, 0, this.bookImageWidth, this.bookImageHeight);
        }
        
        if(pageRightGuiTextures != null) {
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        this.mc.getTextureManager().bindTexture(pageRightGuiTextures);
	        this.drawTexturedModalRect(localWidth, localHeight, 0, 0, this.bookImageWidth, this.bookImageHeight);
        }

        localWidth = localWidth - this.bookImageWidth;
        
        if(bookLeftGuiTextures != null) {
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        this.mc.getTextureManager().bindTexture(bookLeftGuiTextures);
	        this.drawTexturedModalRect(localWidth, localHeight, 256 - this.bookImageWidth, 0, this.bookImageWidth, this.bookImageHeight);
		}
        
        if(pageLeftGuiTextures != null) {
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        this.mc.getTextureManager().bindTexture(pageLeftGuiTextures);
	        this.drawTexturedModalRect(localWidth, localHeight, 256 - this.bookImageWidth, 0, this.bookImageWidth, this.bookImageHeight);
        }
        

        /* Page Numbers */
        if(this.showPageNumbers){
	        String s1 = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf(this.currPage + 1), Integer.valueOf(this.pages.size())});
	        String s2 = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf(this.currPage + 2), Integer.valueOf(this.pages.size())});
	        
	        int l1 = this.fontRendererObj.getStringWidth(s1);
	        int l2 = this.fontRendererObj.getStringWidth(s2);
	        
	        // localWidth + this.bookImageWidth gets us exactly halfway across the gui.
	        // -+ this.bookImageWidth/2 gets us exactly halfway across the left or right page.
	        // - l1/2 and l2/2 centers our string exactly halfway across the left or right page.
	        this.fontRendererObj.drawString(s1, localWidth + this.bookImageWidth - this.bookImageWidth/2 - l1/2, localHeight + 16, 0);
	        this.fontRendererObj.drawString(s2, localWidth + this.bookImageWidth + this.bookImageWidth/2 - l2/2, localHeight + 16, 0);
        }

        
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
    
    
    
    
    
    
    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean isNextButton;		// as opposed to previous
        private static final String __OBFID = "CL_00000745";
        
        private ResourceLocation buttonTexture;

        public NextPageButton(int id, int xPosition, int yPosition, boolean isNextButton)
        {
            super(id, xPosition, yPosition, 23, 13, "");
            this.isNextButton = isNextButton;
        }
        
        public NextPageButton withButtonTexture(ResourceLocation buttonTexture)
        {
        	this.buttonTexture = buttonTexture;
        	return this;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft client, int x, int y)
        {
            if (this.visible && buttonTexture != null)
            {
                boolean flag = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                client.getTextureManager().bindTexture(buttonTexture);
                int k = 0;
                int l = 192;

                if (flag)
                {
                    k += 23;
                }

                if (!this.isNextButton)
                {
                    l += 13;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
            }
        }
    }

}
