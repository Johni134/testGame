package ru.brainmove.pool;

import ru.brainmove.base.SpritesPool;
import ru.brainmove.sprite.EnemyShip;

public class EnemyShipPool extends SpritesPool<EnemyShip> {
    @Override
    protected EnemyShip newObject() {
        return new EnemyShip();
    }
}
