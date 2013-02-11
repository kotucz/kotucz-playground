package spygame;

import javax.microedition.lcdui.*;

class AnimatedList extends Canvas {
    
    int selectedIndex;
    
    String[] items;
    
    static Image image = Game.createImage("watches");
    
    String title;

    static int cellHeight = 12;
    static int yShift = 30;
    
    static Font font;
    
    static int width, height;
    
    int screen = 0;
    
    protected AnimatedList() {
        super();
        setFullScreenMode(true);
        width = getWidth();
        height = getHeight();
    }
          
    /*
     * creates new AnimatedList Screen 
     * @PARAM title text written on the top of the screen
     * @PARAM items texts of items
     * @PARAM image image in the beckground 
     */
    public AnimatedList(String name, String[] items, Image image) {
        this();
        this.title = name;
        setItems(items);
        setImage(image);
    }

    public AnimatedList(int screen) {
        this();
        this.setScreen(screen);
    }
       
    public void itemSelected(int id) {
        switch (screen) {
            case MAIN:
                id++;
                switch (id) {
                    case 0: 
  //                      if (fitem==1) resumeGame();
  //                      if (fitem==2) continueGame();
                    break;
                    case 1: Game.newGame(); break;
                    case 2: Game.showHighScoresScreen();  break;
                    case 3: setScreen(SETS); break;
                    case 4: setScreen(HELP); break;
                    case 5: Game.quitGame(); break;
                    case 6: Game.startMultiplayer(); break;
                    default: Game.showMainScreen();    
                }

                break;
            case PAUSE:
                switch (id) {
                    case 0: Game.resumeGame(); break;
                    case 1: setScreen(SETS); break;
                    case 2: setScreen(HELP); break;
                    case 3: Game.quitGame(); break;
                }

                break;
            case SETS:
                if (id==0) Game.setLangue(++Game.currLang%5); 
                else Game.showMainScreen();
                break;
                    
                       
        }
    }
    
    public static final int MAIN = 1, PAUSE = 2, SETS=3, HELP = 4; 
    
    public void setScreen(int screen) {
        this.screen = screen;
        int[] ids = null;        
        switch (screen) {
            case MAIN:
                ids = new int[] {1, 2, 3, 4, 6, 8};
                break;
            case PAUSE:
                ids = new int[] {7, 3, 4, 6};
                break;
            case SETS:
                ids = new int[] {0, 9};
            break;
                       
        }
        items = new String[ids.length];
        for (int i=0; i<ids.length; i++) {
            items[i] = Game.getText(ids[i]);
        }
        repaint();
    }
    
    public void paint(Graphics g) {
        
        g.setColor(0);
        g.fillRect(0, 0, width, height);
        
//        if (image!=null) g.drawImage(image, width, height, g.RIGHT|g.BOTTOM);
        
        g.drawImage(image, width, height, g.RIGHT|g.BOTTOM);
        
        g.setFont(Game.MENU_FONT);
        g.setColor(0xFF880000);
        long t = System.currentTimeMillis();
        g.drawString(""+((t/60/60/1000)%24)/10+((t/60/60/1000)%24)%10+":"+((t/60/10000)%6)+((t/60/1000)%10)+":"+((t/10000)%6)+((t/1000)%10), width-70, height-91, g.RIGHT|g.BOTTOM);
     
        
        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
        g.setColor(0x00aaaaaa);
        
        if (title!=null) g.drawString(title, 12, 10, g.LEFT|g.TOP);
        
        g.setFont(Game.MENU_FONT);
        for (int i = 0; i<items.length; i++) {
            try {
                if (i==selectedIndex) g.setColor(0x00BB00); else g.setColor(0x00FF00);
                g.drawString(""+items[i], 5, yShift+i*cellHeight, g.LEFT|g.TOP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (selectedIndex>-1) {
            g.setColor(0x00BB00);
            g.drawLine(0, yShift+selectedIndex*cellHeight, width, yShift+selectedIndex*cellHeight);
            g.drawLine(0, yShift+(selectedIndex+1)*cellHeight, width, yShift+(selectedIndex+1)*cellHeight);
        }
    }

    
    public void keyPressed(int key) {
        if (selectedIndex<0) itemSelected(selectedIndex);
        switch (getGameAction(key)) {
        case DOWN:
            if (selectedIndex<items.length-1) 
                selectedIndex++;
            else selectedIndex = 0;
            repaint();
        break;
        case UP:
            if (selectedIndex>0) 
                selectedIndex--;
            else selectedIndex = items.length - 1;
            repaint();
        break;
        case FIRE:
            itemSelected(selectedIndex);
        break;
        }    
    }

    
    

    
    public void pointerReleased(int x, int y) {
        setSelected((y-yShift)/cellHeight);
        itemSelected(selectedIndex);
    }

    public void pointerDragged(int x, int y) {
        setSelected((y-yShift)/cellHeight);
    }
    
    public void pointerPressed(int x, int y) {
        setSelected((y-yShift)/cellHeight);
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    public void setSelected(int i) {
       selectedIndex = i;
       repaint();
    }
        
    public void setItems(String[] items) {
        this.items = items;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
        
    public void setFont(Font f) {
        font = f;
    }
}
