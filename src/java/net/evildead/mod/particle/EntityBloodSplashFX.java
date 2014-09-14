package net.evildead.mod.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.evildead.mod.EvilDead;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityBloodSplashFX extends EntityBloodRainFX
{
    private static final String __OBFID = "CL_00000927";

    public EntityBloodSplashFX(World world, double x, double y, double z, double var1, double var2, double var3)
    {
        super(world, x, y, z);
        this.particleGravity = 0.04F;
        this.nextTextureIndexX();

        if (var2 == 0.0D && (var1 != 0.0D || var3 != 0.0D))
        {
            this.motionX = var1;
            this.motionY = var2 + 0.1D;
            this.motionZ = var3;
        }
    }
    
    // renderParticle override done by EntityBloodRainFX
}