package spygame;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

import javax.microedition.media.*;
import javax.microedition.media.control.ToneControl;
import spygame.*;

public class Agent extends Entity {
	
    static String name = "Tim";
    static int score;
    static int lives; 
	
	/**
	 * Agent
	 *
	 *
	 */
    public Agent() {
            System.out.println("creating Agent");
		
/*		if (level.agent!=null) {
			System.out.println("there is already one agent");
//			return;
		}
*/		
//          shotsFired = 0;
//          shotsHitted = 0;
                
            reloadingPeriod = 3;

            spriteImage = Game.createImage("agent");

            sprite = new Sprite(spriteImage, 24, 48);	

//           sprite.setFrameSequence(new int[] {5, 5, 5, 5, 0, 1, 2, 3, 5, 5, 5, 5, 7, 6, 6, 6, 6});
	
    }

    static int enemiesKilled = 0;
    static int shotsFired = 0;
    static int shotsHitted = 0;
        
//    boolean onLeftWall = false;
//    boolean onRightWall = false;
//    boolean onLadder = false;

       
 
    
    RCCar rccar; 
    
    int weapon = 0;
    
    final static byte 
        GUN = 0,
        M16 = 1,
        NADE = 2,    
        RC = 4;
       
    public void changeWeapon() {
        weapon++;
        weapon%=5;
        if (weapon==RC) {
            if (rccar!=null) state = REMOTE;
        } else if (state==REMOTE) state = WALK;
    }
    
    int[] ammo = new int [] {-1, 120, 3, 0, 2};
    
    public void fire() {
        if (this.reloading>0) return;
        if (ammo[weapon]==0) return;
        ammo[weapon]--;
        this.reloading = reloadingPeriod;  
//		if (going) return;
        System.out.println(this+": fire");
	switch (weapon) {
        case GUN: 
            level.add(new GunShot (this));
            this.reloading = 13;
         break;
        case M16: 
            level.add(new M16Shot (this));
            this.reloading = 2;
         break;
        case NADE:
            level.add(new Nade (this));
         break;
        case RC:
            if (rccar==null) {
                level.add(new RCCar(this));
            } else ammo[weapon]++;
         break;
        }
	
    }
 
    
    void keys(final boolean UP, final boolean DOWN, final boolean LEFT, final boolean RIGHT, final boolean FIRE) {
//        final boolean 
//               UP = (keyState&GameCanvas.UP_PRESSED)!=0, 
//               DOWN = (keyState&GameCanvas.DOWN_PRESSED)!=0,
//               LEFT = (keyState&GameCanvas.LEFT_PRESSED)!=0,
//               RIGHT = (keyState&GameCanvas.RIGHT_PRESSED)!=0,
//               FIRE = (keyState&GameCanvas.FIRE_PRESSED)!=0;
        
        switch (state) {
            case REMOTE:
               if (DOWN) {
                   state = WALK;
                   break;
               } else rccar.keys(UP, DOWN, LEFT, RIGHT, FIRE);
            break;
            default:
                super.keys(UP, DOWN, LEFT, RIGHT, FIRE);
        }
    }
    

    
//        public void goLeft () {
//		setDirection(-1);
//                if (onLeftWall) vy=-4;
//                onRightWall = false;
//                going = true;
//                this.vx=-4;
//	}
//
//	public void goRight () {
//		setDirection(1);
//                if (onRightWall) vy=-4;
//                onLeftWall = false;
//		going = true;
//                this.vx = 4;
//        }

/*	public void doWork () {
		super.doWork();
                    
 //               System.out.println("x:"+getX()+" y:"+getY());
//		
		int w = SpyCanvas.width, h = SpyCanvas.height-32;
		int x = this.x-w/2+this.vx*5, y = this.y-h/2+this.vy*3;
		int maxx = level.cell*level.width-w, maxy = level.cell*level.height-h;
		
		x = (x<0)?0:x;
		x = (x>maxx)?maxx:x;
		y = (y<0)?0:y;
		y = (y>maxy)?maxy:y;
		
		
		level.layers.setViewWindow(x, y, w, h);
	}
*/
        
