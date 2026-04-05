package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.aircraft.enemy.EliteProEnemy;
import edu.hitsz.aircraft.enemy.ElitePlusEnemy;
import edu.hitsz.aircraft.factory.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.gameConfig.GameConfig;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * 游戏主面板，游戏启动
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    //调度器, 用于定时任务调度
    private final Timer timer;
    //时间间隔(ms)，控制刷新频率
    private final int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<BaseProp> props;

    //屏幕中出现的敌机最大数量
    private final int enemyMaxNumber = 5;

    //敌机生成周期
    protected double enemySpawnCycle  =  20;
    private int enemySpawnCounter = 0;

    //英雄机和敌机射击周期
    protected double shootCycle = 20;
    private int shootCounter = 0;

    //当前玩家分数
    private int score = 0;

    //击败敌机数
    private int MobEnemyCount = 0;
    private int EliteEnemyCount = 0;
    private int ElitePlusEnemyCount = 0;
    private int EliteProEnemyCount = 0;
    private int BossEnemyCount = 0;

    //游戏结束标志
    private boolean gameOverFlag = false;

    public Game() {
        heroAircraft = HeroAircraft.getHeroAircraft(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                GameConfig.getInstance().heroAircraftParams.speedX(),
                GameConfig.getInstance().heroAircraftParams.speedY(),
                GameConfig.getInstance().heroAircraftParams.maxHp()
        );

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

        this.timer = new Timer("game-action-timer", true);

    }

    private EnemyFactory getFactory(AircraftTypeEnum type) {
        switch (type) {
            case MOB:
                {
                    MobEnemyCount++;
                    return new MobEnemyFactory();
                }
            case ELITE:
                {
                    EliteEnemyCount++;
                    return new EliteEnemyFactory();
                }
            case ElitePlus:
                {
                    ElitePlusEnemyCount++;
                    return new ElitePlusEnemyFactory();
                }
            case ElitePro:
                {
                    EliteProEnemyCount++;
                    return new EliteProEnemyFactory();
                }
            case BOSS:
                {
                    BossEnemyCount++;
                    return new BossEnemyFactory();
                }
            default:
                {
                    MobEnemyCount++;
                    return new MobEnemyFactory();
                }
        }
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、及结束判定
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                enemySpawnCounter++;
                if (enemySpawnCounter >=enemySpawnCycle) {
                    enemySpawnCounter = 0;
                    // 产生敌机
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        AircraftTypeEnum type = AircraftTypeEnum.MOB;

                        if (score >= 50*(EliteEnemyCount+1) && enemyAircrafts.stream().noneMatch(e -> e instanceof EliteEnemy)) {
                            type = AircraftTypeEnum.ELITE;
                        }
                        if (score >= 90*(ElitePlusEnemyCount+1) && enemyAircrafts.stream().noneMatch(e -> e instanceof ElitePlusEnemy)) {
                            type = AircraftTypeEnum.ElitePlus;
                        }
                        if (score >= 170*(EliteProEnemyCount+1) && enemyAircrafts.stream().noneMatch(e -> e instanceof EliteProEnemy)) {
                            type = AircraftTypeEnum.ElitePro;
                        }
                        if (score >= 300*(BossEnemyCount+1) && enemyAircrafts.stream().noneMatch(e -> e instanceof BossEnemy)) {
                            type = AircraftTypeEnum.BOSS;
                        }
                        EnemyFactory factory = getFactory(type);
                        enemyAircrafts.add(factory.createEnemy(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05)
                        ));
                    }
                }

                // 飞机发射子弹
                shootAction();
                // 子弹移动
                bulletsMoveAction();
                // 飞机移动
                aircraftsMoveAction();
                // 道具移动
                propsMoveAction();
                // 撞击检测
                crashCheckAction();
                // 后处理
                postProcessAction();
                // 重绘界面
                repaint();
                // 游戏结束检查
                checkResultAction();
            }
        };
        // 以固定延迟时间进行执行：本次任务执行完成后，延迟 timeInterval 再执行下一次
        timer.schedule(task,0,timeInterval);

    }

    //***********************
    //      Action 各部分
    //***********************

    private void CreatePropAction(AircraftTypeEnum type, int Locationx, int Locationy) {
        double rand_prop_type = Math.random();
        double rand_if_create = Math.random();
        double createProbability = getCreateProbability(type);
        int speedY = getPropSpeedY(type);

        if (rand_if_create < createProbability) {
            PropTypeEnum propType = getRandomPropType(rand_prop_type);
            props.add(PropFactory.createProp(propType, Locationx, Locationy, 0, speedY));
        }
    }

    private double getCreateProbability(AircraftTypeEnum type) {
        switch (type) {
            case MOB: return 0.2;
            case ELITE: return 0.4;
            case ElitePlus: return 0.5;
            case ElitePro: return 0.7;
            case BOSS: return 2.0; // 总是创建
            default: return 0.0;
        }
    }

    private int getPropSpeedY(AircraftTypeEnum type) {
        return type == AircraftTypeEnum.MOB ? 5 : 2;
    }

    private PropTypeEnum getRandomPropType(double rand) {
        if (rand < 0.25) {
            return PropTypeEnum.BLOOD;
        } else if (rand < 0.5) {
            return PropTypeEnum.BULLET;
        } else if (rand < 0.75) {
            return PropTypeEnum.BOMB;
        } else {
            return PropTypeEnum.BULLET_PLUS;
        }
    }

    private void shootAction() {
        shootCounter++;
        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            //英雄机射击
            heroBullets.addAll(heroAircraft.shoot());
            // TODO 敌机射击
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (BaseProp prop : props) {
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        switch (enemyAircraft.getClass().getSimpleName()) {
                            case "MobEnemy":
                                score += 10;
                                CreatePropAction(AircraftTypeEnum.MOB, enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                                break;
                            case "EliteEnemy":
                                score += 20;
                                CreatePropAction(AircraftTypeEnum.ELITE, enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                                break;
                            case "ElitePlusEnemy":
                                score += 30;
                                CreatePropAction(AircraftTypeEnum.ElitePlus, enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                                break;
                            case "EliteProEnemy":
                                score += 50;
                                CreatePropAction(AircraftTypeEnum.ElitePro, enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                                break;
                            case "BossEnemy":
                                score += 100;
                                CreatePropAction(AircraftTypeEnum.BOSS, enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                                break;
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for (BaseProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                // 英雄机撞击到道具
                // 道具生效
                prop.effect(heroAircraft);
                prop.vanish();
            }
        }

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }

    /**
     * 检查游戏是否结束，若结束：关闭线程池
     */
    private void checkResultAction(){
        // 游戏结束检查英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            timer.cancel(); // 取消定时器并终止所有调度任务
            gameOverFlag = true;
            System.out.println("Game Over!");
        }
    };

    //***********************
    //      Paint 各部分
    //***********************
    /**
     * 重写 paint方法
     * 通过重复调用paint方法，实现游戏动画
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        // Todo: 绘制道具
        paintImageWithPositionRevised(g, props);


        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }

}
