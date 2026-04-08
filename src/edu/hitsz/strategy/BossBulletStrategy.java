package edu.hitsz.strategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

public class BossBulletStrategy implements AbstractBulletStrategy {
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        int power = 30;
        
        // 环射子弹总数
        int shootNum = 20;
        // 子弹的基础合速度大小
        int baseSpeed = 10; 

        // 将 360 度平均分配给每一枚子弹
        for (int i = 0; i < shootNum; i++) {
            // 计算当前子弹的弧度角度: (2 * PI / 总数) * 当前序号
            double angle = (2 * Math.PI / shootNum) * i;
            
            /*
             * 利用三角函数计算分速度：
             * speedX = V * cos(angle)
             * speedY = V * sin(angle)
             */
            int speedX = (int) (baseSpeed * Math.cos(angle));
            int speedY = (int) (baseSpeed * Math.sin(angle));

            // 创建并添加子弹
            BaseBullet bullet = new EnemyBullet(x, y, speedX, speedY, power);
            bullets.add(bullet);
        }

        return bullets;
    }
}
