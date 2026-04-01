package edu.hitsz.aircraft;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

public class ElitePlusEnemy extends AbstractAircraft
{
    //每次射击发射子弹数量
    private int shootNum = 3;

    //子弹威力
    private int power = 30;

    //子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = 1;

    // 左右移动方向
    private int moveDirection = 1; // 1向右，-1向左

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
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
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet bullet;

        // 扇形三发子弹：左、中、右
        int[] speedXs = {-3, 0, 3}; // 左斜、中直、右斜
        for(int i=0; i<shootNum; i++){
            bullet = new EnemyBullet(x, y, speedXs[i], speedY, power);
            res.add(bullet);
        }
        return res;
    }
}
