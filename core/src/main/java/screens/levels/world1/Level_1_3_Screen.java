package screens.levels.world1;

import classes.InputHandler;
import classes.platforms.PlatformGround;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.hod.ultiodivina.Main;
import screens.VictoryScreen;
import screens.levels.BaseLevel;

public class Level_1_3_Screen extends BaseLevel {

    //--- LEVEL ATTRIBUTES ---


    public Level_1_3_Screen(Game game) {
        super(game);
    }

    @Override
    public void show(){
        super.show();

        //--- LEVEL CONFIGURATION ---
        this.levelWidth = 2400;
        this.levelHeight = 1800;
        this.spawnPointX = (int)(levelWidth / 2 - this.player.getBounds().getWidth() / 2);
        this.spawnPointY = PLATFORM_HEIGHT;
        this.backgroundTexture = new Texture(Gdx.files.internal("backgrounds/level1/nivel1-3.png"));
        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/bossWorld1BackgroundMusic.ogg"));
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();
    }

    @Override
    public void render(float delta){
        super.render(delta);
        if(enemies.isEmpty()){
            //game.setScreen(new VictoryScreen(game, new Level_1_3_Screen(game)));
        }
    }

    @Override
    public void dispose(){
        super.dispose();
    }

    @Override
    public void hide(){
        super.hide();
    }

    @Override
    public void setupLevelPlatforms() {
        platforms.add(new PlatformGround(0,0));
        platforms.add(new PlatformGround(800,0));
        platforms.add(new PlatformGround(1600,0));
    }

    @Override
    public void setupLevelEnemies() {

    }

    @Override
    public void setupPlayer() {
        this.player = ((Main) game).player;
        this.player.getBounds().setPosition(spawnPointX, spawnPointY);
        this.inputHandler = new InputHandler(this.player);
    }
}
