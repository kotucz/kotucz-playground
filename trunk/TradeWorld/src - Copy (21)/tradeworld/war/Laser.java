package tradeworld.war;

import java.util.LinkedList;
import java.util.List;
import javax.media.j3d.PickInfo;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.PlayerId;
import tradeworld.Time;
import tradeworld.World;
import tradeworld.WorldObject;
import tradeworld.graphics.Laser3D;

/**
 *
 * @author Kotuc
 */
public class Laser extends WorldObject {

    private final PlayerId playerid;
    private final TimePos3D timepos;

    public Laser(PlayerId playerid, TimePos3D timepos) {
        this.playerid = playerid;
        this.timepos = timepos;
//        this.timepos = new TimePos3D(time, pos, direction, new Vector3d(0, 0, -0.2));

//        this.velocity = direction;
//        this.velocity.normalize();
//
//        this.setPos(pos);

//        last = time;
        Laser3D laser3D = new Laser3D(this);
        this.model = laser3D;
        model.setTransform(timepos.position);

    }

    public Vector3d getDirection() {
        return timepos.velocity;
    }

    @Override
    public void act(Time time) {
        getHits(getWorld(), timepos.position, timepos.velocity);
        if (time.passed(timepos.time.addSec(0.5))) {
            this.remove();
        }
    }

    public List<WorldObject> getHits(World world, Point3d pos, Vector3d dir) {
        PickRay pickRay = new PickRay(pos, dir);
        PickInfo[] infos = world.getWorld3d().getRoot().pickAllSorted(
//                PickInfo.PICK_BOUNDS, PickInfo.NODE | PickInfo.SCENEGRAPHPATH, pickRay);
                PickInfo.PICK_GEOMETRY, PickInfo.NODE | PickInfo.SCENEGRAPHPATH, pickRay);
        List<WorldObject> list = new LinkedList<WorldObject>();
        if (infos == null) {
            System.err.println("FUCK! Pick ray returns null instead of empty array");
        } else {
            for (PickInfo info : infos) {
                SceneGraphPath path = info.getSceneGraphPath();
                System.out.println("Pick " + pickRay + " node " + info.getNode());
                for (int i = 0; i < path.nodeCount(); i++) {
                    System.out.println("pick: " + i + ": " + path.getNode(i));
                }
                if (path.nodeCount() > 0) {
                    WorldObject associatedObject = world.getWorld3d().getAssociatedObject(path.getNode(0));
                    if (associatedObject == null) {
                        System.err.println("No object associated with " + path.getNode(0));
                    } else {
                        if (associatedObject instanceof Soldier) {
                            Soldier sold = (Soldier) associatedObject;
                            if (!this.playerid.equals(sold.playerid)) {
                                sold.damage(1);                                
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}
