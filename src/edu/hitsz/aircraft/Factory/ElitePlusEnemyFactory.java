package edu.hitsz.aircraft.Factory;
import java.util.function.Supplier;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.ElitePlusEnemy;

import java.util.Random;

public class ElitePlusEnemyFactory implements EnemyFactory {

    private final Random random = new Random();

    private final Supplier<Integer> speedXSupplier = () -> random.nextBoolean() ? -5 : 5;

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        int randomSpeedX = speedXSupplier.get();
        //使用随机生成的 speedX
        return new ElitePlusEnemy(
            locationX, 
            locationY, 
            randomSpeedX, 
            5, 
            hp
        );
    }
}
