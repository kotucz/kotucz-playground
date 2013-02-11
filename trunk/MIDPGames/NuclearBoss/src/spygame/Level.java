package spygame;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

import javax.microedition.media.*;
import javax.microedition.media.control.ToneControl;

import java.util.*;
import spygame.*;
/**
 * The class Board knows how the pieces move, handles undo, and
 * handles reading of screens.
 */
public class Level {
	/** number of pixels per cell */
    static int cell = 24;	
           
    static int currentLevel = 1;
    
    protected byte[] array;
    protected byte[] tileIds;
        
//    private byte[] pathmap;	// used for runTo to find shortest path
    int width, height;

    Agent agent;
    
    // Bit definitions for pieces of each board position
    public static final byte 
        FREE = 1, 
        BARREL = 2, 
        EXIT = 4,	
        LADDER = 8,
        SOLID = 16,
        CATCHABLE = 32,
        SEEABLE = 64;    

    protected Image themeImage;
    
 
    /** Tiles forming the background */
    TiledLayer tiles;
 

//	static Agent agent;
    /**
     * Creates new Board initialized to a simple puzzle.
     */
    public Level() {
        System.out.println("new Level");
        agent = new Agent();
        add(agent);
//    	level0();
//    	setup();    
    }

    

    /**
     * Return the pieces at the location.
     * @param x location in the board.
     * @param y location in the board.
     * @return flags indicating what pieces are in this board location.
     * Bit flags; combinations of WALL, PUSHER, STORE, PACKET.
     */
    public byte get(int x, int y) {
        int offset = index(x, y);
        if ((offset<0)||(offset>=array.length)) return 0; else 
		return array[offset];
    }

    /**
     * Set the value of the location.
     */
    private void set(int x, int y, byte value) {
        array[index(x, y)] = value;
    }
	
	/**
	 * Checks if pixel on [x, y] 
	 */
    boolean isXYFree(int x, int y) {
        return is(x/cell, y/cell, FREE);
    }
    
    boolean isFree(int x, int y) {
        return is(x, y, FREE);

    }
    
    boolean is(int x, int y, int mask) {
	return (get(x, y)&mask)!=0;
    }

    /**
     * Compute the index in the array of the x, y location.
     */
    private int index(int x, int y) {
        if (x < 0 || x >= width ||
        y < 0 || y >= height)
            return -1;
        return y * width + x;
    }

    

    /**
     * Get the width of the game board.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the board.
     */
    public int getHeight() {
        return height;
    }

	    /**
     * Setup the theme by reading the images and setting up
     * the sprite and picking the tile size.
     * Uses the current theme index.
     * If the image with the current index can't be found
     * retry with theme = 0.
     * @param image containing all the frames used for the board.
     */
    private void setup() {
 	   
	if (tiles != null) {
	    layers.remove(tiles);
	}

	// Figure out how many cells are needed to cover canvas.
	int w = getWidth();//(width + cell - 1) / cell;
	int h = getHeight();//(height + cell - 1) / cell;

	tiles = new TiledLayer(w, h,
			       themeImage, cell, cell);


	/** Fill it all with background */
//	tiles.fillCells(0, 0, w, h, TILE_FREE);

	// Initialize the background tileset
	System.out.print("Initializing tiles .. ");
	for (int y = 0; y < h; y++) {
	    for (int x = 0; x < w; x++) {
//		updateTile(x, y);
                tiles.setCell(x, y, tileIds[index(x, y)]);
	    }
	}
	
	layers.append(tiles);
	
	System.out.println("OK");
		
	
    }

	int enemyCount = 0;
        
	boolean	objectivesCompleted () {
//            System.out.println("exit"+get(agent.getX(), agent.getY()));
            return ((get(agent.getX(), agent.getY())&EXIT)!=0)&&(enemyCount == 0);
	}
	
	
	/*
	 * called, when level is done
	 */
	void levelCompleted () {
		
                Game.pauseGame();
   //TODO when more levels .-)             
 //               currentLevel++;
		
                Game.saveGame();
                
                Game.restartLevel(currentLevel);
              
	}
	
	public void doWork () {
                     
            
                if (objectivesCompleted()) {
			levelCompleted();
		}
                
                doWorkAll();
                
                
                
                if (agent.state == Agent.REMOTE) {
                    this.lookAt(agent.rccar.x, agent.rccar.y);
                } else this.lookAt(agent.x, agent.y);
	}
	
        
        void lookAt(int x, int y) {
      		int w = SpyCanvas.width, h = (SpyCanvas.height-18);   //32 is infobar height
		x += -w/2;
                y += -h/2;
		int maxx = cell*width-w, maxy = cell*height-h;
		
		x = (x<0)?0:x;
		x = (x>maxx)?maxx:x;
		y = (y<0)?0:y;
		y = (y>maxy)?maxy:y;
		
		layers.setViewWindow(x, y, w, h);

        }

        
        
        LayerManager layers = new LayerManager();
        
        void add(Entity e) {
            e.level = this;
            entities.addElement(e);
            if (e instanceof Enemy) enemyCount++;
            layers.insert(e.sprite, 0);
        }
        
