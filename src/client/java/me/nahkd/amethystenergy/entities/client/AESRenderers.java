package me.nahkd.amethystenergy.entities.client;

import me.nahkd.amethystenergy.entities.AmethystEntities;
import me.nahkd.amethystenergy.entities.client.zhoop.FlyingItemEntityRenderer;
import me.nahkd.amethystenergy.entities.client.zhoop.ZhoopEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class AESRenderers {
	public static void registerAll() {
		EntityRendererRegistry.register(AmethystEntities.ZHOOP, ZhoopEntityRenderer::new);
		EntityRendererRegistry.register(AmethystEntities.FLYING_ITEM, FlyingItemEntityRenderer::new);
	}
}
