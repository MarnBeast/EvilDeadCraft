package net.evildead.common;

import net.evildead.mod.entity.EntityPossessed;
import net.evildead.mod.renderer.RenderPossessed;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class EDProxyClient extends EDProxyCommon{
    
    public void registerRenderThings() {
    	
    	// Entities
    	RenderingRegistry.registerEntityRenderingHandler(EntityPossessed.class, new RenderPossessed(new ModelBiped(), 0));
    }
    
    public void registerTileEntitySpecialRenderer() {
    	
    }
}
