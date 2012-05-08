/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package arachnoid2;

import javax.media.j3d.Transform3D;

/**
 *
 * @author Kotuc
 */
public class Joint {

    // the variable state of the joint e.g. rotation
    Transform3D jointRotation = new Transform3D();
    BodyPart part;
    double limitLow = Double.MIN_VALUE; //-0.5;
    double limitHigh = Double.MAX_VALUE; //0.5;
    double momentum = 10;
    double angle;

    public Joint(BodyPart part) {
        this.part = part;
    }

    void setJointRotation(Transform3D jointRotation) {
        this.jointRotation = jointRotation;
    }

    public void setLimit(double low, double high) {
        this.limitLow = low;
        this.limitHigh = high;
    }


    /**
     * rotation along Z axis
     * @param angle
     */
    void setAngle(double angle) {
        angle = Math.max(limitLow, Math.min(angle, limitHigh));
        this.angle = angle;
        this.jointRotation.rotZ(angle);
    }

    public double getAngle() {
        return angle;
    }




}
