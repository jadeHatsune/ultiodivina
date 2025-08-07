package screens.levels.world1;

import classes.Inputs.InputHandler;
import classes.enemies.EnemyFacing;
import classes.enemies.bosses.asmodeus.Asmodeus;
import classes.enemies.flyingmouth.FlyingMouth;
import classes.enemies.slime.Slime;
import classes.platforms.PlatformAerial;
import classes.platforms.PlatformGround;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import screens.ThanksScreen;
import screens.levels.BaseLevel;
import screens.levels.GameState;

import static classes.AssetDescriptors.*;

public class Level_1_3_Screen extends BaseLevel {

    public Level_1_3_Screen(Game game) {
        super(game);
    }

    @Override
    public void show(){
        super.show();

        //--- LEVEL CONFIGURATION ---
        this.levelWidth = 2400;
        this.levelHeight = 1500;
        this.spawnPointX = 64;
        this.spawnPointY = PLATFORM_HEIGHT - 15;
        this.backgroundTexture = assetManager.get(BG_LEVEL_1_3);
        this.backgroundMusic = assetManager.get(BOSS1_SONG);
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.setVolume(0.5f);

        setupLevelPlatforms();
        setupLevelEnemies();
        setupPlayer();

        this.currentState = GameState.CINEMATIC;
        this.cinematicState = CinematicState.MOVING_TO_BOSS;
        this.cinematicTimer = 0f;
        this.showHud = false;

    }

    @Override
    public void render(float delta){
        super.render(delta);
        if(enemies.isEmpty()){
            game.setScreen(new ThanksScreen(game));
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
        platforms.add(new PlatformGround(800,0, assetManager.get(PLATFORM_GROUND, Texture.class)));
        platforms.add(new PlatformGround(1600,0, assetManager.get(PLATFORM_GROUND, Texture.class)));

        float currentY = VERTICAL_SPACING;
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH*2, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH*3, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH * 2, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH * 3 + AERIAL_PLATFORM_WIDTH + 40, (int) currentY, assetManager.get(PLATFORM_AERO_LEFT, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH * 2, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH * 2, (int) currentY, assetManager.get(PLATFORM_AERO_LEFT)));
        platforms.add(new PlatformAerial((int) (levelWidth - AERIAL_LONG_PLATFORM_WIDTH), (int) currentY, assetManager.get(PLATFORM_AERO_LARGE)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(0, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LARGE, Texture.class)));
        platforms.add(new PlatformAerial((int) levelWidth - AERIAL_LONG_PLATFORM_WIDTH * 2, (int) currentY, assetManager.get(PLATFORM_AERO_RIGHT)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH * 2 , (int) currentY, assetManager.get(PLATFORM_AERO_LEFT)));
        platforms.add(new PlatformAerial(AERIAL_LONG_PLATFORM_WIDTH * 2 + 800 + AERIAL_PLATFORM_WIDTH, (int) currentY, assetManager.get(PLATFORM_AERO_LEFT)));
        platforms.add(new PlatformAerial((int) (levelWidth - AERIAL_PLATFORM_WIDTH), (int) currentY, assetManager.get(PLATFORM_AERO_RIGHT)));
        currentY += VERTICAL_SPACING;
        platforms.add(new PlatformGround((int) (levelWidth / 2 - 400), (int) currentY, assetManager.get(PLATFORM_GROUND, Texture.class)));

    }

    @Override
    public void setupLevelEnemies() {
        enemies.add(new Slime(1600, PLATFORM_HEIGHT,
            getAnimationSprite(1, 7, assetManager.get(SLIME, Texture.class))));
        enemies.add(new Slime((int) platforms.get(21).getBounds().getX() + SLIME_WIDTH,
            (int) platforms.get(21).getBounds().getY() + PLATFORM_HEIGHT,
            getAnimationSprite(1, 7, assetManager.get(SLIME, Texture.class))));
        enemies.add(new Slime((int) platforms.get(9).getBounds().getX() + SLIME_WIDTH,
            (int) platforms.get(9).getBounds().getY() + PLATFORM_HEIGHT,
            getAnimationSprite(1, 7, assetManager.get(SLIME, Texture.class))));
        enemies.add(new Slime((int) platforms.get(14).getBounds().getX() + SLIME_WIDTH,
            (int) platforms.get(14).getBounds().getY() + PLATFORM_HEIGHT,
            getAnimationSprite(1, 7, assetManager.get(SLIME, Texture.class))));

        enemies.add(new FlyingMouth((int) platforms.get(3).getBounds().getX(),
            (int) platforms.get(3).getBounds().getY() + PLATFORM_HEIGHT,
            EnemyFacing.FACING_LEFT,
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_MOVING, Texture.class)),
            getAnimationSprite(1, 12, assetManager.get(FLYING_MOUTH_ATTACKING, Texture.class)),
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_PROJECTILE, Texture.class))));
        enemies.add(new FlyingMouth((int) platforms.get(6).getBounds().getX() + FLYING_MOUTH_WIDTH,
            (int) platforms.get(6).getBounds().getY() + PLATFORM_HEIGHT,
            EnemyFacing.FACING_LEFT,
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_MOVING, Texture.class)),
            getAnimationSprite(1, 12, assetManager.get(FLYING_MOUTH_ATTACKING, Texture.class)),
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_PROJECTILE, Texture.class))));
        enemies.add(new FlyingMouth((int) platforms.get(19).getBounds().getX(),
            (int) platforms.get(19).getBounds().getY() + PLATFORM_HEIGHT,
            EnemyFacing.FACING_LEFT,
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_MOVING, Texture.class)),
            getAnimationSprite(1, 12, assetManager.get(FLYING_MOUTH_ATTACKING, Texture.class)),
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_PROJECTILE, Texture.class))));
        enemies.add(new FlyingMouth((int) platforms.get(20).getBounds().getX(),
            (int) platforms.get(20).getBounds().getY() + PLATFORM_HEIGHT,
            EnemyFacing.FACING_LEFT,
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_MOVING, Texture.class)),
            getAnimationSprite(1, 12, assetManager.get(FLYING_MOUTH_ATTACKING, Texture.class)),
            getAnimationSprite(1, 4, assetManager.get(FLYING_MOUTH_PROJECTILE, Texture.class))));

        boss = new Asmodeus((int) (levelWidth / 2 - (float) 432 / 2), (int) platforms.get(22).getBounds().getY() + PLATFORM_HEIGHT - 15,
            getAnimationSprite(8, 8, assetManager.get(ASMODEUS_SPAWN, Texture.class)),
            getAnimationSprite(1, 8, assetManager.get(ASMODEUS_IDLE, Texture.class)),
            getAnimationSprite(1, 29, assetManager.get(ASMODEUS_INVOKE, Texture.class)),
            getAnimationSprite(1, 8, assetManager.get(ASMODEUS_HALF_LIFE, Texture.class)),
            getAnimationSprite(1, 14, assetManager.get(ASMODEUS_TP_OUT, Texture.class)),
            getAnimationSprite(1, 14, assetManager.get(ASMODEUS_TP_IN, Texture.class)),
            getAnimationSprite(1, 30, assetManager.get(ASMODEUS_DIE, Texture.class)));

        enemies.add(boss);
    }

    @Override
    public void setupPlayer() {
        this.player.getBounds().setPosition(spawnPointX, spawnPointY);
        this.inputHandler = new InputHandler(this.player);
    }
}
