package mymodel;


import model.Ball;

import model.Rules;
import utils.Vec3D;


public class MyBall extends MyEntity {
    public MyBall(Ball ball, Rules rules) {
        super(new Vec3D(ball.x, ball.y, ball.z),
                new Vec3D(ball.velocity_x, ball.velocity_y, ball.velocity_z), ball.radius);

        setAction(new MyAction());

        MASS = rules.BALL_MASS;
        ARENA_E = rules.BALL_ARENA_E;
    }
}
