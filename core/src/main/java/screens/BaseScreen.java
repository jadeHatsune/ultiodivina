package screens;

import classes.Inputs.GamepadMenuController;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hod.ultiodivina.Main;

import java.util.ArrayList;

import static classes.AssetDescriptors.*;

public abstract class BaseScreen implements Screen {

    //--- Virtual Resolution ---
    public static final int VIRTUAL_WIDTH = 800;
    public static final int VIRTUAL_HEIGHT = 600;

    //--- Shared objects ---
    protected final Game game;
    protected AssetManager assetManager;
    protected final BitmapFont font;
    protected final OrthographicCamera camera;
    protected final Viewport viewport;
    protected final SpriteBatch batch;
    protected final Stage stage;

    //--- MUSIC AND SOUNDS ---
    protected Music backgroundMusic;
    protected Sound buttonSound;

    //--- GAMEPAD ---
    protected GamepadMenuController gamepadMenuController;
    protected ArrayList<Button> menuButtons;
    protected Controller controller;

    public BaseScreen(Game game) {
        this.game = game;
        this.assetManager = ((Main) game).assetManager;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AngelFortune.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;
        this.font = generator.generateFont(parameter);
        generator.dispose();

        this.menuButtons = new ArrayList<>();
        this.gamepadMenuController = new GamepadMenuController(menuButtons);
        this.controller = Controllers.getCurrent();

        this.buttonSound = assetManager.get(SOUND_BUTTON, Sound.class);

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        this.batch = new SpriteBatch();
        this.stage = new Stage(viewport, batch);
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0, 0, 0, 1);

        viewport.apply();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        stage.dispose();
    }
    @Override
    public void hide() {
        backgroundMusic.stop();
    }

    @Override public void pause() {}
    @Override public void resume() {}

}
