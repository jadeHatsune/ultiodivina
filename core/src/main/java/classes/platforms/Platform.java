package classes.platforms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;


public class Platform {

   private Texture sprite;
   private Rectangle bounds;

   private int width;
   private int height;

    public Platform(int x, int y, int width, int height, int difY, String spritePath) {
        this.sprite = new Texture(Gdx.files.internal(spritePath));

        if(width == 0){
            this.width = sprite.getWidth();
        }else {
            this.width = width;
        }

        if(height == 0){
            this.height = sprite.getHeight();
        } else {
            this.height = height;
        }

        this.bounds = new Rectangle(x, y, this.width, this.height-difY);

    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(Batch batch){
        batch.draw(sprite, bounds.x, bounds.y, this.width, this.height);
    }

}
