package eu.kaufko.paladin_spells.entity.spells.BedrockSkin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BedrockSkinEntity extends Entity {
    private int duration;

    public BedrockSkinEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
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
                discard();
            }
        }
    }

    @Override
    public void push(double x, double y, double z) {}

    @Override
    public void move(MoverType type, Vec3 pos) {}

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return position();
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction moveFunction) {
        moveFunction.accept(passenger, getX(), getY(), getZ());
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
}