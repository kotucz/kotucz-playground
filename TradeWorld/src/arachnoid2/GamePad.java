package arachnoid2;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;

/**
 *
 * @author Kotuc
 */
public class GamePad {

    private Controller controller;

    private GamePad(Controller contrl) {
//        super("GamePad");
        this.controller = contrl;
    }

    public Controller getController() {
        return controller;
    }

    public static GamePad getActiveGamePad() {
        Controller gamepad = null;
        Event event = new Event();
        System.err.println("PRESS ANY KEY ON CONTROLLER");
        while (null == gamepad) {
            for (Controller controller : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
                System.out.println("controler: "+controller);
                controller.poll();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GamePad.class.getName()).log(Level.SEVERE, null, ex);
                }
                controller.poll();
                if (controller.getEventQueue().getNextEvent(event)) {
                    gamepad = controller;
                }
            }

        }
//        return gamepad;
        return new GamePad(gamepad);
    }

    public static GamePad getGamePad() {
        Controller[] conts = ControllerEnvironment.getDefaultEnvironment().getControllers();
        Controller gamepad = null;

        for (Controller controller : conts) {
//            System.out.println("" + controller.getName() + ":  " + controller.getType());
            if (Controller.Type.GAMEPAD.equals(controller.getType())) {
                gamepad = controller;
            }
//            gamepad = controller;
            Component[] comps = controller.getComponents();
            for (Component component : comps) {
//                System.out.println("" + component.getName() + "" + component.getPollData());
            }
        }

//        return gamepad;
//        return new GamePad(gamepad);
        return new GamePad(conts[1]);
    }
}
