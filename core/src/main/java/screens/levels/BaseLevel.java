package screens.levels;

import classes.InputHandler;
import classes.enemies.Enemy;
import classes.effects.FloatingScore;
import classes.enemies.EnemyState;
import classes.enemies.flyingmouth.FlyingMouth;
import classes.platforms.Platform;
import classes.player.Player;
import classes.projectiles.Projectile;
import classes.projectiles.ProjectileState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import screens.BaseScreen;
import screens.GameOverScreen;
import screens.MainMenuScreen;
import screens.levels.world1.Level_1_1_Screen;

import java.util.Iterator;

public abstract class BaseLevel extends BaseScreen {

    //-- Constants --
    protected static final int PLATFORM_HEIGHT = 64;
    protected static final int AERIAL_LONG_PLATFORM_WIDTH = 300;
    protected static final int AERIAL_PLATFORM_WIDTH = 200;
    protected static final int VERTICAL_SPACING = 130;
    protected static final int SLIME_WIDTH = 61;
    protected static final int FLYING_MOUTH_WIDTH = 64;
    protected static final int FLYING_MOUTH_HEIGHT = 56;

    //--- Sounds ---
    protected Sound projectileSound;

    //--- GameStates
    protected GameState currentState;
    protected Texture backgroundTexture;
    protected float levelWidth;
    protected float levelHeight;

    //--- Pause UI ---
    protected Table pauseTable;
    protected Texture continueButtonTexture;
    protected Texture continueButtonHoverTexture;
    protected Texture restartButtonTexture;
    protected Texture restartButtonHoverTexture;
    protected Texture returnMenuButtonTexture;
    protected Texture returnMenuButtonHoverTexture;
    protected Viewport uiViewport;

    //--- Entities ---
    protected Player player;
    protected int spawnPointX;
    protected int spawnPointY;
    protected Array<Projectile> projectiles;
    protected Array<Projectile> enemiesProjectiles;
    protected Array<Enemy> enemies;
    protected Array<FloatingScore> floatingScores;

    //--- Platforms ---
    protected Array<Platform> platforms;

    //--- HUD ---
    protected OrthographicCamera hudCamera;
    protected Array<Texture> lifeSprites;
    protected Animation<TextureRegion> animationLifeEnds;
    protected float hudAnimationStateTime = 0f;
    protected InputHandler inputHandler;
    protected final GlyphLayout glyphLayout = new GlyphLayout();
    protected int displayScore;

    public BaseLevel(Game game){
        super(game);
    }

    @Override
    public void show(){
        super.show();

        this.currentState = GameState.RUNNING;

        //--- SOUNDS CONFIGURATION ---
        this.projectileSound = assetManager.get("sounds/effects/efectoProyectil.ogg", Sound.class);

        //--- PAUSE UI CONFIGURATION ---
        continueButtonTexture = assetManager.get("buttons/botonContinuar.png", Texture.class);
        continueButtonHoverTexture = assetManager.get("buttons/botonContinuar2.png");
        restartButtonTexture = assetManager.get("buttons/botonReintentar.png");
        restartButtonHoverTexture = assetManager.get("buttons/botonReintentar2.png");
        returnMenuButtonTexture = assetManager.get("buttons/botonVolver.png");
        returnMenuButtonHoverTexture = assetManager.get("buttons/botonVolver2.png");

        //--- HUD CONFIGURATION ---
        this.lifeSprites = new Array<>();
        this.floatingScores = new Array<>();
        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        this.displayScore = 0;
        this.animationLifeEnds = getAnimationSprite(1, 10, assetManager.get("hud/caliz0Vidas.png"));
        this.lifeSprites.add(assetManager.get("hud/caliz1Vidas.png"));
        this.lifeSprites.add(assetManager.get("hud/caliz2Vidas.png"));
        this. lifeSprites.add(assetManager.get("hud/caliz3Vidas.png"));

        uiViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, hudCamera);

        stage.setViewport(uiViewport);
        Gdx.input.setInputProcessor(stage);

