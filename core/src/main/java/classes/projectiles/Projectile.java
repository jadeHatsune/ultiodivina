package classes.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public abstract class Projectile {

    //--- STATES ---
    protected ProjectileFacing currentFacing;
    protected ProjectileState currentState;

    //--- PHYSICS ---
    protected Rectangle bounds;
    protected float speedX;

    //--- ANIMATIONS ---
    protected Animation<TextureRegion> projectileAnimation;
    protected float stateTime;

    //--- PROPERTIES ---
    protected final int damage;

    public Projectile(int damage, float speedX) {
        this.damage = damage;
        this.speedX = speedX;
        this.currentState = ProjectileState.ACTIVE;
    }

    //--- Setters ---
    public void setCurrentState(ProjectileState newState) { this.currentState = newState; }

    //--- Getters ---
    public Rectangle getBounds() { return this.bounds; }

    public int makeDamage() { return this.damage; }

    public ProjectileState getCurrentState() { return this.currentState; }

    public void update(float delta) {
        moveX(delta);
        stateTime += delta;

        if(speedX > 0) {
            currentFacing = ProjectileFacing.FACING_RIGHT;
        } else {
            currentFacing = ProjectileFacing.FACING_LEFT;
        }
    }

    public void moveX(float delta){
        this.bounds.x += speedX * delta;
    }

    public void draw(Batch batch) {
        TextureRegion currentFrame = projectileAnimation.getKeyFrame(stateTime, true);
        if(currentFacing == ProjectileFacing.FACING_LEFT && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if(currentFacing == ProjectileFacing.FACING_RIGHT && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
