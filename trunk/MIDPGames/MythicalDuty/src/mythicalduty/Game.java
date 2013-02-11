package mythicalduty;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.io.*;

import java.util.*;
import java.util.Random;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Date;
import mythicalduty.*;

import kotuc.midp.*;
import mythicalduty.figures.*;


public class Game extends MIDlet/* implements CommandListener*/ {
    public static Display display;
    public static Fight fight;
    static Game midlet;
    static MyCanvas canvas;
    static Traveling traveling;   
    static GoodFigure[] heroes;
    
    public static boolean slowFightModeEnabled = false;
    
    /**
     * Creates new PushPuzzle MIDlet.
     */
    public Game() {
    	MySystem.print("creating midlet .. ");
        
        StoreManager.recordIdentifier = "MythicalDutySaveStore";
        AnimatedList.setFont(MENU_FONT);         
        
        midlet = this;
        display = Display.getDisplay(this);
        
        MySystem.println("OK");
                
    }

/*    
MyCanvasCanvas getCanvas() {
    try {
      // existuje-li javax.microedition.lcdui.game.GameCanvas vime,
      // ze se jedna o MIDP2
      Class.forName("javax.microedition.lcdui.game.GameCanvas");
      reMyCanvasMyCanvas)(Class.forName("MIDP2Canvas")).newInstance();
    } catch(Exception exception) {
      try {
        // existuje-li FullCanvas, pak jde o Nokii
        Class.forName("com.nokia.mid.ui.FullCanvas");
        MyCanvas (MyCanvas)(Class.forName("NokiaCanvas")).newInstance();
      } catch (Exception e) {
        return new MIDP1Canvas();
      }
    }
  }*/
    
//    Timer timer;
    
    /**
     * Start creates the thread to do the timing.
     * It should return immediately to keep the dispatcher
     * from hanging.
     */
    public void startApp() {	
        MySystem.print("start App  .. ");
        
        canvas = new MyCanvas();
/*        timer = new Timer();
        timer.schedule(new TimerTask () {
            public void run() {
                Game.canvas.run();
            }
        }
        , 50, 50);        
        */        
        storeSettings(StoreManager.SM_LOAD);

        showLoadingScreen();
                
        display.setCurrent(canvas);     
        
        
                
        MySystem.loadTexts("/maps/stages.txt");
        
//        if (currLanguage<0) showLanguageScreen();
                
//        display.setCurrent(fight);
//        fight.init();
	showMainScreen();
        MySystem.println("OK");
    }

    /**
     * Pause signals the thread to stop by clearing the thread field.
     * If stopped before done with the iterations it will
     * be restarted from scratch later.
     */
    public void pauseApp() {
//        pauseGame();
    }

    /**
     * Destroy must cleanup everything.
     * Only objects exist so the GC will do all the cleanup
     * after the last reference is removed.
     */
    public void destroyApp(boolean unconditional) {
    	  	
    	storeSettings(StoreManager.SM_SAVE);
    	        
        display.setCurrent((Displayable)null);

    }

    /**
     * Respond to a commands issued on any Screen
     */
/*    public void commandAction(Command c, Displayable s) {
        MySystem.error("commandAction: "+c+" from "+s);
    }*/
    
    private void showIntroScreen() {
        showLoadingScreen();
//	display.setCurrent(new Intro());
//        try {
//        	Thread.sleep(3000);	
//        } catch (Exception ex) {
//        }
    }
    
    static final Font MENU_FONT = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    static final int SELECTED_COLOR = 0x00FF5555;

    static AnimatedList mainScreen;
       
    static void showMainScreen() {
                
        String[] elements;
        int i = 0;
        
        if (paused()) {
            elements = new String[7];
            elements[i++] = "*resume";
        } else {
            elements = new String[6];
        }
        
        elements[i++] = getText(1);
        elements[i++] = getText(3);
        elements[i++] = getText(2);
        elements[i++] = getText(8);
        elements[i++] = getText(4);
        elements[i++] = getText(5);
        
        mainScreen = new AnimatedList(getText(10),  elements, null) {
            public void itemSelected(int id) {
                if (!paused()) id++;
                switch (id) {
                    case 0: travel(); break; // todo !
                    case 1: newGame(); break;
                    case 2: showSaveLoadScreen(StoreManager.SM_LOAD);  break;
                    case 3: if (paused()) showSaveLoadScreen(StoreManager.SM_SAVE);  break;
                    case 4: showSettingsScreen(); break;
                    case 5: showHelpScreen(); break;
                    case 6: quitGame(); break;
                    default: showMainScreen();    
                }
            }
        };

        canvas.setController(mainScreen);
    }

    static AnimatedList setScreen;
    
