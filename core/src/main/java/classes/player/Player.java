package classes.player;

import classes.platforms.Platform;
import classes.projectiles.Projectile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player {

    //--- Constants ---
    private static final float MOVE_SPEED = 200f;
    private static final float JUMP_VELOCITY = 500f;
    private static final float GRAVITY = -800f;

    //--- Sounds ---
    private Sound playerDamageSound;
    private Sound jumpSound;

    //--- Player states ---
    private PlayerState currentState;
    private PlayerFacing currentFacing;
    private boolean isOnGround;

    //--- Physics ---
    private final Rectangle bounds;
    private float speedX;
    private float speedY;

    //--- Animations ---
    private final Animation<TextureRegion> animationIdle;
    private final Animation<TextureRegion> animationWalking;
    private final Animation<TextureRegion> animationJumping;
    private final Animation<TextureRegion> animationDie;
    private final Animation<TextureRegion> animationAttack;
    private float stateTime;

    //-- Properties --
    private final String name;
    private int life;
    private int damage;
    private int score;
    private boolean projectileSpawnedInThisAttack;
    private boolean shouldSpawnProjectile;

    private int attackCooldown;
    private int invincibilityTimer;
    private final int INVINCIBILITY_DURATION = 120;

    public Player(int x, int y) {
        this.name = "Lymhiel";
        this.life = 3;
        this.damage = 1;
        this.currentState = PlayerState.IDLE;
        this.currentFacing = PlayerFacing.FACING_RIGHT;
        this.attackCooldown = 0;
        this.invincibilityTimer = 0;
        this.isOnGround = true;
        this.stateTime = 0f;

        playerDamageSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/efectoDamagePlayer.ogg"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/efectoSaltar.ogg"));

        this.projectileSpawnedInThisAttack = false;
        this.shouldSpawnProjectile = false;

        animationIdle = getAnimationSprite(1, 7, "lymhiel/lymhiel_idle.png");
        animationWalking = getAnimationSprite(1, 6, "lymhiel/lymhiel_walking.png");
        animationJumping = getAnimationSprite(1, 6, "lymhiel/lymhiel_jumping.png");
        animationDie = getAnimationSprite(1, 10, "lymhiel/lymhiel_die.png");
        animationAttack = getAnimationSprite(1, 6, "lymhiel/lymhiel_attack.png");

        TextureRegion firstFrame = animationIdle.getKeyFrame(0);
        this.bounds = new Rectangle(x, y, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());

    }

    //--- Setters ---
    public void setScore(int score) {
        this.score += score;
    }
    public void restartScore(){ this.score = 0; }
    public void restartLife(){ this.life = 3; }
    public void setPlayerState(PlayerState newState){ this.currentState = newState; }

    //--- Getters ---
    public Rectangle getBounds() { return this.bounds; }

    public int getLife() { return this.life; }

    public boolean isDeathAnimationFinished() {
        return currentState == PlayerState.DIE && animationDie.isAnimationFinished(stateTime);
    }

    public boolean shouldSpawnProjectile() {
        return this.shouldSpawnProjectile;
    }

    public PlayerFacing getCurrentFacing() {
        return this.currentFacing;
    }

    public int getScore() { return this.score; }

    public void takeDamage(int dmg){
        if (invincibilityTimer > 0) {
            return;
        }
        playerDamageSound.play(0.5f);
        this.life -= dmg;
        this.invincibilityTimer = INVINCIBILITY_DURATION;
    }

    public void update(float delta, Array<Platform> platforms) {
        if(currentState == PlayerState.DIE) {
            stateTime += delta;
            return;
        }

        if(invincibilityTimer > 0) {
            invincibilityTimer--;
        }

        if(currentState == PlayerState.ATTACKING){
            stopMovingX();
        }

        shouldSpawnProjectile = false;

        speedY += GRAVITY * delta;

        moveX(delta, platforms);
        moveY(delta, platforms);

        stateTime += delta;

        if(life > 0) {
            if(currentState == PlayerState.ATTACKING) {
                if(animationAttack.isAnimationFinished(stateTime)) {
                    transitionToState(PlayerState.IDLE);
                } else {
                    int spawnFrame = 5;
                    if(!projectileSpawnedInThisAttack && animationAttack.getKeyFrameIndex(stateTime) >= spawnFrame) {
                        shouldSpawnProjectile = true;
                        projectileSpawnedInThisAttack = true;
                    }
                }
            } else if (!isOnGround) {
                if(speedY > 0) {
                    transitionToState(PlayerState.JUMPING);
                }else{
                    transitionToState(PlayerState.FALLING);
                }
            } else if (speedX != 0) {
                transitionToState(PlayerState.WALKING);
            } else {
                transitionToState(PlayerState.IDLE);
            }
        } else {
            transitionToState(PlayerState.DIE);
        }

    }

    public void moveX(float delta, Array<Platform> platforms) {
        if (currentState == PlayerState.DIE) {
            speedX = 0;
            return;
        }
        bounds.x += speedX * delta;

        for(Platform platform : platforms) {
            if(bounds.overlaps(platform.getBounds())) {
                if(speedX > 0) {
                    bounds.x = platform.getBounds().x - bounds.width;
                } else if(speedX < 0) {
                    bounds.x = platform.getBounds().x + platform.getBounds().width;
                }
                speedX = 0;
                break;
            }
        }

        if(bounds.x < 0) {
            this.bounds.x = 0;
            this.speedX *= -1;
        }

        if (bounds.x + bounds.width > Gdx.graphics.getWidth()) {
            this.bounds.x = Gdx.graphics.getWidth() - bounds.width;
            this.speedX *= -1;
        }
    }

    public void moveY(float delta, Array<Platform> platforms) {
        bounds.y += speedY * delta;

        isOnGround = false;

        for(Platform platform : platforms) {
            if (bounds.overlaps(platform.getBounds())) {
                if (speedY < 0) {
                    bounds.y = platform.getBounds().y + platform.getBounds().height;
                    speedY = 0;
                    isOnGround = true;
                } else if(speedY > 0) {
                    bounds.y = platform.getBounds().y - bounds.height;
                    speedY = 0;
                }

                break;
            }
        }
    }

    public void moveLeft() {
        speedX = -MOVE_SPEED;
        currentFacing = PlayerFacing.FACING_LEFT;
    }

    public void moveRight() {
        speedX = MOVE_SPEED;
        currentFacing = PlayerFacing.FACING_RIGHT;
    }

    public void stopMovingX() {
        speedX = 0;
    }

    public void jump() {
        if (isOnGround) {
            jumpSound.play(0.5f);
            speedY = JUMP_VELOCITY;
            isOnGround = false;
        }
    }

    public void attack() {
        if(currentState != PlayerState.ATTACKING) {
            transitionToState(PlayerState.ATTACKING);
            projectileSpawnedInThisAttack = false;
        }
    }

    public void transitionToState(PlayerState newState) {
        if(this.currentState != newState) {
            this.currentState = newState;
            this.stateTime = 0f;
        }
    }

    public void draw(Batch batch){
        Animation<TextureRegion> currentAnimation = chooseAnimation();

        boolean isLooping = (currentState != PlayerState.ATTACKING) && (currentState != PlayerState.DIE);

        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, isLooping);

        if(currentFacing == PlayerFacing.FACING_LEFT && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if(currentFacing == PlayerFacing.FACING_RIGHT && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        boolean shouldDraw = true;

        if (invincibilityTimer > 0 && currentState != PlayerState.DIE) {
            if(invincibilityTimer % 8 < 4) {
                shouldDraw = false;
            }
        }
        if(shouldDraw) {
            batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        }

    }

    public Animation<TextureRegion> chooseAnimation() {
        switch (currentState) {
            case WALKING:
                return animationWalking;
            case JUMPING:
            case FALLING:
                return animationJumping;
            case ATTACKING:
                return animationAttack;
            case DIE:
                return animationDie;
            case IDLE:
            default:
                return animationIdle;
        }
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
