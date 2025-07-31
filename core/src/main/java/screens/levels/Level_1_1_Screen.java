package screens.levels;

import classes.InputHandler;
import classes.effects.FloatingScore;
import classes.enemies.slime.Slime;
import classes.enemies.slime.SlimeState;
import classes.platforms.Platform;
import classes.player.Player;
import classes.player.PlayerFacing;
import classes.projectiles.Projectile;
import classes.projectiles.ProjectileState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import screens.GameOverScreen;
import screens.MainMenuScreen;

import java.util.Iterator;

public class Level_1_1_Screen implements Screen {

    //-- Constants --
    private static final int PLATFORM_HEIGHT = 64;
    private static final int AERIAL_PLATFORM_WIDTH = 200;
    private static final int AERIAL_LONG_PLATFORM_WIDTH = 300;
    private static final int GROUND_PLATFORM_DIF_Y = 15;
    private static final int AERIAL_PLATFORM_DIF_Y = 31;
    private static final int VERTICAL_SPACING = 130;
    private static final int SLIME_WIDTH = 61;
    private static final int SPAWN_POINT = 64;

    //--- GameStates
    private final Game game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private BitmapFont font;
    private GameState currentState;

    //--- Pause UI ---
    private Stage uiStage;
    private Table pauseTable;
    private Texture continueButtonTexture;
    private Texture continueButtonHoverTexture;
    private Texture restartButtonTexture;
    private Texture restartButtonHoverTexture;
    private Texture returnMenuButtonTexture;
    private Texture returnMenuButtonHoverTexture;

    //--- Entities ---
    private Player player;
    private Array<Slime> slimes;
    private Array<Projectile> projectiles;
    private Array<FloatingScore> floatingScores;

    //--- Platforms ---
    private Array<Platform> platformsArray;

    //--- HUD ---
    private OrthographicCamera gameCamera;
    private OrthographicCamera hudCamera;
    private Array<Texture> lifeSprites;
    private Animation<TextureRegion> animationLifeEnds;
    private float hudAnimationStateTime = 0f;
    private InputHandler inputHandler;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private int displayScore;

    public Level_1_1_Screen(Game game){
        this.game = game;
    }

