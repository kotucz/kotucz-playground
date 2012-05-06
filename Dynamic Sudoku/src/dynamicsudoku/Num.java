/*
 * Num.java
 *
 * Created on 25. únor 2007, 18:29
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
public class Num extends Dot {
    
    int value;
    
    int pos=-1;
    
    /** Creates a new instance of Num */
    public Num(int n) {
        this.value = n;
    }
    
    void setPos(int p) {
        this.pos = p;
        fx = 15 + SBoard.getX(p%9);
        fy = 27 + SBoard.getY(p/9); 
    }
    
    public void paint(Graphics g) {
        int s = 10;
        if (x<fx) x+=s;
        if (x>fx) x-=s;
        if (y<fy) y+=s;
        if (y>fy) y-=s;
        g.drawString(""+value, (int)x, (int)y);
    }
    
}
