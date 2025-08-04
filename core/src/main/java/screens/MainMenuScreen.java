package screens;

import classes.player.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.hod.ultiodivina.Main;
import screens.levels.world1.Level_1_1_Screen;
import screens.levels.world1.Level_1_3_Screen;

import static classes.AssetDescriptors.*;

public class MainMenuScreen extends BaseScreen {

    private Texture backgroundTexture;
    private Texture playButtonTexture;
    private Texture playButtonTextureHover;
    private Texture exitButtonTexture;
    private Texture exitButtonTextureHover;

    public MainMenuScreen(Game game){
        super(game);
    }

    @Override
    public void show() {
        super.show();

        ((Main) game).player = new Player(0, 0, getAnimationSprite(1, 7, assetManager.get(PLAYER_IDLE, Texture.class)),
            getAnimationSprite(1, 6, assetManager.get(PLAYER_WALKING, Texture.class)),
            getAnimationSprite(1, 6, assetManager.get(PLAYER_JUMP, Texture.class)),
            getAnimationSprite(1, 10, assetManager.get(PLAYER_DIE, Texture.class)),
            getAnimationSprite(1, 6, assetManager.get(PLAYER_ATTACK, Texture.class)),
            getAnimationSprite(1, 6, assetManager.get(PLAYER_PROJECTILE, Texture.class)),
            assetManager.get(SOUND_DMG_PLAYER, Sound.class),
            assetManager.get(SOUND_JUMP, Sound.class));

        backgroundMusic = assetManager.get(MAIN_MENU_SONG, Music.class);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

        backgroundTexture = assetManager.get(BG_MAIN_MENU, Texture.class);

        playButtonTexture = assetManager.get(BTN_PLAY, Texture.class);
        exitButtonTexture = assetManager.get(BTN_EXIT, Texture.class);
        playButtonTextureHover = assetManager.get(BTN_PLAY_HOVER, Texture.class);
        exitButtonTextureHover = assetManager.get(BTN_EXIT_HOVER, Texture.class);

        createMenuTable();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void hide() {
        super.hide();
    }

    public void createMenuTable() {
        Table menuTable = new Table();
        menuTable.setFillParent(true);
        Button.ButtonStyle playStyle = new Button.ButtonStyle();
        playStyle.up = new TextureRegionDrawable(playButtonTexture);
        playStyle.over = new TextureRegionDrawable(playButtonTextureHover);

        Button playButton = getPlayButton(playStyle);

        Button.ButtonStyle exitStyle = new Button.ButtonStyle();
        exitStyle.up = new TextureRegionDrawable(exitButtonTexture);
        exitStyle.over = new TextureRegionDrawable(exitButtonTextureHover);

        Button exitButton = getExitButton(exitStyle);

        menuTable.add(playButton).padBottom(10).padTop(80).row();
        menuTable.add(exitButton).padBottom(10).row();

        menuTable.setVisible(true);

        stage.addActor(menuTable);
    }

    public Button getPlayButton(Button.ButtonStyle playStyle){
        Button playButton = new Button(playStyle);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                buttonSound.play(0.5f);
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                sequenceAction.addAction(Actions.run(() -> {
                    game.setScreen(new Level_1_1_Screen(game));
                    //game.setScreen(new Level_1_3_Screen(game));
                    dispose();
                }));
                stage.addAction(sequenceAction);
            }
        });

        return playButton;
    }

    public Button getExitButton(Button.ButtonStyle exitStyle) {
        Button exitButton = new Button(exitStyle);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                buttonSound.play(0.5f);
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                sequenceAction.addAction(Actions.run(() -> {
                    Gdx.app.exit();
                }));
                stage.addAction(sequenceAction);
            }
        });

        return exitButton;
    }

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
