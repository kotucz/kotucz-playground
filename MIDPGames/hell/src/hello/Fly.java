/*
 * Fly.java
 *
 * Created on 17. zברם 2006, 9:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package hello;

import javax.microedition.lcdui.*;
import java.util.*;

/**
 *
 * @author PC
 */
public class Fly {
    
    static Fly player;

    static FlyCanvas canvas = null;
    
    int x, y;
            
    int dir = STRAIG;

    static final int LEFT = -1, STRAIG = 0, RIGHT = 1;
           
    int speed = SLOW;
    
    static final int STOP = 4, SLOW = 0, FAST = -6;
    
    int health = 1;
    
    int empshots = 3;
    int missiles = 3;
    
    int type;
    
    Fly target;
    
    /** Creates a new instance of Fly like flying into the scene */    
    public Fly(int type, int x) {
        this(type, x, -30);
    }
    
    /** Creates a new instance of Fly */
    public Fly(int type, int x, int y) {
        this.type = type;
        this.y = y;
        this.x = x;
        switch (type) {
            case BONUSE:
            case BONUSR:
            case BONUSL:
                speed = 5;
                break;
            case SHOT1:
                speed = -17;
                break;
            case MISSILE1:
                speed = -15;
                break;
            case SMOKE:
                speed = -3;
                break;
            case ESHOT1:
                speed = 15;
                break;
            case ENEMYM1:
                speed = 2;
                dir = 1;
                health = 20;
                targets.addElement(this);
                break;
            case BOSS1:
                speed = 1;
                dir = 1;
                health = 120;
                targets.addElement(this);
                break;
            case PLAYER1:
                speed = 0;
                health = 100;                
                break;
            case TARGET1:
                speed = 3;
                health = 10;
                targets.addElement(this);
                break;
        }
    }
    


    static final int 
            SHOT1 = 1, 
            SHOT2 = 4,
            SHOT3 = 5,
            PLAYER1 = 2,
            TARGET1 = 3,
            ENEMYM1 = 6,
            ENEMYL1 = 7,
            EXPL1 = 8,
            MISSILE1 = 9,
            ESHOT1 = 10,
            BOSS1 = 11,
            SMOKE = 12,
            BONUSE = 13,
            BONUSL = 14,
            BONUSR = 15,
            EMP1 = 16
            ;
   
    public void paint(Graphics g) {
        act();
        draw(g);
    }
            
    
    protected void act() {
        clip();
        switch (type) {
            case SHOT1:
            case SHOT2:
            case SHOT3:
                playersShot();
             break;
        
            case ESHOT1:
                enemyShot();
             break;
             
            case BONUSE:
            case BONUSR:
            case BONUSL:
                bonusBeh();
             break;
             
            case ENEMYM1:
            case BOSS1:
//                if (isAlive()) {
                    mediumEnemy();
//                } else kill();
                
             break; 
             
            case PLAYER1:    
                playerBeh();
            break;
            
            case MISSILE1:    
                missileBeh();
            break;
            
            case EXPL1:    
                if (spritePhase > 12) delete(); // does not influence the paint this round
            break;
            
            case EMP1:    
                if (spritePhase > 12) {
                    delete(); // does not influence the paint this round
                    target.damage(50);
                }
            break;
                       
            case SMOKE:    
                if (spritePhase > 10) delete(); // does not influence the paint this round
            break;
                        
            case TARGET1:    

//            if (isAlive())
            switch (dir) {
                case LEFT:
                case RIGHT:
                case STRAIG:
                    this.x+=5*dir;
                break;
                
                    
            }// else kill();
            break;
            default:
            break;
        }
        y+=speed;
    }

    int spritePhase;    
    
    protected void draw(Graphics g) {
        try {
            Image img=null;
            switch (type) {
                case PLAYER1:
                    int ind=0;
                    Image img0=null;
                    
                    switch (dir) {
                        case LEFT: ind=1; break;
                        case RIGHT: ind=2; break;
                        case 0: switch (speed) {
                            case FAST: ind = 3; break;
                            case STOP: ind = 4; break;
                            case SLOW: ind = 0; break;
                        } break;
                    }
                    
                    img0 = HImage.player[ind];
                    if (4==ind) ind = 0;
                    img = HImage.player1[ind][((spritePhase++)/2)%HImage.player1[ind].length];
                   
// own drawing                    
                    g.drawImage(img0, x, y, g.VCENTER|g.HCENTER);
                    g.drawImage(img, x, y, g.VCENTER|g.HCENTER);
                    
                    
                    
                    return;
                case SHOT1:
                case ESHOT1:
                    img = HImage.shot1;
                    break;
                case SHOT2:
                    img = HImage.shot2;
                    break;
                case SHOT3:
                    img = HImage.shot3;
                    break;
                case ENEMYM1:
//                    img = HImage.me1;
                    g.drawImage(HImage.me1, x, y, g.VCENTER|g.HCENTER);
                    g.drawImage(HImage.ep1[((spritePhase++)/2)%2], x, y, g.VCENTER|g.HCENTER);
                    return;
                case ENEMYL1:
//                    img = HImage.le1;
                    g.drawImage(HImage.le1, x, y, g.VCENTER|g.HCENTER);
                    g.drawImage(HImage.ep1[((spritePhase++)/2)%2], x, y, g.VCENTER|g.HCENTER);
                    return;
                case BOSS1:
                    g.drawImage(HImage.boss1, x, y, g.VCENTER|g.HCENTER);
                    g.drawImage(HImage.ep1[((spritePhase++)/4)%2], x-23, y+7, g.VCENTER|g.HCENTER);
                    g.drawImage(HImage.ep1[((spritePhase++)/4)%2], x+23, y+7, g.VCENTER|g.HCENTER);
                    return;
                case EXPL1:
                    img = HImage.boom[(spritePhase++)/3];
                    break;
                case SMOKE:
                    img = HImage.smoke[(spritePhase++)/3];
                    break;
                case TARGET1:
                    img = HImage.target1;
                    break;
                case MISSILE1:
                    img = HImage.missile1;
                    break;
                case BONUSE:
                    img = HImage.bonus[0];
                    break;
                case BONUSR:
                    img = HImage.bonus[1];
                    break;
                case BONUSL:
                    img = HImage.bonus[2];
                    break;
                case EMP1:
                    x = target.x;
                    y = target.y;
                    img = HImage.bonus[0];
                    spritePhase++;
                default:
                    Hell.println("ERROR: UNKNOWN TYPE - Fly.draw() type: "+type);
            }
            
            g.drawImage(img, x, y, g.VCENTER|g.HCENTER);
        } catch (Exception ex) {
            Hell.println(""+ex);
            Hell.println("ERROR: Fly.draw() type: "+type+" spritephase : "+spritePhase);
        }

    }

