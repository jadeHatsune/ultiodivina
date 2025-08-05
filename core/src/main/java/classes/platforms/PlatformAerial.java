package classes.platforms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class PlatformAerial extends Platform {

    //--- CONSTANTS ---
    private static final int AERIAL_PLATFORM_DIF_Y = 31;

    public PlatformAerial(int x, int y, Texture sprite) {
        super();
        this.sprite = sprite;

        this.bounds = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight() - AERIAL_PLATFORM_DIF_Y);

    }

    @Override
    public void draw(Batch batch){
        batch.draw(sprite, bounds.x, bounds.y, sprite.getWidth(), sprite.getHeight());
    }
}
