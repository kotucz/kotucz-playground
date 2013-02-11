import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class ship extends MIDlet 
  {

    Display display;
    MyCanvas mc;        
    Thread mcT;


    public void startApp ()  
      {
        display = Display.getDisplay(this);

        mc = new MyCanvas(this);   
        mcT = new Thread(mc);    // novy thread pro MyCanvas
 
        mc.repaint();
        display.setCurrent(mc);  
        mcT.start();  // Spust thread

      }

    public void konec() {}

    public void pauseApp() {}

    protected void destroyApp(boolean unconditional) 
      {
         mc.stop = true;  // zastav tread
      }
  }