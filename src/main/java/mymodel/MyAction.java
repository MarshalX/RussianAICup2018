package mymodel;


import model.Action;

import utils.Vec3D;


public class MyAction {
    private Action action;

    private Vec3D targetVelocity;

    private double jumpSpeed;
    private boolean useNitro;

    public MyAction() {
        targetVelocity = new Vec3D(0, 0, 0);
        jumpSpeed = 0;
        useNitro = false;
    }

    public MyAction(Action action) {
        this.action = action;

        targetVelocity = new Vec3D(action.target_velocity_x, action.target_velocity_y, action.target_velocity_z);
        jumpSpeed = action.jump_speed;
        useNitro = action.use_nitro;
    }

    public Vec3D getTargetVelocity() {
        return targetVelocity;
    }

    public void setTargetVelocity(Vec3D targetVelocity) {
        this.targetVelocity = targetVelocity;

        action.target_velocity_x = targetVelocity.getX();
        action.target_velocity_y = targetVelocity.getY();
        action.target_velocity_z = targetVelocity.getZ();
    }

    public double getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(double jumpSpeed) {
        this.jumpSpeed = jumpSpeed;

        action.jump_speed = jumpSpeed;
    }

    public boolean isUseNitro() {
        return useNitro;
    }

    public void setUseNitro(boolean useNitro) {
        this.useNitro = useNitro;

        action.use_nitro = useNitro;
    }
}
