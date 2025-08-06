package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
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
import screens.levels.BaseLevel;

import static classes.AssetDescriptors.*;

public class VictoryScreen extends BaseScreen {

    private Texture background;
    private Button btnContinue, btnReturn;

    private TextureRegionDrawable continueUp, continueOver;
    private TextureRegionDrawable returnUp, returnOver;

    private final BaseLevel nextLevel;

    public VictoryScreen(Game game, BaseLevel nextLevel) {
        super(game);

        this.nextLevel = nextLevel;
    }

    @Override
    public void show() {
        super.show();

        this.background = assetManager.get(BG_VICTORY, Texture.class);
        this.backgroundMusic = assetManager.get(VICTORY_SONG, Music.class);
        this.backgroundMusic.setLooping(false);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();

        this.continueUp = new TextureRegionDrawable(assetManager.get(BTN_CONTINUE, Texture.class));
        this.continueOver = new TextureRegionDrawable(assetManager.get(BTN_CONTINUE_HOVER, Texture.class));
        this.returnUp = new TextureRegionDrawable(assetManager.get(BTN_BACK, Texture.class));
        this.returnOver = new TextureRegionDrawable(assetManager.get(BTN_BACK_HOVER, Texture.class));
        createVictoryTable();
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

        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        batch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose(){
        super.dispose();
    }

    @Override
    public void hide() {
        super.hide();
    }

    public void createVictoryTable() {
        Table victoryTable = new Table();
        victoryTable.setFillParent(true);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.GREEN);
        Label gameOverLabel = new Label("Victoria", labelStyle);

        Button.ButtonStyle continueStyle = new Button.ButtonStyle();
        continueStyle.up = continueUp;
        continueStyle.over = continueOver;
        btnContinue = getContinueButton(continueStyle);

        Button.ButtonStyle returnStyle = new Button.ButtonStyle();
        returnStyle.up = returnUp;
        returnStyle.over = returnOver;
        btnReturn= getReturnButton(returnStyle);

        menuButtons.add(btnContinue);
        menuButtons.add(btnReturn);

        victoryTable.add(gameOverLabel).padBottom(40).row();
        victoryTable.add(btnContinue).padBottom(10).row();
        victoryTable.add(btnReturn).padBottom(10).row();

        victoryTable.setVisible(true);

        stage.addActor(victoryTable);
    }

    private Button getContinueButton(Button.ButtonStyle continueStyle) {
        Button continueButton = new Button(continueStyle);

        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                //--- Toggle Pause ---
                buttonSound.play(0.5f);
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                sequenceAction.addAction(Actions.run(() -> game.setScreen(nextLevel)));
                stage.addAction(sequenceAction);
            }
        });
        return continueButton;
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
            btnContinue.getStyle().up = continueOver;
        } else {
            btnContinue.getStyle().up = continueUp;
        }

        if(selectedIndex == 1) {
            btnReturn.getStyle().up = returnOver;
        } else {
            btnReturn.getStyle().up = returnUp;
        }

    }

    private void resetButtonStyles(){
        btnContinue.getStyle().up = continueUp;
        btnReturn.getStyle().up = returnUp;
    }

}
