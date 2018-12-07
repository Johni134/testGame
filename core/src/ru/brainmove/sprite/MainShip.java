package ru.brainmove.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.base.Sprite;
import ru.brainmove.math.Rect;
import ru.brainmove.pool.BulletPool;

public class MainShip extends Sprite {

    private Vector2 v0 = new Vector2(0.5f, 0);
    private Vector2 v = new Vector2();

    private boolean pressedLeft;
    private boolean pressedRight;
    private final Sound fire;

    private BulletPool bulletPool;

    private TextureAtlas atlas;

    private Rect worldBounds;
    private boolean mousePressed;
    private Vector2 currentMousePos = new Vector2();

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, Sound fire) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setHeightProportion(0.15f);
        this.bulletPool = bulletPool;
        this.atlas = atlas;
        this.mousePressed = false;
        this.fire = fire;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (mousePressed) {
            if (currentMousePos.x > pos.x)
                moveRight();
            else if (currentMousePos.x < pos.x)
                moveLeft();
            else
                stop();
        }
        if (isInWorldByX(worldBounds, pos.cpy().mulAdd(v, delta).x))
            pos.mulAdd(v, delta);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mousePressed = true;
        currentMousePos = touch;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mousePressed = false;
        stop();
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        currentMousePos = touch;
        return false;
    }

    private void moveRight() {
        v.set(v0);
    }


    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    public void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, atlas.findRegion("bulletMainShip"), pos, new Vector2(0, 0.5f), 0.01f, worldBounds, 1);
        fire.play();
    }
}
