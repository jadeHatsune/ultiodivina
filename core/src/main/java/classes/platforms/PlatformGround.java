package classes.platforms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class PlatformGround extends Platform {

    //--- CONSTANTS ---
    private static final int GROUND_PLATFORM_DIF_Y = 15;

    private final Texture sprite;

    public PlatformGround(int x, int y) {
        super();
        this.sprite = new Texture(Gdx.files.internal("platforms/plataformaTierra.png"));
        this.bounds = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight() - GROUND_PLATFORM_DIF_Y);
    }

    @Override
    public void draw(Batch batch){
        batch.draw(sprite, bounds.x, bounds.y, sprite.getWidth(), sprite.getHeight());
    }

}
