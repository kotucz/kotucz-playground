/*
 * Fly.java
 *
 * Created on 17. zברם 2006, 9:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author PC
 */
public class Unit {
    
    static Doctor player;

    static Level level = null;
    
    int x, y;
    
    int vx, vy;
    
    int dir = RIGHT; // LEFT/0/RIGHT

    static final int LEFT = -1, RIGHT = 1;
           
    int health = 100;
        
    int type;
   
    int state;
    
    Sprite sprite;
                
    static int GRAVITY = 150;
    
    /** Creates a new instance of Unit */
    public Unit(int type, int x, int y) {
        this.type = type;
        setTile(y, x);
        sprite = new Sprite(Images.dr);
        switch (type) {
            case DOCTOR:
                health = 100;
                break;
        }
    }
    
    public void setX(int x) {
        this.x = 100*x;
    }
    
    public void setY(int y) {
        this.y = 100*y;
    }
    
    
    public void setTile(int tx, int ty) {
        setX(tx*level.tiles.getCellWidth());
        setY(ty*level.tiles.getCellHeight());
    }

    public int getTileX() {
        return x/level.tiles.getCellWidth()/100;
        
    }
    
    public int getTileY() {
        return y/level.tiles.getCellHeight()/100;
        
    }
    
    static final int DOCTOR = 1;
   
    public void paint(Graphics g) {
        draw(g);
    }
            
    boolean onground = false;

    public int getX() {
        return x/100;
    }

    public int getY() {
        return y/100;
    }
    
    public void jump(int v) {
        if (onground) {
            vy = -v;
            onground = false;
        }
    }
    
    
    protected void update(long time) {
        clip();
        move(time);
        switch (dir) {
            case LEFT:
                sprite.setTransform(sprite.TRANS_MIRROR);
            break;
            case RIGHT:
                sprite.setTransform(sprite.TRANS_NONE);
            break;
   
                    
        }
        sprite.setPosition(x/100, y/100);
        
    }

    protected void move(long time) {

//        Game.println("move");
        x += vx*time/100;
        
        if (collides()) {
            if (vx>0) {
                setX((getTileX()+1)*level.tiles.getCellWidth()-sprite.getWidth());
            }
            if (vx<0) {
                setX((getTileX()+1)*level.tiles.getCellWidth());
            }
            
//            vx=0;
            vx = -vx;
        
        } 
        
        onground = false;
        
        vy+=GRAVITY*time/100;
        
        y += vy*time/100;
        
        if (collides()) {
            
            if (vy>0) {
                onground = true;
                
                //setY((getTileY()-1)*level.tiles.getCellHeight());
                while (collides()) {
                    this.y-=100;
                }
                vy= 0;
//                vy = -vy;
            }   
            
            if (vy<0) {
                setY((getTileY()+1)*level.tiles.getCellWidth());
                vy = 0;
            }
            
        } 
            
        
//        x++;
    }
    
    int spritePhase;    
    
    protected void draw(Graphics g) {
        try {
            Image img = Images.n2;
            g.drawImage(img, x, y, g.VCENTER|g.HCENTER);
        } catch (Exception ex) {
            Game.println(""+ex);
            Game.println("ERROR: Fly.draw() type: "+type+" spritephase : "+spritePhase);
        }

    }

    protected void fire(int shottype) {
        Unit shot = new Unit(shottype, this.x, this.y);
        shot.dir = player.dir;
        level.add(shot);
    }
    
/*    protected void fireMissile() {
        Fly shot = new Fly(MISSILE1, this.x, this.y);
        FlyCanvas.add(shot);
    }*/
    
    static Vector targets = new Vector();
              
    private void playersShot() {
        
        x+=dir*5;
        
        for (int i=0; i<targets.size(); i++) {
            Unit tgt = ((Unit)targets.elementAt(i));
            
            if (this.hits(tgt)) {
                
                tgt.damage(5);
               
                //delete();
            }
        }
    }
    

    private void bonusBeh() {
        if (this.hits(player)) {
            switch (type) {
//            case BONUSE:    
//        
//                break;
//            case BONUSR:    
//        
//                break;
//            case BONUSL:    
//                player.health+=50;
                default:
                    Game.println("bonus token");
                break;
            }  
            
            delete();// todo bonustoken..   
            
        }
    }
    
    
    public void missileBeh() {
        Game.println(""+targets.size()+" targets");

//        if (random(6)<2)
//            smoke();
       
        int dx = 0;
        int dy = 0;
        
        for (int i=0; i<targets.size(); i++) {
            Unit tgt = ((Unit)targets.elementAt(i));
            
            if (this.hits(tgt)) {
                
                tgt.damage(50);
                
            } 
            
            if (tgt.y>dy) {
                dx = tgt.x;
                dy = tgt.y;
            }            
        } 
        
        
        
        if (this.x>dx) this.x-=8;
        if (this.x<dx) this.x+=8;
                        
    }
        
    private boolean hits (Unit tgt) {
        return Math.abs(x-tgt.x)<30&&Math.abs(y-tgt.y)<20;
    }
            
    void delete() {
        level.remove(this);
        targets.removeElement(this);
    }
    
    void damage(int force) {
        health-=force;
        if (!isAlive()) kill();
    }
    
    boolean isAlive() {
        return health>0;
    }

/** call when is dead (!alive) ;)
 */     
    void kill() {
        health = 0;
        delete();
    }
    
    /** 
     *  deletes fly when leaves the screen not to 
     */
    void clip() {
        switch (type) {
            case DOCTOR:
                if ((getX()>level.tiles.getWidth()+30)
                ||(getX()<-30)
                ||(getY()>level.tiles.getHeight()+30)
                ||(getY()<-30)) {
                    kill();
                }
                break;
            default:
                if ((x>Level.width+30)
                ||(x<-30)
                ||(y>Level.height+30)
                ||(y<-30)) Game.println("Unit out of screen! do nothing?! type:"+type+" x:"+x+"y:"+y);
            break;
        }
    }
    
    int random(int n) {
        return Level.random(n);
    }
            
    boolean collides() {
        sprite.setPosition(x/100, y/100);
        if ((sprite.getX()<0)||(sprite.getX()+sprite.getWidth()>level.tiles.getWidth())) return true;
        return sprite.collidesWith(level.tiles, false);
    }
    
}

