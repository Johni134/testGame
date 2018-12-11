package ru.brainmove.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SoundUtils {

    public static Music initMusic(String path) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.setLooping(true);
        music.setVolume(0.3f);
        return music;
    }

}
