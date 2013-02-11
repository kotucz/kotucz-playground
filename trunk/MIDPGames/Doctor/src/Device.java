/*
 * Device.java
 *
 * Created on 8. èervenec 2007, 22:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
public class Device {
    
    /** Creates a new instance of Device */
    public Device() {
    }
 
    static int width;
    static int height; 
    static String name;
    
    static {
        sek750();
    }
        
    static void sek750() {
        name = "Sony Ericson K750";
        width = 176;
        height = 220;
    }
    
    static void mini() {
        name = "default";
        width = 128;
        height = 128;
    }
    
    
}