    protected void fire(int shottype) {
        if (MISSILE1==shottype) if (player.missiles<1) return; else player.missiles--;
        if (EMP1==shottype) {
            if (player.empshots>0) {
                fireEMP();
                player.empshots--;
            }
            return;
        }
        Fly shot = new Fly(shottype, this.x, this.y);
        shot.dir = player.dir;
        shot.speed+=player.speed/2;
        canvas.add(shot);
    }
    
/*    protected void fireMissile() {
        Fly shot = new Fly(MISSILE1, this.x, this.y);
        FlyCanvas.add(shot);
    }*/
    
    static Vector targets = new Vector();
    
    public void playerBeh() {
        switch (dir) {
                case LEFT:
                case RIGHT:
                case STRAIG:
                    this.x+=8*dir;
                break;
        } 
    }
          
    private void playersShot() {
        
        x+=dir*5;
        
        for (int i=0; i<targets.size(); i++) {
            Fly tgt = ((Fly)targets.elementAt(i));
            
            if (this.hits(tgt)) {
                
                tgt.damage(5);
               
                //delete();
                explode();
            }
        }
    }
    
    private void fireEMP() {
        for (int i=0; i<targets.size(); i++) {
            Fly tgt = ((Fly)targets.elementAt(i));
            
            
                
            Fly emp1 = new Fly(EMP1, tgt.x, tgt.y);
            emp1.target = tgt;
            canvas.add(   emp1  );
            
        }
    }
    
    private void enemyShot() {
        if (this.hits(player)) {
              
            player.damage(5);
               
            explode();
        }
    }

    private void bonusBeh() {
        if (this.hits(player)) {
            switch (type) {
            case BONUSE:    
                player.empshots++;
                break;
            case BONUSR:    
                player.missiles++;
                break;
            case BONUSL:    
                player.health+=50;
                break;
            }  
            
            delete();// todo bonustoken..   
            
        }
    }
    
    public void smoke() {
        Fly smke = new Fly(SMOKE, this.x, this.y);
        canvas.add(smke);
    }
    
    public void missileBeh() {
        Hell.println(""+targets.size()+" targets");

//        if (random(6)<2)
            smoke();
       
        int dx = 0;
        int dy = 0;
        
        for (int i=0; i<targets.size(); i++) {
            Fly tgt = ((Fly)targets.elementAt(i));
            
            if (this.hits(tgt)) {
                
                tgt.damage(50);
                 
                //delete();
                explode();
            } 
            
            if (tgt.y>dy) {
                dx = tgt.x;
                dy = tgt.y;
            }            
        } 
        
        
        
        if (this.x>dx) this.x-=8;
        if (this.x<dx) this.x+=8;
                        
    }
    
    
    private boolean hits (Fly tgt) {
        return Math.abs(x-tgt.x)<30&&Math.abs(y-tgt.y)<20;
    }
    
    private void mediumEnemy() {
          
        if (random(13)<2) {
            dir *= -1;
            fire(ESHOT1);
        }
        
        x+=5*dir;
        
//        x%=FlyCanvas.width;
  
        x = (int)Math.max(30, Math.min(x, FlyCanvas.width-30));
        
        
        
    }

//    private void mediumEnemy() {
//        x+=6;
//        x%=FlyCanvas.width;
//    }
    
    void explode() {
        type = EXPL1;
        spritePhase = 0;
        speed = 0;
    }
    
    void delete() {
        canvas.remove(this);
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
        delete();
        canvas.flyKilled(this);
    }
    
    /** 
     *  deletes fly when leaves the screen not to 
     */
    void clip() {
        switch (type) {
            case TARGET1:
            case ENEMYM1:
            case ESHOT1:
                if (y>FlyCanvas.height+30) {
                    delete();
                }
            break;
            case SHOT1:
            case MISSILE1:
                if (y<-30) {
                    delete();
                }
            break;
            default:
                if ((x>FlyCanvas.width+30)
                ||(x<-30)
                ||(y>FlyCanvas.height+30)
                ||(y<-30)) Hell.println("Fly out of screen! do nothing?! type:"+type+" x:"+x+"y:"+y);
            break;
        }
    }
    
    int random(int n) {
        return FlyCanvas.random(n);
    }
            
}

