package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import screens.levels.world1.Level_1_3_Screen;

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

        backgroundMusic = assetManager.get("sounds/music/mainMenuSong.ogg");
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

        backgroundTexture = assetManager.get("backgrounds/mainMenuBackground.png");

        playButtonTexture = assetManager.get("buttons/botonJugar.png");
        exitButtonTexture = assetManager.get("buttons/botonsalir.png");
        playButtonTextureHover = assetManager.get("buttons/botonJugar2.png");
        exitButtonTextureHover = assetManager.get("buttons/botonsalir2.png");

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
        backgroundTexture.dispose();
        playButtonTexture.dispose();
        playButtonTextureHover.dispose();
        exitButtonTexture.dispose();
        exitButtonTextureHover.dispose();
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
}
