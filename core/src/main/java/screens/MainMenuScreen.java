package screens;

import classes.Inputs.GamepadMenuController;
import classes.player.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
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
import java.util.ArrayList;
import screens.levels.world1.Level_1_1_Screen;
import screens.levels.world1.Level_1_3_Screen;

import static classes.AssetDescriptors.*;

public class MainMenuScreen extends BaseScreen {

    //--- BUTTONS ---
    Button playButton;
    Button exitButton;

    //--- TEXTURES ---
    private Texture backgroundTexture;
    private TextureRegionDrawable playUp, playOver;
    private TextureRegionDrawable exitUp, exitOver;

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

        playUp = new TextureRegionDrawable(assetManager.get(BTN_PLAY, Texture.class));
        playOver = new TextureRegionDrawable(assetManager.get(BTN_PLAY_HOVER, Texture.class));
        exitUp = new TextureRegionDrawable(assetManager.get(BTN_EXIT, Texture.class));
        exitOver = new TextureRegionDrawable(assetManager.get(BTN_EXIT_HOVER, Texture.class));

        this.backgroundMusic = assetManager.get(MAIN_MENU_SONG, Music.class);
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();

        this.backgroundTexture = assetManager.get(BG_MAIN_MENU, Texture.class);

        createMenuTable();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if(Controllers.getControllers().size > 0) {
            gamepadMenuController.update();
            updateButtonFocus();
        } else {
            resetButtonStyles();
        }

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
        playStyle.up = playUp;
        playStyle.over = playOver;

        playButton = getPlayButton(playStyle);

        Button.ButtonStyle exitStyle = new Button.ButtonStyle();
        exitStyle.up = exitUp;
        exitStyle.over = exitOver;

        exitButton = getExitButton(exitStyle);

        menuButtons.add(playButton);
        menuButtons.add(exitButton);

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
                    //game.setScreen(new Level_1_1_Screen(game));
                    game.setScreen(new Level_1_3_Screen(game));
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

    private void updateButtonFocus(){
        int selectedIndex = gamepadMenuController.getSelectedIndex();

        if(selectedIndex == 0) {
            playButton.getStyle().up = playOver;
        } else {
            playButton.getStyle().up = playUp;
        }

        if(selectedIndex == 1) {
            exitButton.getStyle().up = exitOver;
        } else {
            exitButton.getStyle().up = exitUp;
        }

    }

    private void resetButtonStyles(){
        playButton.getStyle().up = playUp;
        exitButton.getStyle().up = exitUp;
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
