package rccars;

import javax.microedition.m3g.Group;

/**
 *
 * @author Kotuc
 */
public class RCTrack {
       
    final float GRAV = 1.8f;
       
    final float FRIC = 0.17f; //0.22f; // 0.3f
    
    private Group track;
        
    public RCTrack() {
        trackAdd("/maps/lvl_celek.m3g", 0, 0, 0);

//        trackAdd("/rovinka.m3g", -110, 0, 0);
//        trackAdd("/rovinka.m3g", 0, 0, 0);
//        trackAdd("/most.m3g", 110, 0, 0);
//        trackAdd("/zatacka.m3g", 110, 0, 0);
//        trackAdd("/rovinka.m3g", 220, -150, -90);
//        trackAdd("/zatacka.m3g", 220, -160, -90);
//        trackAdd("/kopecek.m3g", 110, -270, 0);

    }

    public Group getTrack() {
        return track;
    }
        
    
    /**
     * adds the part of level loaded from file
     * to position specified by x, y, rotZ
     * 
     * @param file
     * @param x
     * @param y
     * @param rotZ
     */
    private void trackAdd(String file, float x, float y, float rotZ) {
        track = new Group();
        
        Group roadGr = RCanvas.loadObject(file, 3);
        roadGr.setScale(10, 10, 10);
        roadGr.preRotate(90, 1, 0, 0);

        Group roadMG = new Group();
        roadMG.setTranslation(x, y, 0);
        roadMG.preRotate(rotZ, 0, 0, 1);

        roadMG.addChild(roadGr);
        track.addChild(roadMG);
    }
    
    
    
}
