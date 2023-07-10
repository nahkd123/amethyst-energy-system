package me.nahkd.amethystenergy.entities.client;

import me.nahkd.amethystenergy.entities.AmethystEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class AESRenderers {
	public static void registerAll() {
		EntityRendererRegistry.register(AmethystEntities.ZHOOP, ZhoopEntityRenderer::new);
		EntityRendererRegistry.register(AmethystEntities.BOOMERANG, BoomerangEntityRenderer::new);
	}
}
