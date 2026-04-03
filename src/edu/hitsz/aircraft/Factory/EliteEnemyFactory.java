package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.gameConfig.GameConfig;

public class EliteEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new EliteEnemy(
            locationX, 
            locationY,
            GameConfig.getInstance().eliteEnemyAircraftParams.speedX(), 
            GameConfig.getInstance().eliteEnemyAircraftParams.speedY(), 
            GameConfig.getInstance().eliteEnemyAircraftParams.maxHp()
        );
    }
}