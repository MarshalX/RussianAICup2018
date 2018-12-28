import com.google.gson.Gson;

import model.*;
import mymodel.*;

import utils.Simulation;
import utils.Vec3D;


class Sphere {
    JsonSphere Sphere;

    public Sphere(JsonSphere jsonBall) {
        Sphere = jsonBall;
    }
}

class JsonSphere {
    double x;
    double y;
    double z;

    double radius = 3;

    double r = 1.0;
    double g = 0.0;
    double b = 0.0;

    double a = 0.5;

    public JsonSphere(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

public final class MyStrategy implements Strategy {
    Gson gson = new Gson();
    Sphere[] path = new Sphere[300];

    double EPS = 1e-5;
    int toSimulate = 200;

    @Override
    public void act(Robot me, Rules rules, Game game, Action action) {
        MyRules myRules = new MyRules(rules);
        MyRobot myRobot = new MyRobot(action, me, rules);
        MyGame myGame = new MyGame(game, rules);
        MyBall myBall = myGame.getBall();
        MyAction myAction = new MyAction(action);

        Vec3D myPos = myRobot.getPosition();
        Vec3D myVel = myRobot.getVelocity();

        Vec3D ballPos = myBall.getPosition();
        Vec3D ballVel = myBall.getVelocity();

        Simulation sim = new Simulation(myRules, myGame);
        for (int i = 0; i < 300; ++i) {
            sim.tick();

            Vec3D pos = myGame.getBall().getPosition();
            path[i] = new Sphere(new JsonSphere(pos.getX(), pos.getY(), pos.getZ()));
        }

        if (!myRobot.isTouch()){
            myAction.setTargetVelocity(new Vec3D(0, -myRules.MAX_ENTITY_SPEED, 0));
            myAction.setJumpSpeed(0);

            return;
        }

        boolean jump = myPos.squareDistance(ballPos) < Math.pow(myRules.BALL_RADIUS + myRules.ROBOT_MAX_RADIUS, 2)
                && myPos.getZ() < ballPos.getZ();

        if (Math.abs(ballPos.getZ()) < EPS && Math.abs(ballPos.getX()) < EPS) {
            myAction.setTargetVelocity(new Vec3D(0, 0, 0).sub(myPos).mul(myRules.ROBOT_MAX_GROUND_SPEED));
            myAction.setJumpSpeed(jump ? myRules.ROBOT_MAX_JUMP_SPEED : 0);

            return;
        }

        boolean isAttacker = myGame.getRobots().length == 2;

        for (MyRobot robot: myGame.getRobots()) {
            if (robot.isTeammate() && robot.getId() != myRobot.getId()) {
                if (robot.getPosition().getZ() < myPos.getZ()) {
                    isAttacker = true;
                }
            }
        }

        if (ballPos.getZ() > EPS - 1) {
            isAttacker = true;
            toSimulate = 50;
        }

        if (isAttacker) {
            for (int i = 0; i != toSimulate; ++i) {
                double t = i * 0.1;

                Vec3D surBallPos = ballPos.add(ballVel.mul(t));
                Vec3D myPos2D = new Vec3D(myPos.getX(), 0, myPos.getZ());
                Vec3D ballPos2D = new Vec3D(ballPos.getX(), 0, ballPos.getZ());
                Vec3D goalPos = new Vec3D(0, 0, 40);

                if (ballPos2D.getZ() > myPos2D.getZ() &&
                        ballPos2D.squareDistance(myPos2D) > Math.pow(myRules.BALL_RADIUS, 2)) {
                    Vec3D deltaPos = ballPos2D.sub(myPos2D);
                    double needSpeed = deltaPos.length() / t;

                    if (0.8 * myRules.ROBOT_MAX_GROUND_SPEED < needSpeed && needSpeed < myRules.ROBOT_MAX_GROUND_SPEED) {
                        Vec3D targetVelocity = deltaPos.normalize().mul(needSpeed);

                        myAction.setTargetVelocity(targetVelocity);
                        myAction.setJumpSpeed(jump ? myRules.ROBOT_MAX_JUMP_SPEED : 0);

                        return;
                    }

                } else {
                    Vec3D ballToGoal = ballPos2D.sub(goalPos);
                    Vec3D deltaPos = ballToGoal.sub(myPos2D);
                    double needSpeed = deltaPos.length() / t;

                    if (0.65 * myRules.ROBOT_MAX_GROUND_SPEED < needSpeed && needSpeed < myRules.ROBOT_MAX_GROUND_SPEED) {
                        Vec3D targetVelocity = deltaPos.normalize().mul(myRules.ROBOT_MAX_GROUND_SPEED);

                        myAction.setTargetVelocity(targetVelocity);
                        myAction.setJumpSpeed(jump ? myRules.ROBOT_MAX_JUMP_SPEED : 0);

                        return;
                    }
                }

                if (surBallPos.getZ() > myPos.getZ()
                        && Math.abs(surBallPos.getX()) < (myRules.getArena().WIDTH / 2)
                        && Math.abs(surBallPos.getZ()) < (myRules.getArena().DEPTH / 2)) {

                    Vec3D deltaPos = surBallPos.sub(myPos);
                    double needSpeed = deltaPos.length() / t;

                    if (needSpeed < myRules.ROBOT_MAX_GROUND_SPEED) {
                        Vec3D targetVelocity = deltaPos.normalize().mul(needSpeed);

                        myAction.setTargetVelocity(targetVelocity);
                        myAction.setJumpSpeed(jump ? myRules.ROBOT_MAX_JUMP_SPEED : 0);

                        return;
                    }
                }
            }
        }

        Vec3D targetPos = new Vec3D(0, 0, -(myRules.getArena().DEPTH / 2) + myRules.getArena().BOTTOM_RADIUS);
        if (ballVel.getZ() < -EPS) {
            double t = (targetPos.getZ() - ballPos.getZ()) / ballVel.getZ();
            double x = ballPos.getX() + ballVel.getX() * t;

            if (Math.abs(x) < myRules.getArena().GOAL_WIDTH / 2) {
                targetPos = new Vec3D(x, targetPos.getY(), targetPos.getZ());
            }
        }

        Vec3D targetVelocity = targetPos.sub(myPos).mul(myRules.ROBOT_MAX_GROUND_SPEED);

        targetVelocity = new Vec3D(targetVelocity.getX(), 0, targetVelocity.getZ());

        myAction.setTargetVelocity(targetVelocity);
        myAction.setJumpSpeed(jump ? myRules.ROBOT_MAX_JUMP_SPEED : 0);
    }
    
    @Override
    public String customRendering() {
        return gson.toJson(path);
    }
}
