package me.nahkd.amethystenergy.entities.client.zhoop;

import org.joml.Quaternionf;

import me.nahkd.amethystenergy.entities.FlyingItemEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FlyingItemEntityRenderer extends EntityRenderer<FlyingItemEntity> {
	private Context ctx;

	public FlyingItemEntityRenderer(Context ctx) {
		super(ctx);
		this.ctx = ctx;
	}

	@Override
	public Identifier getTexture(FlyingItemEntity entity) {
		return new Identifier("minecraft", "missingno");
	}

	@Override
	public void render(FlyingItemEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		var stack = entity.getStack();
		if (stack.isEmpty()) return;

		matrices.push();
		matrices.multiply(new Quaternionf().rotateAxis((float) (entity.getTimeTicked() * Math.PI / 10f), 0, 1, 0));
		matrices.multiply(new Quaternionf().rotateAxis((float) (Math.PI / 2d), 1, 0, 0));
		ctx.getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light, light, matrices, vertexConsumers, entity.getWorld(), light);
		matrices.pop();
	}
}
