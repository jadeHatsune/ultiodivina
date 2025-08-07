package screens.levels;

import classes.Inputs.InputHandler;
import classes.enemies.Enemy;
import classes.effects.FloatingScore;
import classes.enemies.EnemyFacing;
import classes.enemies.EnemyState;
import classes.enemies.bosses.Boss;
import classes.enemies.bosses.asmodeus.Asmodeus;
import classes.enemies.flyingmouth.FlyingMouth;
import classes.enemies.tentacle.Tentacle;
import classes.platforms.Platform;
import classes.player.Player;
import classes.player.PlayerFacing;
import classes.projectiles.Projectile;
import classes.projectiles.ProjectileState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
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
import com.hod.ultiodivina.Main;
import screens.BaseScreen;
import screens.GameOverScreen;
import screens.MainMenuScreen;
import screens.levels.world1.Level_1_1_Screen;
import java.util.Iterator;

import static classes.AssetDescriptors.*;

public abstract class BaseLevel extends BaseScreen {

    protected enum CinematicState {
        MOVING_TO_BOSS,
        BOSS_ANIMATION,
        MOVING_TO_PLAYER,
        BOSS_DEATH,
        BOSS_DEATH_ANIMATION,
        FINISHED
    }

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
    protected float parallaxFactor = 0.5f;
    protected CinematicState cinematicState;
    protected float cinematicTimer;
    protected boolean showHud;

    //--- Pause UI ---
    protected Table pauseTable;
    protected Button btnContinue, btnRestart, btnReturn;
    protected TextureRegionDrawable continueUp, continueOver;
    protected TextureRegionDrawable restartUp, restartOver;
    protected TextureRegionDrawable returnUp, returnOver;
    protected Viewport uiViewport;

    //--- Entities ---
    protected Player player;
    protected int spawnPointX;
    protected int spawnPointY;
    protected Array<Projectile> projectiles;
    protected Array<Projectile> enemiesProjectiles;
    protected Array<Enemy> enemies;
    protected Array<FloatingScore> floatingScores;
    protected Boss boss;

    //--- Platforms ---
    protected Array<Platform> platforms;

    //--- HUD ---
    protected OrthographicCamera hudCamera;
    protected Array<Texture> lifeSprites;
    protected Animation<TextureRegion> animationLifeEnds;
    protected Animation<TextureRegion> animationLifeBarIn;
    protected Array<TextureRegion> lifeBarSections;
    protected float hudAnimationStateTime = 0f;
    protected float hudLifeBarAnimationStateTime = 0f;
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
        Gdx.input.setCursorCatched(true);

        //--- SOUNDS CONFIGURATION ---
        this.projectileSound = assetManager.get(SOUND_PROJECTILE, Sound.class);

        //--- PAUSE UI CONFIGURATION ---
        continueUp = new TextureRegionDrawable(assetManager.get(BTN_CONTINUE, Texture.class));
        continueOver = new TextureRegionDrawable(assetManager.get(BTN_CONTINUE_HOVER, Texture.class));
        restartUp = new TextureRegionDrawable(assetManager.get(BTN_RESTART, Texture.class));
        restartOver = new TextureRegionDrawable(assetManager.get(BTN_RESTART_HOVER, Texture.class));
        returnUp = new TextureRegionDrawable(assetManager.get(BTN_BACK, Texture.class));
        returnOver = new TextureRegionDrawable(assetManager.get(BTN_BACK_HOVER, Texture.class));

        //--- HUD CONFIGURATION ---
        this.lifeSprites = new Array<>();
        this.floatingScores = new Array<>();
        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        this.displayScore = 0;
        this.animationLifeEnds = getAnimationSprite(1, 10, assetManager.get(CHALICE_LIFE_0, Texture.class));
        this.animationLifeBarIn = getAnimationSprite(1, 43, assetManager.get(LIFE_BAR_LOADING, Texture.class));

        Texture lifeBarSheet = assetManager.get(LIFE_BAR, Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(lifeBarSheet,
            lifeBarSheet.getWidth() / 11,
            lifeBarSheet.getHeight());

        lifeBarSections = new Array<>(tmp[0]);

        this.lifeSprites.add(assetManager.get(CHALICE_LIFE_1, Texture.class));
        this.lifeSprites.add(assetManager.get(CHALICE_LIFE_2, Texture.class));
        this. lifeSprites.add(assetManager.get(CHALICE_LIFE_3, Texture.class));

        uiViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, hudCamera);

        stage.setViewport(uiViewport);
        Gdx.input.setInputProcessor(stage);

