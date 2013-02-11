import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.*;

import java.util.*;
import java.io.*;



class Sudoku extends Canvas {
	
	Display display;
	
	static final byte [] PLANE1 = {
		5, 8, 0,	4, 0, 7,	0, 0, 0,
		0, 0, 6,	0, 0, 0,	0, 0, 0,
		0, 0, 0,	5, 0, 0,	0, 1, 3,
		
		0, 0, 0,	1, 0, 0,	0, 2, 5,
		2, 0, 5,	8, 0, 3,	1, 0, 6,
		9, 7, 0,	0, 0, 2,	0, 0, 0,
		
		3, 6, 0,	0, 0, 5,	0, 0, 0,
		0, 0, 0,	0, 0, 0,	7, 0, 0,
		0, 0, 0,	3, 0, 8,	0, 5, 2
	};
	
	final static String PLANE3 = new String("500082100 006004350 001590700 080000000 170000084 000000010 008026400 013700200 004910007"); 
	
	byte[] plane = new byte[9*9];
	
	static DeepSolver solver;
	
	static int curX = 5, curY = 5;
	
	static boolean hintEnabled = false;
	
	static int width, height;
	
    /**
     * Construct a new canvas
     */
    public Sudoku(DeepSolver ds) {
    	height = getHeight();
    	width = getWidth();
    	cell = Math.min(width, height)/9;;
		this.solver = ds;
		this.display = Display.getDisplay(ds);
//		this.plane = new byte[9*9];
//		this.plane = PLANE1;		
		this.addCommand(solver.newCommand);
		this.addCommand(solver.loadCommand);
		this.addCommand(solver.saveCommand);
		this.addCommand(solver.solveCommand);
		this.addCommand(solver.helpCommand);
		this.solver.display.setCurrent((Displayable)this);
		this.repaint();
		this.setCommandListener(solver);
	}

	public Sudoku(Sudoku parent) {
		this.solver = parent.solver;
//		this.plane = new byte[9*9];
		for (int i = 0; i<parent.plane.length; i++) this.plane[i] = parent.plane[i];
		this.addCommand(solver.newCommand);
		this.addCommand(solver.loadCommand);
		this.addCommand(solver.saveCommand);
		this.addCommand(solver.solveCommand);
		this.addCommand(solver.helpCommand);
		this.solver.display.setCurrent((Displayable)this);
		this.repaint();
		this.setCommandListener(solver);
	}
    /**
     * Read the previous level number from the score file.
     * Read in the level data.
     */
    public void init() {
    	repaint();
	}
    
	
	public void set(int x, int y, int value) {
		if (allowed(x, y, value)) plane [x+y*9] = (byte)value;
		else System.out.println("not allowed");
	}
	
	String sets = null;
	
	public void set(String s) {
		plane = new byte[9*9];
		sets = s;
				
			for (int i=0; i<9*9; i++) { 
				plane[i]=readNumber();					
			}
		

	}
	
	public void newPlane() {
		this.plane = new byte[9*9];
	}
	
	byte readNumber() {
		byte val = 0;
		try {
			val = Byte.parseByte(sets.substring(0, 1));
			sets = sets.substring(1);
		} catch (NumberFormatException e) {
			sets = sets.substring(1);
			val = readNumber();
		}
		return val; 
	}
	
	public int get(int x, int y) {
		return plane [x+y*9];
	}   
    
	public int getHorizontalMask(int y) {
		int mask = 0; //1022; //1111111110
		for (int x = 0; x<9; x++) {
			int val;
			if ((val = get(x, y))>0) {
				mask |= 1<<val;
			}  
		}
		return mask;
	}
	
	public int getVerticalMask(int x) {
		int mask = 0; //1022; //1111111110
		for (int y = 0; y<9; y++) {
			int val;
			if ((val = get(x, y))>0) {
				mask |= 1<<val;
			}  
		}
		return mask;
	}
	
	public int getSquareMask(int px, int py) {
		int mask = 0; //1022; //1111111110
		for (int x = 0; x<3; x++)
			for (int y = 0; y<3; y++) {
				int val;
				if ((val = get((px/3)*3+x, (py/3)*3+y))>0) {
					mask |= 1<<val;
				}  
			}
		return mask;
	}
	
// returns int mask 9876543210 if curent number is in square, row or collumn
	public int getMask(int x, int y) {
		if (get(x, y)>0) {
			return (~(1<<get(x, y))&1022);		
		} else {
			return (getVerticalMask(x)|getHorizontalMask(y)|getSquareMask(x, y));
		}
	}
	
		
	public boolean allowed(int x, int y, int value) {
		return ((getMask(x, y)&1<<value)==0);
	}
	
