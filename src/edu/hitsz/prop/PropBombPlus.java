package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class PropBombPlus extends BaseProp {

    public PropBombPlus(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        // TODO 炸弹强化道具2
        System.console().printf("PropBombPlus effect! \n");
    }
    
}
