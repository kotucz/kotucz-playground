package spygame;

import java.io.*;
import java.util.*;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

import javax.microedition.media.*;
import javax.microedition.media.control.ToneControl;
import spygame.*;

public class Entity {
	
//        public int[] WALKING_SEQUENCE = {0, 0,  1, 1, 2, 2, 3, 3};
    
        Level level;
    
	int reloadingPeriod = 12;
	
        protected Sprite sprite;
	protected Image spriteImage;
	
	protected int x=200, y=200, vx = 0, vy = 0;
	
/**
 *  health is percenual
 */        
	protected int health;
		
	public int getX() {
		return (x+level.cell/2)/level.cell;
	}
	
	public int getY() {
		return 1+(y+level.cell/2)/level.cell;
	}
        
        public int getuY() {
		return (crouching?1:0) + (y+level.cell/2)/level.cell;
	}
	
	public void set(int x, int y) {
		this.x = x*Level.cell;
		this.y = (y-1)*Level.cell;
	}
	
	
	
	public Entity() {
 //           super(null, 1, 1);
            health = 100;
	
                
            System.out.println("new "+this);
///             System.out.println("entities = "+entities);
	}
	
	public void setupSkin(String imagename) {
            spriteImage = Game.createImage(imagename);
            sprite = new Sprite(spriteImage);	
        }
	
        final static int
                DEAD = 0+3,
                WALK = DEAD+4, 
                CROUCH = WALK+4,
                JUMP = CROUCH+2,
                FALL = JUMP+1, 
                LADDER = FALL+4, 
                REMOTE = LADDER+1,
                ON_WALL = REMOTE+1,
                CLIMB = ON_WALL+4;
        
        int state = WALK;
    
        public void go () {
            this.x += dir*4;
            if (!validPos()) {
                this.x -= dir*4;
                if (onWall()) {
                    return;
//                    state = ON_WALL;
                }
            } else if (!onFloor()) {
                state = FALL;
            }
            
        }
        
        boolean onWall() {
            int x = getX(), y = getY();
            if (((level.get(x+dir, y-2)&level.FREE)!=0)
            &&((level.get(x+dir, y-1)&level.FREE)==0)) {
                set(x, y);
                state = ON_WALL;
                vy=-1;
                return true;
            } else return false;
        }
        
	boolean onFloor = false;
        boolean crouching = false;
        
        boolean crouching() {
            return crouching||(state==CROUCH);
        }
        
	public void doMove() {
		// should never happend because it causes infinite loop 
                // protection against stucking
                if (!validPos()) {
                    System.err.println(this + ": invalid pos");
                    return;
                }

//		this.x += vx;
//		if (validPos()) {
//			if (vx!=0) sprite.nextFrame();
//		} else {
//			this.x -= vx;
//			this.vx = 0;
//			going = false;
//		}
//		
//		this.y += vy;
//		if (validPos()) {
//			if (!onFloor) vy++;
//			onFloor = false;
//		} else {
//			while (!validPos()) {
//				if (vy>0) this.y--; else this.y++;
//			};
//			if (vy>0) onFloor = true;
////			this.y -= vy;
//			vy = 1;
////			System.out.print(" invalid pos y ");
////			System.err.print(this+": on the floor");
//		}
		
	} 	
        
       void keys(final boolean UP, final boolean DOWN, final boolean LEFT, final boolean RIGHT, final boolean FIRE) {
//       final boolean 
//               UP = (keyState&GameCanvas.UP_PRESSED)!=0, 
//               DOWN = (keyState&GameCanvas.DOWN_PRESSED)!=0,
//               LEFT = (keyState&GameCanvas.LEFT_PRESSED)!=0,
//               RIGHT = (keyState&GameCanvas.RIGHT_PRESSED)!=0,
//               FIRE = (keyState&GameCanvas.FIRE_PRESSED)!=0;
       
       if (RIGHT) setDirection(1);
       if (LEFT) setDirection(-1);
       
       lab0: switch (state) {
//           case STAY:
           case DEAD:
                kill();
               break;
           case WALK:

               
               
               if (DOWN) state = CROUCH-3;
               else
               if (UP) {
//                   jump();
                   this.vy = -11;
                   state = JUMP-1;
                   break;
               }
               else
               if (FIRE) fire();
               else
               if (RIGHT||LEFT) {
                   state = WALK-3;
                   go();
               }
               
           break;
           
           case WALK-1:
           case WALK-2:
           case WALK-3:
                             
               state++;
               go();
                                          
               
           break;
           case JUMP-1:
           case JUMP:
               state++;
               if (RIGHT||LEFT) go();
           break;
           case FALL:
               
               if (RIGHT||LEFT) go();
               else if (UP) if (ladder()) {
                   state=LADDER-3;
                   return;
               }
               if (FIRE) fire();
               
               vy++;
                              
               for (int i = 0; i>vy; i--) {
                   this.y--;
                   if (!validPos()) {
                       this.y++;
                       vy = 0;
                       break lab0;
                   }
               }
               
               for (int i = 0; i<vy; i++) {
                   if (onFloor()) {
                       state = CROUCH-3;
                       vy = 0;
                       break lab0;
                   }
                   this.y++;
               }   
                
            break;
           case CROUCH:
                             
               if (!DOWN) state = WALK;
               
               if (FIRE) fire();
                      
            break;

                       case LADDER:
           case LADDER-1:
           case LADDER-2:
           case LADDER-3:
                         
               if (DOWN) {
                   state = FALL;
                   break;
               }
               
               
               if (UP) if (ladder()) {
                   if (state==LADDER) state = LADDER-3;
                   else state ++;
               }
               
               if (RIGHT||LEFT) {
                   if (UP) vy=-6;
                   go();
               }
                              
            break;
           case ON_WALL:
               if (UP) {
                   state=CLIMB-3;
               } else
               if (DOWN) {
                   state = FALL;
                   break;
               } else
               if (RIGHT||LEFT) {
                   //this.y-=3;
// so can fall                   
                   go();
               }; 
            break;
//           case CLIMB-5:
//           case CLIMB-4:
              
           case CLIMB:
               this.y-=24;
               this.x+=dir*4;
               state=CROUCH-3;
           break;
           case CLIMB-1:
           case CLIMB-2:    
           case CLIMB-3:
               this.y-=8;
               state++;
           break;
           default:
               state++;
       }
       
       /* 
       onLadder = level.is(getX(), getY(), Level.LADDER);
//        System.out.println("ladder "+onLadder+" "+level.get(getX(), getY()));        
        if (UP) {
		up();
        } else if (DOWN) {
		down();
        } else if (onLadder) {
            vy = 0;
        }
        
        if (LEFT&&RIGHT) {
            if (onLeftWall) {
                vy=-6;
                vx=8;
                dir = 1;
            } else if (onRightWall) {
                vy=-6;
                vx=-8;
                dir = -1;
            } else if (dir == 1) goRight(); else goLeft();
        } else if (LEFT) {
            goLeft();
        } else if (RIGHT) {
            goRight();
        } else if (FIRE) {
            fire();
        }*/
/*
        
        if (DOWN) {
          if (onFloor) crouching = true;  
        } 
        if (UP) {
            if (onFloor) jump();
            if (onLeftWall||onRightWall||onLadder) {
                this.vy=-4;
            }
        } 
        if (RIGHT&&LEFT) {
            
        }
        if (RIGHT) {
            vx = 4;
            onLeftWall = false;
            dir = 1;
            going = true;
        } 
        if (LEFT) {
            vx = -4;
            onRightWall = false;
            dir = -1;
            going = true;
        }

        if (FIRE) fire();
  */      
    }
        
