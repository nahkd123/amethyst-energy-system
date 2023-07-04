package me.nahkd.amethystenergy.entities.client;

import me.nahkd.amethystenergy.entities.ZhoopEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;

public class ZhoopEntityRenderer extends EntityRenderer<ZhoopEntity> {
	protected ZhoopEntityRenderer(Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTexture(ZhoopEntity entity) {
		return new Identifier("amethystenergy", "textures/entity/misc/zhoop.png");
	}
}
