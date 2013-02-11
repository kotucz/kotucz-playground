package mythicalduty.figures;

import javax.microedition.lcdui.*;
import mythicalduty.*;

public class EvilFigure extends Figure {
    
    /** Creates a new instance of Figure */
    protected EvilFigure() {
        side = EVIL;
    }
    
/*    void setPos(int id) {
        this.id = id;
        this.anim.pos = new Pos(200-id*50, 80);
    }
*/

    public void addExp(int exp) {
//       this.experience += exp;
//       Game.fight.addAnimation(new FloatingString("+"+exp+" EXP", 0x00FFFFFF, getPos()));
    }
}
	