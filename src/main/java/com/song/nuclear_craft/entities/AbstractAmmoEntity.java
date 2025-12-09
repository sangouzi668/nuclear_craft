package com.song.nuclear_craft.entities;

import com.song.nuclear_craft.NuclearCraft;
import com.song.nuclear_craft.items.AbstractAmmo;
import com.song.nuclear_craft.items.Ammo.AmmoSize;
import com.song.nuclear_craft.items.Ammo.AmmoType;
import com.song.nuclear_craft.items.ItemList;
import com.song.nuclear_craft.misc.ConfigCommon;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class AbstractAmmoEntity extends ThrowableItemProjectile {
    private double energy;
    private double initSpeed;
    private double gravity = 0.03f;
    protected double baseDamage = 30;
    private int age = 0;
    private double bulletSize;
    private final IntOpenHashSet piercedEntities = new IntOpenHashSet(100);
    private boolean isMyImpact = false;
    private double initEnergy;
    
    private static final EntityDataAccessor<Float> CURRENT_MOTION_X = SynchedEntityData.defineId(AbstractAmmoEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> CURRENT_MOTION_Y = SynchedEntityData.defineId(AbstractAmmoEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> CURRENT_MOTION_Z = SynchedEntityData.defineId(AbstractAmmoEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> BULLET_SIZE = SynchedEntityData.defineId(AbstractAmmoEntity.class, EntityDataSerializers.FLOAT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CURRENT_MOTION_X, 0.f);
        this.entityData.define(CURRENT_MOTION_Y, 0.f);
        this.entityData.define(CURRENT_MOTION_Z, 0.f);
        this.entityData.define(BULLET_SIZE, 0.f);
    }

    public AbstractAmmoEntity(EntityType<? extends AbstractAmmoEntity> type, Level world) {
        super(type, world);
    }

    public AbstractAmmoEntity(PlayMessages.SpawnEntity entity, Level world) {
        this(EntityRegister.BULLET_ENTITY.get(), world);
        this.setDeltaMovement(entity.getVelX(), entity.getVelY(), entity.getVelZ());
        this.setRot(entity.getYaw(), entity.getPitch());
        this.setPos(entity.getPosX(), entity.getPosY(), entity.getPosZ());
    }

    public AbstractAmmoEntity(double x, double y, double z, Level world, ItemStack itemStack, Player shooter) {
        super(EntityRegister.BULLET_ENTITY.get(), x, y, z, world);
        this.setItem(itemStack);
        this.setOwner(shooter);
        this.bulletSize = ((AbstractAmmo) itemStack.getItem()).getSize().getSize();
        this.entityData.set(BULLET_SIZE, (float) bulletSize);
        this.setBaseDamage(((AbstractAmmo) itemStack.getItem()).getBaseDamage());
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void setBaseDamage(double baseDamage) {
        this.baseDamage = baseDamage;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    @Override
    public void tick() {
        double kEBefore = -1;
        if (age == 0) {
            initSpeed = this.getDeltaMovement().length();
            this.bulletSize = this.entityData.get(BULLET_SIZE);
            this.energy = getEnergy(initSpeed);
            initEnergy = this.energy;
        }

        if (level().isClientSide) {
            this.setDeltaMovement(this.entityData.get(CURRENT_MOTION_X), this.entityData.get(CURRENT_MOTION_Y), this.entityData.get(CURRENT_MOTION_Z));
        } else {
            Vec3 vector3d = this.getDeltaMovement();
            this.entityData.set(CURRENT_MOTION_X, (float) vector3d.x);
            this.entityData.set(CURRENT_MOTION_Y, (float) vector3d.y);
            this.entityData.set(CURRENT_MOTION_Z, (float) vector3d.z);
        }

        isMyImpact = true;
        if (!level().isClientSide) {
            // Fix 1: Use correct getHitResult method - simplified version
            Vec3 startVec = this.position();
            Vec3 endVec = startVec.add(this.getDeltaMovement());
            Predicate<Entity> filter = entity -> !entity.isSpectator() && entity.isAlive() && entity.isPickable() && canHitEntity(entity);
            
            // Use a simpler method
            HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, filter);
            
            while (!this.isRemoved() && raytraceresult != null) {
                raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, filter);

                if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult) raytraceresult).getEntity();
                    Entity indirect = this.getOwner();
                    if (entity instanceof Player && indirect instanceof Player && !((Player) indirect).canHarmPlayer((Player) entity)) {
                        raytraceresult = null;
                    }
                }

                if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                }
                if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.MISS) {
                    break;
                }
            }
        }
        isMyImpact = false;

        super.tick();

        if (this.energy <= 0) {
            this.discard();
        }
        this.setDeltaMovement(this.getDeltaMovement().add(0, -gravity, 0));

        this.age++;
        
        if (this.age >= 1000) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult entityRayTraceResult) {
        Entity entity = entityRayTraceResult.getEntity();
        this.piercedEntities.add(entity.getId());

        if (entity instanceof ItemEntity || entity instanceof AbstractAmmoEntity) {
            return;
        }

        if (entity instanceof LivingEntity) {
            entity.invulnerableTime = 0;
        }

        double damage = this.baseDamage * getEnergy(this.getDeltaMovement().length()) / this.initEnergy;
        // Fix 2: Use correct DamageSource creation method
        Entity owner = this.getOwner();
        DamageSource damageSource;
        if (owner instanceof LivingEntity) {
            damageSource = this.damageSources().mobProjectile(this, (LivingEntity) owner);
        } else {
            damageSource = this.damageSources().indirectMagic(this, owner);
        }
        boolean result = entity.hurt(damageSource, (float) damage);

        if (result) {
            this.energy -= 30;
        } else {
            this.energy -= 10;
        }
    }

    @Override
    protected void onHit(HitResult result) {
        double kEBefore = this.energy;
        if (isMyImpact && !level().isClientSide) {
            super.onHit(result);
        }
        if (this.energy <= 0) {
            this.discard();
        }
        double factor = Math.sqrt(this.energy / kEBefore);
        this.setDeltaMovement(this.getDeltaMovement().multiply(factor, factor, factor));
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && (this.piercedEntities == null || !this.piercedEntities.contains(entity.getId()));
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult blockRayTraceResult) {
        BlockState blockState = level().getBlockState(blockRayTraceResult.getBlockPos());
        double blastResist = blockState.getExplosionResistance(level(), blockRayTraceResult.getBlockPos(), null);
        if (blastResist > getBlockBreakThreshold() + 1e-3) {
            Direction blockDirection = blockRayTraceResult.getDirection();
            this.ricochetSpeed(blockDirection);
            teleportToHitPoint(blockRayTraceResult);
            this.energy -= this.initEnergy * getRicochetEnergyLoss();
        } else {
            level().destroyBlock(blockRayTraceResult.getBlockPos(), true);
            this.energy -= getEnergyLoss(blastResist);
        }
        this.piercedEntities.clear();
    }

    protected void teleportToHitPoint(HitResult rayTraceResult) {
        Vec3 hitResult = rayTraceResult.getLocation();
        this.setPos(hitResult.x, hitResult.y, hitResult.z);
    }

    public double getRicochetEnergyLoss() {
        return 0.5d;
    }

    public double getBlockBreakThreshold() {
        return ConfigCommon.AMMO_BLOCK_BREAK_THRESHOLD.get();
    }

    public double getEnergyLoss(double blastResist) {
        return 25 + 10 * (blastResist - 2);
    }

    private void ricochetSpeed(Direction direction) {
        switch (direction) {
            case UP:
            case DOWN:
                this.setDeltaMovement(this.getDeltaMovement().multiply(1, -1, 1));
                break;
            case EAST:
            case WEST:
                this.setDeltaMovement(this.getDeltaMovement().multiply(-1, 1, 1));
                break;
            case NORTH:
            case SOUTH:
                this.setDeltaMovement(this.getDeltaMovement().multiply(1, 1, -1));
                break;
            default:
                break;
        }
    }

    protected double getEnergy(double speed) {
        return 0.5 * speed * speed * (this.bulletSize / 9);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemList.AMMO_REGISTRIES_TYPE.get(AmmoSize.SIZE_9MM).get(AmmoType.NORMAL).get();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}