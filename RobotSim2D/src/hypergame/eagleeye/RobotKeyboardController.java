package hypergame.eagleeye;

import robot.input.ControllerTools;
import robot.input.KeyboardDriving;
import robot.output.DiffWheels;

/**
 * @author Kotuc
 */
public class RobotKeyboardController extends KeyboardDriving {

    private final DiffWheels wheels;
    Robot.Gripper gripper;

    double speed = 0;
    double steer = 0;

    double left = 0;
    double right = 0;

    public RobotKeyboardController(Robot robot) {
//        this.wheels = wheels;
        this.wheels = robot;
        this.gripper = robot.gripper;
        if (gripper == null) {
            throw new NullPointerException("gripper");
        }

    }


    public boolean act() {
        keyboard.poll();

        gripper.grip(gripperKey.getPollData() > 0.5);

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

            System.out.println("Keyboard " + forwardKey.getPollData() + " " + backwardKey.getPollData() + " "
                    + rightKey.getPollData() + " " +
                    leftKey.getPollData() + "; " +
                    speed + " " + steer);

            wheels.setSpeedsLR(left, right);

//            wheels.setSpeed(speed);
//        wheels.setSteer(steer * speed);
//            wheels.setSteer(steer);
        }
//            Logger.getLogger("XPadDriving").log(Level.INFO, "speed: " + speed + "; steer: " + steer);
        return true;

    }

}
