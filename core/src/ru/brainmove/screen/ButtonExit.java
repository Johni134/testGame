package ru.brainmove.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.base.Sprite;
import ru.brainmove.math.Rect;

public class ButtonExit extends Sprite {

    private float mainScale;

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("btExit"));
        setHeightProportion(0.155f);
        setLeft(0.1f);
        setBottom(-0.4f);
        mainScale = getScale();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (isMe(touch))
            setScale(mainScale * 1.5f);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        // нажали touchDown и touchUp на кнопке
        if (isMe(touch) && getScale() != mainScale) {
            Gdx.app.exit();
        }
        setScale(mainScale);
        return super.touchUp(touch, pointer);
    }
}
