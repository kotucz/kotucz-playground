package arachnoid2;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Tomas
 */
public class Ballancing {

    private final Arachnoid arachnoid;

    public Ballancing(Arachnoid arachnoid) {
        this.arachnoid = arachnoid;
    }

    /**
     * Keeps toes in place while applying tranform on body
     * @param direction 
     */
    public void ballancing1(Vector3d direction) {
        Transform3D bodyTrans = new Transform3D();

        direction = new Vector3d(direction);

        Vector3d up = new Vector3d(0, 1, 0);
        up.normalize();
//        direction.normalize();


//        bodyTrans.rotZ(0.005 * Math.sin(System.currentTimeMillis() / 1000.0));
        bodyTrans.rotY(0.005 * Math.sin(System.currentTimeMillis() / 1000.0));
//        bodyTrans.rotX(0.001*Math.sin(System.currentTimeMillis() / 2000.0));
//        bodyTrans.setTranslation(
//                new Vector3d(0.001 * Math.sin(System.currentTimeMillis() / 1000.0), 0, 0));

        bodyTrans.invert();

        double parity = 1;
        for (Leg leg : arachnoid.getLegs()) {
            Point3d legRoot = new Point3d();
            arachnoid.getTransform(leg).transform(legRoot);

            Point3d toe = new Point3d();
            leg.getGlobalToeTransform().transform(toe);
            System.out.println("toe: " + toe);

            bodyTrans.transform(toe);

            Vector3d vec = new Vector3d();
            vec.sub(toe, legRoot);

            Transform3D legInv = new Transform3D();
            legInv.invert(arachnoid.getTransform(leg));

            legInv.transform(vec);

            leg.setVectorLocal(vec);

            parity *= -1;
        }
    }
    private double superParity = 1;

    /**
     * Keeps legs on place while body is moving.
     * Up legs are moving forward inverse to ground ones.
     * @param bodyTrans relative trajectory of body
     */
    public void ballancing2(Transform3D bodyTrans) {

        final double max = 0.05;

        Transform3D bodyTransInv = new Transform3D();
        bodyTransInv.invert(bodyTrans);

        // direction to which legs goes up
        // might be normal of the surface - good idea!
        Vector3d up = new Vector3d(0, 1, 0);
        up.normalize();

        for (Leg leg : arachnoid.getLegs()) {
            Point3d legRoot = new Point3d();
            arachnoid.getTransform(leg).transform(legRoot);

            Point3d toe = new Point3d();
            leg.getGlobalToeTransform().transform(toe);
//            System.out.println("toe: " + toe);

            Vector3d toeDir = new Vector3d();
            toeDir.sub(leg.getLocalVector(), leg.toeCenter);

            Point3d toe0 = new Point3d(toe);
            if (leg.up) {
                // forward like body
                bodyTrans.transform(toe);

            } else {
                // backwards on ground
                bodyTransInv.transform(toe);
            }

            Vector3d mov = new Vector3d();
            mov.sub(toe, toe0);
            leg.transfrom.transform(toeDir);
            double prod = mov.dot(toeDir); // positive when toeDir grows
            if ((prod > 0) &&
                    toeDir.length() > max) {
                leg.up = !leg.up;
                toe = toe0;
//                hasExceededBounds = true;
            }

            Vector3d raise = toeDir;
            double phase = raise.dot(up) / up.lengthSquared();
            Vector3d upproj = new Vector3d(up); // ptojection to up vector
            upproj.scale(phase);
            toe.sub(upproj);

            // go up, get new raise
            Vector3d dup = new Vector3d(up);
            dup.normalize();
            dup.scale(((leg.up) ? 1 : -1));
//            dup.scale(0.01);
            dup.scale(Math.max(0, max - toeDir.length()));
//            double upscale = max * max - toeDir.lengthSquared();
//            if (upscale > 0) {
//                dup.scale(Math.sqrt(upscale));
//            }

            toe.add(dup);


//            if (toeDir.length() > max) {
//                System.out.println("bounds exceeded: "+toeDir.length());
//                hasExceededBounds = true;
////                parity*=-1;
//            }


//            Vector3d vec = new Vector3d();
//            vec.sub(toe, legRoot);
//
//            Transform3D legInv = new Transform3D();
//            legInv.invert(arachnoid.getTransform(leg));
//
//            legInv.transform(vec);

            try {
                leg.setVectorGlobal(toe);
            } catch (Exception ex) {
                if (prod > 0) {
                    leg.up = !leg.up;
                }
//                hasExceededBounds = true;
//                leg.up = !leg.up;
                System.out.println(ex.getMessage());
            }
//            parity *= -1;
        }
//        if (hasExceededBounds) {
//            superParity *= -1;
//        }
//        superParity = -parity;
    }

