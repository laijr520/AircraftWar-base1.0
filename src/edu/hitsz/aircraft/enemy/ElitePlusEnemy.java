package edu.hitsz.aircraft.enemy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.TripleEnemyBulletStrategy;

public class ElitePlusEnemy extends AbstractAircraft
{
    // 左右移动方向
    private int moveDirection = 1; // 1向右，-1向左

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setBulletStrategy(new TripleEnemyBulletStrategy());  // 设置为三发子弹策略
    }

    @Override
    public void forward() {
        super.forward();
        // 左右移动
        locationX += moveDirection * 2;
        // 边界检测并反弹
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH - 50) { // 50是飞机宽度近似值
            moveDirection = -moveDirection;
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
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
