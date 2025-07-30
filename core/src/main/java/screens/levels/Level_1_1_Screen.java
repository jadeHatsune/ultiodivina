package screens.levels;

import classes.enemies.slime.Slime;
import classes.platforms.Platform;
import classes.player.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class Level_1_1_Screen implements Screen {

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

    //-- Entities --
    private Player player;
    private Array<Slime> slimes;

    //-- Platforms --
    private Array<Platform> platformsArray;

    public Level_1_1_Screen(Game game){
        this.game = game;

        setupPlatforms();
        setupEnemies();
        setupPlayer();
    }

    @Override
    public void show(){
        batch = new SpriteBatch();
        font = new BitmapFont();
        backgroundTexture = new Texture(Gdx.files.internal("nivel1-1.jpeg"));
    }

    @Override
    public void render(float delta){

        player.update(delta, platformsArray);

        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player.draw(batch);
        for(Platform platform : platformsArray) {
            platform.draw(batch);
        }
        for(Slime slime : slimes){
            slime.draw(batch);
        }

        batch.end();
    }

    @Override
    public void dispose(){
        this.batch.dispose();
        this.font.dispose();

        this.backgroundTexture.dispose();
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
        slimes.add(new Slime(Gdx.graphics.getWidth() - SLIME_WIDTH, PLATFORM_HEIGHT));
    }

    public void setupPlayer() {
        this.player = new Player(SPAWN_POINT, PLATFORM_HEIGHT);
    }

}
