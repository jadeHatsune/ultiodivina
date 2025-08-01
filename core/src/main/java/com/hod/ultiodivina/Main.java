package com.hod.ultiodivina;

import classes.player.Player;
import com.badlogic.gdx.Game;
import screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public Player player;

    @Override
    public void create() {
        this.player = new Player(0, 0);
        setScreen(new MainMenuScreen(this));
    }

}
