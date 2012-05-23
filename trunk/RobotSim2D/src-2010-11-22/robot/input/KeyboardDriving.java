package robot.input;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import robot.output.DiffWheels;

/**
 *
 * @author Tomas
 */
public class KeyboardDriving {

    private final Controller keyboard;
    private Component forwardKey;
    private Component backwardKey;
    private Component rightKey;
    private Component leftKey;
    private Component stopKey;
    private final DiffWheels wheels;
    double factor = 0.2;
    double speed = 0;
    double steer = 0;
    double left = 0;
    double right = 0;
    // factor of influence of arrows on speed

    public KeyboardDriving(DiffWheels wheels) {
        this.wheels = wheels;
        this.keyboard = ControllerTools.getKeyboard();
        findComponents();
        System.out.println("Keyboard driving ready.");
    }

    private void findComponents() {

        for (Component component : keyboard.getComponents()) {

            System.out.println("["+component+"]");

            if ("Up".equals(component.getIdentifier().getName())) {
                forwardKey = component;
            } else if ("Down".equals(component.getIdentifier().getName())) {
                backwardKey = component;
            } else if ("Right".equals(component.getIdentifier().getName())) {
                rightKey = component;
            } else if ("Left".equals(component.getIdentifier().getName())) {
                leftKey = component;
            } else if (" ".equals(component.getIdentifier().getName())) {
                stopKey = component;
            } else {
                // not used
            }

        }

    }

    public boolean act() {
        keyboard.poll();

        speed = (1 - factor) * speed + factor * (forwardKey.getPollData() - backwardKey.getPollData());

        steer = (1 - factor) * steer + factor * (rightKey.getPollData() - leftKey.getPollData());

        if (stopKey.getPollData() > 0.5) {
            wheels.stop();
            speed = 0;
            steer = 0;
        } else {

            if (Math.abs(speed) < 0.05) {
                speed = 0;
            }
            if (Math.abs(steer) < 0.05) {
                steer = 0;
            }

            left = speed + steer;
            right = speed - steer;

//            System.out.println("Keyboard " + forwardKey.getPollData() + " " + backwardKey.getPollData() + " "
//                    + rightKey.getPollData() + " " +
//                    leftKey.getPollData() + "; " +
//                    speed + " " + steer);

            wheels.setSpeedsLR(left, right);

//            wheels.setSpeed(speed);
//        wheels.setSteer(steer * speed);
//            wheels.setSteer(steer);
        }
//            Logger.getLogger("XPadDriving").log(Level.INFO, "speed: " + speed + "; steer: " + steer);
        return true;

    }
}
