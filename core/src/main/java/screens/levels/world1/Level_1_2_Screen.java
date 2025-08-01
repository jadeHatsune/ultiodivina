package screens.levels.world1;

import classes.InputHandler;
import classes.enemies.slime.Slime;
import classes.enemies.EnemyState;
import classes.platforms.Platform;
import classes.player.Player;
import classes.player.PlayerFacing;
import classes.projectiles.Projectile;
import classes.projectiles.ProjectileState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import screens.GameOverScreen;

import java.util.Iterator;

public class Level_1_2_Screen implements Screen {

    //-- Constants --
    private static final int PLATFORM_HEIGHT = 64;
    private static final int AERIAL_PLATFORM_WIDTH = 200;
    private static final int AERIAL_LONG_PLATFORM_WIDTH = 300;
    private static final int GROUND_PLATFORM_DIF_Y = 15;
    private static final int AERIAL_PLATFORM_DIF_Y = 31;
    private static final int VERTICAL_SPACING = 130;
    private static final int SLIME_WIDTH = 61;
    private static final int SPAWN_POINT = 64;

    private final Game game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private BitmapFont font;

    //--- Entities ---
    private Player player;
    private Array<Slime> slimes;
    private Array<Projectile> projectiles;

    //--- Platforms ---
    private Array<Platform> platformsArray;

    //--- HUD ---
    private OrthographicCamera gameCamera;
    private OrthographicCamera hudCamera;
    private Array<Texture> lifeSprites;
    private Animation<TextureRegion> animationLifeEnds;
    private float hudAnimationStateTime = 0f;
    private InputHandler inputHandler;

    public Level_1_2_Screen(Game game){
        this.game = game;
    }

    @Override
    public void show(){
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.backgroundTexture = new Texture(Gdx.files.internal("nivel1-1.jpeg"));

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        setupPlatforms();
        setupEnemies();
        setupPlayer();

        animationLifeEnds = getAnimationSprite(1, 10, "caliz0Vidas.png");
        lifeSprites = new Array<>();

        lifeSprites.add(new Texture("caliz1Vidas.png"));
        lifeSprites.add(new Texture("caliz2Vidas.png"));
        lifeSprites.add(new Texture("caliz3Vidas.png"));
    }

    @Override
    public void render(float delta){

        inputHandler.update();
        player.update(delta, platformsArray);

        if(player.shouldSpawnProjectile()) {
            float projectileSpeed = 500f;
            float startX = player.getCurrentFacing() == PlayerFacing.FACING_RIGHT ? player.getBounds().x + player.getBounds().width : player.getBounds().x;
            float startY = player.getBounds().y + (player.getBounds().height / 3);

            if(player.getCurrentFacing() == PlayerFacing.FACING_LEFT) {
                projectileSpeed = -500f;
            }

            projectiles.add(new Projectile((int) startX, (int) startY, projectileSpeed, "lymhiel_projectile.png"));
        }

        if(player.isDeathAnimationFinished()) { game.setScreen(new GameOverScreen(game)); }

        for(Projectile projectile : projectiles) {
            projectile.update(delta);
        }
        for(Slime slime : slimes) {
            slime.update(delta, platformsArray);
        }

        handleCollisions();
        cleanupEntities();

        ScreenUtils.clear(0, 0, 0, 1);

        //--- Dibujado de juego ---
        batch.begin();

        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player.draw(batch);
        for(Platform platform : platformsArray) {
            platform.draw(batch);
        }
        for(Projectile projectile : projectiles) {
            projectile.draw(batch);
        }
        for(Slime slime : slimes){
            slime.draw(batch);
        }

        batch.end();

        //--- Dibujado de HUD ---
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        int currentLife = player.getLife();
        if (currentLife > 0) {
            Texture lifeSprite = lifeSprites.get(currentLife - 1);
            batch.draw(lifeSprite, 10, Gdx.graphics.getHeight() - lifeSprite.getHeight() - 10);
        } else {
            hudAnimationStateTime += delta;
            TextureRegion currentFrame = animationLifeEnds.getKeyFrame(hudAnimationStateTime, false);
            batch.draw(currentFrame, 10, Gdx.graphics.getHeight() - currentFrame.getRegionHeight() - 10);
        }

        batch.end();

    }

    @Override
    public void dispose(){
        this.batch.dispose();
        this.font.dispose();
        this.backgroundTexture.dispose();
        for (Texture t : lifeSprites) {
            t.dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    public void setupPlatforms() {
        this.platformsArray = new Array<>();
        String platformGroundPath = "plataformaTierra.png";
        String platformAirLeftPath = "plataformaAereoIzquierda.png";
        String platformAirRightPath = "plataformaAereoDerecha.png";
        String platformAirLongPath = "plataformaAereoLarga.png";

        platformsArray.add(new Platform(0, 0, Gdx.graphics.getWidth(), 0, GROUND_PLATFORM_DIF_Y, platformGroundPath));
        float currentY = VERTICAL_SPACING;
        platformsArray.add(new Platform(0, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLongPath));
        platformsArray.add(new Platform(Gdx.graphics.getWidth() - AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLongPath));
        currentY += VERTICAL_SPACING;
        platformsArray.add(new Platform(0, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLeftPath));
        platformsArray.add(new Platform(Gdx.graphics.getWidth() - AERIAL_PLATFORM_WIDTH, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirRightPath));
        currentY += VERTICAL_SPACING;
        platformsArray.add(new Platform((Gdx.graphics.getWidth() / 2) - (AERIAL_LONG_PLATFORM_WIDTH / 2), (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLongPath));


    }

    public void setupEnemies(){
        this.slimes = new Array<>();
        for(Platform platform : platformsArray) {
            slimes.add(new Slime((int) (platform.getBounds().getX() + platform.getBounds().getWidth() - SLIME_WIDTH), (int) platform.getBounds().getY() + PLATFORM_HEIGHT));
        }
    }

    public void setupPlayer() {
        this.player = new Player(SPAWN_POINT, PLATFORM_HEIGHT);
        this.projectiles = new Array<>();
        this.inputHandler = new InputHandler(this.player);
    }

    public void handleCollisions(){
        for(Slime slime : slimes) {
            if(player.getBounds().overlaps(slime.getBounds())) {
                player.takeDamage(slime.makeDamage());
            }
        }

        for(Projectile projectile : projectiles) {
            for(Slime slime : slimes) {
                if(projectile.getBounds().overlaps(slime.getBounds())) {
                    slime.takeDamage(1);
                    projectile.setCurrentState(ProjectileState.DEACTIVATE);
                    break;
                }
            }
        }
    }

    public void cleanupEntities(){
        //--- Limpieza de Slimes ---
        for (Iterator<Slime> iter = slimes.iterator(); iter.hasNext(); ) {
            Slime slime = iter.next();
            if (slime.getCurrentState() == EnemyState.DIE) {
                iter.remove();
            }
        }

        //--- Limpieza de Proyectiles ---
        for (Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext(); ) {
            Projectile projectile = iter.next();

            if (projectile.getCurrentState() == ProjectileState.DEACTIVATE) {
                iter.remove();
            }
        }
    }

    public Animation<TextureRegion> getAnimationSprite(int frameCols, int frameRows, String spriteSheetPath) {
        Texture spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));

        int frameWidth = spriteSheet.getWidth() / frameCols;
        int frameHeight = spriteSheet.getHeight() / frameRows;

        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, frameWidth, frameHeight);
        TextureRegion[] keyFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                keyFrames[index++] = tmp[i][j];
            }
        }

        return new Animation<>(0.1f, keyFrames);
    }
}
