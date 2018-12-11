package ru.brainmove.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.brainmove.base.ScaledButton;
import ru.brainmove.math.Rect;
import ru.brainmove.screen.GameScreen;

public class ButtonPlay extends ScaledButton {

    private Game game;

    public ButtonPlay(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btPlay"));
        setHeightProportion(0.2f);
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + 0.05f);
        setLeft(worldBounds.getLeft() + 0.05f);
    }

    @Override
    protected void actionPerformed() {
        game.setScreen(new GameScreen(game));
    }
}
