package edu.hitsz.application;

import edu.hitsz.gameConfig.DifficultyLevel;

public class PlayerScore {
    private int playerId;
    private String playerName;
    private int score;
    private DifficultyLevel difficulty;
    private long timestamp;

    public PlayerScore(int playerId, String playerName, int score, DifficultyLevel difficulty, long timestamp) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
        this.difficulty = difficulty;
        this.timestamp = timestamp;
    }

    public PlayerScore(String playerName, int score, DifficultyLevel difficulty) {
        this(0, playerName, score, difficulty, System.currentTimeMillis());
    }

    public int getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public DifficultyLevel getDifficulty() { return difficulty; }
    public long getTimestamp() { return timestamp; }

    public void setPlayerId(int playerId) { this.playerId = playerId; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setScore(int score) { this.score = score; }
    public void setDifficulty(DifficultyLevel difficulty) { this.difficulty = difficulty; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
