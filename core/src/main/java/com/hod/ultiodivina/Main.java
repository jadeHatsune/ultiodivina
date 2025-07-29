package com.hod.ultiodivina;

import com.badlogic.gdx.Game;
import screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }

}
