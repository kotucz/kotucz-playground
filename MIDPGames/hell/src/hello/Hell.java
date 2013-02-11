/*
 * Hell.java
 *
 * Created on 17. zברם 2006, 9:23
 */

package hello;

import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author  PC
 * @version
 */
public class Hell extends MIDlet {
    
    static Display display;
    
    static Menu menu; 
    
    static FlyCanvas fc;

//    static int level;
    
    static Hell midlet;

    
    public static void showMenu(int scrn) {
        menu.screen = scrn;
        display.setCurrent(menu);
        menu.flushGraphics();
    }
        
    public static void loadLevel(int levelid) {
        
       fc = new FlyCanvas(levelid);
                    
       
        
    }
    
    
    public void startApp() {
        midlet = this;
        
        display = Display.getDisplay(this);
        
        menu = new Menu();
            
        showMenu(Menu.LOGO);
        
        new Thread(menu).start();
        
    }
    
    public void pauseApp() {
        
    }
    
    public void destroyApp(boolean unconditional) {
        display.setCurrent(null);
        notifyDestroyed();
    }
        
    
    public static void play() {
        display.setCurrent(fc);
        fc.start();
    }

    public static void exit() {
        midlet.destroyApp(true);
    }
    
    public static void println(String text) {
        System.out.println(text);
    }
    
}
