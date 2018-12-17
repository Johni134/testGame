package ru.brainmove.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.brainmove.base.Sprite;
import ru.brainmove.math.Rect;

public class GameOver extends Sprite {
    public GameOver(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
        setHeightProportion(0.05f);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(0);
        setLeft(0);
    }
}
