package net.evildead.mod.gui;

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


public class GuiScreenNecroBook extends GuiScreen{

//    private static final ResourceLocation bookGuiTextures = new ResourceLocation(EvilDead.modid + ":" + "textures/gui/NecroBook.png");
//    private static final ResourceLocation origGuiTextures = new ResourceLocation(EvilDead.modid + ":" + "textures/gui/book.png");
	
	private static final ResourceLocation bookLeftGuiTextures = new ResourceLocation(EvilDead.modid + ":" + "textures/gui/bookleft.png");
	private static final ResourceLocation bookRightGuiTextures = new ResourceLocation(EvilDead.modid + ":" + "textures/gui/bookright.png");

    private final ItemStack bookObj;
    /** Update ticks since the gui was opened */
    private int updateCount;
    private int bookImageWidth = 206;
    private int bookImageHeight = 200;
    private int bookTotalPages = 6;
    private int currPage;
    private NBTTagList bookPages;
    private String bookTitle = "Book of the Dead";
    

    private GuiScreenNecroBook.NextPageButton buttonNextPage;
    private GuiScreenNecroBook.NextPageButton buttonPreviousPage;

	private boolean finalizing;
    
    
	
	public GuiScreenNecroBook(EntityPlayer player, ItemStack stack) {
        this.bookObj = stack;
        
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();
            this.bookPages = nbttagcompound.getTagList("pages", 8);

            if (this.bookPages != null)
            {
                this.bookPages = (NBTTagList)this.bookPages.copy();
                this.bookTotalPages = this.bookPages.tagCount();

                if (this.bookTotalPages < 1)
                {
                    this.bookTotalPages = 1;
                }
            }
        }
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
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        
        int xPos = this.width / 2;

        this.buttonList.add(this.buttonNextPage = new GuiScreenNecroBook.NextPageButton(1, xPos + this.bookImageWidth - 50, 180, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiScreenNecroBook.NextPageButton(2, xPos - this.bookImageWidth + 24, 180, false));
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
        this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1;
        this.buttonPreviousPage.visible = this.currPage > 0;
        //this.buttonDone.visible = true;
    }
    
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                if (this.currPage < this.bookTotalPages - 1)
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
        }
    }
    
    
    
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bookRightGuiTextures);
        int localWidth = this.width / 2;
        byte localHeight = 8;
        this.drawTexturedModalRect(localWidth, localHeight, 0, 0, this.bookImageWidth, this.bookImageHeight);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bookLeftGuiTextures);
        localWidth = localWidth - this.bookImageWidth;
        this.drawTexturedModalRect(localWidth, localHeight, 256 - this.bookImageWidth, 0, this.bookImageWidth, this.bookImageHeight);
        

        /* Page Numbers */
//        String s;
//        String s1;
//        int l;
//        
//        s = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages)});
//        s1 = "";
//
//        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
//        {
//            s1 = this.bookPages.getStringTagAt(this.currPage);
//        }
//
//        l = this.fontRendererObj.getStringWidth(s);
//        this.fontRendererObj.drawString(s, localWidth - l + this.bookImageWidth - 44, localHeight + 16, 0);
//        this.fontRendererObj.drawSplitString(s1, localWidth + 36, localHeight + 16 + 16, 116, 0);

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
    
    
    
    
    
    
    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean isNextButton;		// as opposed to previous
        private static final String __OBFID = "CL_00000745";

        public NextPageButton(int id, int xPosition, int yPosition, boolean isNextButton)
        {
            super(id, xPosition, yPosition, 23, 13, "");
            this.isNextButton = isNextButton;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft client, int x, int y)
        {
            if (this.visible)
            {
                boolean flag = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                client.getTextureManager().bindTexture(GuiScreenNecroBook.bookLeftGuiTextures);
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
