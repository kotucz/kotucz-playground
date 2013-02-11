/*
 * HCanvas.java
 *
 * Created on 23. bøezen 2007, 17:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



/*
 *
 * @author PC
 */
public class HCanvas extends javax.microedition.lcdui.game.GameCanvas {
    
    public static int width, height;
    
    /** Creates a new instance of HCanvas */
    public HCanvas() {
        super(false);
        setFullScreenMode(true);
        
        width = Device.width;
        height = Device.height;
                
//        width = getWidth();
//        height = getHeight();  
        
    }
    
    public void flushGraphics() {
          paint(getGraphics());
          super.flushGraphics();
//        repaint();
//        serviceRepaints();
    }
    

    
}
