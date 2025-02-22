package utils;


import mymodel.*;


public class Simulation {
    private MyRules rules;
    private MyGame game;

    public Simulation(MyRules rules, MyGame game) {
        this.rules = rules;
        this.game = game;
    }

    private Vec3D clamp(Vec3D vec, double max) {
        if (vec.length() > max) {
            return vec.normalize().mul(max);
        }
        return vec;
    }

    private double clamp(double x, double min, double max) {
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
                    - b.getAction().getJumpSpeed() - a.getAction().getJumpSpeed();
            if (deltaVelocity < 0) {
                Vec3D impulse = normal.mul(1 + rules.AVG_HIT_E).mul(deltaVelocity);

                Vec3D newVelocity = a.getVelocity().add(impulse.mul(kA));
                a.setVelocity(newVelocity);
                newVelocity = b.getVelocity().sub(impulse.mul(kB));
                b.setVelocity(newVelocity);
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

        for (MyRobot robot: game.getRobots()) {
            Vec3D targetVelocityChange, newVelocity;
            if (robot.isTouch()) {
                Vec3D targetVelocity = clamp(robot.getAction().getTargetVelocity(), rules.ROBOT_MAX_GROUND_SPEED);
                targetVelocity = targetVelocity.sub(robot.getTouchNormal().mul(robot.getTouchNormal().dotProduct(targetVelocity)));

                targetVelocityChange = targetVelocity.sub(robot.getVelocity());
                if (targetVelocityChange.length() > 0) {
                    double acceleration = rules.ROBOT_ACCELERATION * Math.max(0, robot.getTouchNormal().getY());

                    newVelocity = robot.getVelocity().add(clamp(
                            targetVelocityChange.normalize().mul(acceleration).mul(deltaTime),
                            targetVelocityChange.length()));

                    robot.setVelocity(newVelocity);
                }
            }

            if (robot.getAction().isUseNitro()) {
                targetVelocityChange = clamp(
                        robot.getAction().getTargetVelocity().sub(robot.getVelocity()),
                        robot.getNitroAmount() * rules.NITRO_POINT_VELOCITY_CHANGE);
                if (targetVelocityChange.length() > 0) {
                    Vec3D acceleration = targetVelocityChange.normalize().mul(rules.ROBOT_NITRO_ACCELERATION);
                    Vec3D velocityChange = clamp(acceleration.mul(deltaTime), targetVelocityChange.length());

                    robot.setVelocity(robot.getVelocity().add(velocityChange));
                    robot.setNitroAmount(
                            robot.getNitroAmount() - velocityChange.length() / rules.NITRO_POINT_VELOCITY_CHANGE);
                }
            }

            move(robot, deltaTime);
            robot.setRadius(rules.ROBOT_MIN_RADIUS + (rules.ROBOT_MAX_RADIUS - rules.ROBOT_MIN_RADIUS)
                    * robot.getAction().getJumpSpeed() / rules.ROBOT_MAX_JUMP_SPEED);
        }

        move(game.getBall(), deltaTime);

        for (int i = 0; i < game.getRobots().length; ++i) {
            for (int j = 0; j < i; ++j) {
                collideEntities(game.getRobots()[i], game.getRobots()[j]);
            }
        }

        for (MyRobot robot: game.getRobots()) {
            collideEntities(robot, game.getBall());
            Vec3D collisionNormal = collideWithArena(robot);
            if (collisionNormal == null) {
                robot.setTouch(false);
            } else {
                robot.setTouch(true);
                robot.setTouchNormal(collisionNormal);
            }
        }

        collideWithArena(game.getBall());

        if (Math.abs(game.getBall().getPosition().getZ()) > rules.getArena().DEPTH / 2 + game.getBall().getRadius()) {
//            goal_scored();
        }

        for (MyRobot robot: game.getRobots()) {
            if (robot.getNitroAmount() == rules.MAX_NITRO_AMOUNT) {
                continue;
            }

            for (MyNitroPack pack: game.getNitroPacks()) {
                if (!pack.isAlive()) {
                    continue;
                }
                if (robot.getPosition().sub(pack.getPosition()).length() <= robot.getRadius() + rules.NITRO_PACK_RADIUS) {
                    robot.setNitroAmount(rules.MAX_NITRO_AMOUNT);
                    pack.setAlive(false);
                    pack.setRespawnTicks(rules.NITRO_PACK_RESPAWN_TICKS);
                }
            }

        }
    }

    public void tick() {
        tick(rules.MICROTICKS_PER_TICK);
    }

    public void tick(int microticks) {
        double deltaTime = (double) 1 / rules.TICKS_PER_SECOND;

        for(int i = 0; i < microticks; ++i) {
            update(deltaTime / microticks);
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
        MyArena arena = rules.getArena();

        // Ground
        DistanceAndNormal dan = danToPlane(point, new Vec3D(0, 0, 0), new Vec3D(0, 1, 0));

        // Ceiling
        dan = dan.min(danToPlane(point, new Vec3D(0, arena.HEIGHT, 0), new Vec3D(0, -1, 0)));

        // Side x
        dan = dan.min(danToPlane(point, new Vec3D(arena.WIDTH / 2, 0, 0), new Vec3D(-1, 0, 0)));

        // Side z (goal)
        dan = dan.min(danToPlane(point,
                new Vec3D(0, 0, arena.DEPTH / 2 + arena.GOAL_DEPTH), new Vec3D(0, 0, -1)));

        // Size z
        Vec3D v = new Vec3D(point.getX(), point.getY(), 0).sub(new Vec3D(
                arena.GOAL_WIDTH / 2 - arena.GOAL_TOP_RADIUS,
                arena.GOAL_HEIGHT - arena.GOAL_TOP_RADIUS,
                0));
        if (point.getX() >= arena.GOAL_WIDTH / 2 + arena.GOAL_SIDE_RADIUS
                || point.getY() >= arena.GOAL_HEIGHT + arena.GOAL_SIDE_RADIUS
                || (v.getX() > 0 && v.getY() > 0 && v.length() >= arena.GOAL_TOP_RADIUS + arena.GOAL_SIDE_RADIUS)) {
            dan = dan.min(danToPlane(point, new Vec3D(0, 0, arena.DEPTH / 2), new Vec3D(0, 0, -1)));
        }

        // Side x & ceiling (goal)
        if (point.getZ() >= arena.DEPTH / 2 + arena.GOAL_SIDE_RADIUS) {
            // x
            dan = dan.min(
                    danToPlane(point, new Vec3D(arena.GOAL_WIDTH / 2, 0, 0), new Vec3D(-1, 0, 0)));
            // y
            dan = dan.min(danToPlane(point, new Vec3D(0, arena.GOAL_HEIGHT, 0), new Vec3D(0, -1, 0)));
        }

        // Goal back corners
        if (point.getZ() > arena.DEPTH / 2 + arena.GOAL_DEPTH - arena.BOTTOM_RADIUS) {
            dan = dan.min(danToSphereInner(
                    point,
                    new Vec3D(
                            clamp(
                                    point.getX(),
                                    arena.BOTTOM_RADIUS - arena.GOAL_WIDTH / 2,
                                    arena.GOAL_WIDTH / 2 - arena.BOTTOM_RADIUS
                            ),
                            clamp(
                                    point.getY(),
                                    arena.BOTTOM_RADIUS,
                                    arena.GOAL_HEIGHT - arena.GOAL_TOP_RADIUS
                            ),
                            arena.DEPTH / 2 + arena.GOAL_DEPTH - arena.BOTTOM_RADIUS
                    ),
                    arena.BOTTOM_RADIUS
            ));
        }

        // Corner
        if (point.getX() > arena.WIDTH / 2 - arena.CORNER_RADIUS
                && point.getZ() > arena.DEPTH / 2 - arena.CORNER_RADIUS) {
            dan = dan.min(danToSphereInner(
                    point,
                    new Vec3D(
                            arena.WIDTH / 2 - arena.CORNER_RADIUS,
                            point.getY(),
                            arena.DEPTH / 2 - arena.CORNER_RADIUS
                    ),
                    arena.CORNER_RADIUS
            ));
        }

        // Goal outer corner
        if (point.getZ() < arena.DEPTH / 2 + arena.GOAL_SIDE_RADIUS) {
            // Side x
            if (point.getX() < arena.GOAL_WIDTH / 2 + arena.GOAL_SIDE_RADIUS) {
                dan = dan.min(danToSphereOuter(
                        point,
                        new Vec3D(
                                arena.GOAL_WIDTH / 2 + arena.GOAL_SIDE_RADIUS,
                                point.getY(),
                                arena.DEPTH / 2 + arena.GOAL_SIDE_RADIUS
                        ),
                        arena.GOAL_SIDE_RADIUS
                ));
            }
            // Celling
            if (point.getY() < arena.GOAL_HEIGHT + arena.GOAL_SIDE_RADIUS) {
                dan = dan.min(danToSphereOuter(
                        point,
                        new Vec3D(
                                point.getX(),
                                arena.GOAL_HEIGHT + arena.GOAL_SIDE_RADIUS,
                                arena.DEPTH / 2 + arena.GOAL_SIDE_RADIUS
                        ),
                        arena.GOAL_SIDE_RADIUS
                ));
                // Top corner
                Vec3D o = new Vec3D(
                        arena.GOAL_WIDTH / 2 - arena.GOAL_TOP_RADIUS,
                        arena.GOAL_HEIGHT - arena.GOAL_TOP_RADIUS,
                        0);
                Vec3D v2 = new Vec3D(point.getX(), point.getY(), 0).sub(o);
                if (v2.getX() > 0 && v2.getY() > 0) {
                    o = o.add(v2.normalize().mul(arena.GOAL_TOP_RADIUS + arena.GOAL_SIDE_RADIUS));
                    dan = dan.min(danToSphereOuter(
                            point,
                            new Vec3D(o.getX(), o.getY(), arena.DEPTH / 2 + arena.GOAL_SIDE_RADIUS),
                            arena.GOAL_SIDE_RADIUS
                    ));
                }
            }
        }

        // Goal inside top corners
        if (point.getZ() > arena.DEPTH / 2 + arena.GOAL_SIDE_RADIUS
                && point.getY() > arena.GOAL_HEIGHT - arena.GOAL_TOP_RADIUS) {
            // Side x
            if (point.getX() > arena.GOAL_WIDTH / 2 - arena.GOAL_TOP_RADIUS) {
                dan = dan.min(danToSphereInner(
                        point,
                        new Vec3D(
                                arena.GOAL_WIDTH / 2 - arena.GOAL_TOP_RADIUS,
                                arena.GOAL_HEIGHT - arena.GOAL_TOP_RADIUS,
                                point.getZ()
                        ),
                        arena.GOAL_TOP_RADIUS
                ));
            }
            // Side z
            if (point.getZ() > arena.DEPTH / 2 + arena.GOAL_DEPTH - arena.GOAL_TOP_RADIUS) {
                dan = dan.min(danToSphereInner(
                        point,
                        new Vec3D(
                                point.getX(),
                                arena.GOAL_HEIGHT - arena.GOAL_TOP_RADIUS,
                                arena.DEPTH / 2 + arena.GOAL_DEPTH - arena.GOAL_TOP_RADIUS
                        ),
                        arena.GOAL_TOP_RADIUS
                ));
            }
        }

        // Bottom corners
        if (point.getY() < arena.BOTTOM_RADIUS) {
            // Side x
            if (point.getX() > arena.WIDTH / 2 - arena.BOTTOM_RADIUS) {
                dan = dan.min(danToSphereInner(
                        point,
                        new Vec3D(
                                arena.WIDTH / 2 - arena.BOTTOM_RADIUS,
                                arena.BOTTOM_RADIUS,
                                point.getZ()
                        ),
                        arena.BOTTOM_RADIUS
                ));
            }
            // Side z
            if (point.getZ() > arena.DEPTH / 2 - arena.BOTTOM_RADIUS
                    && point.getX() >= arena.GOAL_WIDTH / 2 + arena.GOAL_SIDE_RADIUS) {
                dan = dan.min(danToSphereInner(
                        point,
                        new Vec3D(
                                point.getX(),
                                arena.BOTTOM_RADIUS,
                                arena.DEPTH / 2 - arena.BOTTOM_RADIUS
                        ),
                        arena.BOTTOM_RADIUS
                ));
            }
            // Side z (goal)
            if (point.getZ() > arena.DEPTH / 2 + arena.GOAL_DEPTH - arena.BOTTOM_RADIUS) {
                dan = dan.min(danToSphereInner(
                        point,
                        new Vec3D(
                                point.getX(),
                                arena.BOTTOM_RADIUS,
                                arena.DEPTH / 2 + arena.GOAL_DEPTH - arena.BOTTOM_RADIUS
                        ),
                        arena.BOTTOM_RADIUS
                ));
            }
            // Goal outer corner
            Vec3D o = new Vec3D(
                    arena.GOAL_WIDTH / 2 + arena.GOAL_SIDE_RADIUS,
                    arena.DEPTH / 2 + arena.GOAL_SIDE_RADIUS,
                    0);
            Vec3D v2 = new Vec3D(point.getX(), point.getZ(), 0).sub(o);
            if (v2.getX() < 0 && v2.getY() < 0 && v2.length() < arena.GOAL_SIDE_RADIUS + arena.BOTTOM_RADIUS) {
                o = o.add(v2.normalize().mul(arena.GOAL_SIDE_RADIUS + arena.BOTTOM_RADIUS));
                dan = dan.min(danToSphereInner(
                        point,
                        new Vec3D(o.getX(), arena.BOTTOM_RADIUS, o.getY()),
                        arena.BOTTOM_RADIUS
                ));
            }
            // Side x (goal)
            if (point.getZ() >= arena.DEPTH / 2 + arena.GOAL_SIDE_RADIUS
                    && point.getX() > arena.GOAL_WIDTH / 2 - arena.BOTTOM_RADIUS) {
                dan = dan.min(danToSphereInner(
                        point,
                        new Vec3D(
                                arena.GOAL_WIDTH / 2 - arena.BOTTOM_RADIUS,
                                arena.BOTTOM_RADIUS,
                                point.getZ()
                        ),
                        arena.BOTTOM_RADIUS
                ));
            }
            // Corner
            if (point.getX() > arena.WIDTH / 2 - arena.CORNER_RADIUS
                    && point.getZ() > arena.DEPTH / 2 - arena.CORNER_RADIUS) {
                Vec3D cornerO = new Vec3D(
                        arena.WIDTH / 2 - arena.CORNER_RADIUS,
                        arena.DEPTH / 2 - arena.CORNER_RADIUS,
                        0);
                Vec3D n = new Vec3D(point.getX(), point.getZ(), 0).sub(cornerO);
                double dist = n.length();
                if (dist > arena.CORNER_RADIUS - arena.BOTTOM_RADIUS) {
                    n = n.div(dist);
                    Vec3D o2 = cornerO.add(n.mul(arena.CORNER_RADIUS - arena.BOTTOM_RADIUS));
                    dan = dan.min(danToSphereInner(
                            point,
                            new Vec3D(o2.getX(), arena.BOTTOM_RADIUS, o2.getY()),
                            arena.BOTTOM_RADIUS
                    ));
                }
            }

        }

        // Ceiling corners
        if (point.getY() > arena.HEIGHT - arena.TOP_RADIUS) {
            // Side x
            if (point.getX() > arena.WIDTH / 2 - arena.TOP_RADIUS) {
                dan = dan.min(danToSphereInner(
                        point,
                        new Vec3D(
                                arena.WIDTH / 2 - arena.TOP_RADIUS,
                                arena.HEIGHT - arena.TOP_RADIUS,
                                point.getZ()
                        ),
                        arena.TOP_RADIUS
                ));
            }
            // Side z
            if (point.getZ() > arena.DEPTH / 2 - arena.TOP_RADIUS) {
                dan = dan.min(danToSphereInner(
                        point,
                        new Vec3D(
                                point.getX(),
                                arena.HEIGHT - arena.TOP_RADIUS,
                                arena.DEPTH / 2 - arena.TOP_RADIUS
                        ),
                        arena.TOP_RADIUS
                ));
            }
            // Corner
            if (point.getX() > arena.WIDTH / 2 - arena.CORNER_RADIUS
                    && point.getZ() > arena.DEPTH / 2 - arena.CORNER_RADIUS) {
                Vec3D cornerO = new Vec3D(
                        arena.WIDTH / 2 - arena.CORNER_RADIUS,
                        arena.DEPTH / 2 - arena.CORNER_RADIUS,
                        0);
                Vec3D dv = new Vec3D(point.getX(), point.getZ(), 0).sub(cornerO);
                if (dv.length() > arena.CORNER_RADIUS - arena.TOP_RADIUS) {
                    Vec3D n = dv.normalize();
                    Vec3D o2 = cornerO.add(n.mul(arena.CORNER_RADIUS - arena.TOP_RADIUS));
                    dan = dan.min(danToSphereInner(
                            point,
                            new Vec3D(o2.getX(), arena.HEIGHT - arena.TOP_RADIUS, o2.getY()),
                            arena.TOP_RADIUS
                    ));
                }
            }
        }

        return dan;
    }

    private DistanceAndNormal danToArena(Vec3D point) {
        boolean negativeX = point.getX() < 0;
        boolean negativeZ = point.getZ() < 0;

        if (negativeX) {
            point = point.reverseX();
        }
        if (negativeZ) {
            point = point.reverseZ();
        }

        DistanceAndNormal result = danToArenaQuarter(point);

        Vec3D normal = result.getNormal();
        if (negativeX) {
            normal = normal.reverseX();
        }
        if (negativeZ) {
            normal = normal.reverseZ();
        }
        result.setNormal(normal);

        return result;
    }
}
