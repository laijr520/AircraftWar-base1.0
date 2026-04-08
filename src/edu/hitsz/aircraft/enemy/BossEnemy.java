package edu.hitsz.aircraft.enemy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.BossBulletStrategy;

public class BossEnemy extends AbstractAircraft {
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setBulletStrategy(new BossBulletStrategy());
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        if(bulletStrategy != null){
            return bulletStrategy.fire(this);
        }else{
            return new LinkedList<>();
        } 
    }

}
