import javax.microedition.lcdui.Graphics;
/*
 * Market.java
 *
 * Created on 30. srpen 2006, 1:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import javax.microedition.lcdui.*;

/**
 *
 * @author PC
 */
public class Market extends ICanvas {
    
    Store store = new Store(12);
    
    /** Creates a new instance of Market */
    public Market() {
        for (int i = Beans.random(12); i>0; i--) {
            store.put(new Bean(Beans.random(3), Beans.random(3), Beans.random(3), Beans.random(3)));
        }
        setInfo(controls);
    }

    String controls = "2 "+Text.PRODAT+"  3 "+Text.KOUPIT+"  4 "+Text.ZPET;
 
    Player player;
    
    public void paint(Graphics g) {
        paintbg(g);
        
        g.drawString("welcome to the market!", border, border, g.TOP|g.LEFT);
        g.drawString(""+player.money+" €", width-border, border, g.RIGHT|g.TOP);
        store.paint(g, 18);
        
        player.store.paint(g, 65);
    }
    
    int act;
    
    public static int ACT_SELL = 2, ACT_BUY = 3;
    
    public void keyPressed(int key) {
                
        switch (key) {
            case KEY_NUM2:
                act = ACT_SELL;
                setInfo(Text.PRODAT);
            break;    
            case KEY_NUM3:
                act = ACT_BUY;
                setInfo(Text.KOUPIT);
            break;
            case KEY_NUM4:
                backToPlayer();
            break;
        }
        
        int ga = getGameAction(key);
        switch (ga) {
        case RIGHT:
        case LEFT:
        case UP:
        case DOWN:
            if (act==ACT_BUY) store.moveCur(ga);
            if (act==ACT_SELL) player.store.moveCur(ga);
            repaint();
        break;
        case FIRE:
            if (act==ACT_SELL) sell(); 
            if (act==ACT_BUY) buy(); 
            setInfo(controls);
            repaint();
        break;        
        }
             
    }
    
    public int price(Bean b) {
        return b.price();
    }
    
    public void sell() {
        player.money += player.store.remove(player.store.cur).price();        
    }
    
    public void buy() {
        Bean b; 
        player.store.put(b = store.remove(store.cur)); 
        player.money -= b.price();        
    }
    
    public void backToPlayer() {
        Beans.display.setCurrent(player);
    }
    
}
