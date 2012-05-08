package arachnoid2;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.java.games.input.Component;
import net.java.games.input.Controller;

/**
 *
 * @author Tomas
 */
public class WalkView  {

    private Arachnoid arachnoid = new Arachnoid();
    private Walking walking = new Walking(arachnoid);
    private Controller gamepad = GamePad.getActiveGamePad().getController();

    public WalkView() {
//        location.addEntity(arachnoid);
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0.0f, 0.0f, 1f));
//        location.getViewTranformGroup().setTransform(t3d);
    }

    protected void simpleUpdate() {
        try {
            gamepad.poll();
            Vector3d dir = new Vector3d(0.001, 0.0, 0.0);
            for (Component component : gamepad.getComponents()) {
                System.out.print(component.getIdentifier().getName() + component.getPollData() + ",");
                if ("x".equals(component.getIdentifier().getName())) {
                    dir.x = component.getPollData();
                }
                if ("y".equals(component.getIdentifier().getName())) {
                    dir.z = component.getPollData();
                }
            }
            dir.scale(0.01);
            System.out.println("dir " + dir);
            double phase = System.currentTimeMillis() / 500.0;
//            walking.rotating(phase);
//            walking.walking1(phase);
//            walking.walking2(phase);
//            walking.walking3(phase);
//            walking.walking4(phase);
//            walking.walking5(phase);            
//            walking.walking6(dir);
            arachnoid.go(dir);
        } catch (Exception ex) {
            System.err.println("" + ex.getMessage());            
        }
    }


}
