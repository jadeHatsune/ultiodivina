package classes.enemies.bosses;

import classes.enemies.Enemy;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Boss extends Enemy {

    protected Animation<TextureRegion> animationHalfLife;

    public Boss(int life, int givenScore) {
        super(life, givenScore);
    }

}

