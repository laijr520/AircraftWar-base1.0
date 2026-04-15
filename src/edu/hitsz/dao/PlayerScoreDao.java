package edu.hitsz.dao;

import java.util.List;

import edu.hitsz.application.PlayerScore;

public interface PlayerScoreDao {
    void findByPlayerId(int playerId);
    List<PlayerScore> findAll();
    void writePlayerScore(PlayerScore playerScore);
    void deletePlayerScore(int playerId);   
}
