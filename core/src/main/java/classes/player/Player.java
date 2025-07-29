package classes.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    public enum State {
        FACING_RIGHT,
        FACING_LEFT
    }

    private String name;
    private int life;
    private int damage;
    private int x;
    private int y;
    private Rectangle bounds;

    private boolean isOnGround;
    private boolean isMoving;
    private State currentState;
    private int shootCooldown;
    private int invincibilityTimer;
    private final int INVINCIBILITY_DURATION = 120;

    private Animation<TextureRegion> animationIdle;
    private Animation<TextureRegion> animationWalking;

    private float stateTime;

    public Player(int x, int y) {
        this.name = "Lymhiel";
        this.life = 3;
        this.damage = 1;
        this.x = x;
        this.y = y;
        this.currentState = State.FACING_RIGHT;
        this.shootCooldown = 0;
        this.invincibilityTimer = 0;
        this.isMoving = false;
        this.stateTime = 0f;

        animationIdle = getAnimationSprite(1, 7, "lymhiel_idle.png");
        animationWalking = getAnimationSprite(1, 6, "lymhiel_walking.png");

        TextureRegion firstFrame = animationIdle.getKeyFrame(0);
        this.bounds = new Rectangle(x, y, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());

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

    public void update(float delta){
        stateTime += delta;
        float speed = 200;

        boolean wasMoving = this.isMoving;

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            bounds.x -= speed * delta;
            currentState = State.FACING_LEFT;
            isMoving = true;
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            bounds.x += speed * delta;
            currentState = State.FACING_RIGHT;
            isMoving = true;
        } else {
            isMoving = false;
        }

        if(wasMoving != this.isMoving) {
            stateTime = 0f;
        }
    }

    public void draw(Batch batch){
        Animation<TextureRegion> currentAnimation = isMoving ? animationWalking : animationIdle;
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        if(currentState == State.FACING_LEFT && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if(currentState == State.FACING_RIGHT && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
