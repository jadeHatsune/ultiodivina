package screens.levels.world1;

import classes.Inputs.InputHandler;
import classes.enemies.slime.Slime;
import classes.platforms.Platform;
import classes.platforms.PlatformAerial;
import classes.platforms.PlatformGround;
import classes.player.PlayerState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.hod.ultiodivina.Main;
import screens.VictoryScreen;
import screens.levels.BaseLevel;

import static classes.AssetDescriptors.*;

public class Level_1_1_Screen extends BaseLevel {

    public Level_1_1_Screen(Game game){
        super(game);
    }

    @Override
    public void show(){
        super.show();

        //--- LEVEL CONFIGURATION ---
        this.levelWidth = 800;
        this.levelHeight = 600;
        this.player.restartScore();
        this.player.restartLife();
        this.player.setPlayerState(PlayerState.IDLE);
        this.spawnPointX = 64;
        this.spawnPointY = PLATFORM_HEIGHT;
        this.backgroundTexture = assetManager.get(BG_LEVEL_1_1);
        this.backgroundMusic = assetManager.get(WORLD1_SONG);
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();

        setupLevelPlatforms();
        setupLevelEnemies();
        setupPlayer();
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

        platforms.add(new PlatformGround(0, 0, assetManager.get(PLATFORM_GROUND, Texture.class)));
        float currentY = VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LEFT, Texture.class)));
        platforms.add(new PlatformAerial((int) (levelWidth - AERIAL_PLATFORM_WIDTH), (int) currentY, assetManager.get(PLATFORM_AERO_RIGHT, Texture.class)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LEFT, Texture.class)));
        platforms.add(new PlatformAerial((int) (levelWidth - AERIAL_PLATFORM_WIDTH), (int) currentY, assetManager.get(PLATFORM_AERO_RIGHT, Texture.class)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial((int) ((levelWidth / 2) - (AERIAL_LONG_PLATFORM_WIDTH / 2)), (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));

    }

    @Override
    public void setupLevelEnemies(){
        for(Platform platform : platforms) {
            enemies.add(new Slime((int) (platform.getBounds().getX() + platform.getBounds().getWidth() - SLIME_WIDTH), (int) platform.getBounds().getY() + PLATFORM_HEIGHT, getAnimationSprite(1, 7, assetManager.get(SLIME, Texture.class))));
        }
    }

    @Override
    public void setupPlayer(){
        this.player = ((Main) game).player;
        this.player.getBounds().setPosition(spawnPointX, spawnPointY);
        this.inputHandler = new InputHandler(this.player);
    }
}
