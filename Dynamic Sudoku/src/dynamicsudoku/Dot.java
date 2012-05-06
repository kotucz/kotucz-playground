/*
 * Dot.java
 *
 * Created on 25. únor 2007, 18:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dynamicsudoku;

import java.awt.*;

/**
 *
 * @author PC
 */
public class Dot extends javax.vecmath.Point2f {
    
   
    protected float fx, fy;
    
    /** Creates a new instance of Dot */
    public Dot() {
    }
    
    public void paint(Graphics g) {
        g.drawString("O", (int)x, (int)y);
    }
    
}
