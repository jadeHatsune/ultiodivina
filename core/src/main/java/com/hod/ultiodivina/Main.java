package com.hod.ultiodivina;

import classes.player.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import screens.LoadingScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public Player player;
    public AssetManager assetManager;

    @Override
    public void create() {
        this.player = new Player(0, 0);
        assetManager = new AssetManager();
        setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose(){
        super.dispose();
        assetManager.dispose();
    }

}
