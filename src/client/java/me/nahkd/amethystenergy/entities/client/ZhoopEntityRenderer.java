package me.nahkd.amethystenergy.entities.client;

import org.joml.Quaternionf;

import me.nahkd.amethystenergy.AmethystEnergy;
import me.nahkd.amethystenergy.entities.ZhoopEntity;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ZhoopEntityRenderer extends EntityRenderer<ZhoopEntity> {
	private static final Identifier TEXTURE = AmethystEnergy.id("textures/entity/misc/zhoop.png");
	private ModelPart model;

	public ZhoopEntityRenderer(Context ctx) {
		super(ctx);

		var modelData = new ModelData();
		modelData.getRoot().addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create()
				.cuboid(0f, 0f, 0f, 16f, 0f, 16f)
				, ModelTransform.pivot(0, 0, 0));
		model = modelData.getRoot().createPart(32, 32);
	}

	@Override
	public Identifier getTexture(ZhoopEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(ZhoopEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		var bladeVel = entity.getVelocity().normalize();
		var bladePitch = Math.asin(-bladeVel.y);
		var bladeYaw = Math.atan2(bladeVel.x, bladeVel.z);// - (Math.PI / 4d);

		matrices.push();
		matrices.multiply(new Quaternionf()
				.rotateAxis((float) bladePitch, 0, 0, 1)
				.rotateAxis((float) -(Math.PI / 4d), 0, 1, 0)
				.rotateAxis((float) bladeYaw, 0, 1, 0));
		matrices.translate(-0.5f, 0f, -0.5f);

		model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityAlpha(TEXTURE)), 0xFF00FF, 0, 1f, 1f, 1f, 1f);
		matrices.pop();

		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
