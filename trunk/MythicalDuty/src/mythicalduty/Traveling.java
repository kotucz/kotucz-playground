/*
 * Traveling.java
 *
 * Created on 30. kvìten 2006, 18:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package mythicalduty;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.figures.EvilFigure;

/**
 *
 * @author PC
 */
public class Traveling extends Controller {
    
    /**
     * Creates a new instance of Traveling
     */
    Traveling() {
        loadStages();
        
//        traveler = new Traveler();
//        setStage(new Stage(0));
    }
    
    static Traveler traveler = new Traveler();
    
    Stage stage;
        
    AnimationManager anims;
    
    Image shade = MyCanvas.createShade(0x55000000);
    
    public void paint(Graphics g) {
        if (shade==null) {
            g.setColor(0);
            g.fillRect(0, 0, MyCanvas.width-1, MyCanvas.height-1);
        } else g.drawImage(shade, 0, 0, g.LEFT|g.TOP);
        anims.setTrans(80-traveler.getPos().x, 80-traveler.getPos().y);
        anims.paint(g);
    }
    
    public void doWork() {

    }

    protected void setStage(Stage stage) {
        this.stage = stage;
        anims = new AnimationManager();
        anims.addAnimation(stage);
        anims.addAnimation(traveler.getAnim());
    }
    
    public void keyPressed(int key) {
        Traveler t = traveler;
        switch (getGameAction(key)) {
        case NW:
        case LEFT:
            t.go(1);
            break;
        case NE:
        case UP:
            t.go(0);
            break;
        case SE:
        case RIGHT:
            t.go(3);
            break;
        case SW:
        case DOWN:    
            t.go(2);
            break;
        case FIRE:
            Game.startFight();
            break;
        case BACK:
            Game.showMainScreen();
            break;
        }
        
    }
    
    protected void pointerPressed(int x, int y) {
        if (y<5) Game.showMainScreen();
        Loc dest = stage.cursorLoc(x, y);
        if (stage.travelerEntered(dest)) traveler.setLoc(dest.a, dest.b);
    }
    
    static Stage[] stages = new Stage[10];
    
    static void loadStages() {
        MySystem.print("loadingStages .. ");
                
        stages = new Stage[10];
        
        String[] lines = MySystem.loadTexts("/maps/stages.txt");
        
        int[] ints=null;
        
        String s = null;
        
        String[] param = null;
        
        int sid = -1;
        int line = 0;
        
        int b=0;
        
        try {
        for (line = 0; line<lines.length; line++) {
            s = lines[line];
            if (s == null) continue;

            //Game.canvas.notify();
                        
            param = MySystem.getWords(s);
            if (param.length==0) continue;
                
            ints = MySystem.getInts(s);
            
            if (param[0].equals("stage")) {
               sid=Integer.parseInt(param[1]);
               MySystem.println("stage "+sid+" :");
                    
               if (stages[sid]==null) {
                   stages[sid] = new Stage();
                   b = 0;
               } else MySystem.error("duplicate definition of stage "+sid+"! line: "+line);
                    
               stages[sid].asize = ints[1];
               stages[sid].bsize = ints[2];
                
               stages[sid].tiles = new int[ints[1]][ints[2]];
            } else if (param[0].equals("b")) {               
                if (b==0) MySystem.print("tiles ");
                MySystem.print("-" + b);
                
                String[] params = MySystem.getWords(s);
                for (int a = 0; a<params.length-1; a++) {
                    stages[sid].tiles[a][b]=Integer.parseInt(params[a+1]);
                }
                
                b++;
            } else if (param[0].equals("exit")) {
                stages[sid].addExit(ints[0], ints[1], ints[2], ints[3], ints[4]);
            } else {
                MySystem.error("stages.txt parsing error. illegal argument: "+param[0]+". line: "+line);
            }
        }
               
        } catch (Exception ex) {
                MySystem.error("stages.txt parsing error. line: "+line);
                ex.printStackTrace();
        }
        
        MySystem.println("OK");
    }
    
    
    static Stage getStage(int i) {
        if (stages[i]==null) {
            MySystem.error("cannot load stage "+i);
        }
        return stages[i];
    }
    
    static void setLocation(int sceneId, int x, int y) {
        if (Game.traveling==null) Game.traveling = new Traveling();
        Game.traveling.setStage(getStage(sceneId));
        Game.traveling.traveler.setLoc(x, y);
    }
    


    
}

