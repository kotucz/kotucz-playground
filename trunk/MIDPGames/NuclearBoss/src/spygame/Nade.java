/*
 * Nade.java
 *
 * Created on 27. èervenec 2006, 16:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package spygame;

import java.util.*; 
import javax.microedition.lcdui.game.*;

/**
 *
 * @author PC
 */
public class Nade extends Shot {
    
    /** Creates a new instance of Nade */
    public Nade(Entity s) {
        super(s);
        
	spriteImage = Game.createImage("nade");
        sprite = new Sprite(spriteImage, 48, 48);	
        sprite.defineReferencePixel(24, 25);
        sprite.setFrameSequence(new int [] {0, 1, 2, 0, 1, 2, 3, 4 ,4 ,5 });
        
        this.x = shooter.x+12;
	this.y = (shooter.crouching()?36:12) + shooter.y;
        this.vx = 8*s.dir;
        this.vy = -4;
        reloading = 55;
    }
    
    public void doMove() {               
        if (state>5) return;
        
        reloading--;
// physics        
        this.x += vx;
        if (!validPos()) {
            this.x -= vx;
            vx=-vx/2;
        }
        this.y += ++vy;
        if (!validPos()) {
            this.y -= vy;
            vx=3*vx/4;
            vy=-1*vy/2;
        }
         
        
        if (reloading>0) return;
// else detonate        
        
		for (Enumeration e = level.entities.elements() ; e.hasMoreElements() ;) {
                    Entity ent = (Entity)e.nextElement();
// checks if has hit any enemy                    
                    if (ent instanceof Shot) continue;
// if hits
                    System.out.println(this+" hits "+ent);
                    ent.injure(Math.max(210-Math.abs(this.x+12-ent.x)-Math.abs((this.y-24)-ent.y), 0));
                    if (ent.health<1) System.out.println(ent+" was shot by "+this);
//                  Game.showMessage(ent+" was shot by "+shooter);
                    if (shooter instanceof Agent) Agent.shotsHitted++;
                    
		}
		
                state = 6;
       
	} 	
 
        boolean validPos() {
            return level.isXYFree(x +4 , y);
        }
        
        public String toString() {
            return shooter+"'s nade";
        }
        
}
