/*
 * SBoard.java
 *
 * Created on 9. únor 2007, 15:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dynamicsudoku;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author PC
 */
public class SBoard extends java.applet.Applet 
        implements KeyListener, MouseListener, Runnable {
    
    
    
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    public void init() {
        // TODO start asynchronous download of heavy resources
        
    }
           
    // TODO overwrite start(), stop() and destroy() methods

    Num[] flownums = new Num[9*9];
    
    public void start() {
        this.addMouseListener(this);
        this.addKeyListener(this);
        setSize(350, 350);
        
        for (int i=0; i<9*9; i++) {
            flownums[i] = new Num(i/9 + 1); 
//            flownums[i].setPos(i);
        }
        
        restart("400160500"+
                "200084000"+
                "600500090"+
                "150007800"+
                "000000000"+
                "002900014"+
                "040006007"+
                "000490003"+
                "008071005");
    } 
    
    public void restart(String set) {
        for (int i = 0; i<9*9; i++) {
            int val=0;
            try {
                val=Integer.parseInt("" + set.charAt(i));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            if (val>0) {
                set(i%9, i/9, val);
                defaults[i] = true;
            }
        }
        
        completed = false;
        startTime = System.currentTimeMillis();
        
        recompute();
        
        new Thread(this).start();
        
    }
    
    public int get(int col, int row) {
        try {
            return values[col+9*row];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return 0;
        }
    }
    
    public void set(int col, int row, int value) {
        if (defaults[col+9*row]) return;
        
        try {
            
            for (int i = 0; i<9*9; i++) {
                if (value>0) {
                    if ((flownums[i].pos<0)&&(flownums[i].value==value)) {
                        flownums[i].setPos(col + 9*row);
                        break;
                    }
                    
                } else {    
                    if ((flownums[i].pos==(col + 9*row))) flownums[i].setPos(-1);
                }
                
            }
            
            values[col+9*row] = value;
        
        } catch (ArrayIndexOutOfBoundsException ex) {}
    }
    
    public int get(int col, int row, int pane) {
        if (pane==0) {
            return values[col+9*row];
        }
        else return 0;
    }
    
    public int panid(int col, int row) {
        return col/3+(row/3)*3;
    }
    
    protected int[] values = new int[9*9];

    public void run() {
        while (true) {
            recompute();
            repaint();
            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    protected boolean[][] avails = new boolean[9*9][10];
    protected boolean[][] usernotes = new boolean[9*9][10];
    protected boolean[][] hideavails = new boolean[9*9][10];
    protected boolean[] defaults = new boolean[9*9];
    
       
    int[][] countrow = new int[9][10];
    int[][] countcol = new int[9][10];
    int[][] countpan = new int[9][10];
    int[] countall = new int[10];
    int[][] availrow = new int[9][10];
    int[][] availcol = new int[9][10];
    int[][] availpan = new int[9][10];
    int[] availall = new int[10];
    int[] availcell = new int[9*9]; // == 1 -> naked
    
    public void recompute() {
        countrow = new int[9][10];
        countcol = new int[9][10];
        countpan = new int[9][10];
        countall = new int[10];
        availrow = new int[9][10];
        availcol = new int[9][10];
        availpan = new int[9][10];
        availall = new int[10];
        availcell = new int[9*9]; // naked
        
        for (int i = 0; i<9*9; i++) {
            countrow[i/9][values[i]]++;
            countcol[i%9][values[i]]++;
            countpan[panid(i%9, i/9)][values[i]]++;
            countall[values[i]]++;
        }
        
        for (int i = 0; i<9*9; i++) {
        for (focus=0; focus<10; focus++) {
// if number focus is available in field i            
            if ((countrow[i/9][focus]==0)
            &&(countcol[i%9][focus]==0)
            &&(countpan[panid(i%9, i/9)][focus]==0)
            &&(values[i]==0)
            &&(!hideavails[i][focus])
            ) {
                avails[i][focus]=true;
                availrow[i/9][focus]++;
                availcol[i%9][focus]++;
                availpan[panid(i%9, i/9)][focus]++;
                availall[focus]++;
                availcell[i]++;
            } else avails[i][focus]=false;
            
        }
        }
        
        if (countall[0]==0) {
            if (!completed) endTime = System.currentTimeMillis();
            completed = true;
        } else completed = false;
        
    }
    
    public void pairs() {
        int[] cts = new int[10];
        int[] avCts = new int[10];
        boolean[][] avs = new boolean[9][10];
        
        boolean[] cont = new boolean[10];
        
    }
    
    long startTime, endTime;
    
    int height = getHeight();
    int width = getWidth();
    static int cHeight = 32;
    static int cWidth = 32;

    private Color unavailablecolor = new Color(0x00FF0000);
    private Color badcolor = new Color(0x00FFAAAA);
//    private Color goodcolor = new Color(0x00FFFFFF);
    private Color filledcolor = new Color(0x00AAAAAA);
    private Color onlycolor = new Color(0x00FFFF00);
    private Color paircolor = new Color(0x00DDFFFF);
    private Color freecolor = new Color(0x00DDFFDD);
    private Color focuscolor = new Color(0x00AAAAFF);
    private Color bgcolor = new Color(0x00DDDDDD);
    private Color selectcolor = new Color(0x00EEEEBB);
    
    private Font font1 = new Font("Arial", Font.BOLD, 18);
    private Font notesfont = new Font("Arial", Font.BOLD, 10);
    
    boolean completed;
    
    int focus = 0;
    
    public void processFlow() {
        for (int i = 0; i<9*9; i++) {
            //if ()
            for (int j = 0; j<9; j++) {
                
            }
        }
    }
    
//    int crossx, crossy;
    
    public void paint(Graphics g) {
        
        recompute();
        
        focus = get(curx, cury);
        
        g.setColor(bgcolor);
        
//        g.fillRect(0, 0, width, height);        
    
        g.setFont(font1);
        
        for (int x = 0; x<9; x++) {
            for (int y = 0; y<9; y++) {
                drawCell(g, x, y);
                
            }
        }
  
        g.setFont(font1);
        
        for (int i=0; i<10; i++) {
            g.drawString(""+i+": "+countall[i], 300, 30+25*i);
        }
        
        g.setColor(Color.RED);
        
        g.drawString(""+curx+" "+ cury, 200, 30);
        
        int x=curx*cWidth;
        int y=cury*cHeight;
        g.drawLine(x+10, 0, x+10, y+22);
        g.drawLine(0, y+10, x+22, y+10); 
        
        if (flowgraphics) {
            drawGrid(g);
            for (int i = 0; i<9*9; i++) {
                flownums[i].paint(g);
            }
        }
          
        
        if (completed) {
            g.setColor(Color.YELLOW);
            g.fillRect(50, 50, 200, 50);
            
            g.setFont(font1);
            g.setColor(Color.BLACK);
            
            g.drawString("gratuluji dokonèil jsi sudoku", 60, 70);
            g.drawString(""+(endTime-startTime), 90, 90);
        }
        
    }

    boolean changed = true;
    
    public void update(Graphics g) {
        if (changed) {
            super.update(g);
            changed = false;
        }
        else paint(g);
    }
    
    public static int getX(int col) {
        return col*cWidth+2*(col/3);
    }
    
    public static int getY(int row) {
        return row*cHeight+2*(row/3);
    }
    
    public void drawCell(Graphics g, int cellx, int celly) {
        
        int x = getX(cellx);
        int y = getY(celly);
        
        if (flowgraphics) focus=0;
        
        int fvalue = values[cellx + 9*celly];
        
        if (get(cellx, celly)==0) g.setColor(freecolor);
        
        if (focus>0) {
            if (countrow[celly][focus]==1) g.setColor(badcolor);
            if (countcol[cellx][focus]==1) g.setColor(badcolor); 
            if (countpan[panid(cellx, celly)][focus]==1) g.setColor(badcolor); 
            
            
            if (avails[cellx+9*celly][focus]) {
                if (availrow[celly][focus]==2) g.setColor(paircolor);
                if (availcol[cellx][focus]==2) g.setColor(paircolor); 
                if (availpan[panid(cellx, celly)][focus]==2) g.setColor(paircolor);    
                
                if (availrow[cellx][focus]==1) g.setColor(onlycolor); 
                if (availcol[cellx][focus]==1) g.setColor(onlycolor); 
                if (availpan[panid(cellx, celly)][focus]==1) g.setColor(onlycolor); 
                
                if (availcell[cellx+9*celly]==1) g.setColor(onlycolor);
            }
        }
        
        if (get(cellx, celly)>0) {
            g.setColor(filledcolor);
            if (fvalue==focus) g.setColor(focuscolor);
        }
        
        if ((curx==cellx)&&(cury==celly)) g.setColor(selectcolor);

        if (get(cellx, celly)>0) {
          if (countrow[celly][fvalue]>1) g.setColor(unavailablecolor);
          if (countcol[cellx][fvalue]>1) g.setColor(unavailablecolor); 
          if (countpan[panid(cellx, celly)][fvalue]>1) g.setColor(unavailablecolor); 
        }
                        
        g.fillRect(x, y, cWidth, cHeight);
        
        
        g.setColor(Color.BLACK);
//        g.drawRect(x, y, cWidth, cHeight);
                        
        if (get(cellx, celly)>0) {
        if (!flowgraphics) {    
            if (defaults[cellx+9*celly]) g.setColor(Color.BLACK);
            else g.setColor(Color.GRAY);
            g.setFont(font1);
            g.drawString(""+get(cellx, celly), x+12, y+25);
        }
        } else {
// notes    
            boolean[] cnotes = new boolean[10];
            if (usernotesenabled) cnotes = usernotes[cellx+9*celly];
                    else cnotes = avails[cellx+9*celly];
//            for (int i=0; i<10; i++) cnotes[i] = ((hideavails[cellx+9*celly][i])?false:avails[cellx+9*celly][i]);
            
            g.setColor(Color.BLACK);
            g.setFont(notesfont);
            
           if (!flowgraphics) {                
            if (cnotes[1]) g.drawString("1", x+5, y+17);
            if (cnotes[2]) g.drawString("2", x+12, y+17);
            if (cnotes[3]) g.drawString("3", x+19, y+17);
            
            if (cnotes[4]) g.drawString("4", x+5, y+25);
            if (cnotes[5]) g.drawString("5", x+12, y+25);
            if (cnotes[6]) g.drawString("6", x+19, y+25);
            
            if (cnotes[7]) g.drawString("7", x+5, y+31);
            if (cnotes[8]) g.drawString("8", x+12, y+31);
            if (cnotes[9]) g.drawString("9", x+19, y+31);
           }
        }
    }
    
    static int bWidth = 300;
    static int bHeight = 300;
    
    public void drawGrid(Graphics g) {
        
        g.setColor(Color.BLUE);
        
        int lx, ly, ex, ey, ls;
        int k=0;
        int r = 200;
        for (ls = 0; ls<bWidth; ls+=cWidth) {
            ly = 0;
            lx = 0;
            ey = 0;
            ex = 0;
            while (ly<bHeight) {
                g.drawLine(ls, ly, ls, ey);

                ly=(int)(ey+Math.random()*r);
                ey=(int)(ly+Math.random()*r);
            }
            while (lx<bHeight) {
                g.drawLine(lx, ls, ex, ls);

                lx=(int)(ex+Math.random()*r);
                ex=(int)(lx+Math.random()*r);
            }
            if ((k==3)||(k==7)) {
                ls-=cWidth-2;
            }
            k++;
        }
        
    }
    
    static boolean flowgraphics = true;
    
/*    class Cell {
        
        int value = 0;
        
        public int get() {
            return value;
        }
        
        public void set(int i) {
            value = i;
        }
        
        
    }*/

    
    
    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    boolean usernotesenabled = false;
    
    public void keyTyped(KeyEvent e) {
//        System.out.println();
        System.out.println(e.getKeyChar());
//        System.out.println(e.getKeyText(e.getKeyCode()));
        int val = Integer.parseInt(""+e.getKeyChar());
        
        if (notesedit) {
            if (usernotesenabled) usernotes[curx+9*cury][val]=!usernotes[curx+9*cury][val];
            else hideavails[curx+9*cury][val]=!hideavails[curx+9*cury][val];
        } else
          if ((get(curx, cury)==0)||(val==0))
            set(curx, cury, val); 
        //repaint();
        changed = true;
    }
       
    int curx, cury;
    
    boolean notesedit;
    
    public void mousePressed(MouseEvent e) {
        curx = (e.getX()/cWidth);
        cury = (e.getY()/cHeight);
        if (e.getButton()==e.BUTTON1) notesedit = false;
        else notesedit = true;
        //repaint();
        changed = true;
    }
    
    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
    
    public void mouseReleased(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}
    
}
