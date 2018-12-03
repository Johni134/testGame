package ru.brainmove.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.brainmove.base.Base2DScreen;
import ru.brainmove.math.Rect;
import ru.brainmove.sprite.Background;

public class MenuScreen extends Base2DScreen {
    private static final int STAR_COUNT = 256;

    private Texture bg;
    private TextureAtlas textureAtlas;

    private Background background;

    private Star[] star;


    @Override
    public void show() {
        super.show();
        textureAtlas = new TextureAtlas("textures/menuAtlas.tpack");
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
        star = new Star[STAR_COUNT];
        for (int i = 0; i < star.length; i++) {
            star[i] = new Star(textureAtlas);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    public void update(float delta) {
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
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
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        bg.dispose();
        super.dispose();
    }
}
