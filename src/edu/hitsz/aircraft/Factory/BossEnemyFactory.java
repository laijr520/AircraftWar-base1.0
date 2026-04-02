package edu.hitsz.aircraft.factory;

import java.util.Random;
import java.util.function.Supplier;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.enemy.BossEnemy;

public class BossEnemyFactory implements EnemyFactory {
    private final Random random = new Random();

    private final Supplier<Integer> speedXSupplier = () -> random.nextBoolean() ? -5 : 5;

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        int randomSpeedX = speedXSupplier.get();
        //使用随机生成的 speedX
        return new BossEnemy(
            locationX, 
            locationY, 
            randomSpeedX, 
            0, 
            hp
        );
    }
}