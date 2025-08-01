package screens.levels.world1;

import classes.InputHandler;
import classes.enemies.slime.Slime;
import classes.platforms.Platform;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.hod.ultiodivina.Main;
import screens.levels.BaseLevel;
import screens.levels.GameState;

public class Level_1_1_Screen extends BaseLevel {

    //--- LEVEL MUSIC ---
    private Music levelMusic;

    public Level_1_1_Screen(Game game){
        super(game);
    }

    @Override
    public void show(){
        super.show();

        //--- LEVEL CONFIGURATION ---
        this.player.restartScore();
        this.spawnPointX = 64;
        this.spawnPointY = PLATFORM_HEIGHT;
        this.backgroundTexture = new Texture(Gdx.files.internal("backgrounds/level1/nivel1-1.jpeg"));
        this.levelMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/level1Song.ogg"));
        this.levelMusic.setLooping(true);
        this.levelMusic.setVolume(0.5f);
        this.levelMusic.play();
    }

    @Override
    public void render(float delta){
        super.render(delta);
        if(currentState == GameState.PAUSED){
            levelMusic.setVolume(0.2f);
        } else {
            levelMusic.setVolume(0.5f);
        }
    }

    @Override
    public void dispose(){
        super.dispose();
        levelMusic.dispose();
    }

    @Override
    public void hide(){
        levelMusic.stop();
    }

    @Override
    public void setupLevelPlatforms() {
        String platformGroundPath = "platforms/plataformaTierra.png";
        String platformAirLeftPath = "platforms/plataformaAereoIzquierda.png";
        String platformAirRightPath = "platforms/plataformaAereoDerecha.png";
        String platformAirLongPath = "platforms/plataformaAereoLarga.png";

        platforms.add(new Platform(0, 0, VIRTUAL_WIDTH, 0, GROUND_PLATFORM_DIF_Y, platformGroundPath));
        float currentY = VERTICAL_SPACING;
        platforms.add(new Platform(0, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLongPath));
        platforms.add(new Platform(VIRTUAL_WIDTH - AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLongPath));
        currentY += VERTICAL_SPACING;
        platforms.add(new Platform(0, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLeftPath));
        platforms.add(new Platform(VIRTUAL_WIDTH - AERIAL_PLATFORM_WIDTH, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirRightPath));
        currentY += VERTICAL_SPACING;
        platforms.add(new Platform((VIRTUAL_WIDTH / 2) - (AERIAL_LONG_PLATFORM_WIDTH / 2), (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLongPath));


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
