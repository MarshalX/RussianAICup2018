package mymodel;


import model.Action;

import utils.Vec3D;


public class MyAction {
    private Action action;

    private Vec3D target_velocity;
    private double target_velocity_x;
    private double target_velocity_y;
    private double target_velocity_z;

    private double jump_speed;
    private boolean use_nitro;

    public MyAction() {
        target_velocity = new Vec3D(0, 0, 0);
        target_velocity_x = 0;
        target_velocity_y = 0;
        target_velocity_z = 0;
        jump_speed = 0;
        use_nitro = false;
    }

    public MyAction(Action action) {
        this.action = action;

        target_velocity_x = action.target_velocity_x;
        target_velocity_y = action.target_velocity_y;
        target_velocity_z = action.target_velocity_z;
        target_velocity = new Vec3D(target_velocity_x, target_velocity_y, target_velocity_z);
        jump_speed = action.jump_speed;
        use_nitro = action.use_nitro;
    }

    public Vec3D getTargetVelocity() {
        return target_velocity;
    }

    public void setTargetVelocity(Vec3D target_velocity) {
        this.target_velocity = target_velocity;
    }

    public double getTargetVelocityX() {
        return target_velocity_x;
    }

    public void setTargetVelocityX(double target_velocity_x) {
        this.target_velocity_x = target_velocity_x;
        action.target_velocity_x = target_velocity_x;
    }

    public double getTargetVelocityY() {
        return target_velocity_y;
    }

    public void setTargetVelocityY(double target_velocity_y) {
        this.target_velocity_y = target_velocity_y;
        action.target_velocity_y = target_velocity_y;
    }

    public double getTargetVelocityZ() {
        return target_velocity_z;
    }

    public void setTargetVelocityZ(double target_velocity_z) {
        this.target_velocity_z = target_velocity_z;
        action.target_velocity_z = target_velocity_z;
    }

    public double getJumpSpeed() {
        return jump_speed;
    }

    public void setJumpSpeed(double jump_speed) {
        this.jump_speed = jump_speed;
        action.jump_speed = jump_speed;
    }

    public boolean isUseNitro() {
        return use_nitro;
    }

    public void setUseNitro(boolean use_nitro) {
        this.use_nitro = use_nitro;
        action.use_nitro = use_nitro;
    }
}
