package mymodel;


import model.Ball;

import utils.Vec3D;


public class MyBall extends MyEntity {
    public MyBall(Ball ball) {
        super(new Vec3D(ball.x, ball.y, ball.z),
                new Vec3D(ball.velocity_x, ball.velocity_y, ball.velocity_z), ball.radius);
    }
}
