/*
 * Vert.java
 *
 * Created on 25. únor 2007, 20:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dynamicsudoku;

import java.awt.*;
import javax.vecmath.*;

/**
 *
 * @author PC
 */
public class Vert extends Dot {
    
    /** Creates a new instance of Vert */
    public Vert() {
    }
    
    float ax, ay;
    
    public void paint(Graphics g, boolean selected) {
        x+=ax;
        y+=ay;
        if (selected) g.setColor(Color.RED); else g.setColor(Color.GREEN); 
        super.paint(g);
    }
    
    public void apply(Vert v2) {
        Vector2f dir = new Vector2f(v2);
        dir.sub(this);
        
        float dirs = dir.length();
        
        dir.scale(1f/(dirs));
        
        if (dirs>100) dir.scale(50000f/(dirs*dirs));
        else dir.scale(-0.0003f*((120-dirs)*(120-dirs)));
        
        ax += dir.x;
        ay += dir.y;
            
    }
    
}
