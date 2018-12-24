package mymodel;


import model.Player;


public class MyPlayer {
    private int id;
    private boolean me;
    private boolean strategy_crashed;
    private int score;

    public MyPlayer() {
        id = 0;
        me = false;
        strategy_crashed = false;
        score = 0;
    }

    public MyPlayer(Player player) {
        id = player.id;
        me = player.me;
        strategy_crashed = player.strategy_crashed;
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

    public boolean isStrategy_crashed() {
        return strategy_crashed;
    }

    public void setStrategy_crashed(boolean strategy_crashed) {
        this.strategy_crashed = strategy_crashed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
