package mymodel;


import model.Player;


public class MyPlayer {
    private int id;
    private boolean me;
    private boolean strategyCrashed;
    private int score;

    public MyPlayer(Player player) {
        id = player.id;
        me = player.me;
        strategyCrashed = player.strategy_crashed;
        score = player.score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public boolean isStrategyCrashed() {
        return strategyCrashed;
    }

    public void setStrategyCrashed(boolean strategyCrashed) {
        this.strategyCrashed = strategyCrashed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