    public boolean ladder () {
        if (level.is(getX(), getY(), Level.LADDER)
        ||level.is(getX(), getY()-1, Level.LADDER)) {
            this.x=level.cell*getX();
//              state = LADDER;
              if (vy==0) {
                  this.y-=3;
                  if (!validPos()) this.y+=3;
              } else
              vy = 0;
              return true;
        }
        return false;
    }
    
    public void jump () {
         this.vy = -11;
         state = FALL;
    }

        public void down () {
	//	if (onFloor) crouching = true;
        }
        
//        boolean going = false;
//        
//      
//	public void goLeft () {
//		setDirection(-1);
//		this.vx = -3;
//	}
//
//	public void goRight () {
//		setDirection(1);
//		this.vx = 3;
//        }
	
	int reloading = 5;

	public void fire () {
		if (this.reloading>0) return;
//		if (going) return;
                System.out.println(this+": fire");
		level.add(new M16Shot (this));
		this.reloading = reloadingPeriod;  
 	}
 	
 	public void injure (int damage) {
 		this.health -= damage;
 		System.out.println(this+" "+health+"hp left");
 		if (this.health<1) this.state=0;
 	}
 	
/*	
	static final int DIR_RIGHT = 1;
	static final int DIR_LEFT = -1;
*/	
	protected int dir = 1;
	
	void setDirection (int dir) {
		this.dir=dir;
                
                if (dir==1) sprite.setTransform(Sprite.TRANS_NONE); 
                else sprite.setTransform(Sprite.TRANS_MIRROR);
	}
	
	/**
     * Update the Sprite location from the board supplied position
     * @param dir the sprite is moving
     */
    void updateSprite() {
//    	if (dir==-1) sprite.setTransform(Sprite.TRANS_MIRROR);
//	if (dir==1) sprite.setTransform(Sprite.TRANS_NONE);
	sprite.setPosition(this.x, this.y);
        sprite.setFrame(state);
     
        
    }

    boolean validPos () {
//		System.out.println(this+"verifying pos: "+!sprite.collidesWith(level.tiles, true));
//		return !sprite.collidesWith(level.tiles, true);
            Level l = level;
            
            return ((crouching()||
                  (l.isXYFree(x+0, y+23)
                &&l.isXYFree(x+23, y+23)
                          
		&&l.isXYFree(x+0, y+0)
		&&l.isXYFree(x+23, y+0)))
                          
                &&l.isXYFree(x+0, y+24)
		&&l.isXYFree(x+23, y+24)
                          
                &&l.isXYFree(x+0, y+47)
		&&l.isXYFree(x+23, y+47)
            );
	}

        boolean onFloor () {
   
            return !(
                  level.isXYFree(x+0, y+48) //pixels under sprite
		&&level.isXYFree(x+23, y+48)
            );
	}
	
        
	public void doWork () {
		doMove();
		
		updateSprite();
		
                reloading--;
	}

	public String info () {
		return "["+getX()+"+"+dir+","+getY()/*+"]:["+vx+","+vy+"]:"+onFloor+":"+reloading*/+"]"+health;
	}
	
	protected void kill () {
//		entities.removeElement(this);
//		SpyCanvas.layers.remove(sprite);
            // TODO sprite.die
            level.kill(this);
            System.out.println(this+": was killed");
	}
	
    void bonus(int b) {
	// do nothing except Agent
    }

    boolean alive() {
        return state>DEAD;
    }
    
}
