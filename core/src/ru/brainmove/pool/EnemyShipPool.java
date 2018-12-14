package ru.brainmove.pool;

import ru.brainmove.base.SpritesPool;
import ru.brainmove.math.Rect;
import ru.brainmove.sprite.EnemyShip;
import ru.brainmove.sprite.MainShip;

public class EnemyShipPool extends SpritesPool<EnemyShip> {

    private BulletPool bulletPool;
    private MainShip mainShip;
    private Rect worldBounds;

    public EnemyShipPool(BulletPool bulletPool, MainShip mainShip, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.mainShip = mainShip;
        this.worldBounds = worldBounds;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool, mainShip, worldBounds);
    }
}
