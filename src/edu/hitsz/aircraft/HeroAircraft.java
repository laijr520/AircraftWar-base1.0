package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    //静态引用自身
    private static volatile HeroAircraft heroAircraft = null;


    //每次射击发射子弹数量
    private int shootNum = 1;

    //子弹威力
    private int power = 30;

    //子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = -1;

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
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }

}
