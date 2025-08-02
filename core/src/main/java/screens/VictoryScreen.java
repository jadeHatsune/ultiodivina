package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import screens.levels.BaseLevel;

public class VictoryScreen extends BaseScreen {

    private Texture buttonContinueTexture;
    private Texture buttonContinueHoverTexture;
    private Texture buttonReturnTexture;
    private Texture buttonReturnHoverTexture;

    private final BaseLevel nextLevel;

    public VictoryScreen(Game game, BaseLevel nextLevel) {
        super(game);

        this.nextLevel = nextLevel;
    }

    @Override
    public void show() {
        super.show();

        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/victorySong.ogg"));
        this.backgroundMusic.setLooping(false);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();

        this.buttonContinueTexture = new Texture(Gdx.files.internal("buttons/botonContinuar.png"));
        this.buttonContinueHoverTexture = new Texture(Gdx.files.internal("buttons/botonContinuar2.png"));
        this.buttonReturnTexture = new Texture(Gdx.files.internal("buttons/botonVolver.png"));
        this.buttonReturnHoverTexture = new Texture(Gdx.files.internal("buttons/botonVolver2.png"));
        createVictoryTable();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose(){
        super.dispose();
        buttonContinueTexture.dispose();
        buttonContinueHoverTexture.dispose();
        buttonReturnTexture.dispose();
        buttonReturnHoverTexture.dispose();
    }

    @Override
    public void hide() {
        super.hide();
    }

    public void createVictoryTable() {
        Table victoryTable = new Table();
        victoryTable.setFillParent(true);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0,0,0, 0.7f));
        pixmap.fill();
        victoryTable.setBackground(new TextureRegionDrawable(new Texture(pixmap)));
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.GREEN);
        Label gameOverLabel = new Label("Victoria", labelStyle);

        Button.ButtonStyle continueStyle = new Button.ButtonStyle();
        continueStyle.up = new TextureRegionDrawable(buttonContinueTexture);
        continueStyle.over = new TextureRegionDrawable(buttonContinueHoverTexture);
        Button restartButton = getContinueButton(continueStyle);

        Button.ButtonStyle returnStyle = new Button.ButtonStyle();
        returnStyle.up = new TextureRegionDrawable(buttonReturnTexture);
        returnStyle.over = new TextureRegionDrawable(buttonReturnHoverTexture);
        Button returnButton = getReturnButton(returnStyle);

        victoryTable.add(gameOverLabel).padBottom(40).row();
        victoryTable.add(restartButton).padBottom(10).row();
        victoryTable.add(returnButton).padBottom(10).row();

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

}
