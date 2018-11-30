package ru.brainmove.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.math.MatrixUtils;
import ru.brainmove.math.Rect;

public class Base2DScreen  implements Screen, InputProcessor {

    // переменная для изменения координационной сетки
    protected final float COORDINATE_NETWORK_CONST = 42f;
    protected SpriteBatch batch;
    protected Matrix4 worldToGl;
    protected Matrix3 screenToWorld;
    private Rect screenBounds; // границы области рисования в пикселях
    private Rect worldBounds; // границы проекции мировых координат
    private Rect glBounds; // дефолтные границы проекции мир - gl
    private Vector2 touch = new Vector2();

    public Base2DScreen() {
        this.screenBounds = new Rect();
        this.worldBounds = new Rect();
        this.glBounds = new Rect(0, 0, 1f, 1f);
        this.worldToGl = new Matrix4();
        this.screenToWorld = new Matrix3();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        batch.getProjectionMatrix().idt();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize width = " + width + " height = " + height);
        screenBounds.setSize(width, height);
        screenBounds.setLeft(0);
        screenBounds.setBottom(0);

        float aspect = width / (float) height;
        worldBounds.setHeight(COORDINATE_NETWORK_CONST);
        worldBounds.setWidth(COORDINATE_NETWORK_CONST * aspect);
        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        batch.setProjectionMatrix(worldToGl);
        MatrixUtils.calcTransitionMatrix(screenToWorld, screenBounds, worldBounds);
        resize(worldBounds);
    }

    private void resize(Rect worldBounds) {
        System.out.println("resize worldBounds width = " + worldBounds.getWidth() + " worldBounds height = " + worldBounds.getHeight());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchDown screenX = " + screenX + " screenY = " + screenY);
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDown(touch, pointer);
        return false;
    }

    private void touchDown(Vector2 touch, int pointer) {
        System.out.println("touchDown touch.x = " + touch.x + " touch.y = " + touch.y);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchUp screenX = " + screenX + " screenY = " + screenY);
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchUp(touch, pointer);
        return false;
    }

    private void touchUp(Vector2 touch, int pointer) {
        System.out.println("touchUp touch.x = " + touch.x + " touch.y = " + touch.y);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println("touchDragged screenX = " + screenX + " screenY = " + screenY);
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDragged(touch, pointer);
        return false;
    }

    private void touchDragged(Vector2 touch, int pointer) {
        System.out.println("touchDragged touch.x = " + touch.x + " touch.y = " + touch.y);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
