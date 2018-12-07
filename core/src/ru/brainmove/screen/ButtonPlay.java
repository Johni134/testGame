package ru.brainmove.screen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.brainmove.base.Sprite;
import ru.brainmove.math.Rect;

public class ButtonPlay extends Sprite {

    private float mainScale;

    public ButtonPlay(TextureAtlas atlas) {
        super(atlas.findRegion("btPlay"));
        setHeightProportion(0.2f);
        setLeft(-0.275f);
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
        setScale(mainScale);
        return super.touchUp(touch, pointer);
    }
}
