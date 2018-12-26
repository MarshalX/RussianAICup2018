package utils;


import mymodel.*;


public class Simulation {
    private MyRules rules;
    private MyGame game;

    public Simulation(MyRules rules, MyGame game) {
        this.rules = rules;
        // TODO: copy of object...
        this.game = game;
    }

    private Vec3D clamp(Vec3D vec, double max) {
        if (vec.length() > max) {
            return vec.normalize().mul(max);
        }
        return vec;
    }

    private double clamp(double min, double x, double max) {
        return Math.max(min, Math.min(x, max));
    }

    private void collideEntities(MyEntity a, MyEntity b) {
        Vec3D deltaPosition = b.getPosition().sub(a.getPosition());
        double distance = deltaPosition.length();
        double penetration = a.getRadius() + b.getRadius() - distance;

        if (penetration > 0) {
            double kA = (1 / a.MASS) / ((1 / a.MASS) + (1 / b.MASS));
            double kB = (1 / b.MASS) / ((1 / a.MASS) + (1 / b.MASS));

            Vec3D normal = deltaPosition.normalize();

            Vec3D newPosition = a.getPosition().sub(normal.mul(penetration).mul(kA));
            a.setPosition(newPosition);
            newPosition = b.getPosition().add(normal.mul(penetration).mul(kB));
            b.setPosition(newPosition);

            double deltaVelocity = b.getVelocity().sub(a.getVelocity()).dotProduct(normal)
                    + b.getAction().getJumpSpeed() + a.getAction().getJumpSpeed();
            if (deltaVelocity < 0) {
                Vec3D impulse = normal.mul(1 + rules.AVG_HIT_E).mul(deltaVelocity);

                Vec3D newVelocity = a.getVelocity().add(impulse.mul(kA));
                a.setVelocity(newVelocity);
                newVelocity = b.getVelocity().sub(impulse.mul(kB));
                a.setVelocity(newVelocity);
            }
        }
    }

    private Vec3D collideWithArena(MyEntity e) {
        DistanceAndNormal dan = danToArena(e.getPosition());
        double penetration = e.getRadius() - dan.getDistance();
        if (penetration > 0) {
            Vec3D newPosition = e.getPosition().add(dan.getNormal().mul(penetration));
            e.setPosition(newPosition);

            double velocity = e.getVelocity().dotProduct(dan.getNormal()) - e.getAction().getJumpSpeed();
            if (velocity < 0) {
                Vec3D newVelocity = e.getVelocity().sub(dan.getNormal().mul(1 + e.ARENA_E).mul(velocity));
                e.setVelocity(newVelocity);

                return dan.getNormal();
            }
        }
        return null;
    }

    private void move(MyEntity e, double deltaTime) {
        Vec3D newVel = clamp(e.getVelocity(), rules.MAX_ENTITY_SPEED);
        Vec3D newPos = e.getPosition().add(newVel.mul(deltaTime));

        newVel = new Vec3D(newVel.getX(), newVel.getY() - rules.GRAVITY * deltaTime, newVel.getZ());
        newPos = new Vec3D(newPos.getX(), newPos.getY() - rules.GRAVITY * deltaTime * deltaTime / 2, newPos.getZ());

        e.setVelocity(newVel);
        e.setPosition(newPos);
    }

    private void update(double deltaTime) {
//        shaffle(game.getRobots());
        // TODO...
    }

    private void tick() {
        double deltaTime = 1 / rules.TICKS_PER_SECOND;

        for(int i = 0; i < rules.MICROTICKS_PER_TICK; ++i) {
            update(deltaTime / rules.MICROTICKS_PER_TICK);
        }

        for (MyNitroPack pack: game.getNitroPacks()) {
            if (pack.isAlive()) {
                continue;
            }

            pack.setRespawnTicks(pack.getRespawnTicks() - 1);

            if (pack.getRespawnTicks() == 0) {
                pack.setAlive(true);
            }
        }
    }

    private DistanceAndNormal danToPlane(Vec3D point, Vec3D pointOnPlane, Vec3D planeNormal) {
        return new DistanceAndNormal(point.sub(pointOnPlane).dotProduct(planeNormal), planeNormal);
    }

    private DistanceAndNormal danToSphereInner(Vec3D point, Vec3D sphereCenter, double sphereRadius) {
        double distance = sphereRadius - point.sub(sphereCenter).length();
        Vec3D normal = sphereCenter.sub(point).normalize();

        return new DistanceAndNormal(distance, normal);
    }

    private DistanceAndNormal danToSphereOuter(Vec3D point, Vec3D sphereCenter, double sphereRadius) {
        double distance = point.sub(sphereCenter).length() - sphereRadius;
        Vec3D normal = point.sub(sphereCenter).normalize();

        return new DistanceAndNormal(distance, normal);
    }

    private DistanceAndNormal danToArenaQuarter(Vec3D point) {
        // TODO: Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        return new DistanceAndNormal();
    }

    private DistanceAndNormal danToArena(Vec3D point) {
        boolean negativeX = point.getX() < 0;
        boolean negativeZ = point.getZ() < 0;

        if (negativeX) {
            point = new Vec3D(-point.getX(), point.getY(), point.getZ());
        }
        if (negativeZ) {
            point = new Vec3D(point.getX(), point.getY(), -point.getZ());
        }

        DistanceAndNormal result = danToArenaQuarter(point);

        Vec3D normal = result.getNormal();
        if (negativeX) {
            result.setNormal(new Vec3D(-normal.getX(), normal.getY(), normal.getZ()));
        }
        if (negativeZ) {
            result.setNormal(new Vec3D(normal.getX(), normal.getY(), -normal.getZ()));
        }

        return result;
    }
}
