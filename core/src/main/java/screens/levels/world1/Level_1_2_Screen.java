package screens.levels.world1;

import classes.InputHandler;
import classes.enemies.EnemyFacing;
import classes.enemies.flyingmouth.FlyingMouth;
import classes.enemies.slime.Slime;
import classes.platforms.PlatformAerial;
import classes.platforms.PlatformGround;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.hod.ultiodivina.Main;
import screens.VictoryScreen;
import screens.levels.BaseLevel;

import static classes.AssetDescriptors.*;

public class Level_1_2_Screen extends BaseLevel {


    public Level_1_2_Screen(Game game) {
        super(game);
    }

    @Override
    public void show(){
        super.show();

        //--- LEVEL CONFIGURATION ---
        this.levelWidth = 800;
        this.levelHeight = 600;
        this.spawnPointX = (int) (VIRTUAL_WIDTH - this.player.getBounds().getWidth());
        this.spawnPointY = PLATFORM_HEIGHT;
        this.backgroundTexture = assetManager.get("backgrounds/level1/nivel1-2.png");
        this.backgroundMusic = assetManager.get("sounds/music/world1BackgroundMusic.ogg");
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();
    }

    @Override
    public void render(float delta){
        super.render(delta);
        if(enemies.isEmpty()){
            game.setScreen(new VictoryScreen(game, new Level_1_3_Screen(game)));
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
        platforms.add(new PlatformGround(0,0, assetManager.get(PLATFORM_GROUND, Texture.class)));
        float currentY = VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LEFT, Texture.class)));
        platforms.add(new PlatformAerial(AERIAL_PLATFORM_WIDTH * 2, (int) currentY, assetManager.get(PLATFORM_AERO_RIGHT, Texture.class)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LEFT, Texture.class)));
        platforms.add(new PlatformAerial(VIRTUAL_WIDTH - AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
    }

    @Override
    public void setupLevelEnemies() {
        enemies.add(new Slime(VIRTUAL_WIDTH - SLIME_WIDTH, PLATFORM_HEIGHT,
            getAnimationSprite(1, 7, assetManager.get(SLIME, Texture.class))));
        enemies.add(new FlyingMouth(FLYING_MOUTH_WIDTH,
            (int) platforms.get(1).getBounds().getY() + PLATFORM_HEIGHT, EnemyFacing.FACING_RIGHT,
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_MOVING, Texture.class)),
            getAnimationSprite(1, 12, assetManager.get(FLYING_MOUTH_ATTACKING, Texture.class)),
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_PROJECTILE, Texture.class))));
        enemies.add(new FlyingMouth(VIRTUAL_WIDTH - FLYING_MOUTH_WIDTH,
            (int) platforms.get(2).getBounds().getY() + FLYING_MOUTH_HEIGHT, EnemyFacing.FACING_LEFT,
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_MOVING, Texture.class)),
            getAnimationSprite(1, 12, assetManager.get(FLYING_MOUTH_ATTACKING, Texture.class)),
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_PROJECTILE, Texture.class))));
        enemies.add(new Slime(SLIME_WIDTH,
            (int) platforms.get(2).getBounds().getY() + PLATFORM_HEIGHT,
            getAnimationSprite(1, 7, assetManager.get(SLIME, Texture.class))));
        enemies.add(new Slime(SLIME_WIDTH,
            (int) platforms.get(4).getBounds().getY() + PLATFORM_HEIGHT,
            getAnimationSprite(1, 7, assetManager.get(SLIME, Texture.class))));
        enemies.add(new Slime((int) platforms.get(5).getBounds().getX(),
            (int) platforms.get(5).getBounds().getY() + PLATFORM_HEIGHT,
            getAnimationSprite(1, 7, assetManager.get(SLIME, Texture.class))));
    }

    @Override
    public void setupPlayer() {
        this.player = ((Main) game).player;
        this.player.getBounds().setPosition(spawnPointX, spawnPointY);
        this.inputHandler = new InputHandler(this.player);
    }
}
