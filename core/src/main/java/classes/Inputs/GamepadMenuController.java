package classes.Inputs;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import java.util.List;

public class GamepadMenuController {

    private final List<Button> buttons;
    private int selectedIndex;
    private boolean inputUpLastFrame;
    private boolean inputDownLastFrame;
    private boolean acceptButtonLastFrame;
    private boolean pauseButtonLastFrame;

    public GamepadMenuController(List<Button> buttons) {
        this.buttons = buttons;
        this.inputUpLastFrame = false;
        this.inputDownLastFrame = false;
        this.acceptButtonLastFrame = false;
        this.pauseButtonLastFrame = false;
        this.selectedIndex = 0;
    }

    public void update(){
        Controller controller = Controllers.getCurrent();
        if(controller == null || buttons.isEmpty()) return;

        float axisY = controller.getAxis(controller.getMapping().axisLeftY);
        float deadZone = 0.5f;

        boolean wantsToMoveUp = controller.getButton(controller.getMapping().buttonDpadUp) || axisY < -deadZone;
        boolean wantsToMoveDown = controller.getButton(controller.getMapping().buttonDpadDown) || axisY > deadZone;

        if (wantsToMoveDown && !inputDownLastFrame) {
            selectedIndex = (selectedIndex + 1) % buttons.size();
        } else if (wantsToMoveUp && !inputUpLastFrame) {
            selectedIndex--;
            if (selectedIndex < 0) {
                selectedIndex = buttons.size() - 1;
            }
        }

        inputUpLastFrame = wantsToMoveUp;
        inputDownLastFrame = wantsToMoveDown;

        boolean acceptButtonNow = controller.getButton(controller.getMapping().buttonA);
        if(acceptButtonNow && !acceptButtonLastFrame) {
            buttons.get(selectedIndex).fire(new ChangeEvent());
        }
        acceptButtonLastFrame = acceptButtonNow;

    }

    public void wantsToPause() {
        Controller controller = Controllers.getCurrent();
        if(controller == null) return;
        boolean pauseButtonNow = controller.getButton(controller.getMapping().buttonStart);
        if(pauseButtonNow && !pauseButtonLastFrame) {
            buttons.get(selectedIndex).fire(new ChangeEvent());
        }
        pauseButtonLastFrame = pauseButtonNow;
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

}
