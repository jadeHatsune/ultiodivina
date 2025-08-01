package screens.levels;

import classes.InputHandler;
import classes.enemies.Enemy;
import classes.effects.FloatingScore;
import classes.enemies.EnemyState;
import classes.enemies.slime.Slime;
import classes.platforms.Platform;
import classes.player.Player;
import classes.player.PlayerFacing;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hod.ultiodivina.Main;
import screens.BaseScreen;
import screens.GameOverScreen;
import screens.MainMenuScreen;
import screens.levels.world1.Level_1_1_Screen;

import java.util.Iterator;

public abstract class BaseLevel extends BaseScreen {

    //-- Constants --
    protected static final int PLATFORM_HEIGHT = 64;
    protected static final int AERIAL_PLATFORM_WIDTH = 200;
    protected static final int AERIAL_LONG_PLATFORM_WIDTH = 300;
    protected static final int GROUND_PLATFORM_DIF_Y = 15;
    protected static final int AERIAL_PLATFORM_DIF_Y = 31;
    protected static final int VERTICAL_SPACING = 130;
    protected static final int SLIME_WIDTH = 61;

    //--- Sounds ---
    protected Sound buttonsSound;
    protected Sound projectileSound;

    //--- GameStates
    protected GameState currentState;
    protected Texture backgroundTexture;

    //--- Pause UI ---
    protected Table pauseTable;
    protected Texture continueButtonTexture;
    protected Texture continueButtonHoverTexture;
    protected Texture restartButtonTexture;
    protected Texture restartButtonHoverTexture;
    protected Texture returnMenuButtonTexture;
    protected Texture returnMenuButtonHoverTexture;

    //--- Entities ---
    protected Player player;
    protected int spawnPointX;
    protected int spawnPointY;
    protected Array<Projectile> projectiles;
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
        this.buttonsSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/efectoBotones.ogg"));
        this.projectileSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/efectoProyectil.ogg"));

        //--- PAUSE UI CONFIGURATION ---
        continueButtonTexture = new Texture(Gdx.files.internal("buttons/botonContinuar.png"));
        continueButtonHoverTexture = new Texture(Gdx.files.internal("buttons/botonContinuar2.png"));
        restartButtonTexture = new Texture(Gdx.files.internal("buttons/botonReintentar.png"));
        restartButtonHoverTexture = new Texture(Gdx.files.internal("buttons/botonReintentar2.png"));
        returnMenuButtonTexture = new Texture((Gdx.files.internal("buttons/botonVolver.png")));
        returnMenuButtonHoverTexture = new Texture(Gdx.files.internal("buttons/botonVolver2.png"));

        //--- HUD CONFIGURATION ---
        this.lifeSprites = new Array<>();
        this.floatingScores = new Array<>();
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        this.displayScore = 0;
        animationLifeEnds = getAnimationSprite(1, 10, "hud/caliz0Vidas.png");
        lifeSprites.add(new Texture("hud/caliz1Vidas.png"));
        lifeSprites.add(new Texture("hud/caliz2Vidas.png"));
        lifeSprites.add(new Texture("hud/caliz3Vidas.png"));

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

        if(currentState == GameState.RUNNING) {
            updateEntities(delta);
        }

        ScreenUtils.clear(0, 0, 0, 1);

        //--- Dibujado de juego ---
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        drawEntities(batch);
        //--- Dibujado de HUD ---
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

    public abstract void setupLevelPlatforms();

    public abstract void setupLevelEnemies();

    public void handleCollisions(){
        for(Enemy enemy : enemies) {
            if(player.getBounds().overlaps(enemy.getBounds())) {
                player.takeDamage(enemy.makeDamage());
            }
        }

        for(Projectile projectile : projectiles) {
            for(Enemy enemy : enemies) {
                if(projectile.getBounds().overlaps(enemy.getBounds())) {
                    enemy.takeDamage(1);
                    projectile.setCurrentState(ProjectileState.DEACTIVATE);
                    break;
                }
            }
        }
    }

   public abstract void setupPlayer();

    public void setupLevelArrays(){
        this.platforms = new Array<>();
        this.enemies = new Array<>();
        this.projectiles = new Array<>();
    }

    public void cleanupEntities(){
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

    public void updateEntities(float delta){
        inputHandler.update();
        player.update(delta, platforms);

        if(player.shouldSpawnProjectile()) {
            float projectileSpeed = 500f;
            float startX = player.getCurrentFacing() == PlayerFacing.FACING_RIGHT ? player.getBounds().x + player.getBounds().width : player.getBounds().x;
            float startY = player.getBounds().y + (player.getBounds().height / 3);

            if(player.getCurrentFacing() == PlayerFacing.FACING_LEFT) {
                projectileSpeed = -500f;
            }
            projectileSound.play(0.5f);
            projectiles.add(new Projectile((int) startX, (int) startY, projectileSpeed, "lymhiel/lymhiel_projectile.png"));
        }

        if(player.isDeathAnimationFinished()) { game.setScreen(new GameOverScreen(game)); }

        for(Projectile projectile : projectiles) {
            projectile.update(delta);
        }

        for(Enemy enemy : enemies) {
            enemy.update(delta, platforms);
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

    public void drawEntities(Batch batch){
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
                buttonsSound.play(0.5f);
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                sequenceAction.addAction(Actions.run(() -> togglePause()));
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
                buttonsSound.play(0.5f);
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                sequenceAction.addAction(Actions.run(() -> {
                    game.setScreen(new MainMenuScreen(game));
                }));
                stage.addAction(sequenceAction);
            }
        });
        return returnButton;
    }

    private Button getRestartButton(Button.ButtonStyle restartStyle) {
        Button restartButton = new Button(restartStyle);

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                buttonsSound.play(0.5f);
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                sequenceAction.addAction(Actions.run(() -> {
                    game.setScreen(new Level_1_1_Screen(game));
                }));
                stage.addAction(sequenceAction);
            }
        });
        return restartButton;
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
