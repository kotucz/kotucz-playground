/*
 * ICanvas.java
 *
 * Created on 29. srpen 2006, 14:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import javax.microedition.lcdui.*;

/**
 *
 * @author PC
 */
public abstract class ICanvas extends Canvas {
    
    static int height, width;
    static int border = 5;
    
    /** Creates a new instance of ICanvas */
    public ICanvas() {
        width = getWidth();
        height = getHeight();
    }
    
    public void paintbg(Graphics g) {
        g.setColor(Beans.COLOR3);
        g.fillRect(0, 0, width, height);
        
        g.setColor(Beans.COLOR1);
        g.drawRect(2, 2, width-4, height-4);
        
        g.setFont(Beans.FONT);
        g.drawString(""+info, border, height-border, g.LEFT|g.BOTTOM);
    }
    
    String info = "";
    
    public void setInfo(String info) {
        this.info = info;
    }
    
}
