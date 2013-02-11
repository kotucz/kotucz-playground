package mythicalduty;

import java.io.*;
import javax.microedition.lcdui.*;
//import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

//import javax.microedition.media.*;
//import javax.microedition.media.control.ToneControl;

import kotuc.midp.*;
import mythicalduty.attacks.*;
import mythicalduty.figures.*;

public class Fight extends Controller/* implements Runnable*/ {
//    static int width;
//    static int height;
//    
//    static Thread thread;
//    static Image shade;
//    
//   /** frames per second */
//    private static final int FPS = 16;

    GoodFigure[] good;
    EvilFigure[] evil;
    
//    /** The Tone good */
//    private Player tonePlayer;
//    /** The ToneController */
//    private ToneControl toneControl;
//    /** Tune to play when puzzle level is solved. */
//    static byte[] shotTune = {
//	ToneControl.VERSION, 1,
//	74, 8,	// 1/8 note
//	75, 8,
//	73, 8
//    };
//
//    /** Tune to play when a packet enters a store */
//    static byte[] storedTune = {
//	ToneControl.VERSION, 1,
//	50, 8,	// 1/8 note
//	60, 8,
//	70, 8
//    };
//
    
    Fight() {
//	super(false); // Don't suppress key events
/*      TODO unbracket
        try {
            setFullScreenMode(true);
        } catch (Exception e) {
            Game.error(this + "cannot switch to fullscreen");
        }
*/                 
//        height = getHeight();
//	width = getWidth();
//        shade = shade();
    }

    Stage stage;
    
