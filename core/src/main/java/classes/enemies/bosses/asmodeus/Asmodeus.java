package classes.enemies.bosses.asmodeus;

import classes.enemies.EnemyFacing;
import classes.enemies.EnemyState;
import classes.enemies.bosses.Boss;
import classes.platforms.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Asmodeus extends Boss {

    private static final int GRAVITY = 0;
    private static final int LIFE = 20;
    private static final int GIVEN_SCORE = 100;
    private static final int DAMAGE = 1;

    private final Animation<TextureRegion> animationTpOut;
    private final Animation<TextureRegion> animationTpIn;
    private boolean teleportedThisAttack;

    private float worldWidth;
    private float worldHeight;
    private float tpTime;

    public Asmodeus(int x, int y,
                    Animation<TextureRegion> animationIntro,
                    Animation<TextureRegion> animationIdle,
                    Animation<TextureRegion> animationAttack,
                    Animation<TextureRegion> animationHalfLife,
                    Animation<TextureRegion> animationTpOut,
                    Animation<TextureRegion> animationTpIn,
                    Animation<TextureRegion> animationDie) {

        super(LIFE, GIVEN_SCORE);

        this.animationIntro = animationIntro;
        this.animationIdle = animationIdle;
        this.animationAttack = animationAttack;
        this.animationHalfLife = animationHalfLife;
        this.animationTpOut = animationTpOut;
        this.animationTpIn = animationTpIn;
        this.animationDie = animationDie;

        this.damage = DAMAGE;
        this.gravity = GRAVITY;
        transitionToState(EnemyState.SPAWNING);
        this.currentFacing = EnemyFacing.FACING_RIGHT;
        this.attackSpawnFrame = 25;
        tpTime = 0f;
        teleportedThisAttack = false;

        TextureRegion firstFrame = animationIdle.getKeyFrame(0);
        this.bounds = new Rectangle(x, y, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());

    }

    @Override
    public void moveX(float delta, Array<Platform> platforms, float worldWidth) {
        this.worldWidth = worldWidth;
    }

    @Override
    public void moveY(float delta, Array<Platform> platforms, float worldHeight) {
        this.worldHeight = worldHeight;
    }

    @Override
    public void processMovement() {
        int nextAction = random.nextInt(3);
        switch (nextAction) {
            case 0:
                this.currentFacing = EnemyFacing.FACING_LEFT;
                break;
            case 1:
                this.currentFacing = EnemyFacing.FACING_RIGHT;
                break;
            case 2:
                attack();
                break;
        }
        this.actionTimer = random.nextInt(180) + 60;
    }

    @Override
    public void attack() {
        int nextAction = random.nextInt(2);
        switch (nextAction){
            case 0:
                if(currentState != EnemyState.ATTACKING) {
                    transitionToState(EnemyState.ATTACKING);
                    projectileSpawnedInThisAttack = false;
                }
                break;
            case 1:
                if(currentState != EnemyState.SPECIAL_ATTACK) {
                    transitionToState(EnemyState.SPECIAL_ATTACK);
                    tpTime = 0f;
                }
                break;
        }
    }

    @Override
    protected void attacking() {
        if(animationAttack.isAnimationFinished(stateTime)) {
            transitionToState(EnemyState.IDLE);
        } else {
            if(!projectileSpawnedInThisAttack && animationAttack.getKeyFrameIndex(stateTime) >= attackSpawnFrame) {
                shouldSpawnProjectile = true;
                projectileSpawnedInThisAttack = true;
            }
        }
    }

    @Override
    public void specialAttack() {
        if(animationTpOut.isAnimationFinished(stateTime) && !teleportedThisAttack) {
            bounds.x = random.nextFloat(worldWidth - bounds.width);
            bounds.y = random.nextFloat(worldHeight - bounds.height);

            bounds.x = bounds.x + 64;
            bounds.y = bounds.y - 10;
            teleportedThisAttack = true;
        }

        if(animationTpIn.isAnimationFinished(tpTime)) {
            transitionToState(EnemyState.IDLE);
            teleportedThisAttack = false;
        }

    }

    @Override
    public void update(float delta, Array<Platform> platforms, float worldWidth, float worldHeight){
        super.update(delta, platforms, worldWidth, worldHeight);
        if(currentLife <= life * 0.5){
            animationIdle = animationHalfLife;
        }
    }

    @Override
    public void draw(Batch batch) {
        if(isTakingDamage) {
            batch.setColor(1, 0, 0, 1);
        }

        TextureRegion currentFrame;

        switch (currentState){
            case SPAWNING:
                currentFrame = animationIntro.getKeyFrame(stateTime, false);
                break;
            case DIE:
                currentFrame = animationDie.getKeyFrame(stateTime, false);
                break;
            case ATTACKING:
                currentFrame = animationAttack.getKeyFrame(stateTime, false);
                break;
            case SPECIAL_ATTACK:
                if (!animationTpOut.isAnimationFinished(stateTime)) {
                    currentFrame = animationTpOut.getKeyFrame(stateTime, false);
                } else {
                    tpTime += Gdx.graphics.getDeltaTime();
                    currentFrame = animationTpIn.getKeyFrame(tpTime, false);
                }
                break;
            case IDLE:
            default:
                currentFrame = animationIdle.getKeyFrame(stateTime, true);
                break;
        }

        if(currentFacing == EnemyFacing.FACING_LEFT && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if(currentFacing == EnemyFacing.FACING_RIGHT && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, bounds.x, bounds.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

        batch.setColor(1, 1, 1, 1);
    }
}
