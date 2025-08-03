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
import screens.levels.world1.Level_1_1_Screen;

public class GameOverScreen extends BaseScreen {

    private Texture buttonRestartTexture;
    private Texture buttonRestartHoverTexture;
    private Texture buttonReturnTexture;
    private Texture buttonReturnHoverTexture;

    public GameOverScreen(Game game) {
        super(game);
    }

    @Override
    public void show(){
        super.show();

        this.backgroundMusic = assetManager.get("sounds/music/gameOverSong.ogg");
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.setVolume(0.5f);
        this.backgroundMusic.play();

        this.buttonRestartTexture = assetManager.get("buttons/botonReintentar.png");
        this.buttonRestartHoverTexture = assetManager.get("buttons/botonReintentar2.png");
        this.buttonReturnTexture = assetManager.get("buttons/botonVolver.png");
        this.buttonReturnHoverTexture = assetManager.get("buttons/botonVolver2.png");

        createGameOverTable();
    }

    @Override
    public void render(float delta){
        super.render(delta);
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose(){
        super.dispose();
        buttonRestartTexture.dispose();
        buttonRestartHoverTexture.dispose();
        buttonReturnTexture.dispose();
        buttonReturnHoverTexture.dispose();
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
        restartStyle.up = new TextureRegionDrawable(buttonRestartTexture);
        restartStyle.over = new TextureRegionDrawable(buttonRestartHoverTexture);
        Button restartButton = getRestartButton(restartStyle);

        Button.ButtonStyle returnStyle = new Button.ButtonStyle();
        returnStyle.up = new TextureRegionDrawable(buttonReturnTexture);
        returnStyle.over = new TextureRegionDrawable(buttonReturnHoverTexture);
        Button returnButton = getReturnButton(returnStyle);

        gameOverTable.add(gameOverLabel).padBottom(40).row();
        gameOverTable.add(restartButton).padBottom(10).row();
        gameOverTable.add(returnButton).padBottom(10).row();

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

}
