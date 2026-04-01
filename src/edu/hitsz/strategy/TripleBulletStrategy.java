
package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import java.util.LinkedList;
import java.util.List;

public class TripleBulletStrategy implements BulletStrategy {
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();

        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        int speedY = 10; // 设置子弹的速度
        int power = 2;

        // 左侧子弹
        EnemyBullet leftBullet = new EnemyBullet(x - 5, y, -3, speedY, power);
        bullets.add(leftBullet);

        // 中间子弹
        EnemyBullet centerBullet = new EnemyBullet(x, y, 0, speedY, power);
        bullets.add(centerBullet);

        // 右侧子弹
        EnemyBullet rightBullet = new EnemyBullet(x + 5, y, 3, speedY, power);
        bullets.add(rightBullet);

        return bullets;
    }
}
