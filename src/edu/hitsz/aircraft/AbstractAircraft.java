package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.BulletStrategy;
import edu.hitsz.strategy.NoBulletStrategy;
import edu.hitsz.strategy.SingleBulletStrategy;
import edu.hitsz.basic.AbstractFlyingObject;
import java.util.List;

/**
 * 所有种类飞机的抽象父类
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {

    //最大生命值
    protected int maxHp;
    protected int hp;

    // 子弹发射策略,允许继承类可见
    protected BulletStrategy bulletStrategy = new NoBulletStrategy();// 默认不发射子弹


    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public void increaseHp(int increase){
        hp += increase;
        if(hp > maxHp){
            hp = maxHp;
        }
    }

    public int getHp() {
        return hp;
    }

    public void setBulletStrategy(BulletStrategy bulletStrategy) {
        this.bulletStrategy = bulletStrategy;
    }


    /**
     * 飞机射击方法
     * @return
     *  可射击对象需实现，返回子弹列表
     *  非可射击对象空实现，返回空列表
     */
    public abstract List<BaseBullet> shoot();

}


