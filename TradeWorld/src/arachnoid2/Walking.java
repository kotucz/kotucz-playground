package arachnoid2;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Tomas
 */
public class Walking {

    private final Arachnoid arachnoid;

    public Walking(Arachnoid arachnoid) {
        this.arachnoid = arachnoid;
    }

    public void rotating(double phase) {
        for (Leg leg : arachnoid.getLegs()) {

//        arachnoid.getLeg().setLength(0.19 * Math.sin(System.currentTimeMillis() / 1000.0));
//        arachnoid.getLeg().setServo1(System.currentTimeMillis() / 5000.0);

            Vector3d vec = new Vector3d(
                    0.1,
                    -0.03 + (0.05 * Math.cos(phase)),
                    0.1 * Math.sin(phase));
            leg.setVectorLocal(vec);

//            arachnoid.getLeg().setTo(new Vector3d(0.1, -0.1, 0.1));
        }
    }

    /**
     * all legs are synchronous, but unnatural
     * @param phase
     */
    public void walking1(double phase) {
        for (Leg leg : arachnoid.getLegs()) {

            Vector3d vec = new Vector3d(
                    0.1,
                    -0.03 + (0.05 * Math.cos(phase)),
                    0.1 * Math.sin(phase));
            Transform3D legInv = new Transform3D();
            legInv.invert(arachnoid.getTransform(leg));
            legInv.transform(vec);
            leg.setVectorLocal(vec);
        }
    }

    /**
     * Very good lookink like jumping.
     * @param phase
     */
    public void walking2(double phase) {
        for (Leg leg : arachnoid.getLegs()) {
            Transform3D legInv = new Transform3D();
            legInv.invert(arachnoid.getTransform(leg));
            Point3d pt = new Point3d();
            arachnoid.getTransform(leg).transform(pt);
            Vector3d vec = new Vector3d(
                    1.5 * pt.x,
                    pt.y - 0.03 + (0.05 * Math.cos(phase)),
                    pt.z + 0.1 * Math.sin(phase));
            legInv.transform(vec);
            leg.setVectorLocal(vec);
        }
    }

    /**
     * Walking like dog - switching opposite leg pairs
     * @param phase
     */
    public void walking3(double phase) {
        double parity = 1;
        for (Leg leg : arachnoid.getLegs()) {
            Transform3D legInv = new Transform3D();
            legInv.invert(arachnoid.getTransform(leg));
            Point3d pt = new Point3d();
            arachnoid.getTransform(leg).transform(pt);
            Vector3d vec = new Vector3d(
                    2 * pt.x,
                    pt.y - 0.04 + parity * (0.03 * Math.cos(phase)),
                    2 * pt.z + parity * 0.1 * Math.sin(phase));
            legInv.transform(vec);
//            if (vec.length() > 0.2) {
//                vec.normalize();
//                vec.scale(0.2);
//            }
            leg.setVectorLocal(vec);
            parity *= -1;
        }
    }

    /**
     * Walking like walking 3 but in any direction.
     * @param phase
     */
    public void walking4(double phase) {
        double parity = 1;
        Vector3d direction = new Vector3d(Math.sin(0.1 * phase), 0, Math.cos(0.1 * phase));
        direction.normalize();
        for (Leg leg : arachnoid.getLegs()) {
            Transform3D legInv = new Transform3D();
            legInv.invert(arachnoid.getTransform(leg));
            Point3d pt = new Point3d();
            arachnoid.getTransform(leg).transform(pt);
            double amp = parity * 0.09 * Math.sin(phase);
            Vector3d vec = new Vector3d(
                    2 * pt.x + amp * direction.x,
                    pt.y - 0.04 + parity * (0.03 * Math.cos(phase)),
                    2 * pt.z + amp * direction.z);
            legInv.transform(vec);
            if (vec.length() > 0.199) {
                vec.normalize();
                vec.scale(0.199);
            }
            leg.setVectorLocal(vec);
            parity *= -1;
        }
    }
    Vector3d toePos = new Vector3d(0, 0.01, 0);

    /**
     * Like real spider 
     * @param phase
     */
    public void walking5(double phase) {
        double parity = 1;
        // TODO direction and up must be "kolme"
        //Vector3d direction = new Vector3d(Math.sin(0.1 * phase), 0, Math.cos(0.1 * phase));
        Vector3d direction = new Vector3d(1, 0, 0); // to right
        Vector3d up = new Vector3d(0, 1, 0);
        direction.normalize();
        double max = 0.05;

        toePos = new Vector3d();
        direction.scale(max * Math.sin(phase));
        toePos.add(direction);

        // calculate up phase
        up.scale(Math.sqrt(max * max - toePos.lengthSquared()));
        up.scale(Math.signum(Math.cos(phase)));
        toePos.add(up);

        for (Leg leg : arachnoid.getLegs()) {
            Point3d toeCenter = new Point3d(0.1, 0, -0.03);
            arachnoid.getTransform(leg).transform(toeCenter);

            //double amp = parity * max * Math.sin(phase);

            Vector3d vec = new Vector3d(
                    toeCenter.x + parity * toePos.x,
                    toeCenter.y + parity * toePos.y,
                    toeCenter.z + parity * toePos.z);

            Transform3D legInv = new Transform3D();
            legInv.invert(arachnoid.getTransform(leg));
            legInv.transform(vec);
//            if (vec.length() > 0.199) {
//                vec.normalize();
//                vec.scale(0.199);
//            }
            leg.setVectorLocal(vec);
            parity *= -1;
        }
    }

    /**
     * Independent on phase
     * @param direction 
     */
    public void walking6(Vector3d direction) {
        final double max = 0.05;
        
        direction = new Vector3d(direction);

//        Vector3d direction0 = new Vector3d(direction);
//        direction.normalize();
//        direction.scale(max);
//        direction.sub(toePos);
//        direction.normalize();
//        direction.scale(direction0.length());


        // TODO direction and up must be "kolme"
        //Vector3d direction = new Vector3d(Math.sin(0.1 * phase), 0, Math.cos(0.1 * phase));
//        Vector3d direction = new Vector3d(1, 0, 0); // to right
        Vector3d up = new Vector3d(0, 1, 0);
        up.normalize();
//        direction.normalize();


        Vector3d v = new Vector3d();
        v.sub(toePos, direction);
        double phase = toePos.dot(up) / up.lengthSquared();
        Vector3d upproj = new Vector3d(up); // ptojection to up vector
        upproj.scale(phase);
        toePos.sub(upproj);

        phase = Math.signum(phase);

        direction.scale(phase);
        toePos.add(direction);
        if (toePos.length() > max) {
            toePos.sub(direction);
            phase *= -1;
        }

        // calculate up phase
        up.scale(phase * Math.sqrt(max * max - toePos.lengthSquared()));
//        up.scale(0.5 * phase * (max - toePos.length()));
        toePos.add(up);

        double parity = 1;
        for (Leg leg : arachnoid.getLegs()) {
            Point3d toeCenter = new Point3d(0.1, 0, -0.03);
            arachnoid.getTransform(leg).transform(toeCenter);

            //double amp = parity * max * Math.sin(phase);

            Vector3d vec = new Vector3d(
                    toeCenter.x + parity * toePos.x,
                    toeCenter.y + parity * toePos.y,
                    toeCenter.z + parity * toePos.z);

            Transform3D legInv = new Transform3D();
            legInv.invert(arachnoid.getTransform(leg));
            legInv.transform(vec);
//            if (vec.length() > 0.199) {
//                vec.normalize();
//                vec.scale(0.199);
//            }
            leg.setVectorLocal(vec);
            parity *= -1;
        }
    }
}
