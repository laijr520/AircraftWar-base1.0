package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.enemy.MobEnemy;
import edu.hitsz.gameConfig.GameConfig;

public class MobEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new MobEnemy(
            locationX, 
            locationY, 
            GameConfig.getInstance().mobEnemyAircraftParams.speedX(), 
            GameConfig.getInstance().mobEnemyAircraftParams.speedY(), 
            GameConfig.getInstance().mobEnemyAircraftParams.maxHp()
        );
    }
}