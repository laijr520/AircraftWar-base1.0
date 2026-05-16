package edu.hitsz.prop;

import edu.hitsz.basic.AbstractFlyingObject;

public class PropBomb extends BaseProp implements Observer {

    public PropBomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
        @Override
    public void effect(AbstractFlyingObject heroAircraft) {
        // TODO: 实现道具效果
    }
    @Override
    public void OnBombActive() {
        // TODO Auto-generated method stub
    }
    @Override
    public void OnFreezeActive() {}
}
