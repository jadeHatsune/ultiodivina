package classes.enemies.slime;

import classes.enemies.Enemy;
import classes.enemies.EnemyFacing;
import classes.enemies.EnemyState;
import classes.platforms.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.Random;


public class Slime extends Enemy {

    //--- Constants ---
    private static final float MOVE_SPEED = 100f;
    private static final float GRAVITY = -800f;
    private static final int DAMAGE = 1;

    //--- Slime states ---
    private int actionTimer;
    private final Random random;

    //-- Animations --
    private final Animation<TextureRegion> animationWalking;
    private final TextureRegion animationIdle;
    private float stateTime;

    public Slime(int x, int y) {
        super(3, 10);

        stateTime = 0f;
        actionTimer = 60;
        random = new Random();
        isTakingDamage = false;
        this.damage = DAMAGE;

        this.animationWalking = getAnimationSprite(1, 7, "enemies/slime/slime.png");
        this.animationIdle = animationWalking.getKeyFrame(0);

        this.bounds = new Rectangle(x, y, animationIdle.getRegionWidth(), animationIdle.getRegionHeight());

    }

    @Override
    public void update(float delta, Array<Platform> platforms) {
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

        if(speedX != 0) {
            transitionToState(EnemyState.WALKING);
        } else {
            transitionToState(EnemyState.IDLE);
        }

        speedY += GRAVITY * delta;
        moveX(delta, platforms);
        moveY(delta, platforms);

        stateTime += delta;

        if(life <= 0) {
            transitionToState(EnemyState.DIE);
        }

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
        }

        if (bounds.x + bounds.width > Gdx.graphics.getWidth()) {
            this.bounds.x = Gdx.graphics.getWidth() - bounds.width;
            this.speedX *= -1;
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
                this.speedX = -MOVE_SPEED;
                currentFacing = EnemyFacing.FACING_LEFT;
                break;
            case 2:
                this.speedX = MOVE_SPEED;
                currentFacing = EnemyFacing.FACING_RIGHT;
                break;
        }
        this.actionTimer = random.nextInt(180) + 60;
    }

    public void transitionToState(EnemyState newState) {
        if(this.currentState != newState) {
            this.currentState = newState;
            this.stateTime = 0f;
        }
    }


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
                currentFrame = animationIdle;
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
