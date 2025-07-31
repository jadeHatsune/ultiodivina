package classes.abstracts;

import classes.platforms.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public abstract class Enemy {

    //-- Physics --
    protected Rectangle bounds;
    protected float speedX;
    protected float speedY;

    //--- Properties ---
    protected int life;


    public Enemy(int x, int y, int life) {
        this.life = life;
    }

    //--- Getters ---
    public Rectangle getBounds() { return this.bounds; }

    public abstract void update(float delta, Array<Platform> platforms);

    public abstract void moveX(float delta, Array<Platform> platforms);

    public abstract void moveY(float delta, Array<Platform> platforms);

    public abstract void processMovement();

    public abstract void draw(Batch batch);

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
