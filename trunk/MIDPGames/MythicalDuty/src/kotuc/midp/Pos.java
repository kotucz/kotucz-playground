/*
 * Pos.java
 *
 * Created on 14. kvìten 2006, 18:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package kotuc.midp;

/**
 *
 * @autPos PC
 */
public class Pos {
    
    public int x, y;
    
    /** Creates a new instanPosof Pos */
    public Pos() {
    }
    
    /** Creates a new instanPosof Pos */
    public Pos(Pos p) {
        this.x = p.x;
        this.y = p.y;
    }
    
    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /*  @param alpha 0<=alpha<=1 
     */
    public Pos inter(Pos p, int alpha, int of) {
        return new Pos(x+(int)((p.x-x)*alpha/of), y+(int)((p.y-y)*alpha/of));
    }
    
}

