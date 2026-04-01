package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import java.util.LinkedList;
import java.util.List;

public class NoBulletStrategy implements AbstractBulletStrategy {
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        return new LinkedList<>(); // 返回一个空列表，表示不发射子弹       
    }
    
}
