/*
 * FlyCanvas.java
 *
 * Created on 17. zברם 2006, 8:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package hello;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.*;

/**
 *
 * @author PC
 */
public class FlyCanvas extends HCanvas implements Runnable {
    
    private static Thread thread;
    final int FPS = 10;
    
    /** Creates a new instance of FlyCanvas */ 
    FlyCanvas(int levelid) {
       
        this.level = levelid;
        
        this.flies = new Vector();
        Fly.targets = new Vector();
        
        Fly.player = new Fly(Fly.PLAYER1, width/2, height+50);
        
        add(Fly.player);
        
// intro flying in the scene
        progress = -51;
        nextWave = 0;       
        wave = 0;
        levelpoint = 0;
        playingendscene = false;
        
        switch (level) {
              case 1:
                  levelstack = levelstack1;
              break;
              case 2:
                  levelstack = levelstack2;
              break;
              default:
                Hell.println("ERROR: undefined level: "+level);
         }
                
         Fly.canvas = this;
               
//        loadLevel(1);
        
    }
        
    Vector flies = new Vector();
    
    public void add(Fly fly) {
        flies.addElement(fly);
    }

    public void remove(Fly fly) {
        flies.removeElement(fly);
    }
    
    
    
    int progress=0;
    
    static int level = 1;
    private static int levelunlocked = 1;
    
    public static boolean isLocked(int i) {
        return (i>levelunlocked);
    }
    
    static int score;    
        
    public void keyPressed(int key) {
        if (key == KEY_STAR) Fly.player.fire(Fly.MISSILE1); 
        if (key == KEY_NUM0) Fly.player.fire(Fly.EMP1); 
        if (key == KEY_POUND) {
// pause game
//            Hell.menu.screen = Menu.MENU;
            pause();
            Hell.showMenu(Menu.MENU);
            
        }     
        
        switch (getGameAction(key)) {
            case LEFT:
                Fly.player.dir = Fly.player.LEFT;
            break;
            case RIGHT:
                Fly.player.dir = Fly.player.RIGHT;
            break;
            case UP:
                Fly.player.speed = Fly.player.FAST;
            break;
            case DOWN:
                Fly.player.speed = Fly.player.STOP;
            break;
            case FIRE:
                Fly.player.fire(Fly.SHOT1);
            break;
        }
    }
    
    public void keyReleased(int key) {
        switch (getGameAction(key)) {
            case LEFT:
                if (Fly.LEFT==Fly.player.dir) Fly.player.dir = Fly.player.STRAIG;
                break;
            case RIGHT:
                if (Fly.RIGHT==Fly.player.dir) Fly.player.dir = Fly.player.STRAIG;
                break;
            
            case UP:
                if (Fly.FAST==Fly.player.speed) Fly.player.speed = Fly.player.SLOW;
                break;
            case DOWN:
                if (Fly.STOP==Fly.player.speed) Fly.player.speed = Fly.player.SLOW;
                break;
        }
    }
    
    
    
    
    
    
    
    
    public void start() {
        thread = new Thread(this);
        thread.start();
    }
         
    public void pause() {
        thread = null;
//        Hell.showMenu();
    }        
        
    
    
