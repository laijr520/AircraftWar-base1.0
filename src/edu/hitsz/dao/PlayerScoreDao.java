package edu.hitsz.dao;

import java.util.List;

import edu.hitsz.application.PlayerScore;
import edu.hitsz.gameConfig.DifficultyLevel;

public interface PlayerScoreDao {

    List<PlayerScore> findAll();

    List<PlayerScore> findByDifficulty(DifficultyLevel difficulty);

    PlayerScore findByPlayerId(int playerId);

    void writePlayerScore(PlayerScore playerScore);

    void deletePlayerScore(int playerId);

    void deleteByIndex(DifficultyLevel difficulty, int rankIndex);
}
