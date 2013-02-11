/*
 * Store.java
 *
 * Created on 29. srpen 2006, 22:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import javax.microedition.lcdui.*;

/**
 *
 * @author PC
 */
public class Store extends ICanvas {
    
    /** Creates a new instance of Store */
    public Store(int size) {
         content = new Bean[size];
    }
    
    public int size() {
        return content.length;
    }    
    
    Bean[] content;
    
    int wrap = 6;
    
    public void put(Bean b) {
        for (int i=0; i<content.length; i++) {
            if (get(i)==null) {
                content[i]=b;
                break;
            }
        }   
    }
    
    public Bean get(int i) {
        if (i<0||i>=content.length) return null;
        return content[i];
    }

    public Bean selected() {
        return get(cur);
    }
    
    public Bean remove(int i) {
        Bean b = content[i];
        content[i] = null;
        return b;
    }
    
    int cur;
    
    public void paint(Graphics g, int y) {
                   
        g.setFont(Beans.FONT);
        g.setColor(Beans.COLOR1);
        
        g.drawRect(border, y, width-2*border, 12);
        g.drawString(Text.SKLADISTE, border+1, y, g.LEFT|g.TOP);
        if (get(cur)!=null) g.drawString(""+get(cur).price()+" €", width-border, y+10, g.RIGHT|g.BASELINE);
        
        g.setColor(Beans.COLOR1);
            
        for (int i = 0; i<content.length; i++) {
            
            if (cur==i) {
                g.setColor(Beans.COLOR2);
            } else g.setColor(Beans.COLOR1);
            
            g.drawRect(border+(i%wrap)*12, y+14+12*(i/wrap), 10, 10);
            try {
                get(i).paint(g, border+(i%wrap)*12, y+14+12*(i/wrap));
            } catch (NullPointerException ex) {
            }
            
        }
            
    }
    
    protected void paint(Graphics g) {
        paintbg(g);
        paint(g, border);
    }
    
    public void moveCur(int ga) {
    switch (ga) {
        case RIGHT:
            cur++;
        break;
        case LEFT:
            cur--;
        break;
        case UP:
            cur-=wrap;
        break;
        case DOWN:
            cur+=wrap;
        break;
    }
    }
    
}
