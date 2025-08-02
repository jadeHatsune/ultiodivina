package classes.projectiles.enemies_projectiles;

import classes.projectiles.Projectile;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ProjectileFlyingMouth extends Projectile {

    public ProjectileFlyingMouth(int x, int y, float speed){
        super(1, speed);
        this.projectileAnimation = getAnimationSprite(1, 4, "enemies/flyingmouth/Ojo_Proyectil-Sheet.png");
        TextureRegion firstFrame = projectileAnimation.getKeyFrame(0);
        this.bounds = new Rectangle(x, y, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());
    }

}
