package mythicalduty.figures;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.*;

public class FigureAnimation extends Animation {
    
    Figure figure;
      
    FigureAnimation (Figure f) {
        this.figure = f;
        setTimeout(FURT);
    }
    
    int blink2;
    
    public void paint(Graphics g) {
        this.pos = figure.getPos();
        
           
        if (Game.fight!=null) {
            if (((Game.fight.selectedAttack==null)&&(Game.fight.selectedHero()==this.figure))
                ||((Game.fight.selectedAttack!=null)&&(Game.fight.selectedAttack.target==this.figure))) {
                g.setColor(0x00FFFF00);
                g.fillArc(getX()-16, getY()-24, 32, 24, (int)MySystem.getTime()%360, (int)MySystem.getTime()%360+180); 
                drawBar(g, getX(), getY()-45, 0x00FF6666, figure.hitpoints, figure.maxhitpoints);
            }
            /*        if (figure.attackable(Game.fight.selectedAttack)||selected()) {
             g.setColor(0x00FFFF00);
            if (selected()) g.fillArc(getX()-16, getY()-24, 32, 24, 0, 360); 
            else  g.drawArc(getX()-16, getY()-24, 32, 24, 0, 360); 
        } else if ((figure==Game.fight.selectedHero)&&(Game.fight.state!=Fight.TARGET_SELECT)) {
            if (selected()) g.fillArc(getX()-16, getY()-24, 32, 24, 0, 360); 
            else  g.drawArc(getX()-16, getY()-24, 32, 24, 0, 360);
        }
 */}

        if (!figure.alive()) {
            image = figure.deadImage;
            g.drawImage(figure.deadImage, getX(), getY(), g.BOTTOM|g.HCENTER);
            return;
        }
        
//        if (Game.random(4)==0) nextFrame();
        image = getImage();
        g.drawImage(image, getX(), getY(), g.BOTTOM|g.HCENTER);
        
        g.setColor(0x00FFFFFF);
        g.drawString(""+((figure.weak())?"WEAK":""), getX()-20, getY()-18, g.TOP|g.LEFT);
        
//        if ((figure.side==Figure.GOOD)||selected()) {
 //           int wi = img.getWidth(), he = img.getHeight();
 //           g.setColor(Game.SELECTED_COLOR);
 //           g.drawRect(getX()-wi/2, getY()-he, wi, he);
 //           g.drawString(""+figure.hitpoints*100/figure.maxhitpoints+"%", getX()-wi/2+2, getY()-he+2, g.TOP|g.LEFT);
/*        if (Game.fight!=null)
        if (//(Game.fight.state==Fight.HERO_SELECT)&&(figure.side==Figure.GOOD)||
            ((Game.fight.selectedAttack!=null)&&((Game.fight.selectedAttack.getTargetType()&figure.side)!=0))) {
// health indicator            
            
        }*/
        
        if (Game.fight!=null)
        if (figure.side==Figure.GOOD&&Game.fight.selectedAttack==null) {
            if (figure.ready()&&((blink2%8)<4)) drawBar(g, getX(), getY()-10, 0x00FFFFFF, 1, 1);
            else drawBar(g, getX(), getY()-10, 0x0066FF66, figure.energy, figure.energyNeeded);
        }
        blink2++;
        
        flashing(g);
    }
    
/*    boolean selected() {
//        if (!figure.alive()) return false;
        if (Game.fight==null) return false;
        if ((Game.fight.state == Fight.HERO_SELECT)&&(figure.side == Figure.GOOD)&&(figure.id == Game.fight.selectedHeroId)) return true;
        if (Game.fight.selectedAttack == null) return false; 
        int type = Game.fight.selectedAttack.getTargetType();
        if (((Game.fight.state == Game.fight.TARGET_SELECT)&&((figure.side&type)!=0))) 
            if ((((Attack.SINGLE&type)!=0)&&(Game.fight.selectedAttack.targetId==figure.id))
                ||((Attack.ALL&type)!=0)) return true;    
        return false;
       
    }
  */  
    void drawBar(Graphics g, int x, int y, int color, int much, int of) {
        g.setColor(color);
        g.fillRect(x-15, y, 30*much/of, 4);
        g.setColor(0x00FFFFFF);
        g.drawRect(x-15, y, 30, 4);
    }
    
    public boolean collidesWith(int x, int y) {
        int w = image.getWidth(), h = image.getHeight();
        if ((getX()-w/2<x)&&(getX()+w/2>x)&&(getY()-h<y)&&(getY()>y)) return true;
        else return false;
    }
    
} 
	