/*
 * FlyCanvas.java
 *
 * Created on 17. zברם 2006, 8:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;

/**
 *
 * @author PC
 */
public class Level extends HCanvas implements Runnable {
    
    private static Thread thread;
    final int FPS = 10;
    
    /** Creates a new instance of FlyCanvas */ 
    Level(int levelid) {
       
        this.level = levelid;
        
        Unit.level = this;
        
        this.layers = new LayerManager();
        
        loadMap();
        
        this.units = new Vector();
        Unit.targets = new Vector();
        
        Unit.player = new Doctor();
        
        add(Unit.player);
             
        
    }
    
    int level = 1;
    
    Vector units;
    
    LayerManager layers;
    
    TiledLayer tiles;
    
    
    
    
    void loadMap () {
        this.tiles = new TiledLayer(20, 20, Images.font, 16, 16); // TODO !!!!!!!! 
        // TODO loadmap 
        tiles.setCell(2, 2, 1);
        tiles.setCell(0, 4, 1);
        layers.append(tiles);

        for (int i = 0; i < 10; i++) {
            tiles.setCell(i, 7, 1);
        }
        
        for (int i = 5; i < 20; i++) {
            tiles.setCell(i, 19, 1);
        }
        
        background = new TiledLayer(2, 2, Images.bg, 176, 220);
        background.setCell(0, 0, 1);
        background.setCell(0, 1, 1);
        background.setCell(1, 0, 1);
        background.setCell(1, 1, 1);
//        background.fillCells(0, 0, 2, 2, 1);
        
        layers.append(background);
        
        
    }
    
    public void add(Unit unit) {
        layers.insert(unit.sprite, 0);
        units.addElement(unit);
        
    }

    public void remove(Unit unit) {
        layers.remove(unit.sprite);
        units.removeElement(unit);
        
    }
    
    
           
    public void keyPressed(int key) {
//        if (key == KEY_STAR) Unit.player.fire(Unit.MISSILE1); 
//        if (key == KEY_NUM0) Unit.player.fire(Unit.EMP1); 
        if (key == KEY_POUND) {
// pause game
//            Hell.menu.screen = Menu.MENU;
            pause();
            Game.showMenu(Menu.MENU);
            
        }     
        
        switch (getGameAction(key)) {
            case LEFT:
                Unit.player.dir = Unit.player.LEFT;
            break;
            case RIGHT:
                Unit.player.dir = Unit.player.RIGHT;
            break;
            case UP:
//                Unit.player.jump(200);
            break;
            case DOWN:
//                Unit.player.speed = Unit.player.STOP;
            break;
            case FIRE:
//                Unit.player.fire(Unit.SHOT1);
                if (!Unit.player.isAlive()) {
                    Game.loadLevel(1);
                    Game.play();
                }
            break;
        }
    }
    
    public void keyReleased(int key) {
        switch (getGameAction(key)) {
            case LEFT:
//                if (Unit.LEFT==Unit.player.dir) Unit.player.dir = Unit.player.STRAIG;
                break;
            case RIGHT:
//                if (Unit.RIGHT==Unit.player.dir) Unit.player.dir = Unit.player.STRAIG;
                break;
            
            case UP:
//                if (Unit.FAST==Unit.player.speed) Unit.player.speed = Unit.player.SLOW;
                break;
            case DOWN:
//                if (Unit.STOP==Unit.player.speed) Unit.player.speed = Unit.player.SLOW;
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
       
    void paintViewCentered(Graphics g) {
        
        int height = this.height - 16; // lista
        
        int minx = 0, miny = 0, 
                maxx = tiles.getWidth()-width,
                maxy = tiles.getHeight()-height;
        
        int cx=Unit.player.getX()+Unit.player.sprite.getWidth()/2,
            cy=Unit.player.getY()+Unit.player.sprite.getHeight()/2;        
        
        int lx, ly;
        
        lx = cx - width/2;
        ly = cy - height/2;
         
        lx = Math.max(minx, Math.min(lx, maxx));
        ly = Math.max(miny, Math.min(ly, maxy));
                       
        layers.setViewWindow(lx, ly, width, height);
        
        int bgx, bgy;
        bgx = lx*(tiles.getWidth()-background.getWidth())/maxx;
        bgy = ly*(tiles.getHeight()-background.getHeight())/maxy;
        background.setPosition(bgx, bgy);
        
//        paintParalaxBG(g);
        layers.paint(g, 0, 0);
        
        g.fillRect(width*lx/maxx - 3, height*ly/maxy - 3, 5, 5);
        
    }
    
    TiledLayer background;
    
    void paintParalaxBG(Graphics g) {
        
    }
    
    public void paint(Graphics g) {
       
        
        
        paintViewCentered(g);
                
        g.drawImage(Images.lista, 0, height, g.BOTTOM|g.LEFT);
        g.setColor(0xFF000000);
        g.fillRect(width-50+41*Unit.player.health/100, height-10, 41-41*Unit.player.health/100, 6);

//        if (!Unit.player.isAlive()) Text.drawString(g, Text.youlose, Text.CENTER, height/2);//Text.youlose.paint(g, 100, 100);
        if (!Unit.player.isAlive()) Text.drawString(g, "you are DEAD", 0, height/2);;               
               
        g.setColor(0);
        g.fillRect(Device.width, 0, 1000, 1000);
        g.fillRect(0, Device.height, 1000, 1000);
     

        
        Text.drawString(g, Device.name+" "+Device.width+"x"+Device.height, 10, 10);
        Text.drawString(g, "time "+System.currentTimeMillis(), 10, 20);
        
    }
    
        


    
    
    
    
    
    
    
    void levelBeh() {
        if (!Unit.player.isAlive()) return;
       // level beh

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
                Unit.player.keys(getKeyStates());
                updateUnits(100);
                flushGraphics();
            }
	
            } catch (Exception e) {
		e.printStackTrace();
	    }
	}

    }
        
    static int random(int n) {
        int r = random.nextInt();
        if (r>=n) r-=n*(r/n);
        if (r<0) r-=n*((r/n)-1);
        Game.println("random("+n+"):"+r);
        return r;
    }
    
    static Random random = new Random();
           
    void updateUnits(long time) {
        for (Enumeration it = units.elements(); it.hasMoreElements();) {
            Unit unit = (Unit) it.nextElement();
            unit.update(time);
        }
    }
    
}
