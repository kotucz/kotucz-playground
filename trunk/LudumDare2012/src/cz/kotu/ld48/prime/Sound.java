package cz.kotu.ld48.prime;

import javax.sound.sampled.Clip;

/**
* @author Kotuc
*/
class Sound {
    private Clip clip;

    public Sound(Clip clip) {
        this.clip = clip;
    }

    void play() {
        // TODO volume mute
//            if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();   // Stop the player if it is still running
            clip.setFramePosition(0); // rewind to the beginning
            clip.start();     // Start playing
//            }
    }
}
