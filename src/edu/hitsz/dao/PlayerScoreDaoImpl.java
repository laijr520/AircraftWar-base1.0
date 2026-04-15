package edu.hitsz.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.hitsz.application.Mode;
import edu.hitsz.application.PlayerScore;

public class PlayerScoreDaoImpl implements PlayerScoreDao {
    private List<PlayerScore> playerScores;

    public PlayerScoreDaoImpl() {
        this.playerScores = new ArrayList<PlayerScore>();
    }

    public PlayerScoreDaoImpl(List<PlayerScore> playerScores) {
        this.playerScores = playerScores;
    }



    @Override
    public void findByPlayerId(int playerId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<PlayerScore> findAll() {
        playerScores.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("file/scores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int playerId = Integer.parseInt(parts[0]);
                int score = Integer.parseInt(parts[1]);
                Mode mode = Mode.valueOf(parts[2]);
                playerScores.add(new PlayerScore(playerId, score, mode));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.console().printf("读取文件失败！\n");
        }
        return playerScores;
    }

    @Override
    public void writePlayerScore(PlayerScore playerScore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("file/scores.txt", true))) {
            writer.write(playerScore.getPlayerId() + "," + playerScore.getScore() + "," + playerScore.getMode());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("写入文件失败！\n");
        }
    }

    @Override
    public void deletePlayerScore(int playerId) {
        // TODO Auto-generated method stub
        
    }

    public List<PlayerScore> getPlayerScores() {
        return playerScores;
    }

    public void addPlayerScore(PlayerScore playerScore) {
        this.playerScores.add(playerScore);
    }
    
}
