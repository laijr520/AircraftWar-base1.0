package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.gameConfig.GameConfig;
;
public class PropBlood extends BaseProp {

    public PropBlood(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        heroAircraft.increaseHp(GameConfig.getInstance().propBloodParams.effectValue());
    }
}
