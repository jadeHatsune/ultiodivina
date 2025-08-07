package classes.enemies;

import classes.platforms.Platform;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

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
    protected int currentLife;
    protected int givenScore;
    protected int damage;
    protected boolean shouldSpawnProjectile;
    protected boolean projectileSpawnedInThisAttack;

    //--- ANIMATIONS ---
    protected Animation<TextureRegion> animationIntro;
    protected Animation<TextureRegion> animationIdle;
    protected Animation<TextureRegion> animationWalking;
    protected Animation<TextureRegion> animationAttack;
    protected Animation<TextureRegion> animationDie;
    protected Animation<TextureRegion> projectileAnimation;
    protected int attackSpawnFrame;


    public Enemy(int life, int givenScore) {
        this.life = life;
        this.currentLife = life;
        this.givenScore = givenScore;
        this.currentState = EnemyState.IDLE;
        this.random = new Random();
        this.isTakingDamage = false;
        this.projectileSpawnedInThisAttack = false;
        this.shouldSpawnProjectile = false;
        this.actionTimer = 60;
        this.stateTime = 0f;
    }

    //--- GETTERS ---
    public Rectangle getBounds() { return this.bounds; }

    public int getGivenScore() { return this.givenScore; }

    public int getCurrentLife() { return this.currentLife; }

    public int getLife() { return this.life; }

    public EnemyState getCurrentState() { return this.currentState; }

    public int makeDamage() { return this.damage; }

    public boolean shouldSpawnProjectile() { return this.shouldSpawnProjectile; }

    public boolean isIntroAnimationFinished() {
        if(animationIntro == null) return true;
        return animationIntro.isAnimationFinished(stateTime);
    }

    public boolean isDieAnimationFinished() {
        if(animationDie == null) return true;
        return animationDie.isAnimationFinished(stateTime);
    }

    //--- SETTERS ---
    public void takeDamage(int dmg) {
        this.currentLife -= dmg;
        isTakingDamage = true;
        damageColorTimer = DAMAGE_COLOR_DURATION;
    }

    public void update(float delta, Array<Platform> platforms, float worldWidth, float worldHeight){
        stateTime += delta;

        if (isTakingDamage) {
            damageColorTimer -= delta;
            if (damageColorTimer <= 0) {
                isTakingDamage = false;
            }
        }


        if (currentState == EnemyState.SPAWNING) {
            if (isIntroAnimationFinished()) {
                currentState = EnemyState.IDLE;
            }
            return;
        }


        if (currentState == EnemyState.DIE) {
            return;
        }


        if (this.currentLife <= 0) {
            transitionToState(EnemyState.DIE);
            return;
        }


        actionTimer--;
        if (actionTimer <= 0) {
            processMovement();
        }

        shouldSpawnProjectile = false;

        if (currentState == EnemyState.ATTACKING) {
            attacking();
        }
        else if(currentState == EnemyState.SPECIAL_ATTACK){
            specialAttack();
        }
        else if (speedX != 0) {
            transitionToState(EnemyState.WALKING);
        } else {
            transitionToState(EnemyState.IDLE);
        }

        speedY += gravity * delta;
        moveX(delta, platforms, worldWidth);
        moveY(delta, platforms, worldHeight);
    }

    public abstract void moveX(float delta, Array<Platform> platforms, float worldWidth);

    public abstract void moveY(float delta, Array<Platform> platforms, float worldHeight);

    public abstract void processMovement();

    public abstract void attack();

    protected abstract void attacking();

    protected abstract void specialAttack();

    public void transitionToState(EnemyState newState) {
        if(this.currentState != newState) {
            this.currentState = newState;
            this.stateTime = 0f;
        }
    }

    public abstract void draw(Batch batch);

}
