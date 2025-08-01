package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import screens.levels.world1.Level_1_1_Screen;

public class MainMenuScreen extends BaseScreen {

    private Texture backgroundTexture;
    private Texture playButtonTexture;
    private Texture playButtonTextureHover;
    private Texture exitButtonTexture;
    private Texture exitButtonTextureHover;

    private TextureRegionDrawable playButtonNormalDrawable;
    private TextureRegionDrawable playButtonHoverDrawable;
    private TextureRegionDrawable exitButtonNormalDrawable;
    private TextureRegionDrawable exitButtonHoverDrawable;

    private Music backgroundMusic;
    private Sound buttonSound;

    public MainMenuScreen(Game game){
        super(game);
    }

    @Override
    public void show(){
        super.show();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/mainMenuSong.ogg"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

        buttonSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/efectoBotones.ogg"));

        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/mainMenuBackground.png"));

        playButtonTexture = new Texture(Gdx.files.internal("buttons/botonJugar.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("buttons/botonsalir.png"));
        playButtonTextureHover = new Texture(Gdx.files.internal("buttons/botonJugar2.png"));
        exitButtonTextureHover = new Texture(Gdx.files.internal("buttons/botonsalir2.png"));

        playButtonNormalDrawable = new TextureRegionDrawable(new TextureRegion(playButtonTexture));
        playButtonHoverDrawable = new TextureRegionDrawable(new TextureRegion(playButtonTextureHover));
        exitButtonNormalDrawable = new TextureRegionDrawable(new TextureRegion(exitButtonTexture));
        exitButtonHoverDrawable = new TextureRegionDrawable(new TextureRegion(exitButtonTextureHover));

        createButtons();
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
        backgroundMusic.dispose();
        buttonSound.dispose();
    }

    @Override
    public void hide() {
        super.hide();
        backgroundMusic.stop();
    }

    public void createButtons() {
        //Botón jugar
        Image playButton = new Image(playButtonTexture);
        playButton.setPosition((float) VIRTUAL_WIDTH / 2 - playButton.getWidth() / 2, (float) VIRTUAL_HEIGHT / 2 - playButton.getHeight() / 2);
        playButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
           @Override
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
               buttonSound.play(0.5f);
               SequenceAction sequenceAction = new SequenceAction();
               sequenceAction.addAction(Actions.delay(0.3f));
               sequenceAction.addAction(Actions.run(() -> {
                   game.setScreen(new Level_1_1_Screen(game));
                   dispose();
               }));
               stage.addAction(sequenceAction);
               return true;
           }

           @Override
            public void enter(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor){
                playButton.setDrawable(playButtonHoverDrawable);
           }

           @Override
            public void exit(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor){
               playButton.setDrawable(playButtonNormalDrawable);
           }
        });
        stage.addActor(playButton);

        //Botón salir
        Image exitButton = new Image(exitButtonTexture);
        exitButton.setPosition((float) VIRTUAL_WIDTH / 2 - exitButton.getWidth() / 2, playButton.getY() - playButton.getHeight() - 20);
        exitButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
           @Override
           public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
               buttonSound.play(0.5f);
               SequenceAction sequenceAction = new SequenceAction();
               sequenceAction.addAction(Actions.delay(0.3f));
               sequenceAction.addAction(Actions.run(() -> {
                   Gdx.app.exit();
               }));
               stage.addAction(sequenceAction);
               return true;
           }

            @Override
            public void enter(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor){
                exitButton.setDrawable(exitButtonHoverDrawable);
            }

            @Override
            public void exit(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor){
                exitButton.setDrawable(exitButtonNormalDrawable);
            }

        });
        stage.addActor(exitButton);
    }
}
