package kotuc.midp;

import javax.microedition.lcdui.*;

public abstract class Controller extends javax.microedition.lcdui.Canvas {
    
    /** Creates a new instance of Controller */
    public Controller() {
    }
    
    public void paint(Graphics g) {
        g.drawString("override paint(g) "+this, 50, 50, g.LEFT|g.TOP);
    }
    
    protected void keyPressed(int kc) {
        MySystem.error("override keyPressed() " + this);
    }
    
    protected void pointerPressed(int x, int y) {
        MySystem.error("override pointerPressed() " + this);
    }
    
    public abstract void doWork() ;

    public static final int 
//            LEFT = MyCanvas.LEFT, 
//            RIGHT = MyCanvas.RIGHT,
//            UP = MyCanvas.UP, 
//            DOWN = MyCanvas.DOWN,
//            OK = MyCanvas.FIRE,
//            FIRE = OK,
            SELECT = FIRE,
            BACK = KEY_POUND,
            CANCEL = BACK, 
            NW = KEY_NUM1,
            SW = KEY_NUM4,
            NE = KEY_NUM2,
            SE = KEY_NUM5
    ;
    
    public int getGameAction(int key) {
        switch (key) {
// keys withou changing - have special functions
            case NW:
            case SW:
            case NE:
            case SE:
            
            case SELECT:
            case BACK:
                
                return key;
            
            default: 
      
             return super.getGameAction(key);
        }
    }
}


