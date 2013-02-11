/*
 * Menu.java
 *
 * Created on 29. srpen 2006, 14:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import javax.microedition.lcdui.*;

/**
 *
 * @author PC
 */
public class Menu extends ICanvas {
    
    /** Creates a new instance of Menu */
    public Menu() {
        
    }
    
    public void paint(Graphics g) {
        g.setColor(Beans.COLOR3);
        g.fillRect(0, 0, width, height);
        
        g.setFont(Beans.FONT);
        g.setColor(Beans.COLOR1);
            
        g.drawString("1 "+Text.NOVA_HRA, 50, 50, g.LEFT|g.BASELINE);
        g.drawString("2 "+Text.KONEC, 50, 62, g.LEFT|g.BASELINE);
        
    }
    
    public void keyPressed(int key) {
        switch (key) {
        case KEY_NUM1:
            newGame();
        break;
        case KEY_NUM2:
            Beans.quitGame();
        break;
        
        }
    }
    
    public void newGame() {
        Beans.display.setCurrent(Beans.player = new Player());
    }
    
}
