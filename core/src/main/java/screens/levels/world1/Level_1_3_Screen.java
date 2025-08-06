package screens.levels.world1;

import classes.Inputs.InputHandler;
import classes.platforms.PlatformAerial;
import classes.platforms.PlatformGround;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import screens.levels.BaseLevel;

import static classes.AssetDescriptors.*;

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
        this.spawnPointX = 64;
        this.spawnPointY = PLATFORM_HEIGHT;
        this.backgroundTexture = assetManager.get(BG_LEVEL_1_3);
        this.backgroundMusic = assetManager.get(BOSS1_SONG);
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
        platforms.add(new PlatformGround(0,0, assetManager.get(PLATFORM_GROUND, Texture.class)));
        platforms.add(new PlatformGround(800,0, assetManager.get(PLATFORM_GROUND, Texture.class)));
        platforms.add(new PlatformGround(1600,0, assetManager.get(PLATFORM_GROUND, Texture.class)));

        float currentY = VERTICAL_SPACING;
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH*2, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH*3, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH*4, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH*2, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH*3 + AERIAL_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LEFT, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH*2, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH * 2, (int) currentY, assetManager.get(PLATFORM_AERO_LEFT)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));

    }

    @Override
    public void setupLevelEnemies() {

    }

    @Override
    public void setupPlayer() {
        this.player.getBounds().setPosition(spawnPointX, spawnPointY);
        this.inputHandler = new InputHandler(this.player);
    }
}
