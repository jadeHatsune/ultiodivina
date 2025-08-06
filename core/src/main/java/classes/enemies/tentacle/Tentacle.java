package classes.enemies.tentacle;

import classes.enemies.Enemy;
import classes.platforms.Platform;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class Tentacle extends Enemy {
    private static final int LIFE = 10;
    private static final int GIVEN_SCORE = 30;

    public Tentacle() {
        super(LIFE, GIVEN_SCORE);

    }

    @Override
    public void moveX(float delta, Array<Platform> platforms, float worldWidth) {

    }

    @Override
    public void moveY(float delta, Array<Platform> platforms) {

    }

    @Override
    public void processMovement() {

    }

    @Override
    public void attack() {

    }

    @Override
    public void draw(Batch batch) {

    }
}
