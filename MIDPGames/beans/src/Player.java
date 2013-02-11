/*
 * Player.java
 *
 * Created on 29. srpen 2006, 15:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author PC
 */
public class Player extends ICanvas {
    
    int money = 1000;
            
    Store store = new Store(18);
    
    /** Creates a new instance of Player */
    public Player() {
        store.put(new Bean());
        store.put(new Bean(0, 1, 0, 1));
        
        setInfo(controls);
    }
    
//    private int cur = 3;
    
    private int act = 0;
    
    public static int ACT_SELL = 2, ACT_CROSS = 1, ACT_BUY = 3;
    
    public void paint(Graphics g) {
        
        paintbg(g);
        
        g.drawString("player 1", border, border, g.LEFT|g.TOP);
        g.drawString(""+money+" €", width-border, border, g.RIGHT|g.TOP);
         
        
        g.setColor(Beans.COLOR1);
            
        g.drawRect(5+1*12, 25, 10, 10);
        g.drawRect(5+2*12, 25, 10, 10);
        if (field1a!=null) field1a.paint(g, 5+1*12, 25);
        if (field1b!=null) field1b.paint(g, 5+2*12, 25);
        
        store.paint(g, 50);
            
    }
    
           
    Bean field1a, field1b;
        
    public void cross() {
        if (field1a==null) {
// put bean into field 
            field1a=store.remove(store.cur);
        } else if (field1b==null) {
// put bean into field 
            field1b=store.remove(store.cur);
        } 
        if (field1b!=null) {
            
//            store.put(new Bean(field1a, field1b)); 
            
            store.put(new Bean(field1a.gene[0], field1b.gene[0], field1a.gene[2], field1b.gene[2]));
            store.put(new Bean(field1a.gene[0], field1b.gene[1], field1a.gene[3], field1b.gene[2]));
            store.put(new Bean(field1a.gene[1], field1b.gene[0], field1a.gene[2], field1b.gene[3]));
            store.put(new Bean(field1a.gene[1], field1b.gene[1], field1a.gene[3], field1b.gene[3]));

            field1a = null;
            field1b = null;
            
            act = 0;
            setInfo(controls);
        }
    }
    
    String controls = "1 "+Text.KRIZIT+"  3 "+Text.OBCHOD;
    
    public void keyPressed(int key) {
        
        switch (key) {
            case KEY_NUM1:
                act = ACT_CROSS;
                setInfo(Text.VYBER_FAZOLE);
                repaint();
            break;
            case KEY_NUM2:

            break;   
            case KEY_NUM3:

                visitMarket();
            break; 
        }
        
        int ga = getGameAction(key);
        switch (ga) {
        case RIGHT:
        case LEFT:
        case UP:
        case DOWN:
            store.moveCur(ga);
            repaint();
        break;
        case FIRE:
            if (act==ACT_CROSS) cross();
            
            repaint();
        break;        
        }
        
    }
    
    public void visitMarket() {
        if (Beans.market == null) Beans.market = new Market();
        Beans.market.player = this;
        Beans.display.setCurrent(Beans.market);
    }
       
    
}
