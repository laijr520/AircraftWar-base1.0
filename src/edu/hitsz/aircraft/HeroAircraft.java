package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.HeroBulletStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    //静态引用自身
    private static volatile HeroAircraft heroAircraft = null;

    public static void resetInstance() {
        synchronized (HeroAircraft.class) {
            heroAircraft = null;
        }
    }

    public static HeroAircraft getHeroAircraft() {
        if (heroAircraft == null) {
            synchronized(HeroAircraft.class) {
                if (heroAircraft == null) {
                    heroAircraft = new HeroAircraft(
                            0, 0, 0, 0, 100);
                }
            }
            return heroAircraft;
        }
        //TODO:remove
        System.console().printf("英雄机重复生成！\n");
        return heroAircraft;
    }

    public static HeroAircraft getHeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        if (heroAircraft == null) {
            synchronized(HeroAircraft.class) {
                if (heroAircraft == null) {
                    heroAircraft = new HeroAircraft(
                            locationX, locationY, speedX, speedY, hp);
                }
            }
            return heroAircraft;
        }
        //TODO:remove
        System.console().printf("英雄机重复生成！\n");
        return heroAircraft;
    }

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setBulletStrategy(new HeroBulletStrategy());
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        if(bulletStrategy != null){
            return bulletStrategy.fire(this);
        }else{
            return new LinkedList<>();
        }
    }
    
}