    static void showSettingsScreen() {
        String[] elements = new String[2];
        elements[0] = getText(9);
        elements[1] = getText(7);
//      for (int i = 0; i<texts.length; i++) elements[i] = texts[i][0];
    	
        setScreen = new AnimatedList(getText(8), elements, createImage("/back.png")) {
            public void itemSelected(int id) {
                switch (id) {
                case 0:
                    showLanguageScreen();
                break;
                case 2:
                    showMainScreen();
                break;
                }
            }
        };
        
    	setScreen.setSelectedIndex(currLanguage); 
    	    	    	   	      		       		
        canvas.setController(setScreen);
    }
    
    static String[] langNames = {"cesky", "english"};//, "deutsch", "espanol", "francois"}; 
    
    static void showLanguageScreen() {
        String[] elements = new String[langNames.length+1];
        
        for (int i = 0; i<langNames.length; i++) elements[i] = langNames[i];
        elements[elements.length-1]=getText(7);
    	
        setScreen = new AnimatedList(getText(9), elements, createImage("/back.png")) {
            public void itemSelected(int id) {
                if (id<0&&id>langNames.length) return;
                if (id==langNames.length) {
                    showSettingsScreen();
                    return;
                }
                showLoadingScreen();
                currLanguage = id;
                storeSettings(StoreManager.SM_SAVE);
                texts = null;
                showMainScreen();
            }
        };
        
    	setScreen.setSelectedIndex(currLanguage); 
	
//     	display.setCurrent(setScreen);
        canvas.setController(setScreen);
    }
    
    static int slmode;
    static void showSaveLoadScreen(int mode) {
        slmode = mode;
        String[] elements = new String[9];
        for (int i = 0; i<8; i++) 
            elements[i] = ""+(i+1)+": "+((saveDates[i]!=0)?(new Date(saveDates[i]).toString()):(getText(11)));//"slot "+(i+1);
    	elements[8] = getText(7);
        
        String title = (mode==StoreManager.SM_SAVE)?getText(2):getText(3);
        
        AnimatedList slScreen = new AnimatedList(title, elements, createImage("/back.png")) {
            public void itemSelected(int id) {
                if (0<=id&&id<8) storeGame(id, slmode);
                else showMainScreen();
            }
        };
        
        slScreen.setSelectedIndex(0); 
	
//     	display.setCurrent(slScreen);
        canvas.setController(slScreen);
    }
    
    static LoadingScreen loading;// = new LoadingScreen();
    
    static void showLoadingScreen() {
        canvas.setController(new LoadingScreen());
        Thread.yield();
//        canvas.repaint();
//        canvas.serviceRepaints();
//        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
//        try {
//            Thread.sleep(100);
//        } catch (Exception ex) {}
//        display.setCurrent(new Form("Loading..."));
    }
    
    public static void showHelpScreen() {
    	String[] help = {
            "Controls"            
        };
    	
        AnimatedList helpScreen = new AnimatedList(getText(14), help, createImage("/back.png")) {
            public void itemSelected(int id) {
                showMainScreen();
            };
        };
    	
        helpScreen.setSelectedIndex(-1);
        
//       	display.setCurrent(helpScreen);
        canvas.setController(helpScreen);
    }
    
    public static Image createImage(String imagename) {
        return MySystem.createImage(imagename);
    }
    
    static boolean paused() {
        return fight!=null||traveling!=null;
    }  
    
    static void resumeGame() {
//        display.setCurrent(fight);
//        paused = false;
//        fight.resumeFight();
//        canvas.setController(c);
//        canvas.resume();
//        display.setCurrent(canvas);
        if (fight!=null) canvas.setController(fight);
        else if (traveling!=null) canvas.setController(traveling);
    }
    
   /* static void pauseGame() {
//        paused = true;
//        fight.pauseFight();
        canvas.pause();
        showMainScreen();
        MySystem.println(getText(6));
    }
 */   
//    static void saveGame() {
//	
//        storeSettings(StoreManager.SM_SAVE);
//	MySystem.println("OK");
//    }
    
