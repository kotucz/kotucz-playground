package tradeworld.graphics;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.MediaContainer;
import javax.media.j3d.PointSound;
import javax.media.j3d.Sound;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import tradeworld.war.Soldier;

/**
 *
 * @author Kotuc
 */
public class Soldier3D extends Model3D {

    private Sound sound = new PointSound(null, 1.0f, new Point3f());
    private final Soldier soldier;

    public void playSound(String soundName) {
//        sound.setSoundData(new MediaContainer("file:res/sounds/" + soundName));
//        sound.setEnable(true);

    }

    public Soldier3D(Soldier soldier) {
        this.soldier = soldier;
        addChild(Tools.superBox(1, 1, 2, "sold-duck1.png"));


        sound.setCapability(Sound.ALLOW_ENABLE_WRITE);
        sound.setCapability(Sound.ALLOW_SOUND_DATA_WRITE);
        sound.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
//		BranchGroup objAdd = new BranchGroup();
//		objAdd.addChild(sound);
//		this.objAdd.addChild(objAdd);

//		this.addPart(new EntityPart(sound));

//		this.behaviors.add();

        addChild(sound);
    }

    @Override
    public void refresh() {
        setPos(soldier.getPos(getWorld3D().getCurrentTime()), 0);
//        System.out.println("ref "+soldier.getPos());
    }
}
