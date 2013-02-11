package kotuc.midp;

import javax.microedition.lcdui.*;

public abstract class AnimatedList extends Controller {
    
    private int selectedIndex = 0;
    
    private String[] items = {"null"};
    
    private Image image = null;
    
    private String name = null;
    
    protected AnimatedList() {
        super();
               
    }
    
    Image shade = MyCanvas.createShade(0x229999CC);
    
    /*
     * creates new AnimatedList Screen 
     * @PARAM name text written on the top of the screen
     * @PARAM items texts of items
     * @PARAM image image in the beckground 
     */
    public AnimatedList(String name, String[] items, Image image) {
        this();
        this.name = name;
        setItems(items);
        setImage(image);
    }
    
    public void setItems(String[] items) {
        this.items = items;
//        if (items.length>0) cellHeight = MyCanvas.height/items.length; else cellHeight = 16;
//        cellHeight = (cellHeight>16)?16:cellHeight;
//       yShift = (this.getHeight() - cellHeight*items.length)/2;
        
        this.cellHeight = 12;
        yShift = 30;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    static int cellHeight;
    static int yShift;
    
    static Font font;
       
    public void paint(Graphics g) {
        
        if (shade==null) { 
            g.setColor(0xFFFFAA);
            g.fillRect(0, 0, MyCanvas.width, MyCanvas.height);
//        g.fillRect(10, 10, 100, 20+items.length*cellHeight);
        } else g.drawImage(shade, 0, 0, g.LEFT|g.TOP);
            
        if (image!=null) g.drawImage(image, MyCanvas.width, MyCanvas.height, g.RIGHT|g.BOTTOM);
        
        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
        g.setColor(0x0000033);
        
        g.drawString(name, 12, 10, g.LEFT|g.TOP);
        
        g.setFont(font);
                
        for (int i = 0; i<items.length; i++) {
            try {
                if (i==selectedIndex) g.setColor(0x0000033); else g.setColor(0x00998855);
                g.drawString(items[i], 5, yShift+i*cellHeight, g.LEFT|g.TOP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
 /*       if (selectedIndex>-1) {
            g.setColor(0x00BB00);
            g.drawLine(0, yShift+selectedIndex*cellHeight, width, yShift+selectedIndex*cellHeight);
            g.drawLine(0, yShift+(selectedIndex+1)*cellHeight, width, yShift+(selectedIndex+1)*cellHeight);
        }
*/    }
    
    public void keyPressed(int key) {
        if (selectedIndex<0) itemSelected(selectedIndex);
        switch (getGameAction(key)) {
        case DOWN:
            if (selectedIndex<items.length-1) 
                selectedIndex++;
            else selectedIndex = 0;
//            repaint();
        break;
        case UP:
            if (selectedIndex>0) 
                selectedIndex--;
            else selectedIndex = items.length - 1;
//            repaint();
        break;
        case FIRE:
            itemSelected(selectedIndex);
//            Settings.repaint();
        break;
        }    
    }

    public void pointerReleased(int x, int y) {
        selectedIndex = (y-yShift)/cellHeight;
        itemSelected(selectedIndex);
//        repaint();
    }

    public void pointerDragged(int x, int y) {
        selectedIndex = (y-yShift)/cellHeight;
//        itemSelected(selectedIndex);
//        repaint();
    }
    
    public void pointerPressed(int x, int y) {
        selectedIndex = (y-yShift)/cellHeight;
//        itemSelected(selectedIndex);
//        repaint();
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    public void setSelectedIndex(int i) {
       selectedIndex = i;
//       repaint();
    }
    
    public abstract void itemSelected(int id);
/*    
    private CommandListener listener;
    
    public void setCommandListener(CommandListener l) {
        this.listener = l;
        super.setCommandListener(l);
    }
  */  

    public void doWork() {
        
    }
    
    public static void setFont(Font f) {
        font = f;
    }
}
