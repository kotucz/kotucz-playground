package spygame;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.io.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import spygame.*;


public class Game extends MIDlet/* implements CommandListener*/ {
    static Display display;
    static SpyCanvas canvas;
    static Level level;
    static Game midlet;
    

    /**
     * Creates new PushPuzzle MIDlet.
     */
    public Game() {
    	midlet = this;
        display = Display.getDisplay(this);
        
        canvas = new SpyCanvas();
        
//        level = new Level();
        
//        canvas.setCommandListener(this);
    }

/*    
    Canvas getCanvas() {
    try {
      // existuje-li javax.microedition.lcdui.game.GameCanvas vime,
      // ze se jedna o MIDP2
      Class.forName("javax.microedition.lcdui.game.GameCanvas");
      return (Canvas)(Class.forName("MIDP2Canvas")).newInstance();
    } catch(Exception exception) {
      try {
        // existuje-li FullCanvas, pak jde o Nokii
        Class.forName("com.nokia.mid.ui.FullCanvas");
        return (Canvas)(Class.forName("NokiaCanvas")).newInstance();
      } catch (Exception e) {
        return new MIDP1Canvas();
      }
    }
  }*/
    
    /**
     * Start creates the thread to do the timing.
     * It should return immediately to keep the dispatcher
     * from hanging.
     */
    public void startApp() {	
        showIntroScreen();
 
        storeSettings(StoreManager.SM_LOAD);
 
//        display.setCurrent(canvas);
//        canvas.init();
	showMainScreen();
    }

    /**
     * Pause signals the thread to stop by clearing the thread field.
     * If stopped before done with the iterations it will
     * be restarted from scratch later.
     */
    public void pauseApp() {
        pauseGame();
    }

    /**
     * Destroy must cleanup everything.
     * Only objects exist so the GC will do all the cleanup
     * after the last reference is removed.
     */
    public void destroyApp(boolean unconditional) {
    	saveGame();
    	
    	storeSettings(StoreManager.SM_SAVE);
    	
        display.setCurrent((Displayable)null);

    }

    /**
     * Respond to a commands issued on any Screen
     */
//    public void commandAction(Command c, Displayable s) {
        
        /*       if (c == newGameCommand) {
            newGame();
        } else if (c == continueCommand) {
            continueGame();
        } else if (c == backCommand) {
            showMainScreen();
        } else if (c == playCommand) {
            display.setCurrent(canvas);
        } else if (c == exitCommand) {
            	destroyApp(false);
            	notifyDestroyed();
        } else if (s == mainScreen) {
            
                      
            
            
        } else if (s==setScreen) {
          	currLanguage = setScreen.getSelectedIndex();
           	showMainScreen();
        }*/
//    }
    
    private void showIntroScreen() {
	display.setCurrent(new Intro());
        try {
        	Thread.sleep(3000);	
        } catch (Exception ex) {
        }
    }
    
    static Font MENU_FONT = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);


//    static AnimatedList mainScreen;
//    static int fitem = 0;
    
    static void showMainScreen() {
        display.setCurrent(new AnimatedList(1));
        
/*        String[] elements;
        int i = 0;
        
        if (paused) {
            elements = new String[7];
            fitem = 1;
            elements[i++] = "*resume";
        } else if ((Level.currentLevel>1)||(Agent.score>0)) {
            elements = new String[7];
            fitem = 2;
            elements[i++] = "*continue";
        } else {
            elements = new String[6];
        }
        elements[i++] = getText(1);
        elements[i++] = getText(2);
        elements[i++] = getText(3);
        elements[i++] = getText(4);
        elements[i++] = getText(6);
        elements[i++] = "*Multiplayer";
        
        
        
        mainScreen = new UltimateMainMenu(elements) {
            public void itemSelected(int id) {
            }
        };
*/
//     	display.setCurrent(mainScreen);
    }

    
    public static void showHighScoresScreen() {
        Canvas bsscreen = new BestScoresScreen();
        
//        bsscreen.newRecord();
        
       	display.setCurrent(bsscreen);
    }

//    static AnimatedList setScreen;
    
