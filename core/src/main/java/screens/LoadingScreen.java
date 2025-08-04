package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hod.ultiodivina.Main;

import static classes.AssetDescriptors.*;

public class LoadingScreen implements Screen {

    private final Main game;
    private final AssetManager assetManager;
    private SpriteBatch batch;
    private Texture backgroundLoading;
    private Animation<TextureRegion> loadingAnimaton;
    private float stateTime;
    private Viewport viewport;
    private OrthographicCamera camera;

    public LoadingScreen(Game game){
        this.game = (Main) game;
        this.assetManager = this.game.assetManager;
    }

    @Override
    public void show() {
        this.batch = new SpriteBatch();
        this.stateTime = 0f;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(800, 600, camera);
        this.backgroundLoading = new Texture(Gdx.files.internal("backgrounds/cargando.png"));
        Texture loadingSheet = new Texture(Gdx.files.internal("hud/loading-Sheet.png"));
        this.loadingAnimaton = getAnimationSprite(1, 12, loadingSheet);

        //--- TEXTURES --- //
        //Backgrounds
        assetManager.load(BG_LEVEL_1_1, Texture.class);
        assetManager.load(BG_LEVEL_1_2, Texture.class);
        assetManager.load(BG_LEVEL_1_3, Texture.class);
        assetManager.load(BG_MAIN_MENU, Texture.class);
        //Buttons
        assetManager.load(BTN_CONTINUE, Texture.class);
        assetManager.load(BTN_CONTINUE_HOVER, Texture.class);
        assetManager.load(BTN_PLAY, Texture.class);
        assetManager.load(BTN_PLAY_HOVER, Texture.class);
        assetManager.load(BTN_RESTART, Texture.class);
        assetManager.load(BTN_RESTART_HOVER, Texture.class);
        assetManager.load(BTN_EXIT, Texture.class);
        assetManager.load(BTN_EXIT_HOVER, Texture.class);
        assetManager.load(BTN_BACK, Texture.class);
        assetManager.load(BTN_BACK_HOVER, Texture.class);
        //Enemies
        assetManager.load(FLYING_MOUTH_ATTACKING, Texture.class);
        assetManager.load(FLYING_MOUTH_MOVING, Texture.class);
        assetManager.load(FLYING_MOUTH_PROJECTILE, Texture.class);
        assetManager.load(SLIME, Texture.class);
        assetManager.load(TENTACLE_IDLE, Texture.class);
        //HUD
        assetManager.load(CHALICE_LIFE_0, Texture.class);
        assetManager.load(CHALICE_LIFE_1, Texture.class);
        assetManager.load(CHALICE_LIFE_2, Texture.class);
        assetManager.load(CHALICE_LIFE_3, Texture.class);
        //Lymhiel
        assetManager.load(PLAYER_ATTACK, Texture.class);
        assetManager.load(PLAYER_DIE, Texture.class);
        assetManager.load(PLAYER_IDLE, Texture.class);
        assetManager.load(PLAYER_JUMP, Texture.class);
        assetManager.load(PLAYER_PROJECTILE, Texture.class);
        assetManager.load(PLAYER_WALKING, Texture.class);
        //Platforms
        assetManager.load(PLATFORM_AERO_RIGHT, Texture.class);
        assetManager.load(PLATFORM_AERO_LEFT, Texture.class);
        assetManager.load(PLATFORM_AERO_LARGE, Texture.class);
        assetManager.load(PLATFORM_GROUND, Texture.class);
        //--- SOUNDS ---
        assetManager.load(SOUND_BUTTON, Sound.class);
        assetManager.load(SOUND_DMG_PLAYER, Sound.class);
        assetManager.load(SOUND_PROJECTILE, Sound.class);
        assetManager.load(SOUND_JUMP, Sound.class);
        //-- MUSIC ---
        assetManager.load(BOSS1_SONG, Music.class);
        assetManager.load(GAME_OVER_SONG, Music.class);
        assetManager.load(MAIN_MENU_SONG, Music.class);
        assetManager.load(VICTORY_SONG, Music.class);
        assetManager.load(WORLD1_SONG, Music.class);


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(backgroundLoading, 0, 0, 800, 600);

        if(assetManager.update()){
            game.setScreen(new MainMenuScreen(game));
        }

        stateTime += delta;

        TextureRegion currentFrame = loadingAnimaton.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, (float)(800 / 2 - currentFrame.getRegionWidth() / 2), (float)(600 / 2 - currentFrame.getRegionHeight() / 2),
            currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }

    public Animation<TextureRegion> getAnimationSprite(int frameCols, int frameRows, Texture spriteSheet) {
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
