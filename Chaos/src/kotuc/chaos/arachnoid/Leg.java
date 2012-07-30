package kotuc.chaos.arachnoid;

import com.sun.j3d.utils.geometry.Box;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

class Leg {

    final double upperLength = 0.1;
    final double lowerLength = 0.1;
    Transform3D transfrom;
    TransformGroup firstTransform = new TransformGroup();
    TransformGroup rotServo1 = new TransformGroup();
    TransformGroup tiltServo2 = new TransformGroup();
    TransformGroup lengthServoTrans = new TransformGroup();
    TransformGroup lengthServo3 = new TransformGroup();
    TransformGroup femurTrans = new TransformGroup();
    TransformGroup shinTrans = new TransformGroup();
    TransformGroup toeTrans = new TransformGroup();
    Arachnoid bot;
    Vector3d toeCenter = new Vector3d(new Vector3d(0.1, -0.03, 0));
    
    boolean up;

    public Leg(Arachnoid bot) {
        super();
        this.bot = bot;
        rotServo1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tiltServo2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        lengthServo3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        firstTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        rotServo1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tiltServo2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        lengthServoTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        lengthServo3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        femurTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        shinTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        toeTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        firstTransform.addChild(rotServo1);
        rotServo1.addChild(tiltServo2);
        tiltServo2.addChild(lengthServoTrans);
        tiltServo2.addChild(femurTrans);
        lengthServoTrans.addChild(lengthServo3);
        lengthServo3.addChild(shinTrans);
        lengthServo3.addChild(toeTrans);

        Transform3D lenT = new Transform3D();
        lenT.setTranslation(new Vector3d(upperLength, 0, 0));
        lengthServoTrans.setTransform(lenT);
        Appearance appear = new Appearance();
        appear.setMaterial(new Material());
        // upper part
        femurTrans.addChild(new Box((float) upperLength / 2, 0.01F, 0.01F, appear));
        Transform3D femuT = new Transform3D();
        femuT.setTranslation(new Vector3d(upperLength / 2.0, 0, 0));
        femurTrans.setTransform(femuT);
        // lower part
        shinTrans.addChild(new Box((float) lowerLength / 2, 0.01F, 0.01F, appear));
        Transform3D shinT = new Transform3D();
        shinT.setTranslation(new Vector3d(lowerLength / 2.0, 0, 0));
        shinTrans.setTransform(shinT);
        // toe
        Transform3D toeT = new Transform3D(); //    V there should not have been a half!
        toeT.setTranslation(new Vector3d(lowerLength, 0, 0));
        toeTrans.setTransform(toeT);
//        setServo2(0.2);
//        setServo3(0.4);
        setVectorLocal(new Vector3d(0.1, -0.03, 0));
    }

    void setServo1(double angle) {
        Transform3D servoTrans = new Transform3D();
        servoTrans.rotY(angle);
        rotServo1.setTransform(servoTrans);
    }

    void setServo2(double angle) {
        Transform3D servoTrans = new Transform3D();
        servoTrans.rotZ(angle);
        tiltServo2.setTransform(servoTrans);
    }

    void setServo3(double angle) {
        Transform3D servoTrans = new Transform3D();
        servoTrans.rotZ(angle);
        lengthServo3.setTransform(servoTrans);
    }

    /**
     * Sets servos so that the length between start and end of leg is length.
     *
     * using: c2=a2+b2-2ab*cos(gama)
     * c .. length
     * a .. upperLength
     * b .. lowerLength
     * gama .. angle between parts
     *
     * @param length
     */
    void setLength(double length) {
        double servoAngle = Math.PI - getGamma(length);
        // outer complement
        setServo3(servoAngle);
        setServo2(-servoAngle / 2);
    }
    private Vector3d vector;

    /**
     * Sets servos so that toe of this let to vector pos in its own coordinate system.
     * @param vector
     * @throws IllegalArgumentException when cannot reach the vector.
     */
    void setVectorLocal(Vector3d vector) {
        double length = vector.length();
        double angle1 = Math.atan2(-vector.z, vector.x);
        double servoAngle = getGamma(length) - Math.PI;
        // outer complement
        double tilt = Math.asin(vector.y / length);
        setServo2(-servoAngle / 2 + tilt);
        setServo1(angle1);
        setServo3(servoAngle);
        this.vector = vector;
    }

    /**
     *
     * @param point point relative to this leg position (e.g. its parent)
     */
    public void setVectorGlobal(Point3d point) {
        Transform3D legInv = new Transform3D();
        legInv.invert(transfrom);
        legInv.transform(point);
        setVectorLocal(new Vector3d(point));
    }

    /**
     * Inverse to setToLocal().
     * @return
     */
    Vector3d getLocalVector() {
        // Vector is not translated!
        Point3d localVector = new Point3d();
        getLocalToeTransform().transform(localVector);
        return new Vector3d(localVector);
    }

    private double getGamma(double length) {
        if (length > (upperLength + lowerLength)) {
            throw new IllegalArgumentException("illegel length: " + length);
        }
        double angle = Math.acos((Math.pow(upperLength, 2) + Math.pow(lowerLength, 2) - Math.pow(length, 2)) / (2 * lowerLength * upperLength));
        return angle;
    }

    Point3d getToePos() {
        return new Point3d(vector);
    }

    Transform3D getGlobalToeTransform() {
        Transform3D trans = new Transform3D();
        Transform3D t3d = new Transform3D();
        firstTransform.getTransform(t3d);
        trans.mul(t3d);
        rotServo1.getTransform(t3d);
        trans.mul(t3d);
        tiltServo2.getTransform(t3d);
        trans.mul(t3d);
        lengthServoTrans.getTransform(t3d);
        trans.mul(t3d);
        lengthServo3.getTransform(t3d);
        trans.mul(t3d);
        toeTrans.getTransform(t3d);
        trans.mul(t3d);

//        trans.invert();
        return trans;
    }

    Transform3D getLocalToeTransform() {
        Transform3D trans = new Transform3D();
        Transform3D t3d = new Transform3D();
        rotServo1.getTransform(t3d);
        trans.mul(t3d);
        tiltServo2.getTransform(t3d);
        trans.mul(t3d);
        lengthServoTrans.getTransform(t3d);
        trans.mul(t3d);
        lengthServo3.getTransform(t3d);
        trans.mul(t3d);
        toeTrans.getTransform(t3d);
        trans.mul(t3d);

//        trans.invert();
        return trans;
    }
}
