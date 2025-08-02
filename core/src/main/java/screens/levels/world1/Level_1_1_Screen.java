package screens.levels.world1;

import classes.InputHandler;
import classes.enemies.slime.Slime;
import classes.platforms.Platform;
import classes.platforms.PlatformAerial;
import classes.platforms.PlatformGround;
import classes.player.PlayerState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.hod.ultiodivina.Main;
import screens.VictoryScreen;
import screens.levels.BaseLevel;

public class Level_1_1_Screen extends BaseLevel {

    public Level_1_1_Screen(Game game){
        super(game);
    }

    @Override
    public void show(){
        super.show();

        //--- LEVEL CONFIGURATION ---
        this.player.restartScore();
        this.player.restartLife();
        this.player.setPlayerState(PlayerState.IDLE);
        this.spawnPointX = 64;
        this.spawnPointY = PLATFORM_HEIGHT;
        this.backgroundTexture = new Texture(Gdx.files.internal("backgrounds/level1/nivel1-1.jpeg"));
        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/world1BackgroundMusic.ogg"));
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();
    }

    @Override
    public void render(float delta){
        super.render(delta);
        if(enemies.isEmpty()){
            game.setScreen(new VictoryScreen(game, new Level_1_2_Screen(game)));
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

        platforms.add(new PlatformGround(0, 0));
        float currentY = VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, "large"));
        platforms.add(new PlatformAerial(VIRTUAL_WIDTH - AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, "large"));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, "left"));
        platforms.add(new PlatformAerial(VIRTUAL_WIDTH - AERIAL_PLATFORM_WIDTH, (int) currentY, "right"));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial((VIRTUAL_WIDTH / 2) - (AERIAL_LONG_PLATFORM_WIDTH / 2), (int) currentY, "large"));

    }

    @Override
    public void setupLevelEnemies(){
        for(Platform platform : platforms) {
            enemies.add(new Slime((int) (platform.getBounds().getX() + platform.getBounds().getWidth() - SLIME_WIDTH), (int) platform.getBounds().getY() + PLATFORM_HEIGHT));
        }
    }

    public void setupPlayer(){
        this.player = ((Main) game).player;
        this.player.getBounds().setPosition(spawnPointX, spawnPointY);
        this.inputHandler = new InputHandler(this.player);
    }
}
