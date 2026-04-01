package edu.hitsz.aircraft.Factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteProEnemy;

public class EliteProEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new EliteProEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
