package mythicalduty;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.figures.Figure;

public class Traveler extends Figure {
    
    public Traveler () {
        getAnim().loadImages("/figures/travlr", 2);
        
        getAnim().setFrameSequence(new int[] {0, 1, 1, 0 /*0, 1, 2, 3*/});
    }
    
    private int dir = 0;
    
    void setDirection(int d) {
        this.dir = d;
        this.getAnim().setFrame(dir);
    }
    
    static int [][] dirmatrix = new int [][] {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
    
    void go(int dir) {
        setDirection(dir);
        
        Loc nl = new Loc(getLoc());
        nl.a += dirmatrix[dir][0];
        nl.b += dirmatrix[dir][1];
        
        if (getStage().travelerEntered(nl)) setLoc(nl.a, nl.b);
        
//        if (nl!=null) loc = nl;
    }
    
    public Stage getStage() {
        return Game.traveling.stage;
    }
    
//    
//    void act() {
//        this.pos = stage.getPos(loc);
//    }
    

}
	