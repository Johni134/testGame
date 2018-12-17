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

import java.util.List;

import ru.brainmove.base.Base2DScreen;
import ru.brainmove.math.Rect;
import ru.brainmove.pool.BulletPool;
import ru.brainmove.pool.EnemyShipPool;
import ru.brainmove.pool.ExplosionPool;
import ru.brainmove.sprite.Background;
import ru.brainmove.sprite.Bullet;
import ru.brainmove.sprite.EnemyShip;
import ru.brainmove.sprite.GameOver;
import ru.brainmove.sprite.MainShip;
import ru.brainmove.sprite.NewGame;
import ru.brainmove.sprite.Star;
import ru.brainmove.utils.AnimateTimer;
import ru.brainmove.utils.EnemiesEmitter;
import ru.brainmove.utils.HealthBar;
import ru.brainmove.utils.SoundUtils;


public class GameScreen extends Base2DScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private TextureAtlas textureAtlas;

    private Background background;

    private Star[] star;

    private MainShip mainShip;

    private GameOver gameOver;
    private NewGame newGame;

    private BulletPool bulletPool;
    private EnemyShipPool enemyShipPool;
    private ExplosionPool explosionPool;

    private EnemiesEmitter enemiesEmitter;

    private Sound fireSound;
    private Sound enemySound;
    private Sound explosionSound;
    private Music music;

    private HealthBar healthBar;

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
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        enemySound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemyShot.mp3"));
        explosionPool = new ExplosionPool(textureAtlas, explosionSound);
        bulletPool = new BulletPool();
        mainShip = new MainShip(textureAtlas, bulletPool, explosionPool, fireSound);

        enemyShipPool = new EnemyShipPool(bulletPool, explosionPool, mainShip, worldBounds, enemySound);
        enemiesEmitter = new EnemiesEmitter(worldBounds, enemyShipPool, textureAtlas);

        gameOver = new GameOver(textureAtlas);
        newGame = new NewGame(textureAtlas, this);

        healthBar = new HealthBar();
        music = SoundUtils.initMusic("sounds/music.mp3");
        music.play();
    }

    public void startNewGame() {
        explosionPool.dispose();
        enemyShipPool.dispose();
        bulletPool.dispose();
        mainShip.setLeft(worldBounds.getLeft() + worldBounds.getHalfWidth() - mainShip.getHalfWidth());
        mainShip.setHp(mainShip.getFullHp());
        mainShip.setDestroyed(false);
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
        if (!mainShip.isDestroyed()) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyShipPool.updateActiveSprites(delta);
            enemiesEmitter.generate(delta);
        }
        explosionPool.updateActiveSprites(delta);
    }

    public void checkCollisions() {
        List<EnemyShip> enemyList = enemyShipPool.getActiveObjects();
        for (EnemyShip enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (enemy.pos.dst2(mainShip.pos) < minDist * minDist) {
                enemy.setDestroyed(true);
                enemy.boom();
                mainShip.damage(mainShip.getHp());
                return;
            }
        }

        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (EnemyShip enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.setDestroyed(true);
                }
            }
        }

        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed() || bullet.getOwner() == mainShip) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                bullet.setDestroyed(true);
                mainShip.damage(bullet.getDamage());
            }
        }
    }

    public void deleteAllDestroyed() {
        enemyShipPool.freeAllDestroyedActiveSprites();
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
    }

    public void draw() {
        Gdx.gl.glClearColor(1, 0.3f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }
        if (!mainShip.isDestroyed()) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyShipPool.drawActiveSprites(batch);
            explosionPool.drawActiveSprites(batch);
            healthBar.setCurMaxValue(mainShip.getHp(), mainShip.getFullHp());
            healthBar.draw(batch, 1);
        } else {
            gameOver.draw(batch);
            newGame.draw(batch);
        }
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
        healthBar.setPosition(worldBounds.getLeft(), worldBounds.getBottom());
        healthBar.setSize(worldBounds.getWidth(), 0.01f);
        newGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        textureAtlas.dispose();
        bulletPool.dispose();
        enemyShipPool.dispose();
        explosionPool.dispose();
        fireSound.dispose();
        music.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        newGame.touchDown(touch, pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
        newGame.touchUp(touch, pointer);
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
