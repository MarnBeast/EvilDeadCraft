package net.evildead.mod.renderer;

import net.evildead.mod.EvilDead;
import net.evildead.mod.entity.EntityPossessed;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderPossessed extends RenderLiving {
	
	private static final ResourceLocation texture = new ResourceLocation(EvilDead.modid + ":" + "textures/model/Possessed.png");
	
	protected ModelBiped modelEntity;

	public RenderPossessed(ModelBase p_i1262_1_, float p_i1262_2_) {
		super(p_i1262_1_, p_i1262_2_);
		modelEntity = ((ModelBiped) mainModel);
	}
	
	public void renderPossessed(EntityPossessed entity, double x, double y, double z, float u, float v) {
		super.doRender(entity, x, y, z, u, v);
	}
	
	public void doRenderLiving(EntityLiving entityLiving, double x, double y, double z, float u, float v) {
		renderPossessed((EntityPossessed) entityLiving, x, y, z, u, v);
	}
	
	public void doRender(Entity entity, double x, double y, double z, float u, float v) {
		renderPossessed((EntityPossessed) entity, x, y, z, u ,v);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return texture;
	}

}
