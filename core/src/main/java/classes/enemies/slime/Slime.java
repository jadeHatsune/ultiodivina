package classes.enemies.slime;

import classes.enemies.Enemy;
import classes.enemies.EnemyFacing;
import classes.platforms.Platform;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

import static screens.BaseScreen.VIRTUAL_WIDTH;


public class Slime extends Enemy {

    //--- Constants ---
    private static final float SLIME_MOVE_SPEED = 100f;
    private static final float SLIME_GRAVITY = -800f;
    private static final int SLIME_DAMAGE = 1;
    private static final int SLIME_LIFE = 3;
    private static final int SLIME_SCORE = 10;

    //-- Animations --
    private final TextureRegion frameIdle;

    public Slime(int x, int y) {
        super(SLIME_LIFE, SLIME_SCORE);

        this.gravity = SLIME_GRAVITY;
        this.damage = SLIME_DAMAGE;

        this.animationWalking = getAnimationSprite(1, 7, "enemies/slime/slime.png");
        this.frameIdle = animationWalking.getKeyFrame(0);

        this.bounds = new Rectangle(x, y, frameIdle.getRegionWidth(), frameIdle.getRegionHeight());

    }

    @Override
    public void moveX(float delta, Array<Platform> platforms) {
        // --- LÓGICA DE SENSOR DE BORDES ---
        if (speedX != 0) {
            int sensorX = (int) (speedX > 0 ? bounds.x + bounds.width : bounds.x - 1);
            int sensorY = (int) (bounds.y - 1); // Un píxel por debajo de sus pies
            Point sensor = new Point(sensorX, sensorY);

            boolean groundAhead = false;
            for (Platform platform : platforms) {
                if (platform.getBounds().contains(sensor.x, sensor.y)) {
                    groundAhead = true;
                    break;
                }
            }
            // Si no hay suelo delante, invierte la dirección
            if (!groundAhead) {
                speedX *= -1;
                currentFacing = (currentFacing == EnemyFacing.FACING_LEFT) ? EnemyFacing.FACING_RIGHT : EnemyFacing.FACING_LEFT;
            }
        }

        bounds.x += speedX * delta;
        if(bounds.x < 0) {
            this.bounds.x = 0;
            this.speedX *= -1;
            this.currentFacing = EnemyFacing.FACING_RIGHT;
        }

        if (bounds.x + bounds.width > VIRTUAL_WIDTH) {
            this.bounds.x = VIRTUAL_WIDTH - bounds.width;
            this.speedX *= -1;
            this.currentFacing = EnemyFacing.FACING_LEFT;
        }
    }

    @Override
    public void moveY(float delta, Array<Platform> platforms) {
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
    public void processMovement(){
        int nextAction = random.nextInt(3);
        switch (nextAction) {
            case 0:
                this.speedX = 0;
                break;
            case 1:
                this.speedX = -SLIME_MOVE_SPEED;
                currentFacing = EnemyFacing.FACING_LEFT;
                break;
            case 2:
                this.speedX = SLIME_MOVE_SPEED;
                currentFacing = EnemyFacing.FACING_RIGHT;
                break;
        }
        this.actionTimer = random.nextInt(180) + 60;
    }

    @Override
    public void attack() { }

    @Override
    public void draw(Batch batch) {
        if(isTakingDamage) {
            batch.setColor(1, 0, 0, 1);
        }

        TextureRegion currentFrame;
        switch (currentState){
            case WALKING:
                currentFrame = animationWalking.getKeyFrame(stateTime, true);
                break;
            case IDLE:
            default:
                currentFrame = frameIdle;
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
