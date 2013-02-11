package rccars;

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;

/**
 * 
 * @author Kotuc
 */
public class RCMIDlet extends MIDlet {

    Display display = null;
    RCanvas revoltCanvas = new RCanvas();

    public void startApp() {
        display = Display.getDisplay(this);
        display.setCurrent(revoltCanvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean b) {
    }
}
