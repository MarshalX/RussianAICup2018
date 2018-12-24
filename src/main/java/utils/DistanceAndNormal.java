package utils;


public class DistanceAndNormal {
    private double distance;
    private Vec3D normal;

    public DistanceAndNormal() {
        distance = 0;
        normal = new Vec3D();
    }

    public DistanceAndNormal(double distance, Vec3D normal) {
        this.distance = distance;
        this.normal = normal;
    }

    public DistanceAndNormal min(DistanceAndNormal dan) {
        return this.distance < dan.getDistance() ? this : dan;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Vec3D getNormal() {
        return normal;
    }

    public void setNormal(Vec3D normal) {
        this.normal = normal;
    }
}
