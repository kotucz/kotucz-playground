package tradeworld.graphics;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupOnElapsedFrames;

/**
 *
 * @author Kotuc
 */
public class ModelUpdater extends Behavior {

    private final Model3D model;

    public ModelUpdater(Model3D model) {
        this.model = model;
    }
    private WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);

    @Override
    public void initialize() {
        wakeupOn(w);
    }

    @Override
    public void processStimulus(Enumeration arg0) {
        model.refresh();
        wakeupOn(w);
//            if (isAlive()) {
//                wakeupOn(w);
//            } else {
//                kill();
//            }
        }
}
