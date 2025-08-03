package classes.enemies.flyingmouth;

import classes.enemies.Enemy;
import classes.enemies.EnemyFacing;
import classes.enemies.EnemyState;
import classes.platforms.Platform;
import classes.projectiles.enemies_projectiles.ProjectileFlyingMouth;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.hod.ultiodivina.Main;

public class FlyingMouth extends Enemy {

    //--- CONSTANTS ---
    private static final int FLYING_MOUTH_LIFE = 5;
    private static final int FLYING_MOUTH_SCORE = 20;
    private static final int FLYING_MOUTH_DAMAGE = 1;
    private static final float FLYING_MOUTH_SPEED = 100f;
    private static final float FLYING_MOUTH_GRAVITY = 0;

    public FlyingMouth(int x, int y, EnemyFacing startFacing) {
        super(FLYING_MOUTH_LIFE, FLYING_MOUTH_SCORE);

        this.gravity = FLYING_MOUTH_GRAVITY;
        this.damage = FLYING_MOUTH_DAMAGE;
        this.currentFacing = startFacing;

        this.animationWalking = getAnimationSprite(1, 4, assetManager.get("enemies/flyingmouth/bocaConOjo_Movimiento-Sheet.png"));
        this.animationAttack = getAnimationSprite(1, 12, assetManager.get("enemies/flyingmouth/bocaConOjo_Expulsion-Sheet.png"));
        this.attackSpawnFrame = 7;

        TextureRegion firstFrame = animationWalking.getKeyFrame(0);

        this.bounds = new Rectangle(x, y, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());
    }

    @Override
    public void moveX(float delta, Array<Platform> platforms, float worldWidth) {
        bounds.x += speedX * delta;
        if(bounds.x < 0) {
            this.bounds.x = 0;
            this.speedX *= -1;
            this.currentFacing = EnemyFacing.FACING_RIGHT;
        }

        if (bounds.x + bounds.width > worldWidth) {
            this.bounds.x = worldWidth - bounds.width;
            this.speedX *= -1;
            this.currentFacing = EnemyFacing.FACING_LEFT;
        }
    }

    @Override
    public void moveY(float delta, Array<Platform> platforms) { }

    @Override
    public void processMovement() {
        int nextAction = random.nextInt(4);
        switch (nextAction) {
            case 0:
                this.speedX = 0;
                break;
            case 1:
                this.speedX = -FLYING_MOUTH_SPEED;
                this.currentFacing = EnemyFacing.FACING_LEFT;
                break;
            case 2:
                this.speedX = FLYING_MOUTH_SPEED;
                this.currentFacing = EnemyFacing.FACING_RIGHT;
                break;
            case 3:
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
            projectileSpawnedInThisAttack = false;
        }
    }

    public ProjectileFlyingMouth setProjectile() {
        float projectileSpeed = 500f;
        float startX = this.currentFacing == EnemyFacing.FACING_RIGHT ? this.bounds.x + this.bounds.width : this.bounds.x;
        float startY = this.bounds.y + (this.bounds.height / 3);

        if(this.currentFacing == EnemyFacing.FACING_LEFT) {
            projectileSpeed = -500f;
        }
        return new ProjectileFlyingMouth((int) startX, (int) startY, projectileSpeed);
    }

    @Override
    public void draw(Batch batch) {
        if(isTakingDamage) {
            batch.setColor(1, 0, 0, 1);
        }

        TextureRegion currentFrame;
        switch (currentState){
            case ATTACKING:
                currentFrame = animationAttack.getKeyFrame(stateTime, false);
                break;
            case IDLE:
            case WALKING:
            default:
                currentFrame = animationWalking.getKeyFrame(stateTime, true);
                break;
        }

        if(currentFacing == EnemyFacing.FACING_LEFT && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if(currentFacing == EnemyFacing.FACING_RIGHT && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(1, 1, 1, 1);
    }
}

