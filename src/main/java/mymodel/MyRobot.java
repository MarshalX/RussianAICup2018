package mymodel;


import model.Action;
import model.Robot;

import utils.Vec3D;


public class MyRobot extends MyEntity {
    private int id;
    private int playerId;
    private boolean teammate;
    private double nitroAmount;

    private boolean touch;
    private Vec3D touchNormal;

    public MyRobot(Robot robot) {
        super(new Vec3D(robot.x, robot.y, robot.z),
                new Vec3D(robot.velocity_x, robot.velocity_y, robot.velocity_z), robot.radius);

        id = robot.id;
        playerId = robot.player_id;
        teammate = robot.is_teammate;
        nitroAmount = robot.nitro_amount;

        touch = robot.touch;

        if (touch) {
            touchNormal = new Vec3D(robot.touch_normal_x, robot.touch_normal_y, robot.touch_normal_z);
        }
    }

    public MyRobot(Action action, Robot robot) {
        this(robot);

        setAction(new MyAction(action));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean isTeammate() {
        return teammate;
    }

    public void setTeammate(boolean isTeammate) {
        this.teammate = isTeammate;
    }

    public double getNitroAmount() {
        return nitroAmount;
    }

    public void setNitroAmount(double nitroAmount) {
        this.nitroAmount = nitroAmount;
    }

    public boolean isTouch() {
        return touch;
    }

    public void setTouch(boolean touch) {
        this.touch = touch;
    }

    public Vec3D getTouchNormal() {
        return touchNormal;
    }

    public void setTouchNormal(Vec3D touchNormal) {
        this.touchNormal = touchNormal;
    }
}
