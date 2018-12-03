package ru.brainmove.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.base.Base2DScreen;

public class MenuScreen extends Base2DScreen {
    private Texture img;
    //private Texture wallpaper;
    // image position
    private Vector2 imgPostition;
    // vector to move up
    private Vector2 vectorMoveUp;
    // vector to move right
    private Vector2 vectorMoveRight;

    // touch vector
    private Vector2 touch;
    // buffer vector
    private Vector2 buf;

    private Vector2 v;

    private Vector2 multedImgPosition;

    private boolean movingByMouse = false;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");

        imgPostition = new Vector2(0, 0);
        v = new Vector2(0.5f, 0.3f);
        vectorMoveRight = new Vector2(5f, 0);
        vectorMoveUp = new Vector2(0, 5f);
        multedImgPosition = new Vector2();

        touch = new Vector2();
        buf = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (movingByMouse) {
            buf.set(touch);
            if (buf.sub(imgPostition).len() >= 0.5f) {
                imgPostition.add(v);
            } else {
                imgPostition.set(touch);
            }
        }
        batch.begin();
        // адаптация к новым координатам
        multedImgPosition = imgPostition.cpy();
        multedImgPosition.mul(screenToWorld);
        batch.draw(img, multedImgPosition.x, multedImgPosition.y, 0.3f * COORDINATE_NETWORK_CONST, 0.3f * COORDINATE_NETWORK_CONST);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        movingByMouse = true;
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        v.set(touch.cpy().sub(imgPostition).setLength(0.5f));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        movingByMouse = false;
        switch (keycode) {
            case Input.Keys.DPAD_UP:
                imgPostition.add(vectorMoveUp);
                break;
            case Input.Keys.DPAD_DOWN:
                imgPostition.sub(vectorMoveUp);
                break;
            case Input.Keys.DPAD_LEFT:
                imgPostition.sub(vectorMoveRight);
                break;
            case Input.Keys.DPAD_RIGHT:
                imgPostition.add(vectorMoveRight);
                break;
        }
        return super.keyDown(keycode);
    }
}
