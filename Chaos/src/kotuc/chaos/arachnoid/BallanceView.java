package kotuc.chaos.arachnoid;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import kotuc.chaos.SimpleGame;
import net.java.games.input.Component;
import net.java.games.input.Controller;

/**
 *
 * @author Tomas
 */
public class BallanceView extends SimpleGame {

    private Arachnoid arachnoid = new Arachnoid();
    private Ballancing ballancing = new Ballancing(arachnoid);
    private Controller gamepad = GamePad.getActiveGamePad().getController();

    public BallanceView() {
        location.addEntity(arachnoid);
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0.0f, 0.0f, 1f));
        location.getViewTranformGroup().setTransform(t3d);

    }

    @Override
    protected void simpleUpdate() {
        try {
            double phase = System.currentTimeMillis() / 500.0;
            gamepad.poll();
            Vector3d dir = new Vector3d(0.001, 0.0, 0.0);
            Vector3d force = new Vector3d(0.0, -1.0, 0.0);
            double roty = 0;
            double rotz = 0;
            double updown = 0;
            for (Component component : gamepad.getComponents()) {
                System.out.print(component.getIdentifier().getName() + component.getPollData() + ",");
                if ("x".equals(component.getIdentifier().getName())) {
                    dir.x = component.getPollData();
                }
                if ("y".equals(component.getIdentifier().getName())) {
                    dir.z = component.getPollData();
                }
                if ("z".equals(component.getIdentifier().getName())) {
                    roty = component.getPollData();
                }
                if ("rx".equals(component.getIdentifier().getName())) {
                    //rotz = component.getPollData();
                    force.x = component.getPollData();
                }
                if ("ry".equals(component.getIdentifier().getName())) {
                    //rotz = component.getPollData();
                    force.z = component.getPollData();
                }
                if ("0".equals(component.getIdentifier().getName())) {
                    updown += 0.003*component.getPollData();
                }
                if ("1".equals(component.getIdentifier().getName())) {
                    updown -= 0.003*component.getPollData();
                }
            }
            dir.scale(0.005);
            roty *= 0.03;
            rotz *= 0.003;
//            System.out.println("dir " + dir);
//            ballancing.ballancing1(dir);

            Transform3D trans = new Transform3D();
//        bodyTrans.rotZ(0.005);
//        bodyTrans.rotY(0.005);
//            bodyTrans.rotX(0.001 * Math.sin(System.currentTimeMillis() / 2000.0));
            trans.setTranslation(dir);
            Transform3D rotY = new Transform3D();
            rotY.rotY(roty);
//            rotY.mul(trans);
//            arachnoid.move(rotY);
            trans.mul(rotY);
//            rotY.rotZ(rotz);
//            trans.mul(rotY);
            arachnoid.setPreferredHeigth(arachnoid.preferredHeigth+updown);
            arachnoid.setRelativeForceVector(force);
            arachnoid.move(trans);
//            ballancing.ballancing2(trans);

        //Thread.sleep(100);
        } catch (Exception ex) {
//            System.err.println("" + ex.getMessage()+" "+ex);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BallanceView().start();
    }
}
