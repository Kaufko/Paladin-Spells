public class BedrockAnchorEntity extends Entity {
    private int duration;

    public BedrockAnchorEntity(EntityType<?> type, Level level) {
        super(type, level);
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
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return position();
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction moveFunction) {
        passenger.setPos(getX(), getY(), getZ());
    }
}