    /**
     * Like ballancing2, but legs are moving to most forward point on their center trajectory
     * @param bodyTrans
     */
    public void ballancing3(Transform3D bodyTrans) {

        final double max = 0.05;

        Transform3D bodyTransInv = new Transform3D();
        bodyTransInv.invert(bodyTrans);

        // direction to which legs goes up
        // might be normal of the surface - good idea!
        Vector3d up = new Vector3d(0, 1, 0);
        up.normalize();

        for (Leg leg : arachnoid.getLegs()) {
            Point3d legRoot = new Point3d();
            arachnoid.getTransform(leg).transform(legRoot);

            Point3d toe = new Point3d();
            leg.getGlobalToeTransform().transform(toe);
//            System.out.println("toe: " + toe);

            Vector3d toeDir = new Vector3d();
            toeDir.sub(leg.getLocalVector(), leg.toeCenter);

            Point3d toe0 = new Point3d(toe);
            if (leg.up) {
                // moving forward

                // desired point

                Point3d toeCentPoint = new Point3d(leg.toeCenter);
                leg.transfrom.transform(toeCentPoint);
                Point3d toeFuture = new Point3d(toeCentPoint);

                while (toeFuture.distance(new Point3d(toeCentPoint)) < (max + 0.02)) {
                    bodyTrans.transform(toeFuture);
                }

                // go to desired point
                Vector3d dir = new Vector3d();
                dir.sub(toeFuture, toe);
                dir.normalize();
                dir.scale(0.0016);

                toe.add(dir);

            } else {
                // backwards on ground
                bodyTransInv.transform(toe);
            }

            Vector3d mov = new Vector3d();
            mov.sub(toe, toe0);
            leg.transfrom.transform(toeDir);
            double prod = mov.dot(toeDir); // positive when toeDir grows
            if ((prod > 0) &&
                    toeDir.length() > max) {
                leg.up = !leg.up;
                toe = toe0;
            }

            Vector3d raise = toeDir;
            double phase = raise.dot(up) / up.lengthSquared();
            Vector3d upproj = new Vector3d(up); // ptojection to up vector
            upproj.scale(phase);
            toe.sub(upproj);

            // go up, get new raise
            Vector3d dup = new Vector3d(up);
            dup.normalize();
            dup.scale(((leg.up) ? 1 : -1));
//            dup.scale(0.01);
            dup.scale(Math.max(0, max - toeDir.length()));
//            double upscale = max * max - toeDir.lengthSquared();
//            if (upscale > 0) {
//                dup.scale(Math.sqrt(upscale));
//            }

            toe.add(dup);

            try {
                leg.setVectorGlobal(toe);
            } catch (Exception ex) {
                if (prod > 0) {
                    leg.up = !leg.up;
                }
                System.out.println(ex.getMessage());
            }

        }
    }

    /**
     * Like balancing3, but now with condition that at least 3 legs are on ground.
     * Also leg heigth improved.
     * @param bodyTrans
     */
    public void ballancing4(Transform3D bodyTrans) {

        final double maxForw = 0.03;
        final double lagging = 0.05;
        final double forwLegSpeed = 0.0033;

        Transform3D bodyTransInv = new Transform3D();
        bodyTransInv.invert(bodyTrans);

        // direction (and heigth) to which legs goes up
        // might be normal of the surface - good idea!
        //Vector3d up = new Vector3d(0, 0.0002, 0);
        Vector3d up = new Vector3d(arachnoid.relativeForceVector);
        //up.normalize();

        // if all legs are down select one leg and set it up
        boolean anyUp = false;
        Leg lastLeg = null;
        // minimal lag which makes any leg go forward
        // should be less than max
        double lastValue = lagging;
        for (Leg leg : arachnoid.getLegs()) {
            anyUp |= leg.up;

            Point3d legRoot = new Point3d();
            arachnoid.getTransform(leg).transform(legRoot);

            Point3d toe = new Point3d();
            leg.getGlobalToeTransform().transform(toe);
//            System.out.println("toe: " + toe);

            Vector3d toeDir = new Vector3d();
            toeDir.sub(leg.getLocalVector(), leg.toeCenter);

            Point3d toe0 = new Point3d(toe);
            if (leg.up) {
                // moving forward

                // desired point
                Point3d toeCentPoint = new Point3d(leg.toeCenter);
                leg.transfrom.transform(toeCentPoint);
                Point3d toeFuture = new Point3d(toeCentPoint);

                while (toeFuture.distance(new Point3d(toeCentPoint)) < (maxForw + 0.03)) {
                    bodyTrans.transform(toeFuture);
                }

                // go to desired point
                Vector3d dir = new Vector3d();
                dir.sub(toeFuture, toe);
                dir.normalize();
                dir.scale(forwLegSpeed);

                toe.add(dir);

            } else {
                // backwards on ground
                bodyTransInv.transform(toe);
            }

            Vector3d mov = new Vector3d();
            mov.sub(toe, toe0);
            leg.transfrom.transform(toeDir);
            double prod = mov.dot(toeDir); // positive when toeDir grows
            if (prod > 0) {
                //leg.up = !leg.up;
                double value = toeDir.length();

                // if it is enough forwards gets down
                if ((leg.up) && (value > maxForw)) {
                    leg.up = false;
                } else {
                    // the most last leg is selected
                    if ((lastLeg == null) || (value > lastValue)) {
                        lastLeg = leg;
                        lastValue = value;
                    }
                }
            //toe = toe0;
            }


            // go up, get new raise simple and elegant
            double phase = toeDir.dot(up) / up.lengthSquared();
            double dev = (((leg.up) ? 1 : 0) - phase);
            Vector3d dup = new Vector3d(up);
            dup.scale(dev);
            if (dup.length() > 0.004) {
                dup.normalize();
                dup.scale(0.004);
            }
//            toe.add(dup);

            try {
                leg.setVectorGlobal(toe);
            } catch (Exception ex) {
                if (prod > 0) {
                    //leg.up = !leg.up;
                }
                System.out.println(ex.getMessage());
            }

        }
        if (!anyUp) {
            // there need not be any leg - all legs can stand and "rest"
            if (lastLeg != null) {
                lastLeg.up = true;
            }
        }
    }
}