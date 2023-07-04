package me.nahkd.amethystenergy.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.world.World;

public class ZhoopEntity extends ProjectileEntity {
	public static final int MAX_TICK = 30;
	public static final double BLOCKS_PER_SEC = 30d;

	private int timeTicked = 0;
	private float bladeDamage = 1f;

	public ZhoopEntity(EntityType<? extends ZhoopEntity> entityType, World world) {
		super(entityType, world);
	}

	public ZhoopEntity(World world) {
		super(AmethystEntities.ZHOOP, world);
	}

	public ZhoopEntity(World world, LivingEntity owner) {
		this(world, owner.getX(), owner.getEyeY() - (double) 0.1f, owner.getZ());
		setOwner(owner);
	}

	public ZhoopEntity(World world, double x, double y, double z) {
		super(AmethystEntities.ZHOOP, world);
		setPos(x, y, z);
	}

	@Override
	protected void initDataTracker() {
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		if (isOwner(entityHitResult.getEntity())) return;

		kill();
		DamageSource damageSource = getDamageSources().indirectMagic(this, getOwner());
		entityHitResult.getEntity().damage(damageSource, bladeDamage);
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		kill();
	}

	public int getTimeTicked() {
		return timeTicked;
	}

	public void setTimeTicked(int timeTicked) {
		this.timeTicked = timeTicked;
	}

	public float getBladeDamage() {
		return bladeDamage;
	}

	public void setBladeDamage(float bladeDamage) {
		this.bladeDamage = bladeDamage;
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("TimeTicked", timeTicked);
		nbt.putFloat("BladeDamage", bladeDamage);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		timeTicked = nbt.getInt("TimeTicked");
		bladeDamage = nbt.getFloat("BladeDamage");
	}

	@Override
	public void tick() {
		super.tick();
		move(MovementType.SELF, getVelocity().normalize().multiply(BLOCKS_PER_SEC / 20d));

		// Collision check
		var hitResult = ProjectileUtil.getCollision(this, this::canHit);
		if (hitResult.getType() != Type.MISS) {
			onCollision(hitResult);
		}

		getWorld().addParticle(ParticleTypes.SWEEP_ATTACK, true, getX(), getY(), getZ(), 0, 0, 0);
		if (timeTicked++ > MAX_TICK) kill();
	}
}
