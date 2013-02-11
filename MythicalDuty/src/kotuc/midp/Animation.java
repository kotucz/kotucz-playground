/*
 * Animation.java
 *
 * Created on 2. kvìten 2006, 18:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package kotuc.midp;

import javax.microedition.lcdui.*;
//import javax.microedition.lcdui.game.*;
//import java.lang.Math;

/**
 *
 * @author PC
 */
public abstract class Animation/* extends Sprite*/{
    
    public Pos pos = new Pos();
//    Loc loc = new Loc();
            
    protected long startt, tout = FURT;
    
    public Image image;
    public Image[] images;
    private int[] frameSequence = null;
    private int frameId;
    
    AnimationManager manager;     
    
    public boolean alive() {
        return (tout==FURT)||(MySystem.getTime()<(startt+tout));
    }
        
    /** Creates a new instance of Animation */
    public Animation() {
        start();
    }
    
    public Animation(String imagename, int c) {
        loadImages(imagename, c);
        start(); 
    }
    
    public void start() {
        startt = MySystem.getTime();
    }
    
    public int getX() {
        return this.getPos().x+manager.getTrans().x;
    }

    public Pos getPos() {
        return pos;
    }

//    public void setImage(Image image) {
//        this.image = image;
//        images = null;
//    }
            
    public int getY() {
        return this.getPos().y+manager.getTrans().y;
    }

    
    
/*    void act0() {
      if ((tout!=FURT)&&(Game.getTime()>(startt+tout))) {
            alive = false;
            finished();
            return;
        }
        act();
    }*/
    
    public void setFrame(int id) {
        frameId = id; 
    }
    
    public void nextFrame() {
        frameId++; 
    }
        
    public void setFrameSequence(int[] seq) {
        this.frameSequence = seq;
    }
    
    public int getFramesCount() {
        return (frameSequence==null)?images.length:frameSequence.length;
    }
    
    public Image getImage() {
        if (images!=null) return images[(frameSequence==null)?frameId%getFramesCount():frameSequence[frameId%getFramesCount()]];
        else return image;
    }
    
    /*  by every callilg of flashing() method decresses by 1 until reach zero
     *  when is negativ the flashing never ends
     */
    public int flashing = 0;
    
    public void flashing(Graphics g) {
        if (flashing==0) return;
        
        if (flashing%4<2) {
            g.setColor(0x00FFFFFF);
            g.fillRect(getX()-25, getY()-50, 50, 50);
        }
        
        flashing--;
    }
    
    public void act() {
        
    }
    
    public static final long FURT = -1;
    
    public void setTimeout(long t) {    
        this.tout = t;
    }
    
       
//    static final int BACK = 2, CENTER = 1, FRONT = 4;
    
//    int align = CENTER; 
    
    public void paint(Graphics g) {
          act();
//        image = getImage();
//        if (image!=null)
          g.drawImage(getImage() , getX(), getY(), g.VCENTER|g.HCENTER);
          flashing(g);
//        switch (align) {
//        case CENTER:
//            g.drawImage(image, x, y, g.VCENTER|g.HCENTER);
//            break;
//        case BACK:
//            g.drawImage(image, x, y, g.LEFT|g.BOTTOM);
//            break;
//        case FRONT:
//            g.drawImage(image, x, y, g.RIGHT|g.TOP);
//            break;
//        }
    }
    
    public void loadImages(String name, int c) {
          int i = 0;
          images = new Image[c];
          for (i=0; i<c; i++) {
              images[i]=MySystem.createImage(name+i+".png");
              
          }
    }
    
    /** 
     *  called when animation ends
     */
    public void finished() {
        
    }
    
/*    private int getX() {
        return startx + (int)(100*Math.cos((double)*(Game.getTime()-startt)/100); 
                
    }
 
    private int getY() {
        return starty + (int)(Game.getTime()-startt)/50;
    }
*/       
}



