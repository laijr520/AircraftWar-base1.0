package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.HeroProBulletStrategy;

public class PropBulletPlus extends BaseProp {

    public PropBulletPlus(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        heroAircraft.setBulletStrategy(new HeroProBulletStrategy());
    }
    
}
