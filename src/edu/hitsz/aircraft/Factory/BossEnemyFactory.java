package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.gameConfig.GameConfig;

public class BossEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new BossEnemy(
            locationX, 
            locationY, 
            GameConfig.getInstance().bossEnemyAircraftParams.speedX(), 
            GameConfig.getInstance().bossEnemyAircraftParams.speedY(), 
            GameConfig.getInstance().bossEnemyAircraftParams.maxHp()
        );
    }
}