/*
 * Hell.java
 *
 * Created on 17. zברם 2006, 9:23
 */



import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author  PC
 * @version
 */
public class Game extends MIDlet {
    
    static Display display;
    
    static Menu menu; 
    
    static Level level;

//    static int level;
    
    static Game midlet;

    
    public static void showMenu(int scrn) {
        menu.screen = scrn;
        display.setCurrent(menu);
        menu.flushGraphics();
    }
        
    public static void loadLevel(int levelid) {
        
       level = new Level(levelid);
                    
       
        
    }
    
    
    public void startApp() {
        midlet = this;
        
        display = Display.getDisplay(this);
        
        menu = new Menu();
            
        showMenu(Menu.LOGO);
        
        new Thread(menu).start();
        
    }
    
    public void pauseApp() {
        // todo application paused
        
    }
    
    public void destroyApp(boolean unconditional) {
        display.setCurrent(null);
        notifyDestroyed();
    }
        
    public static void play() {
        display.setCurrent(level);
        level.start();
    }

    public static void exit() {
        midlet.destroyApp(true);
    }
    
    public static void println(String text) {
        System.out.println(text);
    }
    
}
