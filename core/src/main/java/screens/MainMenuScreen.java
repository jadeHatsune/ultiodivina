package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import screens.levels.Level_1_1_Screen;

public class MainMenuScreen implements Screen {

    private final Game game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private BitmapFont font;

    private Stage stage;

    private Texture playButtonTexture;
    private Texture playButtonTextureHover;
    private Texture exitButtonTexture;
    private Texture exitButtonTextureHover;

    private TextureRegionDrawable playButtonNormalDrawable;
    private TextureRegionDrawable playButtonHoverDrawable;
    private TextureRegionDrawable exitButtonNormalDrawable;
    private TextureRegionDrawable exitButtonHoverDrawable;

    public MainMenuScreen(Game game){
        this.game = game;
    }

    @Override
    public void show(){
        batch = new SpriteBatch();
        font = new BitmapFont();
        backgroundTexture = new Texture(Gdx.files.internal("mainMenuBackground.png"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        playButtonTexture = new Texture(Gdx.files.internal("botonJugar200.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("botonSalir200.png"));
        playButtonTextureHover = new Texture(Gdx.files.internal("botonJugar2-200.png"));
        exitButtonTextureHover = new Texture(Gdx.files.internal("botonsalir2-200.png"));

        playButtonNormalDrawable = new TextureRegionDrawable(new TextureRegion(playButtonTexture));
        playButtonHoverDrawable = new TextureRegionDrawable(new TextureRegion(playButtonTextureHover));
        exitButtonNormalDrawable = new TextureRegionDrawable(new TextureRegion(exitButtonTexture));
        exitButtonHoverDrawable = new TextureRegionDrawable(new TextureRegion(exitButtonTextureHover));

        createButtons();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        playButtonTexture.dispose();
        playButtonTextureHover.dispose();
        exitButtonTexture.dispose();
        exitButtonTextureHover.dispose();
        font.dispose();
        batch.dispose();
        stage.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    public void createButtons() {
        //Botón jugar
        Image playButton = new Image(playButtonTexture);
        playButton.setPosition((float) Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2, 200);
        playButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
           @Override
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
               game.setScreen(new Level_1_1_Screen(game));
               dispose();
               return true;
           }

           @Override
            public void enter(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor){
                playButton.setDrawable(playButtonHoverDrawable);
           }

           @Override
            public void exit(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor){
               playButton.setDrawable(playButtonNormalDrawable);
           }
        });
        stage.addActor(playButton);

        //Botón salir
        Image exitButton = new Image(exitButtonTexture);
        exitButton.setPosition((float) Gdx.graphics.getWidth() / 2 - exitButton.getWidth() / 2, 100);
        exitButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
           @Override
           public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
               exitButton.setDrawable(exitButtonNormalDrawable);
               Gdx.app.exit();
               return true;
           }

            @Override
            public void enter(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor){
                exitButton.setDrawable(exitButtonHoverDrawable);
            }

            @Override
            public void exit(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor){
                exitButton.setDrawable(exitButtonNormalDrawable);
            }

        });
        stage.addActor(exitButton);
    }
}
