package ru.brainmove.pool;

import ru.brainmove.base.SpritesPool;
import ru.brainmove.sprite.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
