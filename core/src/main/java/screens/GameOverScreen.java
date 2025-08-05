package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import screens.levels.world1.Level_1_1_Screen;

import static classes.AssetDescriptors.*;

public class GameOverScreen extends BaseScreen {

    Button btnRestart, btnReturn;

    private TextureRegionDrawable restartUp, restartOver;
    private TextureRegionDrawable returnUp, returnOver;

    public GameOverScreen(Game game) {
        super(game);
    }

    @Override
    public void show(){
        super.show();

        this.backgroundMusic = assetManager.get(GAME_OVER_SONG, Music.class);
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();

        this.restartUp = new TextureRegionDrawable(assetManager.get(BTN_RESTART, Texture.class));
        this.restartOver = new TextureRegionDrawable(assetManager.get(BTN_RESTART_HOVER, Texture.class));
        this.returnUp = new TextureRegionDrawable(assetManager.get(BTN_BACK, Texture.class));
        this.returnOver = new TextureRegionDrawable(assetManager.get(BTN_BACK_HOVER, Texture.class));

        createGameOverTable();
    }

    @Override
    public void render(float delta){
        super.render(delta);

        if(Controllers.getControllers().size > 0) {
            gamepadMenuController.update();
            updateButtonFocus();
        } else {
            resetButtonStyles();
        }

        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose(){
        super.dispose();
    }

    @Override
    public void hide(){
        super.hide();
    }

    public void createGameOverTable() {
        Table gameOverTable = new Table();
        gameOverTable.setFillParent(true);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0,0,0, 0.7f));
        pixmap.fill();
        gameOverTable.setBackground(new TextureRegionDrawable(new Texture(pixmap)));
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label gameOverLabel = new Label("Fin del juego", labelStyle);

        Button.ButtonStyle restartStyle = new Button.ButtonStyle();
        restartStyle.up = restartUp;
        restartStyle.over = restartOver;
        btnRestart = getRestartButton(restartStyle);

        Button.ButtonStyle returnStyle = new Button.ButtonStyle();
        returnStyle.up = returnUp;
        returnStyle.over = returnOver;
        btnReturn = getReturnButton(returnStyle);

        menuButtons.add(btnRestart);
        menuButtons.add(btnReturn);

        gameOverTable.add(gameOverLabel).padBottom(40).row();
        gameOverTable.add(btnRestart).padBottom(10).row();
        gameOverTable.add(btnReturn).padBottom(10).row();

        gameOverTable.setVisible(true);

        stage.addActor(gameOverTable);
    }

    public Button getRestartButton(Button.ButtonStyle restartStyle){
        Button restartButton = new Button(restartStyle);
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                buttonSound.play(0.5f);
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                sequenceAction.addAction(Actions.run(() ->
                    game.setScreen(new Level_1_1_Screen(game))));

                stage.addAction(sequenceAction);
            }
        });

        return restartButton;
    }

    private Button getReturnButton(Button.ButtonStyle returnStyle) {
        Button returnButton = new Button(returnStyle);

        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                buttonSound.play(0.5f);
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                sequenceAction.addAction(Actions.run(() -> game.setScreen(new MainMenuScreen(game))));
                stage.addAction(sequenceAction);
            }
        });
        return returnButton;
    }

    private void updateButtonFocus(){
        int selectedIndex = gamepadMenuController.getSelectedIndex();

        if(selectedIndex == 0) {
            btnRestart.getStyle().up = restartOver;
        } else {
            btnRestart.getStyle().up = restartUp;
        }

        if(selectedIndex == 1) {
            btnReturn.getStyle().up = returnOver;
        } else {
            btnReturn.getStyle().up = returnUp;
        }

    }

    private void resetButtonStyles(){
        btnRestart.getStyle().up = restartUp;
        btnReturn.getStyle().up = returnUp;
    }

}
