package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.dao.PlayerScoreDao;
import edu.hitsz.dao.PlayerScoreDaoImpl;
import edu.hitsz.gameConfig.DifficultyLevel;
import edu.hitsz.gameConfig.GameConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    private static JFrame frame;
    private static StartPanel startPanel;
    private static PlayerScoreDao dao;

    private static Game currentGame;
    private static String currentPlayer;
    private static DifficultyLevel currentDifficulty;

    public static void main(String[] args) {
        System.out.println("Hello Aircraft War");

        dao = new PlayerScoreDaoImpl();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleFrameClose();
            }
        });

        startPanel = new StartPanel();
        startPanel.setOnStart(Main::onDifficultyChosen);

        frame.setContentPane(startPanel);
        frame.setVisible(true);
    }

    // 关闭 = 等同被击毁：有姓名走 game-over 流程保留排名；无姓名直接退出
    private static void handleFrameClose() {
        if (currentGame != null && !currentGame.isGameOver()
                && currentPlayer != null && !currentPlayer.isEmpty()) {
            currentGame.forceGameOver();
        } else {
            frame.dispose();
            System.exit(0);
        }
    }

    private static void onDifficultyChosen(DifficultyLevel difficulty) {
        ScoreBoardDialog pre = new ScoreBoardDialog(
                frame, ScoreBoardDialog.Mode.PRE_GAME, difficulty, dao);
        pre.setOnStart(name -> startGame(difficulty, name));
        pre.setOnCancel(() -> { /* back to StartPanel */ });
        pre.setVisible(true);
    }

    private static void startGame(DifficultyLevel difficulty, String name) {
        GameConfig.setDifficultyLevel(difficulty);
        ImageManager.applyBackground(difficulty);
        HeroAircraft.resetInstance();

        currentDifficulty = difficulty;
        currentPlayer = (name != null && !name.isEmpty()) ? name : null;

        Game game = new Game();
        game.configureSession(difficulty, currentPlayer, dao);
        game.setOnGameOver(Main::onGameFinished);
        currentGame = game;

        frame.setContentPane(game);
        frame.revalidate();
        frame.repaint();
        game.action();
    }

    private static void onGameFinished(int score) {
        int highlightRow = -1;
        if (currentPlayer != null) {
            PlayerScore me = new PlayerScore(currentPlayer, score, currentDifficulty);
            dao.writePlayerScore(me);
            List<PlayerScore> ranked = dao.findByDifficulty(currentDifficulty);
            for (int i = 0; i < ranked.size(); i++) {
                if (ranked.get(i).getPlayerId() == me.getPlayerId()) {
                    highlightRow = i;
                    break;
                }
            }
        }

        ScoreBoardDialog post = new ScoreBoardDialog(
                frame, ScoreBoardDialog.Mode.POST_GAME, currentDifficulty, dao);
        post.setFinalScore(score);
        post.setHighlightRow(highlightRow);
        post.setOnRestart(Main::restartToStart);
        post.setOnExit(Main::exitApp);
        post.setVisible(true);
    }

    private static void restartToStart() {
        HeroAircraft.resetInstance();
        currentGame = null;
        currentPlayer = null;
        currentDifficulty = null;
        frame.setContentPane(startPanel);
        frame.revalidate();
        frame.repaint();
    }

    private static void exitApp() {
        frame.dispose();
        System.exit(0);
    }
}
