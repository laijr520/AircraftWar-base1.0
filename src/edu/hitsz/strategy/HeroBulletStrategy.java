package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class HeroBulletStrategy  implements AbstractBulletStrategy {
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        
        //子弹射击方向 (向上发射：-1，向下发射：1)
        int direction = -1;
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction*5; // 设置子弹的速度
        int power = 30;

        BaseBullet bullet = new HeroBullet(x, y, speedX, speedY, power);
        bullets.add(bullet);
        return bullets;   
    }
    
}
