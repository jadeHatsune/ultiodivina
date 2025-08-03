package classes.platforms;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.hod.ultiodivina.Main;


public abstract class Platform {
    protected Rectangle bounds;
    protected AssetManager assetManager;

    public Platform() {
        this.assetManager = Main.assetManager;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public abstract void draw(Batch batch);

}
