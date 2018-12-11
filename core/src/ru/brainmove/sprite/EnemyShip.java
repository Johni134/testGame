package ru.brainmove.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.base.Ship;
import ru.brainmove.math.Rect;
import ru.brainmove.pool.BulletPool;

public class EnemyShip extends Ship {

    private MainShip mainShip;
    private Vector2 v0 = new Vector2();

    public EnemyShip(BulletPool bulletPool, MainShip mainShip, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.mainShip = mainShip;
        this.worldBounds = worldBounds;
        this.v.set(v0);
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int bulletDamage,
            float reloadInterval,
            float height,
            int hp,
            Sound shootSound
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0f, bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.hp = hp;
        this.shootSound = shootSound;
        setHeightProportion(height);
        reloadTimer = reloadInterval;
        v.set(v0);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        if (getTop() < worldBounds.getTop()) {
            reloadTimer += delta;
            if (reloadTimer >= reloadInterval) {
                reloadTimer = 0f;
                shoot();
            }
        }
        if (isOutside(worldBounds)) {
            setDestroyed(true);
        }
    }
}
