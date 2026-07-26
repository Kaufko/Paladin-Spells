package eu.kaufko.paladin_spells.entity.spells.BedrockSkin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BedrockSkinEntity extends Entity {
    private int duration;

    public BedrockSkinEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.setInvulnerable(true);
        this.blocksBuilding = false;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            if (tickCount > duration || !isVehicle()) {
                ejectPassengers();
                discard();
            }
        }
        if (!this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.08, 0)); // standard gravity accel
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0, 1)); // stop falling once grounded
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
    }
    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        return this.position();
    }

    @Override
    public void push(double x, double y, double z) {}

    @Override
    public void move(MoverType type, Vec3 pos) {
        super.move(type, new Vec3(0, pos.y, 0));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction moveFunction) {
        moveFunction.accept(passenger, getX(), getY(), getZ());
    }

    @Override
    public boolean hasIndirectPassenger(Entity pEntity) {
        return true;
    }


    @Override
    protected void defineSynchedData() {
        // No synced fields needed
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.duration = tag.getInt("Duration");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Duration", this.duration);
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean dismountsUnderwater() {
        return false;
    }





}