package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class PropBullet extends BaseProp {

    public PropBullet(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    
    @Override
    public void effect(HeroAircraft heroAircraft) {
        // TODO Auto-generated method stub
        System.console().printf("PropBullet effect! \n");
    }
}
