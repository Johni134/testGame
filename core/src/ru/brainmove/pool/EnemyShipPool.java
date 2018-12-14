package ru.brainmove.pool;

import com.badlogic.gdx.audio.Sound;

import ru.brainmove.base.SpritesPool;
import ru.brainmove.math.Rect;
import ru.brainmove.sprite.EnemyShip;
import ru.brainmove.sprite.MainShip;

public class EnemyShipPool extends SpritesPool<EnemyShip> {

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private MainShip mainShip;
    private Rect worldBounds;
    private Sound shootSound;

    public EnemyShipPool(BulletPool bulletPool, ExplosionPool explosionPool, MainShip mainShip, Rect worldBounds, Sound shootSound) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.mainShip = mainShip;
        this.worldBounds = worldBounds;
        this.shootSound = shootSound;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool, explosionPool, mainShip, worldBounds, shootSound);
    }
}
