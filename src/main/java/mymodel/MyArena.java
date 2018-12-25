package mymodel;


import model.Arena;


public class MyArena {
    public double WIDTH;
    public double HEIGHT;
    public double DEPTH;
    public double BOTTOM_RADIUS;
    public double TOP_RADIUS;
    public double CORNER_RADIUS;
    public double GOAL_TOP_RADIUS;
    public double GOAL_WIDTH;
    public double GOAL_HEIGHT;
    public double GOAL_DEPTH;
    public double GOAL_SIDE_RADIUS;

    public MyArena(Arena arena) {
        WIDTH = arena.width;
        HEIGHT = arena.height;
        DEPTH = arena.depth;
        BOTTOM_RADIUS = arena.bottom_radius;
        TOP_RADIUS = arena.top_radius;
        CORNER_RADIUS = arena.corner_radius;
        GOAL_TOP_RADIUS = arena.goal_top_radius;
        GOAL_WIDTH = arena.goal_width;
        GOAL_HEIGHT = arena.goal_height;
        GOAL_DEPTH = arena.goal_depth;
        GOAL_SIDE_RADIUS = arena.goal_side_radius;
    }
}
