import model.*;
import mymodel.*;

import utils.Vec3D;


public final class MyStrategy implements Strategy {
    double EPS = 1e-5;

    @Override
    public void act(Robot me, Rules rules, Game game, Action action) {
        MyRobot myRobot = new MyRobot(me);
        MyRules myRules = new MyRules(rules);
        MyGame myGame = new MyGame(game);
        MyBall myBall = myGame.getBall();
        MyAction myAction = new MyAction(action);

        Vec3D myPos = myRobot.getPosition();
        Vec3D myVel = myRobot.getVelocity();

        Vec3D ballPos = myBall.getPosition();
        Vec3D ballVel = myBall.getVelocity();

        if (!myRobot.isTouch()){
            myAction.setTargetVelocityX(0);
            myAction.setTargetVelocityY(-myRules.MAX_ENTITY_SPEED);
            myAction.setTargetVelocityZ(0);
            myAction.setJumpSpeed(0);

            return;
        }

        boolean jump = myPos.squareDistance(ballPos) < Math.pow(myRules.BALL_RADIUS + myRules.ROBOT_MAX_RADIUS, 2)
                && myPos.getZ() < ballPos.getZ();

        boolean isAttacker = myGame.getRobots().length == 2;

        for (MyRobot robot: myGame.getRobots()) {
            if (robot.isTeammate() && robot.getId() != myRobot.getId()) {
                if (robot.getPosition().getZ() < myPos.getZ()) {
                    isAttacker = true;
                }
            }
        }

        if (isAttacker) {
            for (int i = 0; i != 100; ++i) {
                double t = i * 0.1;
                Vec3D surBallPos = ballPos.copy().add(ballVel).mul(t);

                if (surBallPos.getZ() > myPos.getZ()
                        && Math.abs(surBallPos.getX()) < (myRules.getArena().WIDTH / 2)
                        && Math.abs(surBallPos.getZ()) < (myRules.getArena().DEPTH / 2)) {
                    Vec3D deltaPos = surBallPos.copy().sub(myPos);
                    double needSpeed = deltaPos.length() / t;

                    if (0.5 * myRules.ROBOT_MAX_GROUND_SPEED < needSpeed && needSpeed < myRules.ROBOT_MAX_GROUND_SPEED) {
                        Vec3D targetVelocity = deltaPos.copy().normalize().mul(needSpeed);

                        myAction.setTargetVelocityX(targetVelocity.getX());
                        myAction.setTargetVelocityY(0);
                        myAction.setTargetVelocityZ(targetVelocity.getZ());
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
                targetPos.setX(x);
            }
        }

        Vec3D targetVelocity = targetPos.sub(myPos).mul(myRules.ROBOT_MAX_GROUND_SPEED);

        myAction.setTargetVelocityX(targetVelocity.getX());
        myAction.setTargetVelocityY(0);
        myAction.setTargetVelocityZ(targetVelocity.getZ());
        myAction.setJumpSpeed(jump ? myRules.ROBOT_MAX_JUMP_SPEED : 0);
    }
    
    @Override
    public String customRendering() {
        return "";
    }
}