        public void doMove() {
		// should never happend because it causes infinite loop 
                if (!validPos()) {
                    System.err.println(this + ": invalid pos");
                    return;
                }
/*		
		this.x += vx;
		if (validPos()) {
                    if (vx!=0) {
 //                       sprite.nextFrame();
                        onRightWall = false;
                        onLeftWall = false;
                    }
                   vx=0; 
		} else {
                    going = false;
                    while (!validPos()) {
			if (vx>0) this.x--; else this.x++;
                    };
                    // TODO only at corner
                    if (level.is(getX()+dir, getuY(), Level.CATCHABLE)) {
                        if (this.dir==1) onRightWall = true;
                        if (this.dir==-1) onLeftWall = true;
                        if (vy>0) vy=0;
                    }
                        
                        
                    this.vx = dir*4;
		}
        
                this.y += vy;
                if (validPos()) {
                    if (!onFloor) vy++;
                    onFloor = false;
		} else {
                    while (!validPos()) {
			if (vy>0) this.y--; else this.y++;
                    };
                    if (vy>0) onFloor = true;
                    vy = 1;
                }
*/

	} 

//        public void up () {
//            if (onLadder||onLeftWall||onRightWall) this.vy=-4;
//            if (onLadder) x=level.cell*getX();
//            jump();
//        }
//        
//        public void down () {
//		if (onFloor) crouching = true;
//                if (onLadder) x=level.cell*getX();
//                onLadder = false;
//                onRightWall = false;
//                onLeftWall = false;
//                vx = 0;
//        }
//        
	protected void kill () {
		super.kill();
		
		this.lives--;
		
		Game.saveGame();
		
		if (lives<1) {
                    
                    System.out.println("new score 0");
                    
                    try {
        //			Alert a = new Alert("Game Over", "you were killed\nTIP: avoid bullets and shot enemies", Game.createImage("/kia1.png"), AlertType.WARNING); 
        //			Game.pauseGame();
        //                    Game.showHighScoresScreen();	

                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    Game.pauseGame();
                    
                    System.out.println("new score 1");
                    
                    BestScoresScreen bsscreen = new BestScoresScreen();
        
                    System.out.println("new score 2");
                    
                    bsscreen.addNew(name, score);
                    
                    System.out.println("new score 3");
                    
                    Game.display.setCurrent(bsscreen);
                    
                    System.out.println("new score 4");
                    
		} else {
/*			Alert a = new Alert(name + " byl zabit v akci",
				 "máš poslední "+lives+" možnost"+((lives>1)?'i':' '),
				 Game.createImage("/kia1.png"), AlertType.WARNING); 
			a.setTimeout(Alert.FOREVER);
			Game.display.setCurrent(a);
*/			try {
				Thread.sleep(3000);
			} catch (InterruptedException ex) {
			}
			Game.continueGame();	
		} 
				
		
		
		
	}
	
//        int fgoing;
//        
//        void updateSprite() {
//           super.updateSprite();
///*            if (crouching) {
//                sprite.setFrame(4);
//                return;
//            }
//            if (going) {
//                sprite.setFrame(fgoing++/2%4);
//                return;
//            }
//            sprite.setFrame(0);*/
//           sprite.setFrame(state);
//        }
//      
        
        static Image bar1 = Game.createImage("bar1");
        static Sprite gunsprite = new Sprite(Game.createImage("guns"), 32, 16);
        
        public void paintInfoBar(Graphics g, int w, int h) {
            int light = 0x0055798a;
            int dark = 0x0031394a;
            
            g.setColor(dark);
            g.fillRect(0, h-18, w, 18);
            
            g.setColor(light);
            g.drawRect(0, h-18, w-1, 18);
            
            
            
            g.setColor(0x00FFFFFF);
            g.fillRect(4, h-9, 42, 8);//health
            g.setColor(0x00FF0000);
            g.fillRect(4, h-16, lives*8, 6);//lives
            g.fillRect(4, h-9, Math.min(42*health/100, 42), 8);//health
            g.drawImage(bar1, 0, h, g.LEFT|g.BOTTOM);
            
            
            g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
            g.setColor(0x00FFFFFF);
            g.drawString(""+score, w-2, h-2, g.BOTTOM|g.RIGHT);
            
// ammo
            g.setColor(0xFFFFFF);
            if (ammo[weapon]<0) ; else
                g.drawString(""+ammo[weapon], w/2, h-16, Graphics.TOP|Graphics.HCENTER);
            
            gunsprite.setPosition(50, h-17);
            gunsprite.setFrame(weapon);
            gunsprite.paint(g);
            
// score
//                g.setColor(0xFFFF0000);
//		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
///		g.setColor(0xFF5588CC);
//		g.drawString(""+score, w, 0, Graphics.TOP|Graphics.RIGHT);
// lifes
//       		g.setColor(0xFFCC3322);
//		for (int i = 0; i<lives; i++) {
//			g.fillArc(i*12+2, h-14, 10, 10, 0, 360);
//		}
// health
//                g.setColor((255*health/10)<<8);
//		g.fillRect(w-16, h-16, 16, h);
//		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
//		g.setColor(0xFFFF8888);
//		g.drawString(""+health, w, h-16, Graphics.TOP|Graphics.RIGHT);
//                
// ammo
//                g.setColor(0xFF888888);
//		g.drawString(""+weapon+":"+ammo[weapon], w/2, h-16, Graphics.TOP|Graphics.HCENTER);
// 
             

    }
        
//	public void paintInfoBar(Graphics g, int w, int h) {
////		Game.showMessage(""+255*health/10);
//
//// score
//                g.setColor(0xFFFF0000);
//		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
//		g.setColor(0xFF5588CC);
//		g.drawString(""+score, w, 0, Graphics.TOP|Graphics.RIGHT);
//// lifes
//       		g.setColor(0xFFCC3322);
//		for (int i = 0; i<lives; i++) {
//			g.fillArc(i*12+2, h-14, 10, 10, 0, 360);
//		}
//// health
//                g.setColor((255*health/10)<<8);
//		g.fillRect(w-16, h-16, 16, h);
//		g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
//		g.setColor(0xFFFF8888);
//		g.drawString(""+health, w, h-16, Graphics.TOP|Graphics.RIGHT);
//                
//// ammo
//                g.setColor(0xFF888888);
//		g.drawString(""+weapon+":"+ammo[weapon], w/2, h-16, Graphics.TOP|Graphics.HCENTER);
// 
//             
//
//    }

    void bonus(int b) {
	score+=b;
    }
}
