package screens.levels;

import classes.player.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Level_1_1_Screen implements Screen {

    private final Game game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private BitmapFont font;

    //Entidades
    private Player player;


    public Level_1_1_Screen(Game game){
        this.game = game;
        this.player = new Player(64, 10);
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

}
