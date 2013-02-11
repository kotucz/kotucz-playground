package mythicalduty;

import javax.microedition.lcdui.*;
import kotuc.midp.*;

public class AttackSelectAnimation extends Animation {
   
    int w = 50, h = 16;
        
    AttackSelectAnimation () {
        setTimeout(FURT);
        this.pos.y = 0;//MyCanvas.height;//-40;
        image = MySystem.createImage("/attackselectbg.png");
    }
    
    public void paint(Graphics g) {
        if (Game.fight==null) return;
//        if (Game.fight.selectedHero() == null) return;
                
//        this.pos.x = Game.fight.selectedHeroId*w;
        if (Game.fight.selectedAttack!=null) {
            g.setColor(Game.SELECTED_COLOR);
        
            g.drawImage(image, pos.x, pos.y-h, g.LEFT|g.TOP);
            
            g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
            g.drawRect(pos.x, pos.y-h, w-1, h-1);
            g.drawString(""+Game.fight.selectedAttack.name, pos.x+2, pos.y-h+2, g.TOP|g.LEFT);
            return;
        } else if (Game.fight.selectedHero().alive())
        for (int i = 0; i<Game.fight.selectedHero().attacks.length; i++) {
            if (i == Game.fight.selectedHero().attackId) 
                g.setColor(Game.SELECTED_COLOR);
            else        
                g.setColor(0x000000000);
            
//            g.drawImage(image, pos.x, pos.y-h*(i+1), g.LEFT|g.TOP);
            
//            g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
//            g.drawRect(pos.x, pos.y-h*(i+1), w-1, h-1);
//            g.drawString(""+Game.fight.selectedHero().attacks[i].name, pos.x+2, pos.y-h*(i+1)+2, g.TOP|g.LEFT);

            g.drawImage(image, pos.x, pos.y+h*i, g.LEFT|g.TOP);
            
            g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
            g.drawRect(pos.x, pos.y+h*i, w-1, h-1);
            g.drawString(""+Game.fight.selectedHero().attacks[i].name, pos.x+2, pos.y+h*i+2, g.TOP|g.LEFT);

        }
        
        
        
    }
     
} 

