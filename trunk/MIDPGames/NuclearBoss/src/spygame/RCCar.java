/*
 * RCCar.java
 *
 * Created on 18. èervenec 2006, 16:50
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
public class RCCar extends Shot {
        
    /** Creates a new instance of RCCar */
    public RCCar(Agent e) {
        super(e);
        this.sprite = new Sprite(Game.createImage("rc"), 24, 48);
        this.x=e.x;
        this.y=e.y;
        e.rccar = this;
        e.state = e.REMOTE;
        state = 0;
    }
    
//    public void keys(int keyState) {
//        final boolean 
//           UP = (keyState&GameCanvas.UP_PRESSED)!=0, 
//           DOWN = (keyState&GameCanvas.DOWN_PRESSED)!=0,
//           LEFT = (keyState&GameCanvas.LEFT_PRESSED)!=0,
//           RIGHT = (keyState&GameCanvas.RIGHT_PRESSED)!=0,
//           FIRE = (keyState&GameCanvas.FIRE_PRESSED)!=0;

      
    void keys(final boolean UP, final boolean DOWN, final boolean LEFT, final boolean RIGHT, final boolean FIRE) {    
        
        if (state>0) return;
        
        if (LEFT) setDirection(-1);
        if (RIGHT) setDirection(1);
        
        if (RIGHT||LEFT) {
            go();
            go(); 
            state=0;
            
        }
        
        vy++;

        for (int i = 0; i<vy; i++) {
            if (onFloor()) {
                vy = 0;
                break;
            }
            this.y++;
        }

               
        if (UP) {
// destroys itself anyway            
            state=1;
            
            for (Enumeration e = level.entities.elements() ; e.hasMoreElements() ;) {
                    Entity ent = (Entity)e.nextElement();
// checks if has hit any enemy                    
                    if (ent instanceof Shot) continue;
        	
// if hits
                    System.out.println(this+" hits "+ent);
                    ent.injure(Math.max(120-Math.abs(this.x-ent.x)-Math.abs(this.y-ent.y), 0));
                    if (ent.health<1) System.out.println(ent+" was shot by "+this);
//                  Game.showMessage(ent+" was shot by "+shooter);
                    if (shooter instanceof Agent) Agent.shotsHitted++;
                    
            }
        }
        
    }
       
    void updateSprite() {
        if (dir==-1) sprite.setTransform(Sprite.TRANS_MIRROR);
	if (dir==1) sprite.setTransform(Sprite.TRANS_NONE);
	sprite.setPosition(this.x, this.y);
        if (state>5) {
            kill();
            return;
        }
        if (state>0) sprite.setFrame(state++);
        
    }
    
    public void kill() {
        ((Agent)shooter).state = WALK;
        ((Agent)shooter).rccar = null;
        super.kill();
    } 
    
//    boolean validPos () {
//        return (  level.isXYFree(x+0, y+24)
//		&&level.isXYFree(x+23, y+24)
//                          
//                &&level.isXYFree(x+0, y+47)
//		&&level.isXYFree(x+23, y+47)
//            );
//    }
     
//    protected void kill () {
//	// to do
//    }
    
    public void doMove() {
        
    }
    
    public String toString() {
        return shooter+"'s RC car";
    }
    
}
