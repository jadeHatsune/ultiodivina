package classes.platforms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class PlatformAerial extends Platform{

    //--- CONSTANTS ---
    private static final int AERIAL_PLATFORM_DIF_Y = 31;

    private final Texture sprite;

    public PlatformAerial(int x, int y, String type) {
        super();
        switch(type){
            case "left":
                this.sprite = assetManager.get("platforms/plataformaAereoIzquierda.png");
                break;
            case "large":
                this.sprite = assetManager.get("platforms/plataformaAereoLarga.png");
                break;
            case "right":
            default:
                this.sprite = assetManager.get("platforms/plataformaAereoDerecha.png");
        }

        this.bounds = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight() - AERIAL_PLATFORM_DIF_Y);

    }

    @Override
    public void draw(Batch batch){
        batch.draw(sprite, bounds.x, bounds.y, sprite.getWidth(), sprite.getHeight());
    }
}
