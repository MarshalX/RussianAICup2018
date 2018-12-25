package utils;


import java.lang.Math;


public class Vec3D {
    private final double x;
    private final double y;
    private final double z;

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
        return new Vec3D(x + vec.getX(), y + vec.getY(), z + vec.getZ());
    }

    public Vec3D add(double dx, double dy, double dz) {
        return new Vec3D(x + dx, y + dy, z + dz);
    }

    public Vec3D sub(Vec3D vec) {
        return new Vec3D(x - vec.getX(), y - vec.getY(), z - vec.getZ());
    }

    public Vec3D sub(double dx, double dy, double dz) {
        return new Vec3D(x - dx, y - dy, z - dz);
    }

    public Vec3D mul(double scalar) {
        return new Vec3D(x * scalar, y * scalar, z * scalar);
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
        return new Vec3D(-x, -y, -z);
    }

    public double dotProduct(Vec3D vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public Vec3D div(double scalar) {
        return new Vec3D(x / scalar, y / scalar, z / scalar);
    }

    @Override
    public String toString() {
        return "Vector (" + String.valueOf(x) + "; " + String.valueOf(y) + "; " + String.valueOf(z) + ")";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
