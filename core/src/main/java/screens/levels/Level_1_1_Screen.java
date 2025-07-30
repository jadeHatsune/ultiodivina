package screens.levels;

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
    private static final int GROUND_PLATFORM_HEIGHT = 64;
    private static final int AERIAL_PLATFORM_HEIGHT = 64;
    private static final int GROUND_PLATFORM_DIF_Y = 15;
    private static final int AERIAL_PLATFORM_DIF_Y = 31;

    private final Game game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private BitmapFont font;

    //-- Entities --
    private Player player;

    //-- Platforms --
    private Array<Platform> platformsArray;

    public Level_1_1_Screen(Game game){
        this.game = game;

        this.platformsArray = new Array<>();

        this.platformsArray.add(new Platform(0, 0, Gdx.graphics.getWidth(), 0, 15, "plataformaTierra.png"));
        this.platformsArray.add(new Platform(0, (int) platformsArray.get(0).getBounds().getY() + 130,0 , 0, 31, "plataformaAereoIzquierda.png"));
        this.platformsArray.add(new Platform(0, (int) platformsArray.get(1).getBounds().getY() + 130, 0, 0, 31, "plataformaAereoIzquierda.png"));
        this.platformsArray.add(new Platform(Gdx.graphics.getWidth() / 2 - 150, (int) platformsArray.get(2).getBounds().getY() + 130, 0, 0, 31, "plataformaAereoLarga.png"));

        this.player = new Player(64, (int) platformsArray.get(0).getBounds().getHeight());
    }

    @Override
    public void show(){
        batch = new SpriteBatch();
        font = new BitmapFont();
        backgroundTexture = new Texture(Gdx.files.internal("nivel1-1.jpeg"));
    }

    @Override
    public void render(float delta){

        player.update(delta);

        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player.draw(batch);
        for(Platform platform : platformsArray) {
            platform.draw(batch);
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

    }

}
