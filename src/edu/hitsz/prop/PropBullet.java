package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.HeroPlusBulletStrategy;

public class PropBullet extends BaseProp {

    public PropBullet(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    
    @Override
    public void effect(AbstractFlyingObject heroAircraft) {
        ((HeroAircraft) heroAircraft).setBulletStrategy(new HeroPlusBulletStrategy());
    }
}
