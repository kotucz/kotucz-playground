import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

public class Revolt extends MIDlet {
	Display display = null;
	RevoltCanvas revoltCanvas = new RevoltCanvas();
	
	public void startApp() {
		display = Display.getDisplay(this);
		display.setCurrent(revoltCanvas);
	}
	
	public void pauseApp() {
	}
	
	public void destroyApp(boolean b) {
	}
}