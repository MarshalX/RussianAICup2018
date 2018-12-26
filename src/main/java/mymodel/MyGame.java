package mymodel;


import model.Game;
import model.Rules;


public class MyGame {
    private int currentTick;
    private MyPlayer[] players;
    private MyRobot[] robots;
    private MyNitroPack[] nitroPacks;
    private MyBall ball;

    public MyGame(Game game, Rules rules) {
        currentTick = game.current_tick;

        int playersCount = game.players.length;
        int robotsCount = game.robots.length;
        int nitroPacksCount = game.nitro_packs.length;

        players = new MyPlayer[playersCount];
        for (int i = 0; i < playersCount; ++i) {
            players[i] = new MyPlayer(game.players[i]);
        }

        robots = new MyRobot[robotsCount];
        for (int i = 0; i < robotsCount; ++i) {
            robots[i] = new MyRobot(game.robots[i], rules);
        }

        nitroPacks = new MyNitroPack[nitroPacksCount];
        for (int i = 0; i < nitroPacksCount; ++i) {
            nitroPacks[i] = new MyNitroPack(game.nitro_packs[i]);
        }

        ball = new MyBall(game.ball, rules);
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
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

    public MyNitroPack[] getNitroPacks() {
        return nitroPacks;
    }

    public void setNitroPacks(MyNitroPack[] nitroPacks) {
        this.nitroPacks = nitroPacks;
    }

    public MyBall getBall() {
        return ball;
    }

    public void setBall(MyBall ball) {
        this.ball = ball;
    }
}