//    static void showSettingsScreen() {
//        display.setCurrent(new AnimatedList(3));
////        String[] elements = new String[texts.length];
////        
//////        for (int i = 0; i<texts.length; i++) elements[i] = texts[i][0];
////    	
////        setScreen = new AnimatedList(getText(3), elements, createImage("/back.png")) {
////            public void itemSelected(int id) {
////                setLangue(0);
////                showMainScreen();
////            }
////        };
////        
////    	setScreen.setSelected(currLang); 
//    	    	    	   	      		       		
////     	setScreen.addCommand(backCommand);
////     	setScreen.addCommand(okCommand);
//       		
////     	setScreen.setCommandListener(this);
//       	
////       	display.setCurrent(setScreen);
//    }
    
    public static void showHelpScreen() {
        display.setCurrent(new AnimatedList(4));
//    	String[] help = {
//            "Controls:",
//            "<, >, 4, 6 - go left, right",
//            "up, 2 - jump",
//            "5 - fire",
//            "*, # - pause"
//        };
//    	
//        AnimatedList helpScreen = new AnimatedList(getText(14), help, createImage("/back.png")) {
//            public void itemSelected(int id) {
//                showMainScreen();
//            };
//        };
//    	
//        helpScreen.setSelected(-1);
//        
//       	display.setCurrent(helpScreen);
    }
    
    static void startMultiplayer() {
        canvas = new MultiPlayerCanvas();
        display.setCurrent(canvas);
        resumeGame();
    }  
    
    static Hashtable images = new Hashtable();
    
    static Image createImage(String imagename) {
        System.out.println("loading image: /images/"+imagename+".png");
    	Image img1;
    	img1 = (Image)images.get(imagename);
    	if (img1!=null) return img1;
    	int[] rgba;
    	try {
			
			img1 = Image.createImage("/images/"+imagename+".png");
//			int w = img1.getWidth(), h = img1.getHeight();
//			rgba = new int[10000/*w*h*/];
//			System.out.println("image data length: "+rgba.length);

//			img1.getRGB(rgba, 1000, 1000/*rgba.length*/, 0, 0, w, h);
			
			
			
//			for (int i = 0; i<rgba.length; i++) {
//				if ((rgba[i]&0x00FF0000)>20) rgba[i] = 0x00FFFFFF;
//				rgba[i] = 0x88FF8888;
//			} 
			
//			img1 = Image.createRGBImage(rgba, w, h, true); 
			
			images.put(imagename, img1);
			
			return img1;
		} catch (IOException e) {
			System.err.print("špatnì naètený obrázek: \""+"/images/"+imagename+".png\"\n");
			e.printStackTrace();
    	} catch (Exception e) {
                        System.err.print("špatnì naètený obrázek: \""+"/images/"+imagename+".png\"\n");
			e.printStackTrace();
    	}
    	return null;
    };
    
    static void continueGame () {
    	storeSettings(StoreManager.SM_LOAD);
        showMessage(Game.getText(9));
	restartLevel(Level.currentLevel);
    }
    
    static void restartLevel(int id) {
        
        try {

            Level.currentLevel = id;
            level = new Level();

            switch (id) {
                case -1:
                    level.initMulti();
                break;
                case 1:
                    level.init1();
                break;
                case 2:
                    showHighScoresScreen();
                break;
                default:
                    System.err.println("level initialization failed. level id:" + id);
            }

        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        resumeGame();
        
    }
    
    static boolean paused = false;
    
    static void resumeGame() {
//        if (paused) { 
          paused = false;
         
          display.setCurrent(canvas);
          canvas.thread = new Thread(canvas);
          canvas.thread.start();
          
  //      }
    }
    
    static void pauseGame() {
        paused = true;
        canvas.thread = null;
        display.setCurrent(new AnimatedList(2));
//        showMessage("*paused");
    }
    
    static void saveGame() {
	Game.storeSettings(StoreManager.SM_SAVE);
//	Game.showMessage(Game.getText(10));
    }
    
    static void newGame () {
	System.out.println(getText(1));        
               
//        level.setLevel(1);
	Agent.score = 0;
	Agent.lives = 3;
        
        restartLevel(1);
    }
    
    static void quitGame () {
        midlet.destroyApp(false);
        midlet.notifyDestroyed();
    }
    
    static Vector messages = new Vector();
    
    static void showMessage (String message) {
    	messages.insertElementAt(message, 0);	
    }
    
    static void paintLastMessages (Graphics g, int x, int y) {
    	g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
    	g.setColor(0x9933BB22);
    	
    	y+=43;
    	x+=3;
    	
    	int i = 5;
    	
    	for (Enumeration e = messages.elements() ; e.hasMoreElements() ;) {
			if (i<1) break;
			i--;
			String s = (String)e.nextElement();
//         	System.out.println(e.nextElement());
	    	g.drawString(s, x, y, Graphics.TOP|Graphics.LEFT);
	    	y-=8;
	    }

    		
    }
    
    static int currLang = 0;
//    
//    private static final String[][] texts = {
//// CZE       0          1             2               3             4              5            6                                                                      
//        {"Èesky",   "Nová hra", "Nejlepší skore",   "Jazyk",    "Instrukce",    "Info",     "Konec"},
//// ENG
//        {"English", "New Game", "High scores",      "Language", "Intructions",  "About",    "Exit"},
//// DEU
//        {"Deutsch", "Neues Spiel", "Highscores",    "Sprache",  "Anwesungen",   "Info",     "Beenden"},
//// ESP
//        {"Espanol", "Partida nueva", "Récords",     "Idoma",    "Instrucciones","Acerca de", "Salir"},
//// FRA
//        {"Francois","Nouvelle partie", "Meillurs scores", "Langue", "Instructions", "A propos", "Quitter"}
//    };
//        
    
    private static String[] texts;
    
    public static void setLangue(int l) {
        currLang = l;
        texts = MySystem.loadTexts("/cesky.txt");
    }
    
    public static String getText(int id) {
//        if (id<texts[currLanguage].length) return texts[currLanguage][id];
        if (id<texts.length) return ""+id+" "+texts[id];
        return "" +id+"!!!";
    }
    
    static void storeSettings(int act)
    {
      StoreManager sm = new StoreManager(1,act);
      
      System.out.println("Storing :");
         
      Level.currentLevel = sm.data(Level.currentLevel);
      Agent.lives = sm.data(Agent.lives);
      Agent.score = sm.data(Agent.score);
      setLangue(sm.data(currLang));
      
      sm.save();
      // Pokud potrebuju potvrdit nacteni
      // mohu pouzit if (sm.loaded)
      // Pri ukladani je vzdy true
    }
    

  
    
    
}