    @Override
    public void show(){
        this.floatingScores = new Array<>();
        this.batch = new SpriteBatch();
        this.backgroundTexture = new Texture(Gdx.files.internal("backgrounds/level1/nivel1-1.jpeg"));
        this.currentState = GameState.RUNNING;

        this.displayScore = 0;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AngelFortune.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;
        this.font = generator.generateFont(parameter);
        generator.dispose();

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        setupPlatforms();
        setupPlayer();
        setupEnemies();

        //--- PAUSE UI TEXTURES ---
        continueButtonTexture = new Texture(Gdx.files.internal("buttons/botonContinuar.png"));
        continueButtonHoverTexture = new Texture(Gdx.files.internal("buttons/botonContinuar2.png"));
        restartButtonTexture = new Texture(Gdx.files.internal("buttons/botonReintentar.png"));
        restartButtonHoverTexture = new Texture(Gdx.files.internal("buttons/botonReintentar2.png"));
        returnMenuButtonTexture = new Texture((Gdx.files.internal("buttons/botonVolver.png")));
        returnMenuButtonHoverTexture = new Texture(Gdx.files.internal("buttons/botonVolver2.png"));

        //--- HUD CONFIGURATION ---
        animationLifeEnds = getAnimationSprite(1, 10, "hud/caliz0Vidas.png");
        lifeSprites = new Array<>();

        lifeSprites.add(new Texture("hud/caliz1Vidas.png"));
        lifeSprites.add(new Texture("hud/caliz2Vidas.png"));
        lifeSprites.add(new Texture("hud/caliz3Vidas.png"));

        //--- PAUSE UI CONFIGURATION ---
        uiStage = new Stage(new ScreenViewport());
        createPauseTable();
        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    public void render(float delta){

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        if(currentState == GameState.RUNNING) {
            updateEntities(delta);
        }

        ScreenUtils.clear(0, 0, 0, 1);

        //--- Dibujado de juego ---
        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();

        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player.draw(batch);
        for(Platform platform : platformsArray) {
            platform.draw(batch);
        }
        for(Projectile projectile : projectiles) {
            projectile.draw(batch);
        }
        for(Slime slime : slimes){
            slime.draw(batch);
        }

        batch.end();

        //--- Dibujado de HUD ---
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        int currentLife = player.getLife();
        if (currentLife > 0) {
            Texture lifeSprite = lifeSprites.get(currentLife - 1);
            batch.draw(lifeSprite, 10, Gdx.graphics.getHeight() - lifeSprite.getHeight() - 10);
        } else {
            hudAnimationStateTime += delta;
            TextureRegion currentFrame = animationLifeEnds.getKeyFrame(hudAnimationStateTime, false);
            batch.draw(currentFrame, 10, Gdx.graphics.getHeight() - currentFrame.getRegionHeight() - 10);
        }

        glyphLayout.setText(font, "Puntuación: " + displayScore);
        float textX = (Gdx.graphics.getWidth() - glyphLayout.width) / 2;
        float textY = (Gdx.graphics.getHeight()) * 0.95f;
        font.draw(batch, glyphLayout, textX, textY);

        for (FloatingScore score : floatingScores) {
            score.draw(batch, font);
        }

        batch.end();

        uiStage.act(delta);
        uiStage.draw();

    }

    @Override
    public void dispose(){
        this.batch.dispose();
        this.font.dispose();
        this.backgroundTexture.dispose();
        for (Texture t : lifeSprites) {
            t.dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    public void setupPlatforms() {
        this.platformsArray = new Array<>();
        String platformGroundPath = "platforms/plataformaTierra.png";
        String platformAirLeftPath = "platforms/plataformaAereoIzquierda.png";
        String platformAirRightPath = "platforms/plataformaAereoDerecha.png";
        String platformAirLongPath = "platforms/plataformaAereoLarga.png";

        platformsArray.add(new Platform(0, 0, Gdx.graphics.getWidth(), 0, GROUND_PLATFORM_DIF_Y, platformGroundPath));
        float currentY = VERTICAL_SPACING;
        platformsArray.add(new Platform(0, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLongPath));
        platformsArray.add(new Platform(Gdx.graphics.getWidth() - AERIAL_LONG_PLATFORM_WIDTH, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLongPath));
        currentY += VERTICAL_SPACING;
        platformsArray.add(new Platform(0, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLeftPath));
        platformsArray.add(new Platform(Gdx.graphics.getWidth() - AERIAL_PLATFORM_WIDTH, (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirRightPath));
        currentY += VERTICAL_SPACING;
        platformsArray.add(new Platform((Gdx.graphics.getWidth() / 2) - (AERIAL_LONG_PLATFORM_WIDTH / 2), (int) currentY, 0, 0, AERIAL_PLATFORM_DIF_Y, platformAirLongPath));


    }

    public void setupEnemies(){
        this.slimes = new Array<>();
        for(Platform platform : platformsArray) {
            slimes.add(new Slime((int) (platform.getBounds().getX() + platform.getBounds().getWidth() - SLIME_WIDTH), (int) platform.getBounds().getY() + PLATFORM_HEIGHT));
        }
    }

    public void setupPlayer() {
        this.player = new Player(SPAWN_POINT, PLATFORM_HEIGHT);
        this.projectiles = new Array<>();
        this.inputHandler = new InputHandler(this.player);
    }

    public void handleCollisions(){
        for(Slime slime : slimes) {
            if(player.getBounds().overlaps(slime.getBounds())) {
                player.takeDamage(slime.makeDamage());
            }
        }

        for(Projectile projectile : projectiles) {
            for(Slime slime : slimes) {
                if(projectile.getBounds().overlaps(slime.getBounds())) {
                    slime.takeDamage(1);
                    projectile.setCurrentState(ProjectileState.DEACTIVATE);
                    break;
                }
            }
        }
    }

    public void cleanupEntities(){
        //--- Limpieza de Slimes ---
        for (Iterator<Slime> iter = slimes.iterator(); iter.hasNext(); ) {
            Slime slime = iter.next();
            if (slime.getCurrentState() == SlimeState.DIE) {
                player.setScore(slime.getGivenScore());
                String scoreText = "+" + slime.getGivenScore();
                floatingScores.add(new FloatingScore(scoreText, slime.getBounds().x, slime.getBounds().y + slime.getBounds().height));
                iter.remove();
            }
        }

        //--- Limpieza de Proyectiles ---
        for (Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext(); ) {
            Projectile projectile = iter.next();
            if (projectile.getCurrentState() == ProjectileState.DEACTIVATE) {
                iter.remove();
            }
        }
    }

    public void updateEntities(float delta) {
        inputHandler.update();
        player.update(delta, platformsArray);

        if(player.shouldSpawnProjectile()) {
            float projectileSpeed = 500f;
            float startX = player.getCurrentFacing() == PlayerFacing.FACING_RIGHT ? player.getBounds().x + player.getBounds().width : player.getBounds().x;
            float startY = player.getBounds().y + (player.getBounds().height / 3);

            if(player.getCurrentFacing() == PlayerFacing.FACING_LEFT) {
                projectileSpeed = -500f;
            }

            projectiles.add(new Projectile((int) startX, (int) startY, projectileSpeed, "lymhiel/lymhiel_projectile.png"));
        }

        if(player.isDeathAnimationFinished()) { game.setScreen(new GameOverScreen(game)); }

        for(Projectile projectile : projectiles) {
            projectile.update(delta);
        }
        for(Slime slime : slimes) {
            slime.update(delta, platformsArray);
        }

        handleCollisions();
        cleanupEntities();

        for (Iterator<FloatingScore> iter = floatingScores.iterator(); iter.hasNext(); ) {
            FloatingScore score = iter.next();
            score.update(delta);
            if (score.isFinished()) {
                iter.remove();
            }
        }

        if (displayScore < player.getScore()) {
            displayScore ++;
        }
    }

    public void createPauseTable() {
        pauseTable = new Table();
        pauseTable.setFillParent(true);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0,0,0, 0.7f));
        pixmap.fill();
        pauseTable.setBackground(new TextureRegionDrawable(new Texture(pixmap)));
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label pauseLabel = new Label("PAUSA", labelStyle);

        //--- Continue Button ---
        Button.ButtonStyle continueStyle = new Button.ButtonStyle();
        continueStyle.up = new TextureRegionDrawable(continueButtonTexture);
        continueStyle.over = new TextureRegionDrawable(continueButtonHoverTexture);
        Button continueButton = new Button(continueStyle);

        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                //--- Toggle Pause ---
                togglePause();
            }
        });

