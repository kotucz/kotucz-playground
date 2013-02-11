package spygame;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

import javax.microedition.media.*;
import javax.microedition.media.control.ToneControl;

import java.lang.*;
import java.util.*;

public class Enemy extends Entity {
        
//        Vector enemies = new Vector();
        
	 Enemy() {
//            health=3;
//            level.enemyCount++;
//            level.enemiesLeft++;
//		setupSkin("/enemy1.png");
            spriteImage = Game.createImage("enemy");

            sprite = new Sprite(spriteImage, 24, 48);	
         }
	
	public Enemy(int x, int y, int dir) {
		this();
		set(x, y);
		setDirection(dir);
	}
	
	boolean agentSpotted() {
		int ax = level.agent.getX(), ay = level.agent.getY(), auy = level.agent.getuY();
		
                for (int x = this.getX(), y = this.getuY(); (level.get(x, y)&Level.SEEABLE)!=0; x+=this.dir) {
			if ((x==ax)&&((y==ay)||(auy==y))) return true; 
		}
                
                for (int x = this.getX(), y = this.getY(); (level.get(x, y)&Level.SEEABLE)!=0; x+=this.dir) {
			if ((x==ax)&&((y==ay)||(auy==y))) return true; 
		}
                return false;
	}

        public void doMove() {
            if (!alive()) keys(false, false, false, false, false);
            if (agentSpotted()) {
                keys(false, false, false, false, true);
            } else {
                int x = this.getX(), y = this.getY();

                if (level.isFree(x+dir, y)) {
                    keys(level.isFree(x+dir, y+1), false, dir==-1, dir==1, false);                   
                } else {
                    setDirection(-1*dir);
                }
                
            }
        }
            
//	public void doWork () {
//
//		System.out.print(this);
//		if (agentSpotted()) {
//			fire();
//                        if (dir==1) goRight(); else goLeft();
//		} else {
//			int x = this.getX(), y = this.getY();
//			
//			boolean noMove = true;
//					
//			if ((dir==1)&&(level.isFree(x+1, y))&&(!level.isFree(x+1, y+1))) {
//				goRight(); 
//				noMove = false;
//			} else {
//				setDirection(-1);
//			} 
//			if ((dir==-1)&&(level.isFree(x-1, y))&&(!level.isFree(x-1, y+1))) {
//				goLeft(); 
//				noMove = false;
//			} else {
//				setDirection(1);
//			}		
//			if (noMove) jump();
//						
//		}
//
////		System.out.print(info());
//		super.doWork();
////		System.out.print("\n");	
//	}
	
	protected void kill () {
//		level.enemiesLeft--;
//		level.enemyCount--;
		Agent.enemiesKilled++;
//		System.out.println("Enemy down");
		//Agent.bonus(100);
		super.kill();
                Game.showMessage("*enemy down "+level.enemyCount+" *left");
	}
	


}
