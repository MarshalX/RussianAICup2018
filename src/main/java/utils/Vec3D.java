package utils;


import java.lang.Math;


public class Vec3D {
    private double x;
    private double y;
    private double z;

    public Vec3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D(Vec3D vec) {
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
    }

    public Vec3D copy(){
        return new Vec3D(this);
    }

    public Vec3D add(Vec3D vec) {
        x += vec.getX();
        y += vec.getY();
        z += vec.getZ();

        return this;
    }

    public Vec3D add(double dx, double dy, double dz) {
        x += dx;
        y += dy;
        z += dz;

        return this;
    }

    public Vec3D sub(Vec3D vec) {
        x -= vec.getX();
        y -= vec.getY();
        z -= vec.getZ();

        return this;
    }

    public Vec3D sub(double dx, double dy, double dz) {
        x -= dx;
        y -= dy;
        z -= dz;

        return this;
    }

    public Vec3D mul(double scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;

        return this;
    }

    public double length() {
        return Math.hypot(Math.hypot(x, y), z);
    }

    public Vec3D normalize() {
        return this.mul((1 / this.length()));
    }

    public double squareDistance(Vec3D vec) {
        double tx = x - vec.x;
        double ty = y - vec.y;
        double tz = y - vec.z;

        return tx * tx + ty * ty + tz * tz;
    }

    public double squareDistance(double x, double y, double z) {
        double tx = this.x - x;
        double ty = this.y - y;
        double tz = this.z - z;

        return tx * tx + ty * ty + tz * tz;
    }

    public double squareLength() {
        return x * x + y * y + z * z;
    }

    public Vec3D reverse() {
        x = -x;
        y = -y;
        z = -z;

        return this;
    }

    public double dotProduct(Vec3D vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public Vec3D div(double scalar) {
        x /= scalar;
        y /= scalar;
        z /= scalar;

        return this;
    }

    public Vec3D copyFrom(Vec3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;

        return this;
    }

    @Override
    public String toString() {
        return "Vector (" + String.valueOf(x) + "; " + String.valueOf(y) + "; " + String.valueOf(z) + ")";
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
