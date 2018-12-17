package ru.brainmove.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.brainmove.base.Base2DScreen;
import ru.brainmove.base.Font;
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
    private static final float FONT_SIZE = 0.02f;

    private static final float LEVEL_FONT_SIZE = 0.05f;

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private static final String LEVEL_TEXT = "LEVEL ";

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
    private State state;
    private int frags;
    private int level;
    private Font font;
    private Font levelFont;
    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbHP = new StringBuilder();
    private StringBuilder sbLevel = new StringBuilder();
    private StringBuilder sbLevelText = new StringBuilder();
    private StringBuilder sbLevelTimer = new StringBuilder();
    private float levelTimeInterval = 4f;
    private float nextLevelTimer = 0f;

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
        mainShip = new MainShip(textureAtlas, bulletPool, explosionPool, worldBounds, fireSound);

        enemyShipPool = new EnemyShipPool(bulletPool, explosionPool, mainShip, worldBounds, enemySound);
        enemiesEmitter = new EnemiesEmitter(worldBounds, enemyShipPool, textureAtlas);

        gameOver = new GameOver(textureAtlas);
        newGame = new NewGame(textureAtlas, this);

        healthBar = new HealthBar();
        music = SoundUtils.initMusic("sounds/music.mp3");
        music.play();
        font = new Font("font/font.fnt", "font/font.png");
        font.setFontSize(FONT_SIZE);

        levelFont = new Font("font/old_english.fnt", "font/old_english.png");
        levelFont.setColor(Color.YELLOW);
        levelFont.setFontSize(LEVEL_FONT_SIZE);
        startNewGame(1);
    }

    public GameScreen(Game game) {
        super(game);
    }

    private AnimateTimer animateTimer;

    public void startNewGame(int level) {
        state = State.PLAYING;

        frags = 0;
        this.level = level;
        mainShip.setToNewGame();

        bulletPool.freeAllActiveObjects();
        enemyShipPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();

        mainShip.setLeft(worldBounds.getLeft() + worldBounds.getHalfWidth() - mainShip.getHalfWidth());
        mainShip.setHp(mainShip.getFullHp());
        mainShip.setDestroyed(false);
    }

    public void update(float delta) {
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
        if (state == State.PLAYING) {
            if (!mainShip.isDestroyed()) {
                mainShip.update(delta);
                bulletPool.updateActiveSprites(delta);
                enemyShipPool.updateActiveSprites(delta);
                enemiesEmitter.generate(delta, level);
            }
        }
        if (state == State.NEXT_LEVEL) {
            nextLevelTimer += delta;
            if (nextLevelTimer >= levelTimeInterval) {
                nextLevelTimer = 0f;
                startNewGame(level);
            }
        }
        explosionPool.updateActiveSprites(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        deleteAllDestroyed();
        draw();
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
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
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
                    if (enemy.isDestroyed()) {
                        frags++;
                        setLevel(frags >= 10);
                    }
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
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
            }
        }
    }

    public void setLevel(boolean isNextLevel) {
        if (isNextLevel) {
            this.level++;
            state = State.NEXT_LEVEL;
        }
    }

    public void draw() {
        Gdx.gl.glClearColor(1, 0.3f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        switch (state) {
            case PLAYING:
                if (!mainShip.isDestroyed()) {
                    mainShip.draw(batch);
                }
                bulletPool.drawActiveSprites(batch);
                enemyShipPool.drawActiveSprites(batch);
                healthBar.setCurMaxValue(mainShip.getHp(), mainShip.getFullHp());
                healthBar.draw(batch, 1);
                break;
            case GAME_OVER:
                gameOver.draw(batch);
                newGame.draw(batch);
                break;
            case NEXT_LEVEL:
                sbLevelText.setLength(0);
                sbLevelTimer.setLength(0);
                levelFont.draw(batch, sbLevelText.append(LEVEL_TEXT).append(level), worldBounds.pos.x, worldBounds.pos.y, Align.center);
                levelFont.draw(batch, sbLevelTimer.append((int) (levelTimeInterval - nextLevelTimer) + 1), worldBounds.pos.x, worldBounds.pos.y - LEVEL_FONT_SIZE, Align.center);
                break;
        }
        printInfo();
        batch.end();
    }

    public void deleteAllDestroyed() {
        enemyShipPool.freeAllDestroyedActiveSprites();
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
    }

    public void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft(), worldBounds.getTop());
        font.draw(batch, sbHP.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop(), Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(level), worldBounds.getRight(), worldBounds.getTop(), Align.right);
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
        font.dispose();
        super.dispose();
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
    public boolean touchDown(Vector2 touch, int pointer) {
        switch (state) {
            case PLAYING:
                mainShip.touchDown(touch, pointer);
                break;
            case GAME_OVER:
                newGame.touchDown(touch, pointer);
                break;
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        switch (state) {
            case PLAYING:
                mainShip.touchUp(touch, pointer);
                break;
            case GAME_OVER:
                newGame.touchUp(touch, pointer);
                break;
        }
        return super.touchUp(touch, pointer);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            mainShip.touchDragged(touch, pointer);
        }
        return super.touchDragged(touch, pointer);
    }

    private enum State {PLAYING, GAME_OVER, NEXT_LEVEL}
}