        //--- PAUSE UI CONFIGURATION ---
        createPauseTable();
        setupLevelArrays();
        setupLevelPlatforms();
        setupLevelEnemies();
        setupPlayer();
    }

    @Override
    public void render(float delta){
        super.render(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        if(currentState == GameState.PAUSED){
            backgroundMusic.setVolume(0.2f);
        } else {
            backgroundMusic.setVolume(0.5f);
        }

        if(currentState == GameState.RUNNING) {
            updateEntities(delta);
        }

        camera.position.x = player.getBounds().x;
        camera.position.x = MathUtils.clamp(camera.position.x, (float) VIRTUAL_WIDTH / 2, levelWidth - ((float) VIRTUAL_WIDTH / 2));

        camera.position.y = player.getBounds().y;
        camera.position.y = MathUtils.clamp(camera.position.y, (float) VIRTUAL_HEIGHT / 2, levelHeight - ((float) VIRTUAL_HEIGHT / 2));

        //--- Dibujado de juego ---
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, levelWidth, levelHeight);
        drawEntities(batch);

        //--- Dibujado de HUD ---
        uiViewport.apply();
        drawHUD(batch, delta);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose(){
        super.dispose();
        this.backgroundTexture.dispose();
        for (Texture t : lifeSprites) {
            t.dispose();
        }
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    public abstract void setupLevelPlatforms();

    public abstract void setupLevelEnemies();

    public abstract void setupPlayer();

    public void handleCollisions() {
        for(Enemy enemy : enemies) {
            if(player.getBounds().overlaps(enemy.getBounds())) {
                player.takeDamage(enemy.makeDamage());
            }
        }

        for(Projectile projectile : enemiesProjectiles){
            if(player.getBounds().overlaps((projectile.getBounds()))){
                player.takeDamage(projectile.makeDamage());
            }
        }

        for(Projectile projectile : projectiles) {
            for(Enemy enemy : enemies) {
                if(projectile.getBounds().overlaps(enemy.getBounds())) {
                    enemy.takeDamage(projectile.makeDamage());
                    projectile.setCurrentState(ProjectileState.DEACTIVATE);
                    break;
                }
            }
        }
    }

    public void setupLevelArrays() {
        this.platforms = new Array<>();
        this.enemies = new Array<>();
        this.projectiles = new Array<>();
        this.enemiesProjectiles = new Array<>();
    }

    public void cleanupEntities() {
        //--- Limpieza de Proyectiles ---
        for (Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext(); ) {
            Projectile projectile = iter.next();
            if (projectile.getCurrentState() == ProjectileState.DEACTIVATE) {
                iter.remove();
            }
        }
        //--- Limpieza de Enemigos ---
        for (Iterator<Enemy> iter = enemies.iterator(); iter.hasNext(); ) {
            Enemy enemy = iter.next();
            if (enemy.getCurrentState() == EnemyState.DIE) {
                player.setScore(enemy.getGivenScore());
                String scoreText = "+" + enemy.getGivenScore();
                floatingScores.add(new FloatingScore(scoreText, enemy.getBounds().x, enemy.getBounds().y + enemy.getBounds().height));
                iter.remove();
            }
        }
    }

    public void updateEntities(float delta) {
        inputHandler.update();
        player.update(delta, platforms, levelWidth);

        if(player.shouldSpawnProjectile()) {
            projectileSound.play(0.5f);
            projectiles.add(player.setProjectile());
        }

        if(player.isDeathAnimationFinished()) { game.setScreen(new GameOverScreen(game)); }

        for(Projectile projectile : projectiles) {
            projectile.update(delta);
        }

        for(Enemy enemy : enemies) {
            enemy.update(delta, platforms, levelWidth);
            if(enemy instanceof FlyingMouth){
                FlyingMouth flyingMouth = (FlyingMouth) enemy;
                if(flyingMouth.shouldSpawnProjectile()){
                    projectileSound.play(0.5f);
                    enemiesProjectiles.add(flyingMouth.setProjectile());
                }
            }
        }

        for(Projectile projectile : enemiesProjectiles){
            projectile.update(delta);
        }

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

        handleCollisions();
        cleanupEntities();
    }

    public void drawEntities(Batch batch) {
        player.draw(batch);
        for(Platform platform : platforms) {
            platform.draw(batch);
        }
        for(Enemy enemy : enemies){
            enemy.draw(batch);
        }
        for(Projectile projectile : projectiles) {
            projectile.draw(batch);
        }
        for(Projectile projectile : enemiesProjectiles){
            projectile.draw(batch);
        }
    }

    public void drawHUD(Batch batch, float delta){
        batch.setProjectionMatrix(hudCamera.combined);

        int currentLife = player.getLife();
        if (currentLife > 0) {
            Texture lifeSprite = lifeSprites.get(currentLife - 1);
            batch.draw(lifeSprite, 10, VIRTUAL_HEIGHT - lifeSprite.getHeight() - 10);
        } else {
            hudAnimationStateTime += delta;
            TextureRegion currentFrame = animationLifeEnds.getKeyFrame(hudAnimationStateTime, false);
            batch.draw(currentFrame, 10, VIRTUAL_HEIGHT - currentFrame.getRegionHeight() - 10);
        }

        glyphLayout.setText(font, "Puntuación: " + displayScore);
        float textX = (VIRTUAL_WIDTH - glyphLayout.width) / 2;
        float textY = (VIRTUAL_HEIGHT) * 0.95f;
        font.draw(batch, glyphLayout, textX, textY);

        for (FloatingScore score : floatingScores) {
            score.draw(batch, font);
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
        Button continueButton = getContinueButton(continueStyle);

        //--- Restart Button ---
        Button.ButtonStyle restartStyle = new Button.ButtonStyle();
        restartStyle.up = new TextureRegionDrawable(restartButtonTexture);
        restartStyle.over = new TextureRegionDrawable(restartButtonHoverTexture);
        Button restartButton = getRestartButton(restartStyle);

        //--- Return Main Manu Button ---
        Button.ButtonStyle returnStyle = new Button.ButtonStyle();
        returnStyle.up = new TextureRegionDrawable(returnMenuButtonTexture);
        returnStyle.over = new TextureRegionDrawable(returnMenuButtonHoverTexture);
        Button returnButton = getReturnButton(returnStyle);

        //--- Add Buttons to the table ---
        pauseTable.add(pauseLabel).padBottom(40).row();
        pauseTable.add(continueButton).padBottom(10).row();
        pauseTable.add(restartButton).padBottom(10).row();
        pauseTable.add(returnButton).padBottom(10).row();

        pauseTable.setVisible(false);

        stage.addActor(pauseTable);

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
                sequenceAction.addAction(Actions.run(() -> togglePause()));
                stage.addAction(sequenceAction);
            }
        });
        return continueButton;
    }

    private Button getRestartButton(Button.ButtonStyle restartStyle) {
        Button restartButton = new Button(restartStyle);

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                buttonSound.play(0.5f);
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                sequenceAction.addAction(Actions.run(() -> game.setScreen(new Level_1_1_Screen(game))));
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

    public void togglePause() {
        if(currentState == GameState.RUNNING) {
            currentState = GameState.PAUSED;
            pauseTable.setVisible(true);
        }else if (currentState == GameState.PAUSED) {
            currentState = GameState.RUNNING;
            pauseTable.setVisible(false);
        }
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
