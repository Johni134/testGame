package ru.brainmove.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.math.Rect;
import ru.brainmove.math.Rnd;
import ru.brainmove.pool.EnemyShipPool;
import ru.brainmove.sprite.EnemyShip;

public class EnemiesEmitter {

    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_SMALL_BULLET_VY = -0.3f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 0.8f;
    private static final int ENEMY_SMALL_HP = 1;
    private final Vector2 enemySmallV = new Vector2(0f, -0.2f);
    private Rect worldBounds;
    private float generateInterval = 4f;
    private float generateTimer;
    private TextureRegion[][] enemySmallRegion = new TextureRegion[3][];
    private TextureRegion bulletRegion;

    private EnemyShipPool enemyPool;

    private Sound fireSound;

    public EnemiesEmitter(Rect worldBounds, EnemyShipPool enemyPool, TextureAtlas atlas, Sound fireSound) {
        this.worldBounds = worldBounds;
        this.enemyPool = enemyPool;
        this.enemySmallRegion[0] = Regions.split(atlas.findRegion("enemy0"), 1, 2, 2);
        this.enemySmallRegion[1] = Regions.split(atlas.findRegion("enemy1"), 1, 2, 2);
        this.enemySmallRegion[2] = Regions.split(atlas.findRegion("enemy2"), 1, 2, 2);

        this.bulletRegion = atlas.findRegion("bulletEnemy");

        this.fireSound = fireSound;
    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            EnemyShip enemy = enemyPool.obtain();

            enemy.set(
                    enemySmallRegion[(int) ((Math.random() * this.enemySmallRegion.length) - 0.1f)],
                    enemySmallV,
                    bulletRegion,
                    ENEMY_SMALL_BULLET_HEIGHT,
                    ENEMY_SMALL_BULLET_VY,
                    ENEMY_SMALL_BULLET_DAMAGE,
                    ENEMY_SMALL_RELOAD_INTERVAL,
                    ENEMY_SMALL_HEIGHT,
                    ENEMY_SMALL_HP,
                    fireSound
            );
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(), worldBounds.getRight() - enemy.getHalfWidth());
            enemy.setBottom(worldBounds.getTop());
        }
    }
}
