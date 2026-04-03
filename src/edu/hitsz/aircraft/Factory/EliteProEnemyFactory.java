package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.enemy.EliteProEnemy;
import edu.hitsz.gameConfig.GameConfig;

public class EliteProEnemyFactory implements EnemyFactory {

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new EliteProEnemy(
            locationX, 
            locationY, 
            GameConfig.getInstance().eliteProEnemyAircraftParams.speedX(), 
            GameConfig.getInstance().eliteProEnemyAircraftParams.speedY(),
            GameConfig.getInstance().eliteProEnemyAircraftParams.maxHp()
        );
    }
}