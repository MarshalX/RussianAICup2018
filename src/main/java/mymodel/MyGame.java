package mymodel;


import model.Game;


public class MyGame {
    private int current_tick;
    private MyPlayer[] players;
    private MyRobot[] robots;
    private MyNitroPack[] nitro_packs;
    private MyBall ball;

    public MyGame() {
        current_tick = 0;
        players = new MyPlayer[0];
        robots = new MyRobot[0];
        nitro_packs = new MyNitroPack[0];
        ball = new MyBall();
    }

    public MyGame(Game game) {
        current_tick = game.current_tick;

        int players_count = game.players.length;
        int robots_count = game.robots.length;
        int nitro_packs_count = game.nitro_packs.length;

        players = new MyPlayer[players_count];
        for (int i = 0; i < players_count; ++i) {
            players[i] = new MyPlayer(game.players[i]);
        }

        robots = new MyRobot[robots_count];
        for (int i = 0; i < robots_count; ++i) {
            robots[i] = new MyRobot(game.robots[i]);
        }

        nitro_packs = new MyNitroPack[nitro_packs_count];
        for (int i = 0; i < nitro_packs_count; ++i) {
            nitro_packs[i] = new MyNitroPack(game.nitro_packs[i]);
        }

        ball = new MyBall(game.ball);
    }

    public int getCurrent_tick() {
        return current_tick;
    }

    public void setCurrent_tick(int current_tick) {
        this.current_tick = current_tick;
    }

    public MyPlayer[] getPlayers() {
        return players;
    }

    public void setPlayers(MyPlayer[] players) {
        this.players = players;
    }

    public MyRobot[] getRobots() {
        return robots;
    }

    public void setRobots(MyRobot[] robots) {
        this.robots = robots;
    }

    public MyNitroPack[] getNitro_packs() {
        return nitro_packs;
    }

    public void setNitro_packs(MyNitroPack[] nitro_packs) {
        this.nitro_packs = nitro_packs;
    }

    public MyBall getBall() {
        return ball;
    }

    public void setBall(MyBall ball) {
        this.ball = ball;
    }
}
