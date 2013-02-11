/*
 * UltimateMainMenu.java
 *
 * Created on 28. èervenec 2006, 13:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package spygame;

import javax.microedition.lcdui.*;

/**
 *
 * @author PC
 */
public class UltimateMainMenu extends AnimatedList {
    
    /** Creates a new instance of UltimateMainMenu */
    public UltimateMainMenu(String[] elements) {
        setItems(elements);
    }
    
    public void itemSelected(int i) {
        
    }
            
    public void paint(Graphics g) {
        int width = getWidth(), height = getHeight();
        
        g.setColor(0);
        g.fillRect(0, 0, width, height);
        
  
        
        
        g.setFont(Game.MENU_FONT);
        for (int i = 0; i<items.length; i++) {
            try {
                if (i==selectedIndex) g.setColor(0x00BB00); else g.setColor(0x00FF00);
                g.drawString(items[i], 5, yShift+i*cellHeight, g.LEFT|g.TOP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (selectedIndex>-1) {
            g.setColor(0x00BB00);
            g.drawLine(0, yShift+selectedIndex*cellHeight, width, yShift+selectedIndex*cellHeight);
            g.drawLine(0, yShift+(selectedIndex+1)*cellHeight, width, yShift+(selectedIndex+1)*cellHeight);
        }
        try {
            
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        repaint();
    }
    
}
