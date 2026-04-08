package edu.hitsz.strategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

public class HeroPlusBulletStrategy implements AbstractBulletStrategy {
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        
        //子弹射击方向 (向上发射：-1，向下发射：1)
        int direction = -1;
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction*2;
        int speedY = aircraft.getSpeedY() + direction*5; // 设置子弹的速度
        int power = 30;

        // 左侧子弹
        HeroBullet leftBullet = new HeroBullet(x - 5, y, -3, speedY, power);
        bullets.add(leftBullet);

        // 中间子弹
        HeroBullet centerBullet = new HeroBullet(x, y, 0, speedY, power);
        bullets.add(centerBullet);

        // 右侧子弹
        HeroBullet rightBullet = new HeroBullet(x + 5, y, 3, speedY, power);
        bullets.add(rightBullet);

        return bullets;   
    }
}
