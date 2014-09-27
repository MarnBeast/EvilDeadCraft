package net.evildead.mod.particle;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityDropBloodParticleFX extends EntityFX
{
    /** The height of the current bob */
    private int bobTimer;
    private static final String __OBFID = "CL_00000901"; 
    
    private boolean checkGround = false;		// ignore check for ground until this becomes true
    // basically, if we start inside a block, continue dripping till we leave the block. Once we've
    // dripped out of the block, start checking for the ground again.

    public EntityDropBloodParticleFX(World world, double x, double y, double z)
    {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = this.motionY = this.motionZ = 0.0D;
        
        this.particleRed = 0.7F;
        this.particleGreen = 0.0F;
        this.particleBlue = 0.0F;
        this.setParticleTextureIndex(113);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.bobTimer = 40;
        this.particleMaxAge = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
        this.motionX = this.motionY = this.motionZ = 0.0D;
    }
    
    public void renderParticle(Tessellator tess, float partialTicks, float par3, float par4, float par5, float par6, float par7) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(EvilDead.particleTextures);
        super.renderParticle(tess, partialTicks, par3, par4, par5, par6, par7);
        //Minecraft.getMinecraft().getTextureManager().bindTexture(EvilDead.defaultParticleTextures);
    }
    

    public int getBrightnessForRender(float p_70070_1_)
    {
        return super.getBrightnessForRender(p_70070_1_);
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float p_70013_1_)
    {
        return super.getBrightness(p_70013_1_);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.particleRed = 0.7F;
        this.particleGreen = 0.1F;
        this.particleBlue = 0.2F;

        this.motionY -= (double)this.particleGravity;

        if (this.bobTimer-- > 0)
        {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
            this.setParticleTextureIndex(113);
        }
        else
        {
            this.setParticleTextureIndex(112);
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.particleMaxAge-- <= 0)
        {
            this.setDead();
        }

        if (this.onGround && this.checkGround)
        {
            this.setDead();
            //this.worldObj.spawnParticle("splash", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            
            EntitySplashFX suspended = new EntitySplashFX(this.worldObj, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        	suspended.setRBGColorF(1.0F, 0.2F, 0.2F);
        	Minecraft.getMinecraft().effectRenderer.addEffect(suspended);
            
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
        else if(!this.onGround) {
        	this.checkGround = true;
        }

        Material material = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = (double)((float)(MathHelper.floor_double(this.posY) + 1) - BlockLiquid.getLiquidHeightPercent(this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))));

            if (this.posY < d0)
            {
                this.setDead();
            }
        }
    }
}