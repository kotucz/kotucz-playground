/*
 * AnimationManager.java
 *
 * Created on 28. kvìten 2006, 13:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package kotuc.midp;

import java.util.*;

/**
 *
 * @author PC
 */
public class AnimationManager {
    
    private Vector animations;
    
    private Pos trans = new Pos();
    
    /** Creates a new instance of AnimationManager */
    public AnimationManager() {
        animations = new Vector();
    }
    
    public void addAnimation(Animation a) {
        animations.addElement(a);
        a.manager = this;
    }
    
    public Pos getTrans() {
        return trans;
    }

    public void setTrans(int x, int y) {
        this.trans.x = x;
        this.trans.y = y;
    }
     
    
    
    public void paint(javax.microedition.lcdui.Graphics g) {
        for (Enumeration e = animations.elements() ; e.hasMoreElements() ;) {
        try {
//                 Game.error("animation");
            Animation a = (Animation)e.nextElement();
            if (a.alive()) 
                a.paint(g);
            else {
                animations.removeElement(a);
                a.finished();
            }
        } catch (Exception ex) {ex.printStackTrace();}; 
        }

    }

    /*
        for (Enumeration e = animations.elements() ; e.hasMoreElements() ;) {
        try{
            Animation a = (Animation)e.nextElement();
        
            if (a.alive) 
                a.act0();
            else animations.removeElement(a);
        } catch (Exception ex) {ex.printStackTrace();}; 
        }
    */



}
