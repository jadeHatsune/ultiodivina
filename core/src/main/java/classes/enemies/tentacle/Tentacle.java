package classes.enemies.tentacle;

import classes.enemies.Enemy;
import classes.enemies.EnemyFacing;
import classes.enemies.EnemyState;
import classes.platforms.Platform;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Tentacle extends Enemy {
    private static final int LIFE = 3;
    private static final int GIVEN_SCORE = 30;
    private static final float GRAVITY = -800f;

    private final Rectangle attackBounds;
    private boolean isAttackActive;

    public Tentacle(int x, int y,
                    EnemyFacing facing,
                    Animation<TextureRegion> animationSpawn,
                    Animation<TextureRegion> animationIdle,
                    Animation<TextureRegion> animationAttack) {
        super(LIFE, GIVEN_SCORE);

        this.animationIntro = animationSpawn;
        this.animationIdle = animationIdle;
        this.animationAttack = animationAttack;
        this.currentState = EnemyState.SPAWNING;
        this.currentFacing = facing;

        this.gravity = GRAVITY;

        TextureRegion firstFrame = animationIdle.getKeyFrame(0);

        this.bounds = new Rectangle(x, y, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());
        this.attackBounds = new Rectangle();
        this.isAttackActive = false;

    }

    public Rectangle getAttackBounds() {
        return this.attackBounds;
    }

    public boolean isAttackActive() {
        return this.isAttackActive;
    }

    @Override
    public void moveX(float delta, Array<Platform> platforms, float worldWidth) { }

    @Override
    public void moveY(float delta, Array<Platform> platforms, float worldHeight) {
        bounds.y += speedY * delta;
        for(Platform platform : platforms) {
            if (bounds.overlaps(platform.getBounds())) {
                if (speedY < 0) {
                    bounds.y = platform.getBounds().y + platform.getBounds().height;
                    speedY = 0;
                } else if(speedY > 0) {
                    bounds.y = platform.getBounds().y - bounds.height;
                    speedY = 0;
                }

                break;
            }
        }
    }

    @Override
    public void processMovement() {
        int nextAction = random.nextInt(2);
        switch (nextAction) {
            case 0:
                this.speedX = 0;
                break;
            case 1:
                this.speedX = 0;
                attack();
                break;
        }
        this.actionTimer = random.nextInt(180) + 60;
    }

    @Override
    public void attack() {
        if(currentState != EnemyState.ATTACKING) {
            transitionToState(EnemyState.ATTACKING);
        }
    }

    @Override
    protected void attacking() {
        if(animationAttack.isAnimationFinished(stateTime)) {
            transitionToState(EnemyState.IDLE);
        }
    }

    @Override
    protected void specialAttack() { }

    @Override
    public void update(float delta, Array<Platform> platforms, float worldWidth, float worldHeight) {
        super.update(delta, platforms, worldWidth, worldHeight);

        if (currentState == EnemyState.ATTACKING) {
            int currentFrameIndex = animationAttack.getKeyFrameIndex(stateTime);

            int ATTACK_SPAWN_FRAME = 14;
            if (currentFrameIndex >= ATTACK_SPAWN_FRAME) {
                isAttackActive = true;
                float attackWidth = 93;
                float attackHeight = 60;
                attackBounds.set(bounds.x, bounds.y + bounds.height, attackWidth, attackHeight);

            } else {
                isAttackActive = false;
            }

            if (animationAttack.isAnimationFinished(stateTime)) {
                transitionToState(EnemyState.IDLE);
            }
        } else {
            isAttackActive = false;
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
            case ATTACKING:
                currentFrame = animationAttack.getKeyFrame(stateTime, false);
                break;
            case IDLE:
            default:
                currentFrame = animationIdle.getKeyFrame(stateTime, true);
                break;
        }

        if(currentFacing == EnemyFacing.FACING_RIGHT && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if(currentFacing == EnemyFacing.FACING_LEFT && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, bounds.x, bounds.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

        batch.setColor(1, 1, 1, 1);
    }
}