        //--- Restart Button ---
        Button.ButtonStyle restartStyle = new Button.ButtonStyle();
        restartStyle.up = new TextureRegionDrawable(restartButtonTexture);
        restartStyle.over = new TextureRegionDrawable(restartButtonHoverTexture);
        Button restartButton = new Button(restartStyle);

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.setScreen(new Level_1_1_Screen(game));
                dispose();
            }
        });

        //--- Return Main Manu Button ---
        Button.ButtonStyle returnStyle = new Button.ButtonStyle();
        returnStyle.up = new TextureRegionDrawable(returnMenuButtonTexture);
        returnStyle.over = new TextureRegionDrawable(returnMenuButtonHoverTexture);
        Button returnButton = new Button(returnStyle);

        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        //--- Add Buttons to the table ---
        pauseTable.add(pauseLabel).padBottom(40).row();
        pauseTable.add(continueButton).padBottom(10).row();
        pauseTable.add(restartButton).padBottom(10).row();
        pauseTable.add(returnButton).padBottom(10).row();

        pauseTable.setVisible(false);

        uiStage.addActor(pauseTable);

    }

    public void togglePause() {
        if(currentState == GameState.RUNNING) {
            currentState = GameState.PAUSED;
            pauseTable.setVisible(true);
        }else if (currentState == GameState.PAUSED) {
            currentState = GameState.RUNNING;
            pauseTable.setVisible(false);
        }
    }

    public Animation<TextureRegion> getAnimationSprite(int frameCols, int frameRows, String spriteSheetPath) {
        Texture spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));

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