    void init() {
        anims = new AnimationManager();       
        
        stage = Game.getFightStage(1);
        anims.addAnimation(stage);
        anims.setTrans(MyCanvas.width/2, 30);        
        
        try {
            if (evil == null) MySystem.error("Error fight.init() evil null");
            if (good == null) MySystem.error("Error fight.init() good null");
            
            for (int i = 0; i<evil.length; i++) {
//                evil[i].id=i;
//                evil[i].anim.pos = new Pos(width-20-i*30, 60);
                evil[i].energyNeeded = 3000+1000*MySystem.random(10);
                evil[i].setLoc(0+i, evil.length-i);
                anims.addAnimation(evil[i].getAnim());                
            
            }

            for (int i = 0; i<good.length; i++) {
//                good[i].id=i;
//                good[i].anim.pos = new Pos(20+i*30, height - 50);;
                good[i].energyNeeded = 1000*(1+MySystem.random(7));
                
                good[i].setLoc(3+i, 3+good.length-i);
                anims.addAnimation(good[i].getAnim());
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        anims.addAnimation(new AttackSelectAnimation());
        
    	System.out.println("..Initialized");
        
             
        selectAttack();
        
//      resumeFight();
    
    }
    
    protected void keyPressed(int key) {
        int ga = getGameAction(key);
        
        if (ga ==BACK) {
            Game.showMainScreen();
        }
        
//        if (!Attack.animating)
        if (selectedAttack==null) {
// select hero and attack            
            if ((ga == LEFT) && (selectedHeroId>0)) {
                selectedHeroId--;
            } else if ((ga == RIGHT) && (selectedHeroId<good.length-1)) {
                selectedHeroId++;
            } 
            
            heroSelected(selectedHeroId);
//            stage.cursor = good[selectedHeroId].loc;
            
            if (ga == DOWN) { 
                selectedHero().nextAttack();
            } else if (ga == UP) {
                selectedHero().prevAttack();
            } else if (ga == FIRE) {
                attackSelected(selectedHero().attacks[selectedHero().attackId]);
            }
            
            
        } else {
// select target            
            if (ga == LEFT) {
                selectedAttack.prevTarget();
            } else if (ga == RIGHT) {
                selectedAttack.nextTarget();
            } else if (ga == FIRE) {                
                doAttack();
            } 
            else {
                selectAttack();
            }
 //           stage.cursor = selectedAttack.target.loc; 
        }
        
//        if (keyCode == KEY_NUM9) addAnimation(new FloatingString("KOTUC JE KANEC!!!", 0x00FF00, new Pos(50, 50)));
//        else if (keyCode == KEY_NUM8) new Attack(good[0], evil[0]);
    }
        
    protected void pointerPressed(int x, int y) {
        if (MyCanvas.height-5<y) Game.showMainScreen();
        Loc cursor = stage.cursorLoc(x, y);
        MySystem.println("pointer pressed "+cursor);
        
        if (selectedAttack!=null) {
// select target            
            MySystem.println("selecting target:");
//            if ((selectedAttack.target!=null)&&cursor.equals(selectedAttack.target.loc)) doAttack();
            if ((selectedAttack.target!=null)&&selectedAttack.target.getAnim().collidesWith(x, y)) {
                doAttack();
                return;
            }
         
            for (int i = 0; i<selectedAttack.targets.length; i++) {
//            if (selectedAttack.targets[i].loc.equals(cursor)) {
            if (selectedAttack.targets[i].getAnim().collidesWith(x, y)) {
                targetSelected(i);
                return;
            };
            }
            selectAttack();
        } else {
// select hero and/or attack

                 
            MySystem.println("selecting hero:");
            for (int i = 0; i<good.length; i++) {
//                if ((good[i].loc.equals(cursor))/*&&good[i].ready()*/) {
                if (good[i].getAnim().collidesWith(x, y)) {
                    
                    heroSelected(i);
                    return;
                }
            }
            
            if (x<50) {
                MySystem.println("selecting attack:");
                int i = (/*MyCanvas.height-*/y)/16;
                try {
                    attackSelected(selectedHero().attacks[i]);
                    return;
                } catch (ArrayIndexOutOfBoundsException e) {}
            }   
        }
        
    }
    
    Image shade = MyCanvas.createShade(0x55004400);
    
/**
     * Paint the contents of the MyCanvas.
     * The clip rectangle of the canvas is retrieved and used
     * to determine which cells of the board should be repainted.
     * 
     * @param g Graphics context to paint to.
     */
    public void paint(Graphics g) {
//	flushGraphics();
       if (shade!=null) {
            g.drawImage(shade, 0, 0, g.TOP|g.LEFT);
       } else {
           g.setColor(0x22000000);
           g.fillRect(0, 0, MyCanvas.width-1, MyCanvas.height-1);
       }
        anims.paint(g);

    
        g.setColor(0xDDDDDD);
        g.drawString(speedFPSString, 2, 2, g.TOP|g.LEFT);           		
        
        g.setFont(Game.MENU_FONT);
        
        if (isLose()) {
            g.setColor(0x00FF0000);
            g.drawString("*YOU LOSE", MyCanvas.width/2, MyCanvas.height/2, g.HCENTER|g.BOTTOM);           		
        }
        
        if (isWin()) {
            g.setColor(0x0000FF00);
            g.drawString("YOU WON", MyCanvas.width/2, MyCanvas.height/2, g.HCENTER|g.BOTTOM);           		
            
        }
        
        Game.paintLastMessages(g, 0, 16);
	
        g.setColor(0x0000FFFF); 
        g.drawRect(0, 0, MyCanvas.width-1, MyCanvas.height-1);
        
//          Game.level.agent.paintInfoBar(g, width, height);

    }

     
   
    /**
     * The canvas is being displayed.
     * Stop the event handling and animation thread.
     */
//    protected void showNotify() {
////        resumeFight(); 
//    }
//
//    void resumeFight() {
//       Game.display.setCurrent(this);
//       thread = new Thread(this);
//       thread.start();
//       anims.addAnimation(new FloatingString("let the FIGHT begin!", 0xFF2233, new Pos(100, 80)));
//    }
//    
//    void pauseFight() {
//       thread = null;
//    }
//    
//    protected void hideNotify() {
//       pauseFight();
//    }

    
    static String speedFPSString = "";
    
    int timeInc = 0;
    
    static Graphics g;
   
//    /**
//     * The main event processor. Events are polled and
//     * actions taken based on the directional events.
//     */
//    public void run() {
////	g = getGraphics(); // Of the buffered screen image
//        Thread mythread = Thread.currentThread();
//        
//        
//        
//	// Loop handling events
//	while (mythread == thread) {
//	    try { // Start of exception handler
//
//            
//            long runStart = System.currentTimeMillis();    
//            
//// Check user input and update positions if necessary
//
////          int keyState = getKeyStates();
//                
//	    doWork();
//            
//                        
//            if (mythread == thread) {
//                repaint();
//                serviceRepaints();
//            }
//                       
//            
//            if (isWin());// Game.showMainScreen();
//            if (isLose());// Game.showMainScreen();
//            
//            long runEnd = System.currentTimeMillis();          
//            timeInc = (int)(runEnd - runStart);
//            long runSlow = 1000/FPS - timeInc;
//            if (runSlow < 0) runSlow = 0;
//            
//            Game.time += runEnd - runStart + runSlow;
//            
//            try {
//                speedFPSString = 1000/timeInc+"fps";
//                
////              System.out.println(speedFPSString);
//            } catch (Exception e) {speedFPSString = "  fps";}               
//                         
//            try { Thread.sleep(runSlow); } catch (Exception e) {} 
//		         
//                
////            if (mythread == thread) {
////                flushGraphics();
////            }
//
//
//        } catch (Exception e) {
//		e.printStackTrace();
//	    }
//	}
//
//     }

    protected AnimationManager anims;
    
    public void addAnim(Animation a) {
        anims.addAnimation(a);
    }
    
//    LayerManager layers = new LayerManager();
        
//    Pos ve = new Pos(1, 0);
    
    Loc cursor = new Loc();
    
    public void doWork() {
        
//        MySystem.println("work");
/*        
        Pos v = anims.getTrans();
        anims.setTrans(v.x+ve.x, v.y+ve.y);
        
        if ((v.x<-50)||(v.y<0)||(v.x>150)||(v.y>50)) {
            int i = ve.x;
            ve.x = ve.y;
            ve.y = -i;
        }
*/                
        if (isWin()) 
            Game.travel();// Game.showMainScreen();

        if (isLose()) Game.gameOver();// Game.showMainScreen();

        
//        good[0].addEnergy();
        
        if (!Attack.animating) {
            for (int i = 0; i<good.length; i++) good[i].addEnergy();
            enemyInteligence();
        }
    }    
    
//    final static int DONT_SELECT = 0 , HERO_SELECT = 1, ATTACK_SELECT = 2, TARGET_SELECT = 3; 
//    int state = 0;
    
    int selectedHeroId = 0;
    public GoodFigure selectedHero() {
        return good[selectedHeroId];
    }
    
    void heroSelected(int id) {
        selectedHeroId = id;
//        this.selectedHero = this.good[id];

//        showHeroIcons();
//        state = HERO_SELECT;
//        selectedHero = null;
//        if (selectedHeroId < 0) selectedHeroId = 0;
    }
    
//    void heroSelected() {
//        selectedHero = good[selectedHeroId];
//        selectAttack();
//    }
    
    
    public Attack selectedAttack;    
    void selectAttack() {
        selectedAttack = null;
//        state = ATTACK_SELECT;
        heroSelected(selectedHeroId);
        
//      attackSelected();
    }
    
    void attackSelected(Attack a) {
        selectedAttack = a;
        selectTarget();
    }
          
    void selectTarget() {
//        state = TARGET_SELECT;
        
        if ((selectedAttack.getTargetType()&Figure.GOOD)!=0) 
                selectedAttack.targets = good;
                else selectedAttack.targets = evil;
        
        if ((selectedAttack.target==null)||(!selectedAttack.target.alive())) selectedAttack.prevTarget();
        
            
        
    }
    
    
    void targetSelected(int id) {
        selectedAttack.targetId = id;
        selectedAttack.target = selectedAttack.targets[id];
    }
    
    void doAttack() {
        if (selectedAttack.isPossible()) {    
                selectedAttack.attack();
               
                selectedAttack = null;
                selectAttack();
        }
    }
    
    private boolean shown = false;

    public Stage getStage() {
        return stage;
    }
    void showHeroIcons() {
//        if (shown) return;
//        for (int i = 0; i<good.length; i++) {
//            anims.addAnimation(new HeroIcon(good[i]));
////            addAnimation(new StatusAnimation(good[i]));;
//        }
//        
//        shown = true;
    }
    
    void enemyInteligence() {
        for (int i = 0; i<evil.length; i++) {
        try{
            EvilFigure e = evil[i];
            
            if (!e.alive()) continue;
            
            e.addEnergy();
            
            if (!e.ready()) continue;  
            
//            Game.error("evil selected "+i);
                           
            Attack a = evil[i].attacks()[0];
//            Game.error("attack selected "+a);
            a.setTarget(good[MySystem.random(good.length)]);
//            Game.error("target selected "+a.target);
            a.attack();
        } catch (Exception ex) {ex.printStackTrace();};         
        }
        
        
    }

    boolean isWin() {
        for (int i = 0; i<evil.length; i++) {
            if (evil[i].alive()) return false;
        }
        return true;
    }
    
    boolean isLose() {
        for (int i = 0; i<good.length; i++) {
            if (good[i].alive()) return false;
        }
        return true;
    }
    
    void tryEscape() {
        if (MySystem.random(evil.length+1)==0) Game.travel();
    }
    

    
//    /**
//     * Play the simple tune supplied.
//     */
//    void play(byte[] tune) {
//	try {
//	    if (tonePlayer == null) {
//		// First time open the tonePlayer
//		tonePlayer = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
//		tonePlayer.realize();
//		toneControl = (ToneControl)tonePlayer.getControl("javax.microedition.media.control.ToneControl");
//	    }
//	    tonePlayer.deallocate();
//	    toneControl.setSequence(tune);
//	    tonePlayer.start();
//	} catch (Exception ex){
//	    e.printStackTrace();
//	}
//    }
//   
   
    /*
     * Close the tune good and release resources.
     */
//    void closePlayer() {
//	if ( tonePlayer != null ) {
//	    toneControl = null;
//	    tonePlayer.close();
//	    tonePlayer = null;
//	}
//    }
}
