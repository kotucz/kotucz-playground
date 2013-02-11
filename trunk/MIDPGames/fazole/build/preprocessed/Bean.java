/*
 * Bean.java
 *
 * Created on 29. srpen 2006, 14:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import javax.microedition.lcdui.*;

/**
 *
 * @author PC
 */
public class Bean {
    
    /** Creates a new instance of Bean */
    public Bean() {
        gene = new int[] {0, 0, 0, 0};
    }

    public Bean(int g0, int g1, int g2, int g3) {
        gene = new int[] {g0, g1, g2, g3};
    }
    
    
    public Bean(Bean pa, Bean pb) {
        gene = new int[] {1, 0, 1, 0};
    }
    
    public int price() {
        return (gene[0]+1)*100+(gene[1]+1)*300+(gene[2]+1)*120+(gene[3]+1)*400;
    }
    
    int gene[] = new int[4];
    
    int colorsin[] =  {0xFF0000, 0xFFFF00, 0xFF00FF};
    int colorsout[] = {0x00FF00, 0x00FFFF, 0x0000FF};
    
    public void paint(Graphics g, int x, int y) {
        
        g.setColor(colorsin[Math.min(gene[0], gene[1])]);
        g.fillArc(x, y, 10, 10, 0, 360);
                
        g.setColor(colorsout[Math.min(gene[2], gene[3])]);
        g.drawArc(x, y, 10, 10, 0, 360);
        g.drawArc(x+1, y+1, 8, 8, 0, 360);
        
//        g.setColor(0x00FFFFAA);
//        g.drawString("g:"+gene[0]+"-"+gene[1]+"|"+gene[2]+"-"+gene[3], x, y, g.LEFT|g.TOP);
    }
    
}
