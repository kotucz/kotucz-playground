package spygame;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

import javax.microedition.media.*;
import javax.microedition.media.control.ToneControl;
import spygame.*;

class SpyCanvas extends GameCanvas implements Runnable {
    /** The width of the canvas */
    static int width;
    /** The height of the canvas */
    static int height;

   
    

    /** Thread used for key handling and animation */
    static Thread thread;

    /** number of milliseconds between screen updates */
//    private static final int PanRate = 50;
	/** frames per second */
    static final int FPS = 16;
	
    /** The Tone player */
    private Player tonePlayer;
    /** The ToneController */
    private ToneControl toneControl;
    /** Tune to play when puzzle level is solved. */
    static byte[] shotTune = {
	ToneControl.VERSION, 1,
	100, 1	// 1/8 note
    };

    /** Tune to play when a packet enters a store */
    static byte[] storedTune = {
	ToneControl.VERSION, 1,
	50, 8	// 1/8 note
    };

    public SpyCanvas() {
	super(false); // Don't suppress key events
	setFullScreenMode(true);
        height = getHeight();
	width = getWidth();
        
    }

    /**
     * Cleanup and destroy.
     */
    public void destroy() {
	hideNotify();
    }

    /**
     * Handle a repeated arrow keys as though it were another press.
     * @param keyCode the key pressed.
     */
    protected void keyPressed(int keyCode) {
        if (Game.paused) {
            Game.resumeGame();
            return;
        }
        if (keyCode==KEY_POUND) {
            Game.pauseGame();
        return;
        }
        if (keyCode==KEY_STAR) {
            Game.level.agent.changeWeapon();
        }
    }
 
    
    protected void pointerPressed(int x, int y) {
//	targetx = (x - tiles.getX()) / cell;
//	targety = (y - tiles.getY()) / cell;
    }

    /**
     * Add a listener to notify when the level is solved.
     * The listener is send a List.SELECT_COMMAND when the
     * level is solved.
     * @param l the object implementing interface CommandListener
     */
/*    public void setCommandListener(CommandListener l) {
	super.setCommandListener(l);
        listener = l;
    }*/

    /**
     * Paint the contents of the Canvas.
     * The clip rectangle of the canvas is retrieved and used
     * to determine which cells of the board should be repainted.
     * @param g Graphics context to paint to.
     */
    public void paint(Graphics g) {
		flushGraphics();
    }

    /**
     * The canvas is being displayed.
     * Stop the event handling and animation thread.
     */
    protected void showNotify() {

    }

    /**
     * The canvas is being removed from the screen.
     * Stop the event handling and animation thread.
     */
    protected void hideNotify() {
	Game.pauseGame();	
    }
    
    

    void paint() {
      	g.setColor(0);
	g.fillRect(0, 0, width, height);
		
	// Draw all the layers and flush
	Game.level.layers.paint(g, 0, 0);//y 16
		
	Game.paintLastMessages(g, 0, 16);
	
        Game.level.agent.paintInfoBar(g, width, height);
//	Game.agent.paintLowerInfo(g, 0, height-16, width, 16);

    }
    
    
    
    static Graphics g;
	
    /**
     * The main event processor. Events are polled and
     * actions taken based on the directional events.
     */
    public void run() {
	g = getGraphics(); // Of the buffered screen image
        Thread mythread = Thread.currentThread();
        
        
	// Loop handling events
	while (mythread == thread) {
	    try { // Start of exception handler

            long runStart = System.currentTimeMillis();
        // Check user input and update positions if necessary
            int keyState = getKeyStates();

            Game.level.agent.keys((keyState&GameCanvas.UP_PRESSED)!=0, 
                (keyState&GameCanvas.DOWN_PRESSED)!=0,
                (keyState&GameCanvas.LEFT_PRESSED)!=0,
                (keyState&GameCanvas.RIGHT_PRESSED)!=0,
                (keyState&GameCanvas.FIRE_PRESSED)!=0);


        Game.level.doWork();
       
        paint();       
               
        long runEnd = System.currentTimeMillis();          
        long runSlow = 1000/FPS - (runEnd - runStart);
            
        if (runSlow < 0) runSlow = 0;
  
     
        try { Thread.sleep(runSlow); } catch (Exception e) {} 
	         
                
	if (mythread == thread) {
	    flushGraphics();
	}


        } catch (Exception e) {
		e.printStackTrace();
	    }
	}

     }

    /**
     * Play the simple tune supplied.
     */
    void play(byte[] tune) {
	try {
	    if (tonePlayer == null) {
		// First time open the tonePlayer
		tonePlayer = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
		tonePlayer.realize();
		toneControl = (ToneControl)tonePlayer.getControl("javax.microedition.media.control.ToneControl");
	    }
	    tonePlayer.deallocate();
	    toneControl.setSequence(tune);
	    tonePlayer.start();
	} catch (Exception ex){
	    System.out.println(ex.getMessage());
	}
    }
   
   
    /*
     * Close the tune player and release resources.
     */
    void closePlayer() {
	if ( tonePlayer != null ) {
	    toneControl = null;
	    tonePlayer.close();
	    tonePlayer = null;
	}
    }
}
