package ru.brainmove.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.base.Base2DScreen;
import ru.brainmove.math.Rect;
import ru.brainmove.pool.BulletPool;
import ru.brainmove.pool.EnemyShipPool;
import ru.brainmove.sprite.Background;
import ru.brainmove.sprite.MainShip;
import ru.brainmove.sprite.Star;
import ru.brainmove.utils.AnimateTimer;
import ru.brainmove.utils.EnemiesEmitter;
import ru.brainmove.utils.SoundUtils;


public class GameScreen extends Base2DScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private TextureAtlas textureAtlas;

    private Background background;

    private Star[] star;

    private MainShip mainShip;

    private BulletPool bulletPool;
    private EnemyShipPool enemyShipPool;

    private EnemiesEmitter enemiesEmitter;

    private Sound fireSound;
    private Sound enemySound;
    private Music music;

    public GameScreen(Game game) {
        super(game);
    }

    private AnimateTimer animateTimer;

    @Override
    public void show() {
        super.show();
        animateTimer = new AnimateTimer(1f);
        textureAtlas = new TextureAtlas("textures/mainAtlas.tpack");
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
        star = new Star[STAR_COUNT];
        for (int i = 0; i < star.length; i++) {
            star[i] = new Star(textureAtlas);
        }
        fireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gunSilencer.mp3"));
        enemySound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemyShot.mp3"));
        bulletPool = new BulletPool();
        mainShip = new MainShip(textureAtlas, bulletPool, fireSound);
        enemyShipPool = new EnemyShipPool(bulletPool, mainShip, worldBounds);
        enemiesEmitter = new EnemiesEmitter(worldBounds, enemyShipPool, textureAtlas, enemySound);
        music = SoundUtils.initMusic("sounds/music.mp3");
        music.play();
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        deleteAllDestroyed();
        draw();
    }

    public void update(float delta) {
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
        mainShip.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyShipPool.updateActiveSprites(delta);
        enemiesEmitter.generate(delta);
    }

    public void checkCollisions() {

    }

    public void deleteAllDestroyed() {
        enemyShipPool.freeAllDestroyedActiveSprites();
        bulletPool.freeAllDestroyedActiveSprites();
    }

    public void draw() {
        Gdx.gl.glClearColor(1, 0.3f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyShipPool.drawActiveSprites(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        textureAtlas.dispose();
        bulletPool.dispose();
        enemyShipPool.dispose();
        fireSound.dispose();
        music.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
        return super.touchUp(touch, pointer);
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        mainShip.touchDragged(touch, pointer);
        return super.touchDragged(touch, pointer);
    }
}
