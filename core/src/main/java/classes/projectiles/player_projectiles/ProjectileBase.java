package classes.projectiles.player_projectiles;

import classes.projectiles.Projectile;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ProjectileBase extends Projectile {

    public ProjectileBase(int x, int y, float speed) {
        super(1, speed);
        this.projectileAnimation = getAnimationSprite(1, 6, "lymhiel/lymhiel_projectile.png");
        TextureRegion firstFrame = projectileAnimation.getKeyFrame(0);
        this.bounds = new Rectangle(x, y, firstFrame.getRegionWidth(), firstFrame.getRegionHeight());
    }

}