    public void paint(Graphics g) {
        
//        g.setColor(0xFFFFFF);
        
//        g.fillRect(0, 0, width, height);
        
//        g.translate(0, -player.y+150);
        
        g.setColor(0xFFFF0000);
        
        drawGround(g);

        for (int i = 0; i<flies.size(); i++) {
            ((Fly)flies.elementAt(i)).paint(g);
        }
        
//        Text.hell.paint(g, 23, 31, g.LEFT|g.TOP);
//        drawText(g, Text.hell, 23, 31, g.LEFT|g.TOP);
//        g.drawString(Text.HELL+" starts", 23, 31, g.LEFT|g.TOP);
        
        new Text("progress " + progress).paint(g, 10, 10);
        new Text("score " + score).paint(g, 10, 50);
        new Text("targets " + Fly.targets.size()).paint(g, 10, 20);
        new Text("flies " + flies.size()).paint(g, 10, 30);
        new Text("health " + Fly.player.health).paint(g, 10, 40);
        if (levelpoint<levelstack.length) new Text("next wave " + levelstack[levelpoint]).paint(g, 10, 60);
        new Text("level " + level).paint(g, 10, 70);
        
        g.drawImage(HImage.energybar, 10, height-20, g.TOP|g.LEFT);
        g.setColor(0xFF000000);
        g.fillRect(10+41*Fly.player.health/100, height-20, 41-41*Fly.player.health/100, 6);
        
        Text.write(g, Fly.player.empshots+" e "+Fly.player.missiles+" m", Text.RIGHT, height-20, Text.BOLD);
        
//        g.drawString("progress: " + progress, 10, 10, g.LEFT|g.TOP);
//        g.drawString("score: " + score, width-10, 10, g.RIGHT|g.TOP);
//        g.drawString("targets:  " + Fly.targets.size(), 10, 20, g.LEFT|g.TOP);
//        g.drawString("flies:    " + flies.size(), 10, 30, g.LEFT|g.TOP);
//        g.drawString("health:   " + Fly.player.health, 10, 40, g.LEFT|g.TOP);
        

        if (!Fly.player.isAlive()) Text.write(g, Text.youlose, Text.CENTER, height/2, Text.BOLD);//Text.youlose.paint(g, 100, 100);
        if (isEnd()) Text.write(g, Text.misscompl, Text.CENTER, height/2, Text.BOLD);//Text.misscompl.paint(g, 100, 100);
        
    }
    
    public void drawGround(Graphics g) {
        
        switch (level) {
        case 1:
            drawBg(g, HImage.sea1);
            g.drawImage(HImage.stones[0], 10, (progress+80)%height, g.LEFT|g.TOP);
            g.drawImage(HImage.stones[0], 90, (progress+32)%height, g.LEFT|g.TOP);
            g.drawImage(HImage.stones[0], 60, (progress+120)%height, g.LEFT|g.TOP);
            g.drawImage(HImage.stones[1], 30, (progress+138)%height, g.LEFT|g.TOP);
            g.drawImage(HImage.stones[1], 70, (progress+45)%height, g.LEFT|g.TOP);
            g.drawImage(HImage.stones[1], 80, (progress+3)%height, g.LEFT|g.TOP);
        break;
        case 2:
            drawBg(g, HImage.grass1);
        break;
        case 3:
            drawBg(g, HImage.rock1);
        break;
        case 4:
            g.drawImage(HImage.sea1, 0, (progress%175)-175, g.LEFT|g.TOP);
            g.drawImage(HImage.sea1, 0, (progress%175), g.LEFT|g.TOP);
            g.drawImage(HImage.sea1, 0, (progress%175)+175, g.LEFT|g.TOP);
            g.drawImage(HImage.sea1, 153, (progress%175)-175, g.LEFT|g.TOP);
            g.drawImage(HImage.sea1, 153, (progress%175), g.LEFT|g.TOP);
            g.drawImage(HImage.sea1, 153, (progress%175)+175, g.LEFT|g.TOP);

            g.drawString("t", 50, (90+progress)%height, g.LEFT|g.TOP);
            g.drawString("t", 30, (40+progress)%height, g.LEFT|g.TOP);
            g.drawString("t", 70, (80+progress)%height, g.LEFT|g.TOP);
            g.drawString("t", 20, (12+progress)%height, g.LEFT|g.TOP);
            g.drawString("t", 10, (50+progress)%height, g.LEFT|g.TOP);
            g.drawString("t", 99, (10+progress)%height, g.LEFT|g.TOP);
        break;
        case 5:
            int iw = 55;
            int ih = 55; 
            for (int iy = -ih+progress%ih; iy<(height+ih); iy += ih) {
                for (int ix = -iw; ix<(width+iw); ix += iw){
                    g.drawImage(HImage.rock1, ix, iy, g.LEFT|g.TOP);
                }
            }
        break;
        }
        
    }
    
    public void drawBg(Graphics g, javax.microedition.lcdui.Image img) {
        int iw = img.getWidth();
        int ih = img.getHeight(); 
        for (int iy = -ih+progress%ih; iy<(height+ih); iy += ih) {
            for (int ix = -iw; ix<(width+iw); ix += iw){
                g.drawImage(img, ix, iy, g.LEFT|g.TOP);
            }
        }
        
    }
        


    
    
    public boolean isEnd() {
        return (levelpoint>=levelstack.length)&&(Fly.targets.isEmpty());
    }
    
