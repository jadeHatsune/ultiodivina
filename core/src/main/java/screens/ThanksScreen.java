package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import static classes.AssetDescriptors.*;
import static classes.AssetDescriptors.BTN_BACK;
import static classes.AssetDescriptors.BTN_BACK_HOVER;

public class ThanksScreen extends BaseScreen {

    private Texture background;

    private Button btnReturn;

    private TextureRegionDrawable returnUp, returnOver;

    public ThanksScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        this.background = assetManager.get(BG_THANKS, Texture.class);
        this.backgroundMusic = assetManager.get(GAME_OVER_SONG, Music.class);
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();

        this.returnUp = new TextureRegionDrawable(assetManager.get(BTN_BACK, Texture.class));
        this.returnOver = new TextureRegionDrawable(assetManager.get(BTN_BACK_HOVER, Texture.class));

        createThanksTable();
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
    public void hide(){
        super.hide();
    }

    public void createThanksTable() {
        Table thanksTable = new Table();
        thanksTable.setFillParent(true);

        Button.ButtonStyle returnStyle = new Button.ButtonStyle();
        returnStyle.up = returnUp;
        returnStyle.over = returnOver;
        btnReturn = getReturnButton(returnStyle);

        menuButtons.add(btnReturn);

        thanksTable.add(btnReturn).padBottom(10).padTop(240).row();

        thanksTable.setVisible(true);

        stage.addActor(thanksTable);
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
            btnReturn.getStyle().up = returnOver;
        } else {
            btnReturn.getStyle().up = returnUp;
        }

    }

    private void resetButtonStyles(){
        btnReturn.getStyle().up = returnUp;
    }
}
