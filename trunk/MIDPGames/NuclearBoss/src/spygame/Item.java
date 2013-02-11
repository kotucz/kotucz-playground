/*
 * Item.java
 *
 * Created on 28. èervenec 2006, 11:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package spygame;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.game.*;
import java.util.*;

/**
 *
 * @author PC
 */
public class Item extends Entity {
       
    int type;
    
    /** Creates a new instance of Item */
    public Item(int x, int y, int type) {
        set(x, y);
        this.type = type;
        
        sprite = new Sprite(Game.createImage("items"), 24, 48);
        
    }
       
    void updateSprite() {
        sprite.setPosition(this.x, this.y);
        sprite.setFrame(type);
    }
    
    public void doMove() {
        int y = level.agent.getY();
        if ((level.agent.getX()==this.getX())&&
                ((y == this.getY())
                ||(y == this.getuY()))
        ) this.pick(level.agent);
    }
    
    void pick(Entity e) {
        switch (this.type) {
            case 0:
                // medkit 
                e.health+=40;
                this.kill();
                break;
            case 1:
                // mine
                e.injure(50);
                this.kill();
                break;
                
        }
    }
    
}