    void playingIntroScene() {
        if (Fly.player.y>(height-50)) Fly.player.y-=5;
    }
    
    boolean playingendscene;
    
    int nextWave;
    int wave;
    

    final int[] levelstack1 = {
        100, 3,  Fly.ENEMYM1, 25,   Fly.ENEMYM1, 50,   Fly.ENEMYM1, 75,
        300, 3,  Fly.ENEMYM1, 25,   Fly.ENEMYM1, 50,   Fly.ENEMYM1, 75,
        600, 3,  Fly.ENEMYM1, 25,   Fly.ENEMYM1, 50,   Fly.ENEMYM1, 75,
        900, 1,  Fly.BOSS1, 50,
        1000, 0
    };
    
    final int[] levelstack2 = {
        100, 3,  Fly.ENEMYM1, 25,   Fly.ENEMYL1, 50,   Fly.ENEMYM1, 75,
        300, 3,  Fly.ENEMYL1, 25,   Fly.ENEMYM1, 50,   Fly.ENEMYL1, 75,
        600, 3,  Fly.ENEMYL1, 25,   Fly.ENEMYL1, 50,   Fly.ENEMYL1, 75,
        900, 1,  Fly.BOSS1, 50,
        1200, 1,  Fly.BOSS1, 50,
        1000, 0
    };
    
    int[] levelstack;// = new int[0];
    int levelpoint;
    
    void waves() {
        if (levelpoint<levelstack.length)
        if (progress>levelstack[levelpoint]) {
            int newe = levelstack[++levelpoint];
            for (int i = 0; i<newe; i++) {
                add(new Fly(levelstack[++levelpoint], width*levelstack[++levelpoint]/100));
            }
            levelpoint++;
        }
    }
    
    
    void levelBeh() {
        if (!Fly.player.isAlive()) return;
       // level beh
        progress += 3; // comment?
        if (playingendscene) {
            Fly.player.y-=10;
            if (Fly.player.y<-50) {
                // todo unlock level+1
                level++;
//                Hell.menu.screen = Menu.
                Hell.showMenu(Menu.LEVEL_SELECT);//loadLevel(++level);
            }
        } else if (progress<0) {
            playingIntroScene();
        } else
        if (isEnd()) {
            flies = new Vector();
            add(Fly.player);
            playingendscene=true;            
        } else {
            waves();            
//            if ((nextWave<progress)&&(progress<1000)) { 
//                  add(new Fly(Fly.BOSS1, width/2));
//                add(new Fly(Fly.ENEMYM1, random(width)));
//                add(new Fly(Fly.ENEMYM1, random(width)));
//                nextWave+=500;
//            }
            
        }

    }

    
    public void run() {
        Thread mythread = Thread.currentThread();
        
               
	// Loop handling events
	while (mythread == thread) {
	    try { // Start of exception handler

            long runStart = System.currentTimeMillis();
        // Check user input and update positions if necessary

//            int keyState = getKeyStates();
   
               
            long runEnd = System.currentTimeMillis();          
            long runSlow = 1000/FPS - (runEnd - runStart);
            
            if (runSlow < 0) runSlow = 0;
        
            try {
                System.out.println(1000/(runEnd - runStart)+"fps");
            } catch (Exception e) {}          
              
            try { Thread.sleep(runSlow); } catch (Exception e) {} 
	         
                
            if (mythread == thread) {
                levelBeh();
                flushGraphics();
            }
	
            } catch (Exception e) {
		e.printStackTrace();
	    }
	}

    }
    
    void flyKilled(Fly target) {
        switch (target.type) {
            case Fly.ENEMYM1:
            case Fly.ENEMYL1:
            case Fly.TARGET1:
  
                score+=100;
                add(new Fly(Fly.BONUSE+random(3), target.x, target.y));
//                add(new Fly(target.type, random(width)));
//                if (random(10)<2) add(new Fly(target.type, random(width)));
            break;
        }
    }
    
    static int random(int n) {
        int r = random.nextInt();
        if (r>=n) r-=n*(r/n);
        if (r<0) r-=n*((r/n)-1);
        Hell.println("random("+n+"):"+r);
        return r;
    }
    
    static Random random = new Random();
    
    static final int NUM_LEVELS = 8;
    
}
