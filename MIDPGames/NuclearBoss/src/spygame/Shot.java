package spygame;

import java.io.*;
import java.lang.*;
import java.util.*;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

import javax.microedition.media.*;
import javax.microedition.media.control.ToneControl;
import spygame.*;



public abstract class Shot extends Entity {

	protected Entity shooter;
	
        protected int damage = 20;
        
	protected Shot(Entity shooter) {
                sprite = new Sprite(Game.createImage("shot2"), 8, 6);	
                sprite.setFrameSequence(new int [] {0, 0, 1, 1, 1, 1, 2, 2 ,2 ,2 });
            
                state = 0;
       
                this.x = shooter.x+12+shooter.dir*4;
		this.y = (shooter.crouching()?36:12) + shooter.y;
		this.shooter = shooter;			
		this.setDirection(shooter.dir);
		this.vx = shooter.dir*8;
		if (shooter instanceof Agent) Agent.shotsFired++;	
                Game.canvas.play(SpyCanvas.shotTune);
        }
	
        void updateSprite() {
            sprite.setFrame(state);
            if (state==5) state = 3;
            else state++;
            if (state==10) this.kill();
    	    sprite.setRefPixelPosition(this.x, this.y);
        }
        
	public void doMove() {               
            if (state>5) return;
            
                            
		this.x += vx;
		
		for (Enumeration e = level.entities.elements() ; e.hasMoreElements() ;) {
                    Entity ent = (Entity)e.nextElement();
// checks if has hit any enemy                    
                    if ((ent instanceof Shot)||(ent.equals(shooter))||((shooter instanceof Enemy)&&(ent instanceof Enemy))||(!ent.alive())||(!ent.sprite.collidesWith(this.sprite, true))) continue;
        	
// if hits
                    System.out.println(this+" hits "+ent);
                    ent.injure(damage);
                    if (ent.health<1) System.out.println(ent+" was shot by "+this);
//                  Game.showMessage(ent+" was shot by "+shooter);
                    if (shooter instanceof Agent) Agent.shotsHitted++;
                    
                    this.state=6;
		}
		
		if (!validPos()) {
                    this.kill();
                }
            
        } 	
        
        
        boolean validPos() {
            return level.isXYFree(x +4 , y);
        }
        
        boolean alive() {
            return false;
        }
        
        public String toString() {
            return shooter+"'s bullet";
        }
        
        
}

class GunShot extends Shot {

        public GunShot(Entity shooter) {
            super(shooter);
                sprite.setImage(Game.createImage("shot1"), 14, 10);	
                sprite.defineReferencePixel(7, 5);
            damage = 35;    
        }
       
        public String toString() {
            return shooter+"'s bullet";
        }
 
        
        
}

class M16Shot extends Shot {

        public M16Shot(Entity shooter) {
            super(shooter);
                sprite.setImage(Game.createImage("shot2"), 8, 6);	
                sprite.defineReferencePixel(4, 3);
            damage = 20;
        }
       
        public String toString() {
            return shooter+"'s bullet";
        }
       
        
}

