/*
 * LoadingScreen.java
 *
 * Created on 9. èerven 2006, 16:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mythicalduty;

import kotuc.midp.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author PC
 */
public class LoadingScreen extends Controller {
    
    /** Creates a new instance of LoadingScreen */
    public LoadingScreen() {
    
    }
    
    public void doWork() {
        progress+=vel/20;
        vel++;
/*        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
*/    } 
    
    int progress = 0, vel = 100;
    
    Image image = Game.createImage("/back.png");
    
    Image shade = MyCanvas.createShade(0x22FFCC88);
    
    public void paint(Graphics g) {
        
        if (shade==null) {
            g.setColor(0x00FFFF99);
            g.fillRect(0, 0, MyCanvas.width, MyCanvas.height);
        } else g.drawImage(shade, 0, 0, g.LEFT|g.TOP);
        
        g.setColor(0x00AAAA88);
        g.drawString(Game.getText(14), MyCanvas.width/2, MyCanvas.height/3, g.HCENTER|g.BOTTOM);
        g.drawString(Game.getText(15), MyCanvas.width/2, MyCanvas.height/2, g.HCENTER|g.BOTTOM);
        
        g.drawImage(image, progress%MyCanvas.width, 2*MyCanvas.height/3, g.HCENTER|g.BOTTOM);
    }
    
}
