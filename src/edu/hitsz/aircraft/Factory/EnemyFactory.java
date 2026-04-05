package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;

public abstract  interface  EnemyFactory {
    AbstractAircraft createEnemy(int locationX, int locationY);
}