/*
 * Beans.java
 *
 * Created on 29. srpen 2006, 14:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.util.*;

/**
 *
 * @author PC
 */
public class Beans extends MIDlet {
    
    /** Creates a new instance of Beans */
    public Beans() {
        midlet = this;
        display = Display.getDisplay(this);
    }
    
    static Display display;    
    static Beans midlet;    
    static Player player;
    static Market market;
    
    public void startApp() {
        display.setCurrent(new Menu());
    }
    
    public void pauseApp() {
        
    }

    public static final int COLOR1 = 0x00FFFFAA, COLOR2 = 0x00AAAAAA, COLOR3 = 0x00221111; 
    public static final Font FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL); 
    
    public void destroyApp(boolean b) {
        // todo save
        display.setCurrent((Displayable)null);
        println("game quit");
    }

    public static void quitGame() {
        midlet.destroyApp(true);
        midlet.notifyDestroyed();
    } 

    public static void println(String s) {
        System.out.println(s);
    }
    
    private static Random randomGenerator = new Random(System.currentTimeMillis());
    
    public static int random(int i) {
        if (i<2) return 0;
        return Math.abs(randomGenerator.nextInt()%i);
    }
    
}

