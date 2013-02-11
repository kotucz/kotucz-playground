/*
 * MultiPlayerCanvas.java
 *
 * Created on 27. èerven 2006, 19:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package spygame;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

/**
 *
 * @author PC
 */
public class MultiPlayerCanvas extends SpyCanvas {
    
//    Level level = new Level();
    
    /** Creates a new instance of MultiPlayerCanvas */
    public MultiPlayerCanvas() {
        Game.restartLevel(-1);
//         Game.level = new Level();
    }
    
    BTUnit mp;
    
    /**
     * The main event processor. Events are polled and
     * actions taken based on the directional events.
     */
    public void run() {
	g = getGraphics(); // Of the buffered screen image
        Thread mythread = Thread.currentThread();
        
        g.drawString("initializing", 10, 10, g.LEFT|g.TOP);
        
        mp = new BTUnit();
        
        g.drawString("waiting for players", 10, 30, g.LEFT|g.TOP);
                
        try {
            
            mp.openConnection(new javax.bluetooth.UUID("2d26618601fb47c28d9f10b8ec891363", false));
        } catch (Exception ex) {
            ex.printStackTrace();
            Game.showMainScreen();
            return;
        }
        
        System.out.println("bt ready");
        g.drawString("connected", 10, 50, g.LEFT|g.TOP);
        
//        level.init1();
 //       Game.level.initMulti();
        agent2.set(0, 2);
        Game.level.add(agent2);
        Game.level.add(new Enemy(8, 2, 1));
        
                
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
        
            if (Game.level.agent.health>0) {
                mp.dataout.write(keyState);
            } else {
                quitMultiplayer();
            }
            
            keyState = mp.datain.read();
            if (keyState!=-1) {
                agent2.keys((keyState&GameCanvas.UP_PRESSED)!=0, 
                (keyState&GameCanvas.DOWN_PRESSED)!=0,
                (keyState&GameCanvas.LEFT_PRESSED)!=0,
                (keyState&GameCanvas.RIGHT_PRESSED)!=0,
                (keyState&GameCanvas.FIRE_PRESSED)!=0);
            } else {
                quitMultiplayer();
            }
    
////            agent.doWork();
	
            Game.level.doWork();

//          multiplayer();
        
            paint();       
               
        long runEnd = System.currentTimeMillis();          
        long runSlow = 1000/FPS - (runEnd - runStart);
            
        if (runSlow < 0) runSlow = 0;
        
        try {
            System.out.println(1000/(runEnd - runStart)+"fps");
        } catch (Exception e) {}          
              
        try { Thread.sleep(runSlow); } catch (Exception e) {} 
	         
                
	if (mythread == thread) {
	    flushGraphics();
	}

		// g.drawString("PushPuzzle Level " + level, 0, height,
		//			     Graphics.BOTTOM|Graphics.LEFT);
/*		try {
		    mythread.sleep(PanRate);
		} catch (java.lang.InterruptedException e) {
		    // Ignore
		}
  */            

        } catch (Exception e) {
		e.printStackTrace();
	    }
	}

     }

    
     void multiplayer() {
        try {
            
//            System.out.println(this+" dout "+mp.dataout+" din "+mp.datain);
            
            mp.dataout.write(250);
            mp.dataout.write(Game.level.agent.x);
            mp.dataout.write(Game.level.agent.y);
            
            if (mp.datain.read()<0) System.err.println("read wrong int awaiting 250");
            
//            if (!=250) System.err.println("read wrong int awaiting 250");;/// possibly bad
            agent2.x = mp.datain.read();
            agent2.y = mp.datain.read();
                      
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
     }
     
     void quitMultiplayer() {
        try {
            mp.dataout.write(-1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
         mp.closeConnection();
         Game.showMainScreen();
     }
     
     /*
     void paint() {
      	g.setColor(0);
	g.fillRect(0, 0, width, height);
		
	// Draw all the layers and flush
	Game.level.layers.paint(g, 0, 16);
		
	Game.paintLastMessages(g, 0, 16);
	//TODO move to Game
	Game.level.agent.paintInfoBar(g, width, height);
//	Game.agent.paintLowerInfo(g, 0, height-16, width, 16);

    }
    
     */
     
     Agent agent2 = new Agent();
     
     class MultiAgent extends Agent {
         public void doWork() {
             updateSprite();
         }
         
         public void doMove() {
             
         }
         
     }
    
}
