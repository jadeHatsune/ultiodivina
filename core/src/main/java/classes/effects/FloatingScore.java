package classes.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;

public class FloatingScore {
    private final String text;
    private float x, y;
    private float timer;
    private final float lifetime;
    private final Color color;

    public FloatingScore(String text, float startX, float startY) {
        this.text = text;
        this.x = startX;
        this.y = startY;
        this.lifetime = 1.0f;
        this.timer = this.lifetime;
        this.color = new Color(Color.WHITE);
    }

    public void update(float delta) {
        if(timer > 0) {
            timer -= delta;
            y += 1 + delta;
            color.a = Interpolation.fade.apply(timer/lifetime);
        }
    }

    public void draw(Batch batch, BitmapFont font) {
        font.setColor(this.color);
        font.draw(batch, text, x, y);
        font.setColor(Color.WHITE);
    }

    public boolean isFinished() {
        return timer <= 0;
    }
}
