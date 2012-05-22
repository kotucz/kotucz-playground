package evomotion;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.PhysicsUpdateCallback;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.util.SimplePhysicsGame;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class RobotPhys extends SimplePhysicsGame {

    private final int generationSize = 10;
    private final float generationTime = 20;
    //private final float dt = 1f/128;
    private InputHandler physicsStepInputHandler;
    private DynamicPhysicsNode dynamicNode;
    private Robot[] robots = new Robot[generationSize];

    protected void simpleInitGame() {

        for (int i = 0; i < robots.length; i++) {
            robots[i] = new Robot(getPhysicsSpace(), rootNode);
        }

//        createScene();

//         first we will create a floor and sphere like in Lesson4
        StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();

        //        rootNode.attachChild(staticNode);
//        PhysicsBox floorBox = staticNode.createBox("ramp");
//        floorBox.getLocalScale().set(10, 0.5f, 10);
////        floorBox.setLocalRotation(new Quaternion(0, 0, 1, 0.03f));
//        DynamicPhysicsNode dynamicSphereNode = getPhysicsSpace().createDynamicNode();
//        rootNode.attachChild(dynamicSphereNode);


        StaticPhysicsNode staticNode2 = getPhysicsSpace().createStaticNode();
        rootNode.attachChild(staticNode2);
        PhysicsBox floorBox2 = staticNode.createBox("floor");
        floorBox2.getLocalScale().set(100, 0.5f, 100);


        // we want to take in account now what was already mentioned in Lesson3:
        // forces must be applied for each physics step if you want a constant force applied
        // thus we create an input handler that gets invoked each physics step
        physicsStepInputHandler = new InputHandler();
        getPhysicsSpace().addToUpdateCallbacks(new PhysicsUpdateCallback() {

            public void beforeStep(PhysicsSpace space, float time) {
//                time = dt;
                for (int i = 0; i < robots.length; i++) {
                    robots[i].act(robottime, time);
                }
                physicsStepInputHandler.update(time);
            }

            public void afterStep(PhysicsSpace space, float time) {
//                time = dt;
                Vector3f center = new Vector3f();
                robottime += time;
                if (robottime > generationTime) {

                    // evaluate


                    // best of generation
                    // comment for total best
                    bestBrain = new Brain();

                    for (Robot robot : robots) {
                        robot.evaluate();
                        if (robot.brain.score > bestBrain.score) {
//                            System.out.println("NEW BEST!:");
                            bestBrain = robot.brain;
                        }
                    }

                    // sort by quality
                    Arrays.sort(robots);
                    System.out.println("SCORES " + Arrays.toString(robots));

                    newGeneration();
                }


            }
        });


        // now we add an input action to move the sphere while a key is pressed
        // the action is defined below
        // we register it to be invoked every update of the input handler while the HOME key (POS1) is down
        //                              ( last parameter value is 'true' )
        // note: as the used input handler gets updated each physics step the force is framerate independent -
        //       we can't use the normal input handler here!

        // register an action for the other direction, too

//        physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(0, 0, force), 0),
//                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_1, InputHandler.AXIS_NONE, true);
//        physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(0, 0, -force), 0),
//                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_2, InputHandler.AXIS_NONE, true);
//
//        physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(0, 0, force), 1),
//                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_3, InputHandler.AXIS_NONE, true);
//        physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(0, 0, -force), 1),
//                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_4, InputHandler.AXIS_NONE, true);
//
//        physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(0, 0, force), 2),
//                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_5, InputHandler.AXIS_NONE, true);
//        physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(0, 0, -force), 2),
//                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_6, InputHandler.AXIS_NONE, true);

//        physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(0, 0, force), 3),
//                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_7, InputHandler.AXIS_NONE, true);
//        physicsStepInputHandler.addAction(new MyInputAction(new Vector3f(0, 0, -force), 3),
//                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_8, InputHandler.AXIS_NONE, true);
        newGeneration();
        showPhysics = true;
    }
    int gen = 0;

    void newGeneration() {
        gen++;
        System.out.println("GENERATION " + gen);
        robottime = 0;
        // first 3 are unchanged
        for (int i = 0; i < robots.length; i++) {
            robots[i].createRobot((i - generationSize / 2) * 5f);
        }

//        if (gen > 2) {
        for (int i = 3; i < robots.length; i++) {

            if (Math.random() > Math.pow(robots[i].brain.score / bestBrain.score, 1)) {
                robots[i].brain = Brain.crosover(robots[new Random().nextInt(i)].brain, robots[new Random().nextInt(i)].brain);
            }

//            if (Math.random() > Math.pow(robots[i].brain.score / bestBrain.score, 1)) {
//                robots[i].brain = Brain.copyOf(bestBrain);
//            }

            for (; Math.random() < 0.1;) {
                robots[i].brain.mutate();
            }
//            }
        }
    }
    float robottime = 0;
    Brain bestBrain = new Brain();

    /**
     * An action that get's invoked while a key is down.
     */
    private class MyInputAction extends InputAction {

        private final Vector3f direction;
        private final Vector3f appliedForce = new Vector3f();
        private final int limbid;

        /**
         * The action get the node it should move and the direction it should move in.
         *
         * @param direction force that should be applied on each invocation of the action
         */
        public MyInputAction(Vector3f direction, int limb) {
            this.limbid = limb;
            // simply remember in member variables
            this.direction = direction;
        }

        /**
         * This method gets invoked upon key event
         *
         * @param evt more data about the event (we don't need it)
         */
        public void performAction(InputActionEvent evt) {
//            appliedForce.set(direction).multLocal(evt.getTime());
//            dynamicNode = limbs.get(limbid);
//            dynamicNode.addTorque(appliedForce);
//
//            appliedForce.set(direction).multLocal(-evt.getTime());
//            dynamicNode = limbs.get(limbid + 1);
//            dynamicNode.addTorque(appliedForce);
        }
    }

    /**
     * The main method to allow starting this class as application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.WARNING); // to see the important stuff
        new RobotPhys().start();

    }
}
