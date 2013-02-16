package robot.input;

import hypergame.eagleeye.Robot;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import org.jbox2d.common.Vec2;
import robot.output.DiffWheels;

/**
 *
 * @author Tomas
 */
public class KeyboardDriving {

    protected final Controller keyboard;
    protected Component forwardKey;
    protected Component backwardKey;
    protected Component rightKey;
    protected Component leftKey;
    protected Component stopKey;
    protected Component gripperKey;


    protected double factor = 0.2;


    private double upvalue;
    private double rightvalue;
    // factor of influence of arrows on speed


    public KeyboardDriving() {
        this.keyboard = ControllerTools.getKeyboard();
        findComponents();
        System.out.println("Keyboard driving ready.");
    }

    //
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
            } else if ("G".equals(component.getIdentifier().getName())) {
                gripperKey = component;
            } else {
                // not used
            }

        }

    }


    boolean previousup = false;

    public Vec2 actPlayer() {
        keyboard.poll();

        upvalue = (1 - factor) * upvalue + factor * (forwardKey.getPollData() - backwardKey.getPollData());

        rightvalue = (1 - factor) * rightvalue + factor * (rightKey.getPollData() - leftKey.getPollData());

        float jump = 0;

        boolean up = forwardKey.getPollData() > 0.5;
        if (up && !previousup) {
            jump = 1;
        }
        previousup = up;

        if (stopKey.getPollData() > 0.5) {
            upvalue = 0;
            rightvalue = 0;
        } else {

            if (Math.abs(upvalue) < 0.05) {
                upvalue = 0;
            }
            if (Math.abs(rightvalue) < 0.05) {
                rightvalue = 0;
            }

//            System.out.println("Keyboard " + forwardKey.getPollData() + " " + backwardKey.getPollData() + " "
//                    + rightKey.getPollData() + " " +
//                    leftKey.getPollData() + "; " +
//                    upvalue + " " + rightvalue);

//            wheels.setSpeed(upvalue);
//        wheels.setSteer(rightvalue * upvalue);
//            wheels.setSteer(rightvalue);
        }
//            Logger.getLogger("XPadDriving").log(Level.INFO, "upvalue: " + upvalue + "; rightvalue: " + rightvalue);
//        return new Vec2((float)rightvalue, (float)upvalue);
        return new Vec2((float)rightvalue, (float)jump);

    }




}
