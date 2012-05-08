package tradeworld.graphics;

import com.sun.j3d.utils.geometry.Text2D;
import java.awt.Font;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Node;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.PointSound;
import javax.media.j3d.Sound;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import tradeworld.GetTransform;

/**
 *
 * @author Kotuc
 */
public class Soldier3D extends Model3D {

    private Sound sound = new PointSound(null, 1.0f, new Point3f());
    private final GetTransform soldier;

    public void playSound(String soundName) {
//        sound.setSoundData(new MediaContainer("file:res/sounds/" + soundName));
//        sound.setEnable(true);
    }

    public Soldier3D(GetTransform soldier) {
        this.soldier = soldier;

        addChild(createBody());

//        addChild(Tools.superBox(1, 1, 2, "sold-duck1.png"));

        sound.setCapability(Sound.ALLOW_ENABLE_WRITE);
        sound.setCapability(Sound.ALLOW_SOUND_DATA_WRITE);
        sound.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        addChild(sound);

        addChild(createTextNotification(soldier.toString(), new Color3f(0, 1, 0)));
    }

    private static Node createBody() {

        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3d(0, 0.9, 0));
        TransformGroup soldBody = new TransformGroup(t3d);
        soldBody.addChild(Tools.superBox(0.8, 1.8, 0.5, "armytoad1.png"));

        Transform3D t3d2 = new Transform3D();
        Transforms.rotateFromXAnimToZeroAngle(t3d2);

        TransformGroup transformGroup = new TransformGroup(t3d2);
        transformGroup.addChild(soldBody);

        return transformGroup;
    }

    private static Node createTextNotification(String text, Color3f color) {
        Text2D text2d = new Text2D(
                text,
                color, "Arial", 12, Font.BOLD);
        text2d.setRectangleScaleFactor(0.02f);


        Quat4d quat = new Quat4d();
        quat.set(new AxisAngle4d(1, 0, 0, 0.5 * Math.PI));
        Transform3D t3d = new Transform3D();
        t3d.set(quat, new Vector3d(0, 0, 2), 1);
        TransformGroup transformGroup = new TransformGroup(t3d);

        transformGroup.addChild(new OrientedShape3D(text2d.getGeometry(), text2d.getAppearance(), OrientedShape3D.ROTATE_ABOUT_AXIS, new Vector3f(0, 1, 0)));

        return transformGroup;
    }

    @Override
    public void refresh() {
        Transform3D t3d = new Transform3D();
        soldier.getTransform(t3d);
        this.setTransform(t3d);
//        setTransform(soldier.getPos(getWorld3D().getCurrentTime()), soldier.getPan());
//        setTransform(soldier.getPos(getWorld3D().getCurrentTime()), soldier.getPan());
//        System.out.println("ref "+soldier.getPos());
    }
}
