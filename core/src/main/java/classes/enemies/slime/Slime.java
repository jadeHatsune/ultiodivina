package classes.enemies.slime;

import classes.abstracts.Enemy;
import classes.player.PlayerFacing;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Slime extends Enemy {

    //--- Slime states ---
    private SlimeState currentState;
    private SlimeFacing currentFacing;

    //-- Animations --
    private final Animation<TextureRegion> animationWalking;
    private final TextureRegion animationIdle;
    private float stateTime;

    public Slime(int x, int y) {
        super(x, y);

        currentState = SlimeState.IDLE;
        stateTime = 0f;

        this.animationWalking = getAnimationSprite(1, 7, "slime.png");
        this.animationIdle = animationWalking.getKeyFrame(0);

        this.bounds = new Rectangle(x, y, animationIdle.getRegionWidth(), animationIdle.getRegionHeight());

    }

    @Override
    public void update() {

    }

    @Override
    public void moveX() {

    }

    @Override
    public void moveY() {

    }

    @Override
    public void processMovement(){

    }

    @Override
    public void draw(Batch batch) {
        Animation<TextureRegion> currentAnimation;
        TextureRegion currentFrame;
        switch (currentState){
            case WALKING:
                currentAnimation = animationWalking;
            case IDLE:
            default:
                currentAnimation = null;
        }

        if(currentAnimation != null){
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = animationIdle;
        }

        if(currentFacing == SlimeFacing.FACING_LEFT && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if(currentFacing == SlimeFacing.FACING_RIGHT && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
