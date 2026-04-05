package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import java.util.LinkedList;
import java.util.List;

public class DoubleEnemyBulletStrategy implements AbstractBulletStrategy {
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();

        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        int speedX = 0;
        int speedY = 10; // 设置子弹的速度
        int power = 2;

        // 左侧子弹
        EnemyBullet leftBullet = new EnemyBullet(x - 5, y, speedX, speedY, power);
        bullets.add(leftBullet);

        // 右侧子弹
        EnemyBullet rightBullet = new EnemyBullet(x + 5, y, speedX, speedY, power);
        bullets.add(rightBullet);

        return bullets;
    }
    
}
