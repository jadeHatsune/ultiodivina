package classes.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Projectile {

    //--- Projectile States ---
    private ProjectileFacing currentFacing;
    private ProjectileState currentState;

    //--- Physics ---
    private Rectangle bounds;
    private float speedX;

    //--- Animations ---
    private final Animation<TextureRegion> projectileAnimation;
    private float stateTime;


    public Projectile(int x, int y, float speedX, String spritePath) {
        this.speedX = speedX;
        this.currentState = ProjectileState.ACTIVE;
        this.projectileAnimation = getAnimationSprite(1, 6, spritePath);
        TextureRegion firstFrame = projectileAnimation.getKeyFrame(0);
        this.bounds = new Rectangle(x, y, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());
    }

    //--- Setters ---
    public void setCurrentState(ProjectileState newState) { this.currentState = newState; }

    //--- Getters ---
    public Rectangle getBounds() { return this.bounds; }

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

    public Animation<TextureRegion> getAnimationSprite(int frameCols, int frameRows, String spriteSheetPath) {
        Texture spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));

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
