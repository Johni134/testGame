package ru.brainmove.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.base.Sprite;
import ru.brainmove.math.Rect;

public class EnemyShip extends Sprite {

    private Rect worldBounds;

    private Vector2 v = new Vector2();

    public EnemyShip() {

    }

    public void set(
            TextureRegion[] regions,
            Vector2 pos0,
            Vector2 v0,
            float height,
            Rect worldBounds
    ) {
        this.regions = regions;
        this.pos.set(pos0);
        this.v.set(v0);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        if (this.worldBounds.getLeft() > getLeft())
            setLeft(worldBounds.getLeft());
        if (this.worldBounds.getRight() < getRight())
            setRight(worldBounds.getRight());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) {
            setDestroyed(true);
        }
    }
}
