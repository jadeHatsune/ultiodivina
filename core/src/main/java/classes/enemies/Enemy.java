package classes.enemies;

import classes.platforms.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.hod.ultiodivina.Main;

import java.util.Random;

public abstract class Enemy {

    //--- CONSTANTS ---
    private static final float DAMAGE_COLOR_DURATION = 0.2f;

    //--- STATES ---
    protected boolean isTakingDamage;
    protected float damageColorTimer;
    protected EnemyState currentState;
    protected EnemyFacing currentFacing;
    protected int actionTimer;
    protected final Random random;
    protected float stateTime;

    //-- PHYSICS --
    protected Rectangle bounds;
    protected float speedX;
    protected float speedY;
    protected float gravity;

    //--- PROPERTIES ---
    protected int life;
    protected int givenScore;
    protected int damage;
    protected boolean shouldSpawnProjectile;
    protected boolean projectileSpawnedInThisAttack;

    //--- ANIMATIONS ---
    protected Animation<TextureRegion> animationIdle;
    protected Animation<TextureRegion> animationWalking;
    protected Animation<TextureRegion> animationAttack;
    protected Animation<TextureRegion> projectileAnimation;
    protected int attackSpawnFrame;


    public Enemy(int life, int givenScore) {
        this.life = life;
        this.givenScore = givenScore;
        this.currentState = EnemyState.IDLE;
        this.random = new Random();
        this.isTakingDamage = false;
        this.projectileSpawnedInThisAttack = false;
        this.shouldSpawnProjectile = false;
        this.actionTimer = 60;
    }

    //--- GETTERS ---
    public Rectangle getBounds() { return this.bounds; }
    public int getGivenScore() { return this.givenScore; }
    public EnemyState getCurrentState() { return this.currentState; }
    public int makeDamage() { return this.damage; }
    public boolean shouldSpawnProjectile() {
        return this.shouldSpawnProjectile;
    }

    //--- SETTERS ---
    public void takeDamage(int dmg) {
        this.life -= dmg;
        isTakingDamage = true;
        damageColorTimer = DAMAGE_COLOR_DURATION;
    }

    public void update(float delta, Array<Platform> platforms, float worldWidth){
        if(isTakingDamage) {
            damageColorTimer -= delta;
            if(damageColorTimer <= 0) {
                isTakingDamage = false;
            }
        }

        actionTimer--;
        if (actionTimer <= 0) {
            processMovement();
        }

        shouldSpawnProjectile = false;

        speedY += gravity * delta;
        moveX(delta, platforms, worldWidth);
        moveY(delta, platforms);

        stateTime += delta;

        if(this.life > 0){
            if(currentState == EnemyState.ATTACKING) {
                if(animationAttack.isAnimationFinished(stateTime)) {
                    transitionToState(EnemyState.IDLE);
                } else {
                    if(!projectileSpawnedInThisAttack && animationAttack.getKeyFrameIndex(stateTime) >= attackSpawnFrame) {
                        shouldSpawnProjectile = true;
                        projectileSpawnedInThisAttack = true;
                    }
                }
            } else if(speedX != 0) {
                transitionToState(EnemyState.WALKING);
            } else {
                transitionToState(EnemyState.IDLE);
            }
        } else {
            transitionToState(EnemyState.DIE);
        }
    }

    public abstract void moveX(float delta, Array<Platform> platforms, float worldWidth);

    public abstract void moveY(float delta, Array<Platform> platforms);

    public abstract void processMovement();

    public abstract void attack();

    public void transitionToState(EnemyState newState) {
        if(this.currentState != newState) {
            this.currentState = newState;
            this.stateTime = 0f;
        }
    }

    public abstract void draw(Batch batch);

}
