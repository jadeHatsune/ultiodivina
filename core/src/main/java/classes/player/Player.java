package classes.player;

import classes.platforms.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player {

    //--- Constants ---
    private static final float MOVE_SPEED = 200f;
    private static final float JUMP_VELOCITY = 400f;
    private static final float GRAVITY = -800f;

    //--- Player states ---
    private PlayerState currentState;
    private boolean isOnGround;
    private boolean isMoving;

    //--- Physics ---
    private final Rectangle bounds;
    private float speedX;
    private float speedY;

    //--- Animations ---
    private final Animation<TextureRegion> animationIdle;
    private final Animation<TextureRegion> animationWalking;
    private float stateTime;

    //-- Properties --
    private final String name;
    private int life;
    private int damage;

    private int shootCooldown;
    private int invincibilityTimer;
    private final int INVINCIBILITY_DURATION = 120;

    public Player(int x, int y) {
        this.name = "Lymhiel";
        this.life = 3;
        this.damage = 1;
        this.currentState = PlayerState.FACING_RIGHT;
        this.shootCooldown = 0;
        this.invincibilityTimer = 0;
        this.isMoving = false;
        this.stateTime = 0f;

        animationIdle = getAnimationSprite(1, 7, "lymhiel_idle.png");
        animationWalking = getAnimationSprite(1, 6, "lymhiel_walking.png");

        TextureRegion firstFrame = animationIdle.getKeyFrame(0);
        this.bounds = new Rectangle(x, y, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());

    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void update(float delta){
        processInput();

        speedY += GRAVITY * delta;

        bounds.x += speedX * delta;
        bounds.y += speedY * delta;

        stateTime += delta;
        boolean wasMoving = this.isMoving;
        isMoving = (speedX != 0);
        if(wasMoving != isMoving) {
            stateTime = 0f;
        }

    }

    public void processInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            speedX = -MOVE_SPEED;
            currentState = PlayerState.FACING_LEFT;
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            speedX = MOVE_SPEED;
            currentState = PlayerState.FACING_RIGHT;
        } else {
            speedX = 0;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isOnGround){
            speedY = JUMP_VELOCITY;
        }

    }

    public void applyPhysics(Platform groundPlatform, Array<Platform> platformsAereo) {

        this.isOnGround = false;

        if(this.getBounds().overlaps(groundPlatform.getBounds())) {
            handlePlatformCollision(groundPlatform.getBounds());
        }

    }

    public void handlePlatformCollision(Rectangle platformBounds) {

    }

    public void draw(Batch batch){
        Animation<TextureRegion> currentAnimation = isMoving ? animationWalking : animationIdle;
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        if(currentState == PlayerState.FACING_LEFT && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if(currentState == PlayerState.FACING_RIGHT && currentFrame.isFlipX()) {
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