	public boolean allowed(int mask, int value) {
		return ((mask&1<<value)==0);
	}
	
	public int allowedCount(int x, int y) {
		int mask = getMask(x, y);
		int count = 0;
		
		for (int i = 1; i<(1<<10); i<<=1) {
			if ((i&mask)>0) count++;
		}		
		
		return 9-count;
	}
	
	public boolean isDead() {
		// if any cell has 0 possible values, this sudoku has no solution
		for (int i = 0; i<9*9; i++) {
			if ((plane[i]==0)&&(allowedCount(i%9, i/9)==0)) return true;
		};
		return false;
	}
	
	public Stack extend(int x, int y) {
		Stack stack = new Stack();
		int mask = getMask(x, y);
		
		for (byte i = 1; i<10; i++) {
			if (allowed(mask, i)) {
				Sudoku sdk = new Sudoku(this);
				sdk.set(x, y, i);
				stack.push(sdk);
			}
		}
		
		return stack;
	}
	
	public boolean solved() {
		for (int i = 0; i<plane.length; i++) {
			if (plane[i]==0) return false;
		}
		return true;
	}
	
	
	
	public void load (int id) {
		System.out.print("loading...");
		try{
			RecordStore rs = RecordStore.openRecordStore("Sudoku", true);
			this.plane = rs.getRecord(id);
			System.out.println("loaded");			
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
		
/*		switch (id) {
		
		case 1 :
			this.plane = PLANE1;
		break;
		
		case 3 :
			this.set(PLANE3); 
		break;
		
		default : 
			//this.plane = new int [9*9];
			newPlane();
		break; 
		}
*/	}
	
	
	public void load (String filename) {
		System.out.println("loading...");
//		this.plane=PLANE1;
//		this.set(PLANE3);
		
		InputStream is = null;
	    try {
		is = getClass().getResourceAsStream("/plane2.sdk");
//		is = getClass().getResourceAsStream("/data/" + filename);

		if (is != null) {
		    byte[] b = null;
		    is.read(b);
		    String s = new String(b);

// set plane to loaded string		    
		    this.set(s);
//		    this.set(PLANE3);
		    
		    is.close();
		} else {
		    System.out.println("Could not find the game board");
		}
	    } catch (java.io.IOException ex) {
			newPlane();
	    }
		repaint();
	}
	
	public void save (int id) {
		System.out.print("saving...");
		try {
			RecordStore rs = RecordStore.openRecordStore("Sudoku", true);
			if (rs.getNumRecords()<id) 
				rs.addRecord(this.plane, 0, this.plane.length);
			else rs.setRecord(id, this.plane, 0, this.plane.length);
			System.out.println("saved");
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Cleanup and destroy.
     */
    public void destroy() {
	}

    /**
     * Handle a repeated arrow keys as though it were another press.
     * @param keyCode the key pressed.
     */
    protected void keyRepeated(int keyCode) {
/*        int action = getGameAction(keyCode);
        switch (action) {
        case Canvas.LEFT:
        case Canvas.RIGHT:
        case Canvas.UP:
        case Canvas.DOWN:
*/            keyPressed(keyCode);
//	    break;
//      default:
//      break;
//        }
    }
    
    

    /**
     * Handle a single key event.
     * The LEFT, RIGHT, UP, and DOWN keys
     * Other keys are ignored and have no effect.
     * Repaint the screen on every action key.
     */
    protected void keyPressed(int keyCode) {

	// Protect the data from changing during painting.

//	    if ((keyCode>KEY_NUM0)||(keyCode<KEY_NUM9)) 
	switch (keyCode) {
	    case Canvas.KEY_NUM0 : 
	    	set(curX, curY, 0);
	    break;
	    case Canvas.KEY_NUM1 : 
	    	set(curX, curY, 1);
	    break;
	    case Canvas.KEY_NUM2 : 
	    	set(curX, curY, 2);
	    break;
	    case Canvas.KEY_NUM3 : 
	    	set(curX, curY, 3);
	    break;
	    case Canvas.KEY_NUM4 : 
	    	set(curX, curY, 4);
	    break;
	    case Canvas.KEY_NUM5 : 
	    	set(curX, curY, 5);
	    break;
	    case Canvas.KEY_NUM6 : 
	    	set(curX, curY, 6);
	    break;
	    case Canvas.KEY_NUM7 : 
	    	set(curX, curY, 7);
	    break;
	    case Canvas.KEY_NUM8 : 
	    	set(curX, curY, 8);
	    break;
	    case Canvas.KEY_NUM9 : 
	    	set(curX, curY, 9);
	    break;	    
		
		case Canvas.KEY_POUND : 
			//show/hide hint
	    	hintEnabled=true;
	    break;
		
		case Canvas.KEY_STAR : 
			solver.run();
	    break;
		
	    default : 

    	    int action = getGameAction(keyCode);
	
		    switch (action) { 
		    case Canvas.LEFT:
		    	if (curX>0) curX--;
			break;
		    case Canvas.RIGHT:
		    	if (curX<8) curX++;
			break;
		    case Canvas.DOWN:
		    	if (curY<8) curY++;
			break;
		    case Canvas.UP:
		    	if (curY>0) curY--;
			break;
	
		// 	case 0: // Ignore keycode that don't map to actions.
//	    	default:
//			return;
	    	};
	};
//	     else {
// if key is number assign its value to current cell	    	
//	   		set(curX, curY, keyCode-Canvas.KEY_NUM0);
//	   		set(curX, curY, 5); 
//	    }
	    
		repaint();	    

    }
       
	


    /**
     * Called when the pointer is pressed. 
     * @param x location in the Canvas
     * @param y location in the Canvas
     */
    protected void pointerPressed(int x, int y) {
    	curX = x/cell;
    	curY = y/cell;
    	repaint();
    }

    /**
     * Add a listener to notify when the level is solved.
     * The listener is send a List.SELECT_COMMAND when the
     * level is solved.
     * @param l the object implementing interface CommandListener
     */
    public void setCommandListener(CommandListener l) {
		super.setCommandListener(l);
    }

	static int cell;

    /**
     * Queue a repaint for a area around the specified location.
     * @param loc an encoded location from Board.getPusherLocation
     * @param dir that the pusher moved and flag if it pushed a packet
     */
    /**
     * Paint the contents of the Canvas.
     * The clip rectangle of the canvas is retrieved and used
     * to determine which cells of the board should be repainted.
     * @param g Graphics context to paint to.
     */
    protected void paint(Graphics g) {
	// Lock the board to keep it from changing during paint

	    // Figure what part needs to be repainted.
/*	    int clipx = g.getClipX();
	    int clipy = g.getClipY();
	    int clipw = g.getClipWidth();
	    int cliph = g.getClipHeight();
*/	    
  		
  
  		g.setColor(0xFFFFFF);
  		g.fillRect(0, 0, width, height);
  
  		for (int x = 0; x < 9; x++) {
  			for (int y = 0; y < 9; y++) {
  				if (y==curY||x==curX) {g.setColor(0xFF0000);} else {g.setColor(0x000000);};
  				g.drawRect(x*cell+x/3, y*cell+y/3, cell, cell);
				

//	  			g.drawString(Integer.toBinaryString(getHorizontalMask(y)) , 2, y*cell-2, Graphics.TOP|Graphics.LEFT);
  				if (get(x, y)>0) g.drawString(String.valueOf(get(x, y)) , x*cell+x/3+cell/2, y*cell+y/3, 16|1);
  				else {
  					if ((allowedCount(x, y)==1)&&(hintEnabled)) g.drawString("D" , x*cell+x/3+cell/2, y*cell+y/3, 1|16);
  					if (allowedCount(x, y)<1) g.drawString("X" , x*cell+x/3+cell/2, y*cell+y/3, 1|16);
  				}
  			} 
//  			g.drawString(Integer.toBinaryString(getMask(curX, curY)) , 2, 1*cell-2, Graphics.TOP|Graphics.LEFT);
  		} 
  		g.drawString(Integer.toBinaryString(getMask(curX, curY)), width/2, height-2, g.HCENTER|g.BOTTOM);
  
	    // Fill entire area with ground color
//	    g.drawString("Sudoku" + " by kotuc", 0, 14,
//			 Graphics.TOP|Graphics.LEFT);
		hintEnabled=false;	
    }
}
