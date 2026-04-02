// package edu.hitsz.gameConfig;

// public class GameConfig {

//     private static DifficultyLevel difficultyLevel = DifficultyLevel.NORMAL;

//     public final AircraftParams heroAircraftParams;
//     public final AircraftParams mobEnemyAircraftParams;
//     public final AircraftParams eliteEnemyAircraftParams;
//     public final AircraftParams eliteProEnemyAircraftParams;
//     public final AircraftParams elitePlusEnemyAircraftParams;
//     public final AircraftParams bossEnemyAircraftParams;

//     public final BulletParams heroBulletParams;
//     public final BulletParams mobEnemyBulletParams;
//     public final BulletParams eliteEnemyBulletParams;
//     public final BulletParams eliteProEnemyBulletParams;
//     public final BulletParams elitePlusEnemyBulletParams;
//     public final BulletParams bossEnemyBulletParams;

//     public final PropParams propBloodParams;
//     public final PropParams propBulletParams;
//     public final PropParams propBombParams;
//     public final PropParams propBombPlusParams;

//     private final int baseHp = 100;
//     private final int baseSpeedX = 10;
//     private final int baseSpeedY = 10;
//     private final int basePower = 100;
//     private final int baseBulletSpeedX = 0;
//     private final int baseBulletSpeedY = 15;

//     private static GameConfig instance = null;

//     public static void setDifficultyLevel(DifficultyLevel difficultyLevel) {
//         GameConfig.difficultyLevel = difficultyLevel;   
//     }
//     //TODO: 根据难度调整游戏参数
//     private GameConfig() {
//         switch (difficultyLevel) {
//             case NORMAL -> {
                
//             }
//             case HARD -> {
       
//             }
//             case EXPERT -> {
                
//             }
//             default -> throw new IllegalStateException("Unexpected value: " + difficultyLevel);
//         }
//     }
//     public static GameConfig getInstance() {
//         if (instance == null) {
//             instance = new GameConfig();
//         }
//         return instance;
//     }
// }
