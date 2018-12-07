package ru.brainmove;

import com.badlogic.gdx.Game;

import ru.brainmove.screen.MenuScreen;

public class Star2DGame extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}
