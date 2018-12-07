package ru.brainmove.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.brainmove.base.ScaledButton;
import ru.brainmove.math.Rect;

public class ButtonExit extends ScaledButton {

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("btExit"));
        setHeightProportion(0.15f);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + 0.05f);
        setRight(worldBounds.getRight() - 0.05f);
    }

    @Override
    public void actionPerformed() {
        Gdx.app.exit();
    }
}
