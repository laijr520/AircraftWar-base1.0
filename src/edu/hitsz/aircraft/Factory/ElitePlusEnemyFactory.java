package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.enemy.ElitePlusEnemy;
import edu.hitsz.gameConfig.GameConfig;

public class ElitePlusEnemyFactory implements EnemyFactory {

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new ElitePlusEnemy(
            locationX, 
            locationY, 
            GameConfig.getInstance().elitePlusEnemyAircraftParams.speedX(),
            GameConfig.getInstance().elitePlusEnemyAircraftParams.speedY(), 
            GameConfig.getInstance().elitePlusEnemyAircraftParams.maxHp()
        );
    }
}
