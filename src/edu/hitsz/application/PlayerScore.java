package edu.hitsz.application;

public class PlayerScore {
    private int playerId;
    private int score;
    private Mode mode;
    
    public PlayerScore(int playerId, int score, Mode mode) {
        this.playerId = playerId;
        this.score = score;
        this.mode = mode;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getScore() {
        return score;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public Mode getMode() {
        return mode;
    }
}
