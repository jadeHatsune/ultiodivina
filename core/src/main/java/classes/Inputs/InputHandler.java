package classes.Inputs;

import classes.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class InputHandler {

    private final Player player;
    private boolean jumpButtonPressedLastFrame = false;
    private boolean attackButtonPressedLastFrame = false;

    public InputHandler(Player player) {
        this.player = player;
    }

    public void update() {
        Controller controller = Controllers.getCurrent();
        boolean wantsToMoveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean wantsToMoveRight = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean wantsToJump = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        boolean wantsToAttack = Gdx.input.isKeyJustPressed(Input.Keys.Q);

        if (controller != null) {
            float axisX = controller.getAxis(controller.getMapping().axisLeftX);
            float deadZone = 0.5f;

            if (axisX < -deadZone) {
                wantsToMoveLeft = true;
            } else if (axisX > deadZone) {
                wantsToMoveRight = true;
            }

            if (controller.getButton(controller.getMapping().buttonDpadLeft)) {
                wantsToMoveLeft = true;
            } else if (controller.getButton(controller.getMapping().buttonDpadRight)) {
                wantsToMoveRight = true;
            }

            boolean jumpPressedNow = controller.getButton(controller.getMapping().buttonA);
            if (jumpPressedNow && !jumpButtonPressedLastFrame) {
                wantsToJump = true;
            }
            jumpButtonPressedLastFrame = jumpPressedNow;

            boolean attackPressedNow = controller.getButton(controller.getMapping().buttonX);
            if (attackPressedNow && !attackButtonPressedLastFrame) {
                wantsToAttack = true;
            }
            attackButtonPressedLastFrame = attackPressedNow;
        }

        if (wantsToMoveLeft) {
            player.moveLeft();
        } else if (wantsToMoveRight) {
            player.moveRight();
        } else {
            player.stopMovingX();
        }

        // Salto
        if (wantsToJump) {
            player.jump();
        }

        // Ataque
        if (wantsToAttack) {
            player.attack();
        }

    }

}
