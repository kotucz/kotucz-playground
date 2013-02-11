import javax.microedition.lcdui.game.GameCanvas;
/*
 * Doctor.java
 *
 * Created on 9. èervenec 2007, 15:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
public class Doctor extends Unit {
    
    /** Creates a new instance of Doctor */
    public Doctor() {
        super(DOCTOR, 0, 0);
    }
    
    void keys(int keys) {
        
        if (!isAlive()) return;
        
        final boolean
                UP = (keys&GameCanvas.UP_PRESSED)!=0,
                DOWN = (keys&GameCanvas.DOWN_PRESSED)!=0,
                LEFT = (keys&GameCanvas.LEFT_PRESSED)!=0,
                RIGHT = (keys&GameCanvas.RIGHT_PRESSED)!=0,
                FIRE = (keys&GameCanvas.FIRE_PRESSED)!=0;
        
        if (onground) {
            if (RIGHT) vx+=50;
            else if (LEFT) vx-=50;
            else vx/=2;
   
        } else {
            
            if (RIGHT) vx+=30;
            else if (LEFT) vx-=30;
            else vx=vx;
   
            
        }
        
        if (UP) jump(1200);
        
        switch (state) {
            
        }
    }
    
    
    
}
