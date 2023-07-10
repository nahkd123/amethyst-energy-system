package me.nahkd.amethystenergy.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.world.World;

public class BoomerangEntity extends ProjectileEntity {
	private static final TrackedData<ItemStack> STACK = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final int FLYING_TIME = 20;

	private int timeTicked = 0;
	private float damage = 1f;

	public BoomerangEntity(EntityType<? extends BoomerangEntity> type, World world) {
		super(type, world);
		setStack(ItemStack.EMPTY);
	}

	public BoomerangEntity(World world) {
		this(AmethystEntities.BOOMERANG, world);
	}

	public BoomerangEntity(World world, double x, double y, double z) {
		this(world);
		setPos(x, y, z);
	}

	public BoomerangEntity(World world, Entity owner) {
		this(world, owner.getX(), owner.getEyeY(), owner.getZ());
		setOwner(owner);
	}

	public BoomerangEntity(World world, ItemStack stack) {
		this(world);
		setStack(stack);
	}

	public int getTimeTicked() { return timeTicked; }
	public void setTimeTicked(int timeTicked) { this.timeTicked = timeTicked; }

	public float getDamage() { return damage; }
	public void setDamage(float damage) { this.damage = damage; }

	public ItemStack getStack() {
		return getDataTracker().get(STACK);
	}

	public void setStack(ItemStack stack) {
		if (stack == null || stack.isEmpty()) getDataTracker().set(STACK, ItemStack.EMPTY);
		else getDataTracker().set(STACK, stack);
	}

	@Override
	protected void initDataTracker() {
		getDataTracker().startTracking(STACK, ItemStack.EMPTY);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		timeTicked = nbt.getInt("TimeTicked");
		setStack(nbt.isEmpty()? ItemStack.EMPTY : ItemStack.fromNbt(nbt.getCompound("Item")));
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		var stack = getDataTracker().get(STACK);

		super.writeCustomDataToNbt(nbt);
		nbt.putInt("TimeTicked", timeTicked);
		nbt.put("Item", stack.isEmpty()? new NbtCompound() : stack.writeNbt(new NbtCompound()));
	}

	@Override
	public void tick() {
		super.tick();
		timeTicked++;

		var owner = getOwner();
		if (owner == null) return;
		move(MovementType.SELF, getVelocity());

		var targetDist = timeTicked < FLYING_TIME? (timeTicked / 20f) : (1 - (timeTicked - FLYING_TIME) / 20f);
		targetDist = Math.max(Math.min(targetDist, 1.0f), 0.0f);
		var targetPos = owner.getRotationVector().multiply(targetDist * 20f).add(owner.getEyePos());

		var vel = targetPos.subtract(getPos()).multiply(1.0 - targetDist);
		setVelocity(vel);
		velocityDirty = true;

		var hitResult = ProjectileUtil.getCollision(this, this::canHit);
		if (hitResult.getType() != Type.MISS) {
			// Stuck here!
			onCollision(hitResult);
		}

		if (timeTicked > FLYING_TIME * 2 + 3) {
			kill();
			var ie = new ItemEntity(getWorld(), getPos().x, getPos().y, getPos().z, getStack());
			ie.addVelocity(getVelocity());
			getWorld().spawnEntity(ie);
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if (isOwner(entityHitResult.getEntity())) {
			if (timeTicked < FLYING_TIME) return;

			kill();
			if (getOwner() instanceof LivingEntity living) {
				if (living instanceof PlayerEntity player) {
					if (!player.getInventory().insertStack(getStack())) player.dropItem(getStack(), true);
				} else {
					living.setStackInHand(living.getActiveHand(), getStack());
				}
			}
		} else {
			entityHitResult.getEntity().damage(getWorld().getDamageSources().indirectMagic(this, getOwner()), damage);
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		kill();

		var ie = new ItemEntity(getWorld(), getPos().x, getPos().y, getPos().z, getStack());
		ie.addVelocity(getVelocity());
		getWorld().spawnEntity(ie);
	}
}
