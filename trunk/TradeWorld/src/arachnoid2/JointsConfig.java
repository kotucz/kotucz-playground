package arachnoid2;

import java.util.List;

/**
 *
 * @author Kotuc
 */
public class JointsConfig extends VectorNd {
   

    public JointsConfig() {
    }

    public JointsConfig(List<Joint> joints) {
        storeJoints(joints);
    }

    public void storeJoints(List<Joint> joints) {
        values = new double[joints.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = joints.get(i).getAngle();
        }
    }

    public void applyValues(List<Joint> joints) {
        for (int i = 0; i < values.length; i++) {
            joints.get(i).setAngle(values[i]);
        }
    }
}
