/*
 * MyCanvas.java
 *
 * Created on 30. kvìten 2006, 18:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *  MyCanvas is universal default screen, on which is content displayed by Controller
 *    
 *  
 */
package kotuc.midp;

import javax.microedition.lcdui.*;
import javax.microedition.*;
import java.lang.*;

/**
 *
 * @author PC
 */
public class MyCanvas extends Canvas implements Runnable, CommandListener {
    
    public static int width;
    public static int height;
    
    Thread thread;
        
   /** frames per second */
    private static final int FPS = 16;

    static MyCanvas current;
    
    /**
     * Creates a new instance of MyCanvas
     */
    public MyCanvas() {
 /*       try {
            setFullScreenMode(true);
        } catch (Exception ex) {
            MySystem.error(this + ": cannot setFullScreenMode!");
            ex.printStackTrace();
        }               
   */     current=this;
        width = getWidth();
        height = getHeight();
//        addCommand(backCommand);
//        addCommand(fireCommand);
//        setCommandListener(this);
    }
    
    static Controller controller;
    
    public void paint(Graphics g) {
        controller.paint(g);
    }
    
    protected void keyPressed(int keyCode) {
        controller.keyPressed(keyCode);

    }
       
    protected void pointerPressed(int x, int y) {
        controller.pointerPressed(x, y);
    }
    
    /**
     *  starts Thread, witch paints canvas in FPS
     *   
     */
    public void resume() {
       pause();
       thread = new Thread(this);
//     thread.setPriority(Thread.MAX_PRIORITY);
       thread.start();
    }
    
    public void pause() {
       thread = null;
    }

    protected void showNotify() {
        resume();
    }
    
    protected void hideNotify() {
        pause();
    }

    private Command backCommand = new Command("*back", Command.BACK, 5),
    fireCommand = new Command("*select", Command.OK, 2);
    
    
    public void commandAction(Command c, Displayable s) {
        if (c==backCommand) keyPressed(KEY_POUND);
        if (c==fireCommand) keyPressed(KEY_NUM5);
    }
    
     /**
     * The main event processor. Events are polled and
     * actions taken based on the directional events.
     */
    public void run() {
//	g = getGraphics(); // Of the buffered screen image
        Thread mythread = Thread.currentThread();
        
	// Loop handling events
	while (mythread == thread) {
	    try { // Start of exception handler
            
            long runStart = System.currentTimeMillis();    
            
// Check user input and update positions if necessary

//          int keyState = getKeyStates();
            
            controller.doWork();
                                    
            if (mythread == thread) {
                repaint();
                serviceRepaints();
            }
                       
            
            long runEnd = System.currentTimeMillis();          
            int timeInc = (int)(runEnd - runStart);
            long runSlow = 1000/FPS - timeInc;
            if (runSlow < 0) runSlow = 0;
            
            MySystem.time +=  Settings.gameSpeed;      
        
            try { Thread.sleep(runSlow); } catch (Exception e) {} 
		         
                
//            if (mythread == thread) {
//                flushGraphics();
//            }


        } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public void setController(Controller controller) {
        this.controller = controller;
        this.resume();
        repaint();
        serviceRepaints();
        MySystem.println("##### current Controller: "+controller);
    }

    public static Image createShade(int color) {
     /*   try {
            int[] rgb = new int[width*height];
            for (int i = 0; i<rgb.length; i++) rgb[i] = color;
            return Image.createRGBImage(rgb, width, height, true);
        } catch (Exception ex) {
            MySystem.error("MyCanvas.shade() : cannot create shade image!");
            ex.printStackTrace();
        }
       */ return null;
    }


    public Controller getController() {
        return controller;
    }

    public boolean paused() {
        return (thread==null)&&(controller!=null);
    }
}
