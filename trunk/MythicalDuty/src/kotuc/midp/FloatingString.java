package kotuc.midp;

import javax.microedition.lcdui.*;

public class FloatingString extends Animation {
    
    String text;
    int color;
   
    /** Creates a new instance of Animation */
    public FloatingString(String s, int c, Pos pos) {
        this.text = s;
        this.color = c;
        
        setTimeout(1000);
        
        this.pos = pos;
    }
    
    public void paint(Graphics g) {
        this.pos.y--;
        g.setColor(color);
        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
        g.drawString(text, getX(), getY(), g.BASELINE|g.HCENTER);
    }
   
}
