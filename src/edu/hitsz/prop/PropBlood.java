package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.gameConfig.GameConfig;
;
public class PropBlood extends BaseProp {

    public PropBlood(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(AbstractFlyingObject heroAircraft) {
        ((HeroAircraft) heroAircraft).increaseHp(GameConfig.getInstance().propBloodParams.effectValue());
    }
}
