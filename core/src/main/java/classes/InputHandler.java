package classes;

import classes.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputHandler {

    private Player player;

    public InputHandler(Player player) {
        this.player = player;
    }

    public void update() {
        // Movement
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight();
        } else {
            player.stopMovingX();
        }
        // Jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.jump();
        }

        //Attack
        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            player.attack();
        }

    }

}
