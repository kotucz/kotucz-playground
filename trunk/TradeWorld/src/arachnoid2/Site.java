/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arachnoid2;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public class Site {

    BodyPart part;
    Transform3D offset = new Transform3D();

    public Site(BodyPart part, Vector3d vec) {
        this.part = part;
        offset.setIdentity();
        offset.setTranslation(vec);
    }

    Point3d getPoint() {
        Point3d point = new Point3d();
        Transform3D trans = new Transform3D();
        trans.setIdentity();
        part.getTransform(trans);
        trans.mul(this.offset);
        trans.transform(point);
        return point;
    }
}
