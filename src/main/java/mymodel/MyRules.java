package mymodel;


import model.Rules;


public class MyRules {
    private MyArena arena;

    public int MAX_TICK_COUNT;
    public int TEAM_SIZE;
    public long SEED;
    public double ROBOT_MIN_RADIUS;
    public double ROBOT_MAX_RADIUS;
    public double ROBOT_MAX_JUMP_SPEED;
    public double ROBOT_ACCELERATION;
    public double ROBOT_NITRO_ACCELERATION;
    public double ROBOT_MAX_GROUND_SPEED;
    public double ROBOT_ARENA_E;
    public double ROBOT_RADIUS;
    public double ROBOT_MASS;
    public int TICKS_PER_SECOND;
    public int MICROTICKS_PER_TICK;
    public int RESET_TICKS;
    public double BALL_ARENA_E;
    public double BALL_RADIUS;
    public double BALL_MASS;
    public double MIN_HIT_E;
    public double MAX_HIT_E;
    public double MAX_ENTITY_SPEED;
    public double MAX_NITRO_AMOUNT;
    public double START_NITRO_AMOUNT;
    public double NITRO_POINT_VELOCITY_CHANGE;
    public double NITRO_PACK_X;
    public double NITRO_PACK_Y;
    public double NITRO_PACK_Z;
    public double NITRO_PACK_RADIUS;
    public double NITRO_PACK_AMOUNT;
    public int NITRO_PACK_RESPAWN_TICKS;
    public double GRAVITY;

    public MyRules() {
        MAX_TICK_COUNT = 0;
        TEAM_SIZE = 0;
        SEED = 0;
        ROBOT_MIN_RADIUS = 0;
        ROBOT_MAX_RADIUS = 0;
        ROBOT_MAX_JUMP_SPEED = 0;
        ROBOT_ACCELERATION = 0;
        ROBOT_NITRO_ACCELERATION = 0;
        ROBOT_MAX_GROUND_SPEED = 0;
        ROBOT_ARENA_E = 0;
        ROBOT_RADIUS = 0;
        ROBOT_MASS = 0;
        TICKS_PER_SECOND = 0;
        MICROTICKS_PER_TICK = 0;
        RESET_TICKS = 0;
        BALL_ARENA_E = 0;
        BALL_RADIUS = 0;
        BALL_MASS = 0;
        MIN_HIT_E = 0;
        MAX_HIT_E = 0;
        MAX_ENTITY_SPEED = 0;
        MAX_NITRO_AMOUNT = 0;
        START_NITRO_AMOUNT = 0;
        NITRO_POINT_VELOCITY_CHANGE = 0;
        NITRO_PACK_X = 0;
        NITRO_PACK_Y = 0;
        NITRO_PACK_Z = 0;
        NITRO_PACK_RADIUS = 0;
        NITRO_PACK_AMOUNT = 0;
        NITRO_PACK_RESPAWN_TICKS = 0;
        GRAVITY = 0;
    }

    public MyRules(Rules rules) {
        arena = new MyArena(rules.arena);

        MAX_TICK_COUNT = rules.max_tick_count;
        TEAM_SIZE = rules.team_size;
        SEED = rules.seed;
        ROBOT_MIN_RADIUS = rules.ROBOT_MIN_RADIUS;
        ROBOT_MAX_RADIUS = rules.ROBOT_MAX_RADIUS;
        ROBOT_MAX_JUMP_SPEED = rules.ROBOT_MAX_JUMP_SPEED;
        ROBOT_ACCELERATION = rules.ROBOT_ACCELERATION;
        ROBOT_NITRO_ACCELERATION = rules.ROBOT_NITRO_ACCELERATION;
        ROBOT_MAX_GROUND_SPEED = rules.ROBOT_MAX_GROUND_SPEED;
        ROBOT_ARENA_E = rules.ROBOT_ARENA_E;
        ROBOT_RADIUS = rules.ROBOT_RADIUS;
        ROBOT_MASS = rules.ROBOT_MASS;
        TICKS_PER_SECOND = rules.TICKS_PER_SECOND;
        MICROTICKS_PER_TICK = rules.MICROTICKS_PER_TICK;
        RESET_TICKS = rules.RESET_TICKS;
        BALL_ARENA_E = rules.BALL_ARENA_E;
        BALL_RADIUS = rules.BALL_RADIUS;
        BALL_MASS = rules.BALL_MASS;
        MIN_HIT_E = rules.MIN_HIT_E;
        MAX_HIT_E = rules.MAX_HIT_E;
        MAX_ENTITY_SPEED = rules.MAX_ENTITY_SPEED;
        MAX_NITRO_AMOUNT = rules.MAX_NITRO_AMOUNT;
        START_NITRO_AMOUNT = rules.START_NITRO_AMOUNT;
        NITRO_POINT_VELOCITY_CHANGE = rules.NITRO_POINT_VELOCITY_CHANGE;
        NITRO_PACK_X = rules.NITRO_PACK_X;
        NITRO_PACK_Y = rules.NITRO_PACK_Y;
        NITRO_PACK_Z = rules.NITRO_PACK_Z;
        NITRO_PACK_RADIUS = rules.NITRO_PACK_RADIUS;
        NITRO_PACK_AMOUNT = rules.NITRO_PACK_AMOUNT;
        NITRO_PACK_RESPAWN_TICKS = rules.NITRO_PACK_RESPAWN_TICKS;
        GRAVITY = rules.GRAVITY;
    }

    public MyArena getArena() {
        return arena;
    }

    public void setArena(MyArena arena) {
        this.arena = arena;
    }
}