        void kill(Entity e) {
            entities.removeElement(e);
            if (e instanceof Enemy) enemyCount--;
            if ((e instanceof Enemy)||(e instanceof Agent)) ; else layers.remove(e.sprite);
        }
        
        protected Vector entities = new Vector();

        
    	public void doWorkAll () {
//		System.out.println("Entities ..");
		for (Enumeration e = entities.elements() ; e.hasMoreElements() ;) {
        	((Entity)e.nextElement()).doWork();
     		
     	}
  //   	System.out.println("Entities .. DONE");

	}
	
	public void killAll () {
	   for (Enumeration e = entities.elements() ; e.hasMoreElements() ;) {
        	layers.remove(((Entity)e.nextElement()).sprite);
           }
            entities.removeAllElements(); 

	}    
        
        
  void init1 () {
              
        width = 16;
        height = 8;
        
        themeImage = Game.createImage("1min");
        
        tileIds = new byte [] {
        3,  2,  2,  2,  2,  2,  2,  2,  2,  2, 17,  2,    14, 18, 18, 18,
        4,  5,  2,  8,  2,  8,  2,  2,  2,  2, 17,  2,    13, 18, 18, 12,     
        6,  7,  2,  9,  2,  9, 11,  2,  2,  2, 17,  2,    14, 18, 17, 18,    
        1,  1,  1,  1,  1,  1,  1,  1,  2,  2, 17,  2,    15, 16, 17, 16,
        2,  2,  2, 23,  2,  2,  2,  2,  2,  2, 17,  2,    14, 18, 17, 18,
        2,  2, 22,  2,  2,  2,  2,  2,  2,  2, 17,  2,    14, 18, 17, 18,
        2,  2, 19, 20, 21,  2, 11,  2,  2,  2, 17,  2,    14, 18, 17, 18,
        1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,    15, 16, 16, 16    
        };
        
        array = new byte [] {
            _, _, _, _, _, _, _, _, _, _, H, _,   B, _, _, _,
            _, _, _, _, _, _, _, _, _, _, H, _,   B, _, _, _,
            _, _, _, _, _, _, B, _, _, _, H, _,   B, _, H, _,
            X, X, X, X, X, X, X, C, _, _, H, _,   C, X, H, X,
            E, _, _, _, _, _, _, _, _, _, H, _,   B, _, H, _,
            E, _, _, _, _, _, _, _, _, _, H, _,   B, _, H, _,
            E, _, B, B, B, _, B, _, _, _, H, _,   B, _, H, _,
            X, X, X, X, X, X, X, X, X, X, X, X,   X, X, X, X
            
        };
        
        agent.set(0, 2);
//        add(new Enemy(12, 2, -1));
        add(new Enemy(8, 6, -1));
        add(new Enemy(4, 2, 1));
        add(new Enemy(1, 6, 1));
        add(new Item(13, 2, 0));
        add(new Item(5, 6, 0));
        add(new Item(6, 6, 1));
        add(new Item(9, 6, 1));
        add(new Item(12, 6, 1));
        
        compile();
        
    }

    void initMulti () {
              
        width = 16;
        height = 8;
        
        themeImage = Game.createImage("1min");
        
        tileIds = new byte [] {
        3,  2,  2,  2,  2,  2,  2,  2,  2,  2, 17,  2,    14, 18, 18, 18,
        4,  5,  2,  8,  2,  8,  2,  2,  2,  2, 17,  2,    13, 18, 18, 12,     
        6,  7,  2,  9,  2,  9, 11,  2,  2,  2, 17,  2,    14, 18, 17, 18,    
        1,  1,  1,  1,  1,  1,  1,  1,  2,  2, 17,  2,    15, 16, 17, 16,
        2,  2,  2, 23,  2,  2,  2,  2,  2,  2, 17,  2,    14, 18, 17, 18,
        2,  2, 22,  2,  2,  2,  2,  2,  2,  2, 17,  2,    14, 18, 17, 18,
        2,  2, 19, 20, 21,  2, 11,  2,  2,  2, 17,  2,    14, 18, 17, 18,
        1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,    15, 16, 16, 16    
        };
        
        array = new byte [] {
            _, _, _, _, _, _, _, _, _, _, H, _,   B, _, _, _,
            _, _, _, _, _, _, _, _, _, _, H, _,   B, _, _, _,
            _, _, _, _, _, _, B, _, _, _, H, _,   B, _, H, _,
            X, X, X, X, X, X, X, C, _, _, H, _,   C, X, H, X,
            E, _, _, _, _, _, _, _, _, _, H, _,   B, _, H, _,
            E, _, _, _, _, _, _, _, _, _, H, _,   B, _, H, _,
            E, _, B, B, B, _, B, _, _, _, H, _,   B, _, H, _,
            X, X, X, X, X, X, X, X, X, X, X, X,   X, X, X, X
            
        };
        
        agent.set(0, 2);
        
        compile();
        
    }
  
  
  
    void compile() {
        setup();
    }    
        

    static final byte _ = FREE|SEEABLE;
    static final byte X = SOLID;
    static final byte H = LADDER|FREE|SEEABLE;
    static final byte C = CATCHABLE|SOLID;
    static final byte E = EXIT|FREE|SEEABLE;
    static final byte B = BARREL|FREE;
    
            
    
        
}   

