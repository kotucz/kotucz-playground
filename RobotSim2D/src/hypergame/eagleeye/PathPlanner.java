package hypergame.eagleeye;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import hypergame.Displayer;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Kotuc
 */
public class PathPlanner {

    Robot robot;
    private float speed;
    private float rot;

    public PathPlanner(Robot robot) {
        this.robot = robot;
    }
    Vec2 tgt = new Vec2(1, 1);
    Vec2 pos = new Vec2();
    Vec2 dir = new Vec2();

    void act() {

        pos = robot.body.getPosition();
        dir = robot.body.getWorldVector(new Vec2(0, 1));
        Vec2 rightdir = robot.body.getWorldVector(new Vec2(1, 0));

        Vec2 tdir = tgt.sub(pos);

//        float dist = tdir.normalize();
//        robot.setSpeedsLR(dist, dist);

//        speed = Vec2.dot(dir, tdir)*0.1f;
        rot = Vec2.dot(rightdir, tdir)*0.1f;

        robot.setSpeedsLR(speed + rot, speed - rot);


    }

    void paint(Displayer g2) {

//        Stroke stroke = g2.getStroke();
//        g2.setStroke(new BasicStroke(0.01f));

        g2.setColor(Color.GREEN);
        g2.drawLine(pos, tgt);

        g2.setColor(Color.PINK);
        g2.drawVector(pos, dir);

//        g2.setFont(g2.getFont().deriveFont(0.1f));
        System.out.println("dot " + speed + " rot " + rot);


//        g2.setStroke(stroke);

        { // simulate numerical

            GeneralPath gp = new GeneralPath();

            PoseState pose = new PoseState();
//            pose.setTo(robot.body.getXForm());
            pose.setTo(robot);

            Vec2 posa = new Vec2(pose.getPosition());

            g2.setColor(Color.RED);

            for (int i = 0; i < 1000; i++) {
                pose.update(0.001f);
//                pose.xform.position.x+=0.01;
                Vec2 posb = new Vec2(pose.getPosition());
                g2.drawLine(posa, posb);
                posa = posb;
            }



        }

        { // simulate absolute


            PoseState pose = new PoseState();
//            pose.setTo(robot.body.getXForm());
            pose.setTo(robot);

            g2.setColor(Color.GREEN);

            Vec2 posa = new Vec2(pose.getPosition());

            for (int i = 0; i < 100; i++) {
                pose.setTo(robot);
                pose.update(0.01f * i);
//                pose.xform.position.x+=0.01;
                Vec2 posb = new Vec2(pose.getPosition());
                g2.drawLine(posa, posb);
                posa = posb;
            }


        }


    }

    class PoseState {

        final double wheeldiam = 0.24f;
        Transform xform = new Transform();
        double lwheelvel = 0;
        double rwheelvel = 0;
        double laccel = 2;
        double raccel = 1;

        public PoseState() {
            xform.setIdentity();
        }

        void setTo(Transform xform) {
            this.xform.set(xform);
        }

        void setTo(Robot robot) {
            this.xform.set(robot.body.getTransform());
            this.lwheelvel = robot.lwheel.angularVelocity;
            this.rwheelvel = robot.rwheel.angularVelocity;
            this.laccel = robot.lwheel.speed;
            this.raccel = robot.rwheel.speed;
        }

        Vec2 getPosition() {
            return xform.position;
        }

        void update(float secs) {

            double ldist = lwheelvel * secs + 0.5 * laccel * secs * secs;
            double rdist = rwheelvel * secs + 0.5 * raccel * secs * secs;

            lwheelvel += laccel * secs;
            rwheelvel += raccel * secs;

//            odometry1(ldist, rdist);
            odometry2(ldist, rdist);
        }
        
        void odometry1(double ldist, double rdist) {

            float rotangleccw = (float) ((rdist - ldist) / (wheeldiam * Math.PI));

            Vec2 fw = new Vec2((float) Math.cos(0.5 * rotangleccw + 0.5 * Math.PI), (float) Math.sin(0.5 * rotangleccw + 0.5 * Math.PI));
//            Vec2 fw = new Vec2((float) Math.cos(0.5 * rotangleccw + Math.PI / 2), (float) Math.sin(rotangleccw + Math.PI / 2));
            fw.normalize();

            moveRelFw(getWorldVector(fw.mul((float) (ldist + rdist) / 2.0f)));
            rotateCCW(rotangleccw);

//            XForm movexf = new XForm();
//
//            movexf.R.setAngle(rotangleccw);
//	    movexf.position.set(getWorldVector(fw.mul((float)(ldist+rdist)/2.0f)));
//
////            xform.position.set(xform.position.add(movexf.position));
////            xform.position.set(XForm.mul(xform, fw));
//            xform.position = xform.position.add(movexf.position);
//            xform.R.set(xform.R.mul(movexf.R));
////            xform.R.set(movexf.R.mul(xform.R));

        }

        

        void odometry2(double ldist, double rdist) {
            float rotangleccw = (float) ((rdist - ldist) / (wheeldiam * Math.PI));
            Vec2 fw = new Vec2((float) Math.cos(0.5 * rotangleccw + 0.5 * Math.PI), (float) Math.sin(0.5 * rotangleccw + 0.5 * Math.PI));
            fw.normalize();
            double dist1 = (ldist + rdist) / 2.0f;
            double dist = 0;
            //            if (rotangleccw < Math.PI / 2.0) {
            //                dist = dist1 * Math.cos(rotangleccw / 2.0); // around 0
            //            }
            dist = 2.0 * dist1 * Math.sin(rotangleccw / 2.0) / rotangleccw; // 0 <<
            moveRelFw(getWorldVector(fw.mul((float) dist)));
            rotateCCW(rotangleccw);
        }

        /**
         * Moves relative forward
         */
        void moveRelFw(Vec2 fw) {
            xform.position.set(xform.position.add(fw));
        }

        void rotateCCW(float angle) {
//            Mat22 R = new Mat22(angle);
            Mat22 R = Mat22.createRotationalTransform(angle);
            xform.R.set(xform.R.mul(R));
        }

        Vec2 getWorldVector(Vec2 localVector) {
            return Mat22.mul(xform.R, localVector);
        }
    }


}
