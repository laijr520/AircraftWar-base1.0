package edu.hitsz.prop;

import edu.hitsz.basic.AbstractFlyingObject;

public class PropFreeze extends BaseProp implements Observer {

    public PropFreeze(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(AbstractFlyingObject heroAircraft) {
        
    }

    @Override
    public void OnBombActive() {}
    @Override
    public void OnFreezeActive() {
        //TODO
    }
    
}
