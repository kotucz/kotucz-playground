package arachnoid2;

import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;
import tradeworld.graphics.World3D;

/**
 *
 * @author Kotuc
 */
public class StaticModel {

    BodyPart root;
    List<BodyPart> parts = new ArrayList<BodyPart>();
    List<Joint> joints = new ArrayList<Joint>();
    final double halfBodySize = 0.4;
    final double upLegHalfLength = 0.6;
    final double downLegHalfLength = 0.75;
    List<Site> sites = new ArrayList<Site>();

    public StaticModel() {

        {
            root = new BodyPart("Body");
//            root.mass = 0f;
            Transform3D tr = new Transform3D();
            tr.setTranslation(new Vector3d(5, 5, 2));
            root.setJointLocation(tr);
            root.halfBodyBox = new Vector3d(halfBodySize - 0.15, halfBodySize - 0.15, 0.1);
            parts.add(root);
            root.joint.setLimit(-10000, 100000);
//            joints.add(root.joint);
        }

        for (int i = 0; i < 4; i++) {

            // upper leg part
            BodyPart part0 = new BodyPart("hip " + i);

            {
                // symetry transform
                Transform3D trsym = new Transform3D();
                trsym.rotZ(i * Math.PI / 2);

                Transform3D tmptr = new Transform3D();

                tmptr.setTranslation(new Vector3d(halfBodySize, 0, 0));
//                trsym.setTranslation(new Vector3d(halfBodySize, 0, 0));
                trsym.mul(tmptr);
                part0.setJointLocation(trsym);

                part0.halfBodyBox = new Vector3d(0.1, 0.1, 0.1);

                root.addPart(part0);
                part0.joint.momentum = 0;
                part0.joint.setLimit(-1, 1);
                joints.add(part0.joint);
                parts.add(part0);

            }
            BodyPart part1 = new BodyPart("tight " + i);
            {
                Transform3D tmptr = new Transform3D();
                tmptr.rotX(Math.PI / 2);

                part1.setJointLocation(tmptr);
                part1.halfBodyBox = new Vector3d(upLegHalfLength - 0.15, 0.1, 0.1);

                tmptr.setIdentity();
                tmptr.setTranslation(new Vector3d(upLegHalfLength, 0, 0));
                part1.bodyShift.set(tmptr);

                part0.addPart(part1);
                part1.joint.setLimit(-0.5, 1.5);
                joints.add(part1.joint);
                parts.add(part1);

            }

            {
                // lower leg part
                BodyPart part2 = new BodyPart("shin " + i);

                {
                    Transform3D tmptr1 = new Transform3D();
                    tmptr1.setIdentity();
                    tmptr1.setTranslation(new Vector3d(2 * upLegHalfLength, 0, 0));
                    part2.setJointLocation(tmptr1);
                }
                part2.halfBodyBox = new Vector3d(downLegHalfLength, 0.1, 0.1);
                {
                    Transform3D tmptr2 = new Transform3D();
                    tmptr2.setIdentity();
                    tmptr2.setTranslation(new Vector3d(downLegHalfLength, 0, 0));
                    part2.bodyShift.set(tmptr2);
                }
                Site endSite = new Site(part2, new Vector3d(2 * downLegHalfLength, 0, 0));

                part1.addPart(part2);

                sites.add(endSite);
                part2.joint.setLimit(-2, 0);
                joints.add(part2.joint);
                parts.add(part2);


            }
        }
    }

//    JointsConfig getJointsConfig() {
//        return new JointsConfig(joints);
//    }

    void getJoints(VectorNd config) {
//        config.applyValues(joints);
        int i = 0;
        for (Joint joint : joints) {
            config.values[i] = joint.getAngle();
            i++;
        }

    }

    void setJoints(VectorNd config) {
//        config.applyValues(joints);
        int i = 0;
        for (Joint joint : joints) {
            joint.setAngle(config.values[i]);
            i++;
        }

    }

    SitesConfig getSitesConfig() {
        return new SitesConfig(sites);
    }

    void createGraphics(World3D world3d) {
        for (BodyPart part : parts) {
            Part3D part3D = new Part3D(part);
            world3d.addModel(part3D);
        }
    }
}
