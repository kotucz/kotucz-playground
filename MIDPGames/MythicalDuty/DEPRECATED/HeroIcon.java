package mythicalduty;

import javax.microedition.lcdui.*;
import kotuc.midp.*;

class HeroIcon extends Animation {

    Figure figure;
    
    int w = 50, h = 40;
    
    HeroIcon (Figure f) {
        this.figure = f;
        setTimeout(FURT);
        pos = new Pos(figure.id*w, Game.canvas.height-h);
        image = MySystem.createImage("/heroiconbg.png"); 
    }
    
    public void paint(Graphics g) {
        this.pos.x = figure.id*w;
                
        if ((Game.fight.state == Fight.HERO_SELECT)&&(Game.fight.selectedHeroId == figure.id)) 
            g.setColor(Game.SELECTED_COLOR);
        else        
            g.setColor(0x00000000);
        
        
        g.drawImage(image, pos.x, pos.y, g.LEFT|g.TOP);
        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
        g.drawRect(pos.x, pos.y, w-1, h-1);
        g.drawString("good "+figure.id, pos.x+2, pos.y+2, g.TOP|g.LEFT);
        g.drawString(""+figure.hitpoints+"/"+figure.maxhitpoints, pos.x+w-2, pos.y+11, g.TOP|g.RIGHT);
        g.drawString(""+figure.experience+"/"+figure.skilllevel+"Lv.", pos.x+w-2, pos.y+22, g.TOP|g.RIGHT);

        
        drawBar(g, 0x00FF8888, 0, figure.hitpoints, figure.maxhitpoints);
        drawBar(g, 0x0088FF88, 1, figure.energy, figure.energyNeeded);
        drawBar(g, 0x008888FF, 2, figure.experience-figure.levelExpNeed[figure.skilllevel], figure.levelExpNeed[figure.skilllevel+1]-figure.levelExpNeed[figure.skilllevel]);
//        drawBar(g, 0x008888FF, 2, 750, figure.levelExpNeed[figure.skilllevel]-figure.levelExpNeed[figure.skilllevel-1]);
//        drawBar(g, 0x008888FF, 2, 750, 1000);
        
/*        g.setColor(0x00000000);
        g.fillRect(pos.x+46, pos.y+2, 4, (h-4));
        g.setColor(0x0088FF88);
        g.fillRect(pos.x+46, pos.y+2, 4, (h-4)*figure.energy/figure.energyNeeded);
  */                      
    }

    void drawBar(Graphics g, int color, int fromleft, int much, int of) {
        if (figure.alive()) g.setColor(color);
            else g.setColor(0x00888888);
        
        g.fillRect(pos.x+w-1-5*(fromleft+1), pos.y+2, 4, (h-4));
        g.setColor(0x00555555);
        g.fillRect(pos.x+w-1-5*(fromleft+1), pos.y+2, 4, (h-4)*(of-much)/of);
    
    }
    
}
