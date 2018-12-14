package ru.brainmove.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.brainmove.base.ScaledButton;
import ru.brainmove.math.Rect;
import ru.brainmove.screen.GameScreen;

public class NewGame extends ScaledButton {

    private GameScreen gameScreen;

    public NewGame(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("message_new_game"));
        setHeightProportion(0.05f);
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + getHeight());
        setLeft(worldBounds.getLeft() + worldBounds.getHalfWidth() - getHalfWidth());
    }

    @Override
    protected void actionPerformed() {
        gameScreen.startNewGame();
    }
}
