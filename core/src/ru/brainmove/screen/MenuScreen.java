package ru.brainmove.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.base.Base2DScreen;

public class MenuScreen extends Base2DScreen {
    private Texture img;
    private Texture wallpaper;
    // image position
    private Vector2 imgPostition;
    // vector to move up
    private Vector2 vectorMoveUp;
    // vector to move right
    private Vector2 vectorMoveRight;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        wallpaper = new Texture("space.jpg");
        imgPostition = new Vector2(0, 0);
        vectorMoveRight = new Vector2(5f, 0);
        vectorMoveUp = new Vector2(0, 5f);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(wallpaper, 0, 0);
        batch.draw(img, imgPostition.x, imgPostition.y);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
        wallpaper.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        imgPostition.set(screenX, Gdx.graphics.getHeight() - screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case 19:
                imgPostition.add(vectorMoveUp);
                break;
            case 20:
                imgPostition.sub(vectorMoveUp);
                break;
            case 21:
                imgPostition.sub(vectorMoveRight);
                break;
            case 22:
                imgPostition.add(vectorMoveRight);
                break;
        }
        return super.keyDown(keycode);
    }
}
