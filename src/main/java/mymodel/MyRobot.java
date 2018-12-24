package mymodel;


import model.Action;
import model.Robot;

import utils.Vec3D;


public class MyRobot extends MyEntity {
    private int id;
    private int player_id;
    private boolean teammate;
    private double nitro_amount;

    private boolean touch;
    private Vec3D touch_normal;

    public MyRobot() {
        super();

        id = 0;
        player_id = 0;
        teammate = false;
        nitro_amount = 0;

        touch = false;
        touch_normal = null;
    }

    public MyRobot(Robot robot) {
        super(new MyAction(), new Vec3D(robot.x, robot.y, robot.z),
                new Vec3D(robot.velocity_x, robot.velocity_y, robot.velocity_z), robot.radius);

        id = robot.id;
        player_id = robot.player_id;
        teammate = robot.is_teammate;
        nitro_amount = robot.nitro_amount;

        touch = robot.touch;

        if (touch) {
            touch_normal = new Vec3D(robot.touch_normal_x, robot.touch_normal_y, robot.touch_normal_z);
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
        return player_id;
    }

    public void setPlayerId(int player_id) {
        this.player_id = player_id;
    }

    public boolean isTeammate() {
        return teammate;
    }

    public void setTeammate(boolean is_teammate) {
        this.teammate = is_teammate;
    }

    public double getNitroAmount() {
        return nitro_amount;
    }

    public void setNitroAmount(double nitro_amount) {
        this.nitro_amount = nitro_amount;
    }

    public boolean isTouch() {
        return touch;
    }

    public void setTouch(boolean touch) {
        this.touch = touch;
    }

    public Vec3D getTouchNormal() {
        return touch_normal;
    }

    public void setTouchNormal(Vec3D touch_normal) {
        this.touch_normal = touch_normal;
    }
}
