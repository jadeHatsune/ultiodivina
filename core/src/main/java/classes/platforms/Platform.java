package classes.platforms;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;


public abstract class Platform {
    protected Rectangle bounds;

    public Platform() { }

    public Rectangle getBounds() {
        return bounds;
    }

    public abstract void draw(Batch batch);

}