    static void newGame () {
	showLoadingScreen();
        MySystem.println(getText(1));
        heroes = new GoodFigure[2]; 
        heroes[0] = (Figure.createHero());
        heroes[1] = (Figure.createHero());
        
//        startFight(new EvilFigure[3]);
        Traveling.setLocation(0, 5, 5);
        travel();
        
//        resumeGame(traveling);
        
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
    
    static int currLanguage = 0;
    /*
    private static final String[][] texts = {
// CZE       0          1             2               3             4              5            6                                                                      
        {"Èesky",   "Nová hra", "Nejlepší skore",   "Jazyk",    "Instrukce",    "Info",     "Konec"},
// ENG
        {"English", "New Game", "High scores",      "Language", "Intructions",  "About",    "Exit"},
// DEU
        {"Deutsch", "Neues Spiel", "Highscores",    "Sprache",  "Anwesungen",   "Info",     "Beenden"},
// ESP
        {"Espanol", "Partida nueva", "Récords",     "Idoma",    "Instrucciones","Acerca de", "Salir"},
// FRA
        {"Francois","Nouvelle partie", "Meillurs scores", "Langue", "Instructions", "A propos", "Quitter"}
    };
       */ 
    
    private static String[] texts;
    
    public static String getText(int id) {
        id--;
        if (texts==null) texts = MySystem.loadTexts("/langs/"+currLanguage+"/menu.txt");
        try {
            if (null==texts[id]) return "chyba id "+id; 
            return texts[id];
        } catch (Exception ex) {
            ex.printStackTrace();
            return "chyba id "+id;
        }
        
    }
    

        
    

    protected static long[] saveDates = new long[8]; 
    
    static void storeSettings(int act)
    { 
      showLoadingScreen();
        
      MySystem.print("Storing settings .. ");  
    
      StoreManager sm = new StoreManager(8,act);
      
      currLanguage = sm.data(currLanguage);
//      slowFightModeEnabled = sm.data(slowFightModeEnabled);
            
      Settings.gameSpeed = sm.data(Settings.gameSpeed);
      
      for (int i = 0; i<8; i++) {
          saveDates[i] = sm.data(saveDates[i]);
      }
      
      sm.save();
      // Pokud potrebuju potvrdit nacteni
      // mohu pouzit if (sm.loaded)
      // Pri ukladani je vzdy true
      
      MySystem.println("OK");
    }

    
    
    static void storeGame(int slot, int act)
    {  
      showLoadingScreen();
        
      MySystem.print("Storing Mythical Duty. slot"+(slot+1)+" .. ");
        
      StoreManager sm = new StoreManager(slot,act);
      
      if (act==sm.SM_LOAD) {
          fight=null;
      }      
      
      GoodFigure[] oldHeroes = heroes;
      if (oldHeroes==null) {
          
          oldHeroes=new GoodFigure[4];
          oldHeroes[0] = Figure.createHero();
          oldHeroes[1] = Figure.createHero();
          oldHeroes[2] = Figure.createHero();
          oldHeroes[3] = Figure.createHero();
      }
      
      heroes = new GoodFigure[sm.data(oldHeroes.length)];
 
      for (int i = 0; i<heroes.length; i++) {
//        Thread.yield();
          MySystem.print("/");
          heroes[i] = Figure.createHero();
          heroes[i].skilllevel = sm.data(oldHeroes[i].skilllevel);
          MySystem.print("\\");
          heroes[i].experience = sm.data(oldHeroes[i].experience);
          MySystem.print("/");
          heroes[i].hitpoints = sm.data(oldHeroes[i].hitpoints);
          MySystem.print("\\");
          heroes[i].maxhitpoints = sm.data(oldHeroes[i].maxhitpoints);
          MySystem.print("/ ");
      }
      
      if (sm.data(traveling!=null)) {
          traveling.setLocation(sm.data(1), sm.data(Traveling.traveler.getLoc().a), sm.data(Traveling.traveler.getLoc().b));
          travel();
      }
          
      
//      if (sm.data(fight!=null)) startFight(new EvilFigure[3]);
        
      sm.save();
      
      // Pokud potrebuju potvrdit nacteni
      // mohu pouzit if (sm.loaded)
      // Pri ukladani je vzdy true
      
      Game.canvas.notify();
      
      MySystem.println("OK");
      
      if (act==sm.SM_SAVE) saveDates[slot]=System.currentTimeMillis();
      
      storeSettings(StoreManager.SM_SAVE);  
      
      if (paused()) resumeGame(); else showMainScreen();
    }
    
  
    static void startFight(EvilFigure[] enemies) {
        fight = new Fight();
                        
        fight.good = heroes; 
        fight.evil = enemies;
        
        fight.init();

        canvas.setController(fight);
    }

    static void startFight() {
        EvilFigure[] enemies = new EvilFigure[MySystem.random(3)+1];
        
        for (int i =0; i<enemies.length; i++) switch (MySystem.random(3)) {
            case 0:
            enemies[i] = Figure.createNightmare();
            break;
            case 1:
            enemies[i] = Figure.createAlien();
            break;
            case 2:
            enemies[i] = Figure.createMud();
            break;
            default:
            enemies[i] = Figure.createMud();
        }
        
                
        startFight(enemies);
    }
    
    static void endFight() {
        fight = null;
        travel();
    }
    
    static void travel() {
        if (traveling==null) traveling = new Traveling();
        canvas.setController(traveling);
    }
    
    static void gameOver() {
        showMainScreen();
    }

    static Stage getFightStage(int i) {
        switch (i) {
         
        case 1:
        return new Stage(2) {
            public boolean travelerEntered(Loc loc) {
                return true;
            }
        };
        
        }
        return null;
    }
    
}