        //--- PAUSE UI CONFIGURATION ---
        createPauseTable();
        setupLevelArrays();
        this.player = ((Main) game).player;
    }

    @Override
    public void render(float delta){
        super.render(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        gamepadMenuController.wantsToPause();

        if(currentState == GameState.RUNNING) {
            Gdx.input.setCursorCatched(true);
            updateEntities(delta);
            camera.position.x = player.getBounds().x;
            camera.position.y = player.getBounds().y;
            backgroundMusic.setVolume(0.5f);
        } else if(currentState == GameState.CINEMATIC){
            updateCinematic(delta);
        } else if(currentState == GameState.PAUSED){
            backgroundMusic.setVolume(0.2f);
            if(Controllers.getControllers().size > 0) {
                gamepadMenuController.update();
                updateButtonFocus();
            } else {
                Gdx.input.setCursorCatched(false);
                resetButtonStyles();
            }
        }

        camera.position.x = MathUtils.clamp(camera.position.x, (float) VIRTUAL_WIDTH / 2, levelWidth - ((float) VIRTUAL_WIDTH / 2));
        camera.position.y = MathUtils.clamp(camera.position.y, (float) VIRTUAL_HEIGHT / 2, levelHeight - ((float) VIRTUAL_HEIGHT / 2));

        //--- Dibujado de juego ---
        batch.begin();
        float backgroundX = (camera.position.x - VIRTUAL_WIDTH / 2f) * parallaxFactor;
        float backgroundY = (camera.position.y - VIRTUAL_HEIGHT / 2f) * parallaxFactor;

        batch.draw(backgroundTexture, backgroundX, backgroundY,
            backgroundTexture.getWidth(), backgroundTexture.getHeight());

        drawEntities(batch);

        //--- Dibujado de HUD ---
        uiViewport.apply();
        if(showHud) {
            drawHUD(batch, delta);
        }
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose(){
        super.dispose();
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
            if (enemy instanceof Tentacle) {
                Tentacle tentacle = (Tentacle) enemy;
                if (tentacle.isAttackActive() && tentacle.getAttackBounds().overlaps(player.getBounds())) {
                    player.takeDamage(tentacle.makeDamage());
                }
            }
            else if (player.getBounds().overlaps(enemy.getBounds())) {
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
                if (enemy.isDieAnimationFinished()) {
                    player.setScore(enemy.getGivenScore());
                    String scoreText = "+" + enemy.getGivenScore();
                    floatingScores.add(new FloatingScore(scoreText, enemy.getBounds().x, enemy.getBounds().y + enemy.getBounds().height));
                    iter.remove();
                }
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

        Array<Enemy> spawnedEnemies = new Array<>();

        for(Enemy enemy : enemies) {
            enemy.update(delta, platforms, levelWidth, levelHeight);
            if(enemy instanceof FlyingMouth){
                FlyingMouth flyingMouth = (FlyingMouth) enemy;
                if(flyingMouth.shouldSpawnProjectile()){
                    projectileSound.play(0.5f);
                    enemiesProjectiles.add(flyingMouth.setProjectile());
                }
            }

            if(enemy instanceof Asmodeus){
                Asmodeus asmodeus = (Asmodeus) enemy;
                if(asmodeus.shouldSpawnProjectile()){
                    EnemyFacing facing;
                    int position;
                    if(player.getPlayerFacing() == PlayerFacing.FACING_LEFT){
                        facing = EnemyFacing.FACING_LEFT;
                        position = 64;
                    }else{
                        facing = EnemyFacing.FACING_RIGHT;
                        position = -64;
                    }

                    spawnedEnemies.add(new Tentacle((int) player.getBounds().x + position, (int) player.getBounds().y, facing,
                        getAnimationSprite(1, 7, assetManager.get(TENTACLE_SPAWN, Texture.class)),
                        getAnimationSprite(1, 11, assetManager.get(TENTACLE_IDLE, Texture.class)),
                        getAnimationSprite(1, 18, assetManager.get(TENTACLE_ATTACK, Texture.class))));
                }
            }
        }

        enemies.addAll(spawnedEnemies);

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

        if (enemies.contains(boss, true) && boss.getCurrentState() == EnemyState.DIE && this.currentState != GameState.CINEMATIC) {
            backgroundMusic.stop();
            this.currentState = GameState.CINEMATIC;
            this.cinematicState = CinematicState.BOSS_DEATH;
            this.cinematicTimer = 0f;
            this.showHud = false;
        }

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
        for (FloatingScore score : floatingScores) {
            score.draw(batch, font);
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

        if(enemies.contains(boss, true)){
            float barX = VIRTUAL_WIDTH - 150;
            float barY = VIRTUAL_HEIGHT - 64;
            if(!animationLifeBarIn.isAnimationFinished(hudLifeBarAnimationStateTime)){
                hudLifeBarAnimationStateTime += delta;
                TextureRegion currentFrame = animationLifeBarIn.getKeyFrame(hudLifeBarAnimationStateTime, false);
                batch.draw(currentFrame, barX, barY);
            } else {
                int totalLife = boss.getLife();
                float healthRatio = (float) boss.getCurrentLife() / totalLife;

                int totalFrames = 11;
                int frameIndex = (totalFrames - 1) - (int)Math.ceil(healthRatio * (totalFrames - 1));

                if (boss.getCurrentLife() == 0) {
                    frameIndex = totalFrames - 1;
                } else if (frameIndex < 0) {
                    frameIndex = 0;
                }

                TextureRegion currentFrame = lifeBarSections.get(frameIndex);
                batch.draw(currentFrame, barX, barY);
            }
        }

    }

    protected void updateCinematic(float delta) {
        cinematicTimer += delta;

        float playerX = player.getBounds().x;
        float playerY = player.getBounds().y;
        float bossX = boss.getBounds().x + boss.getBounds().width / 2;
        float bossY = boss.getBounds().y + boss.getBounds().height / 2;

        float timeToMove = 2.0f;

        float progress;

        switch(cinematicState){
            case MOVING_TO_BOSS:
                progress = Math.min(1f, cinematicTimer / timeToMove);
                camera.position.x = Interpolation.sine.apply(playerX, bossX, progress);
                camera.position.y = Interpolation.sine.apply(playerY, bossY, progress);
                if(progress >= 1f){
                    cinematicState = CinematicState.BOSS_ANIMATION;
                    cinematicTimer = 0f;
                }
                break;
            case BOSS_ANIMATION:
                boss.update(delta, platforms, levelWidth, levelHeight);
                if(boss.isIntroAnimationFinished()){
                    cinematicState = CinematicState.MOVING_TO_PLAYER;
                    cinematicTimer = 0f;
                }
                break;
            case BOSS_DEATH:
                progress = Math.min(1f, cinematicTimer / timeToMove);
                camera.position.x = Interpolation.sine.apply(playerX, bossX, progress);
                camera.position.y = Interpolation.sine.apply(playerY, bossY, progress);
                if(progress >= 1f){
                    cinematicState = CinematicState.BOSS_DEATH_ANIMATION;
                    cinematicTimer = 0f;
                }
                break;
            case BOSS_DEATH_ANIMATION:
                boss.update(delta, platforms, levelWidth, levelHeight);
                if(boss.isDieAnimationFinished()){
                    cinematicState = CinematicState.MOVING_TO_PLAYER;
                    cinematicTimer = 0f;
                }
                break;
            case MOVING_TO_PLAYER:
                progress = Math.min(1f, cinematicTimer / timeToMove);
                camera.position.x = Interpolation.sine.apply(bossX, playerX, progress);
                camera.position.y = Interpolation.sine.apply(bossY, playerY, progress);
                if(progress >= 1f) {
                    cinematicState = CinematicState.FINISHED;
                    currentState = GameState.RUNNING;
                    showHud = true;
                    backgroundMusic.play();
                }
                break;
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
        continueStyle.up = continueUp;
        continueStyle.over = continueOver;
        btnContinue = getContinueButton(continueStyle);

        //--- Restart Button ---
        Button.ButtonStyle restartStyle = new Button.ButtonStyle();
        restartStyle.up = restartUp;
        restartStyle.over = restartOver;
        btnRestart = getRestartButton(restartStyle);

        //--- Return Main Manu Button ---
        Button.ButtonStyle returnStyle = new Button.ButtonStyle();
        returnStyle.up = returnUp;
        returnStyle.over = returnOver;
        btnReturn = getReturnButton(returnStyle);

        menuButtons.add(btnContinue);
        menuButtons.add(btnRestart);
        menuButtons.add(btnReturn);

        //--- Add Buttons to the table ---
        pauseTable.add(pauseLabel).padBottom(40).row();
        pauseTable.add(btnContinue).padBottom(10).row();
        pauseTable.add(btnRestart).padBottom(10).row();
        pauseTable.add(btnReturn).padBottom(10).row();

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

    private void updateButtonFocus(){
        int selectedIndex = gamepadMenuController.getSelectedIndex();

        if(selectedIndex == 0) {
            btnContinue.getStyle().up = continueOver;
        } else {
            btnContinue.getStyle().up = continueUp;
        }

        if(selectedIndex == 1) {
            btnRestart.getStyle().up = restartOver;
        } else {
            btnRestart.getStyle().up = restartUp;
        }

        if(selectedIndex == 2) {
            btnReturn.getStyle().up = returnOver;
        } else {
            btnReturn.getStyle().up = returnUp;
        }

    }

    private void resetButtonStyles(){
        btnContinue.getStyle().up = continueUp;
        btnRestart.getStyle().up = restartUp;
        btnReturn.getStyle().up = returnUp;
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
