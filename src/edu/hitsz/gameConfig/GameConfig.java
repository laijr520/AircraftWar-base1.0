package edu.hitsz.gameConfig;

import java.util.Random;

public class GameConfig {

    private static DifficultyLevel difficultyLevel = DifficultyLevel.NORMAL;
    private static volatile GameConfig instance = null;

    // 参数对象
    public final AircraftParams heroAircraftParams;
    public final AircraftParams mobEnemyAircraftParams;
    public final AircraftParams eliteEnemyAircraftParams;
    public final AircraftParams ElitePlusEnemyAircraftParams;
    public final AircraftParams EliteProEnemyAircraftParams;
    public final AircraftParams bossEnemyAircraftParams;

    public final BulletParams heroBulletParams;
    public final BulletParams mobEnemyBulletParams;
    public final BulletParams eliteEnemyBulletParams;
    public final BulletParams ElitePlusEnemyBulletParams;
    public final BulletParams EliteProEnemyBulletParams;
    public final BulletParams bossEnemyBulletParams;

    public final PropParams propBloodParams;
    public final PropParams propBulletParams;
    public final PropParams propBombParams;
    public final PropParams propBombPlusParams;

    // 基础数值
    private static final int BASE_HP = 100;
    private static final int BASE_SPEED_X = 10;
    private static final int BASE_SPEED_Y = 10;
    private static final int BASE_POWER = 100;

    private final Random random = new Random();


    private GameConfig() {
        // 根据难度获取系数
        double difficultyFactor = getDifficultyFactor();

        // 初始化各类参数（具体数值基于基础值 * factor 计算，此处留空占位）
        this.heroAircraftParams = initHeroParams(difficultyFactor);
        this.mobEnemyAircraftParams = initMobEnemyParams(difficultyFactor);
        this.eliteEnemyAircraftParams = initEliteEnemyParams(difficultyFactor);
        this.ElitePlusEnemyAircraftParams = initElitePlusEnemyParams(difficultyFactor);
        this.EliteProEnemyAircraftParams = initEliteProEnemyParams(difficultyFactor);
        this.bossEnemyAircraftParams = initBossEnemyParams(difficultyFactor);

        this.heroBulletParams = initHeroBulletParams(difficultyFactor);
        this.mobEnemyBulletParams = initMobEnemyBulletParams(difficultyFactor);
        this.eliteEnemyBulletParams = initEliteEnemyBulletParams(difficultyFactor);
        this.ElitePlusEnemyBulletParams = initElitePlusEnemyBulletParams(difficultyFactor);
        this.EliteProEnemyBulletParams = initEliteProEnemyBulletParams(difficultyFactor);
        this.bossEnemyBulletParams = initBossEnemyBulletParams(difficultyFactor);

        this.propBloodParams = initPropBloodParams(difficultyFactor);
        this.propBulletParams = initPropBulletParams(difficultyFactor);
        this.propBombParams = initPropBombParams(difficultyFactor);
        this.propBombPlusParams = initPropBombPlusParams(difficultyFactor);
    }

    // 获取难度系数
    //TODO: 调整系数值
    private double getDifficultyFactor() {
        return switch (difficultyLevel) {
            case NORMAL -> 1.0;
            case HARD -> 1.5;
            case EXPERT -> 2.0;
            default -> throw new IllegalStateException("Unexpected difficulty: " + difficultyLevel);
        };
    }

    // 各参数的初始化
    private AircraftParams initHeroParams(double factor) { 
        int maxHp = (int)(BASE_HP);
        return new AircraftParams(0,0,maxHp); }
    private AircraftParams initMobEnemyParams(double factor) { 
        int maxHp = (int)(0.2 * BASE_HP * factor);
        int speedY = (int)(BASE_SPEED_Y);
        return new AircraftParams(0,speedY,maxHp); 
    }
    private AircraftParams initEliteEnemyParams(double factor) { 
        int maxHp = (int)(0.2 * BASE_HP * factor);
        int speedY = (int)(0.8 * BASE_SPEED_Y);
        return new AircraftParams(0,speedY,maxHp); 
    }
    private AircraftParams initElitePlusEnemyParams(double factor) { 
        int maxHp = (int)(0.2 * BASE_HP * factor);
        int speedX = (int)(random.nextBoolean() ? 0.3 * BASE_SPEED_X : -0.3 * BASE_SPEED_X);
        int speedY = (int)(0.8 * BASE_SPEED_Y);
        return new AircraftParams(speedX,speedY,maxHp); 
    }
    private AircraftParams initEliteProEnemyParams(double factor) { 
        int maxHp = (int)(0.4 * BASE_HP * factor);
        int speedX = (int)(random.nextBoolean() ? 0.6 * BASE_SPEED_X : -0.6 * BASE_SPEED_X);
        int speedY = (int)(0.6 * BASE_SPEED_Y);
        return new AircraftParams(speedX,speedY,maxHp); 
    }
    private AircraftParams initBossEnemyParams(double factor) { 
        int maxHp = (int)(0.8  * BASE_HP * factor);
        int speedX = (int)(random.nextBoolean() ? BASE_SPEED_X : -1 * BASE_SPEED_X);
        return new AircraftParams(speedX,0,maxHp); 
    }

    private BulletParams initHeroBulletParams(double factor) { 
        //TODO
        return new BulletParams(0); 
    }
    private BulletParams initMobEnemyBulletParams(double factor) { 
        return new BulletParams(0); 
    }
    private BulletParams initEliteEnemyBulletParams(double factor) { /* TODO */ 
        int power = (int)(0.02 * BASE_POWER * factor);
        return new BulletParams(power);
    }
    private BulletParams initElitePlusEnemyBulletParams(double factor) { /* TODO */ return new BulletParams(0); }
    private BulletParams initEliteProEnemyBulletParams(double factor) { /* TODO */ return new BulletParams(0); }
    private BulletParams initBossEnemyBulletParams(double factor) { /* TODO */ return new BulletParams(0); }

    private PropParams initPropBloodParams(double factor) { /* TODO */ return new PropParams((int)(BASE_HP)); }
    private PropParams initPropBulletParams(double factor) { /* TODO */ return new PropParams(0); }
    private PropParams initPropBombParams(double factor) { /* TODO */ return new PropParams(0); }
    private PropParams initPropBombPlusParams(double factor) { /* TODO */ return new PropParams(0); }

    // 难度设置方法（重置单例实例）
    public static synchronized void setDifficultyLevel(DifficultyLevel level) {
        if (difficultyLevel != level) {
            difficultyLevel = level;
            instance = null;   // 下次获取时重新创建
        }
    }

    // 线程安全的单例获取
    public static GameConfig getInstance() {
        GameConfig instance = GameConfig.instance;
        if (instance == null) {
            synchronized (GameConfig.class) {
                instance = GameConfig.instance;
                if (instance == null) {
                    GameConfig.instance = instance = new GameConfig();
                }
            }
        }
        return instance;
    }

    //获取当前难度
    public static DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
}