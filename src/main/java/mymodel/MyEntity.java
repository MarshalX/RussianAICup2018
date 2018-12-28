package mymodel;


import utils.Vec3D;


public class MyEntity {
    private MyAction action;
    private Vec3D position;
    private Vec3D velocity;
    private double radius;

    public double MASS;
    public double ARENA_E;

    public MyEntity(Vec3D position, Vec3D velocity, double radius) {
        this.action = new MyAction();
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
    }

    public MyEntity(MyAction action, Vec3D position, Vec3D velocity, double radius) {
        this(position, velocity, radius);

        this.action = action;
    }

    public MyAction getAction() {
        return action;
    }

    public void setAction(MyAction action) {
        this.action = action;
    }

    public Vec3D getPosition() {
        return position;
    }

    public void setPosition(Vec3D position) {
        this.position = position;
    }


    public Vec3D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec3D velocity) {
        this.velocity = velocity;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
