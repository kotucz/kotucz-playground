import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

import java.util.*;

public class DeepSolver extends MIDlet implements CommandListener, Runnable {
    Display display;
    private Stack stack;
    
    Command saveCommand = new Command("Ulož", Command.SCREEN, 10);
    Command loadCommand = new Command("Nacti", Command.SCREEN, 1);
//    Command exitCommand = new Command("Exit", Command.EXIT, 60);
    Command solveCommand = new Command("Vyres", Command.SCREEN, 3);
    Command newCommand = new Command("Novy", Command.SCREEN, 5);
    Command helpCommand = new Command("Help", Command.HELP, 12);
	Command backCommand = new Command("Back", Command.BACK, 1);
//    private Command prevCommand = new Command("Previous Level", Command.SCREEN, 23);
//    private Command aboutCommand = new Command("About", Command.HELP, 30);

    /**
     * Creates new PushPuzzle MIDlet.
     */
    public DeepSolver() {
		display = Display.getDisplay(this);
		
		stack = new Stack();
		stack.push(new Sudoku(this));
    }


	public void run() {
		Sudoku sdk;		
//		while 
		if	(!((Sudoku)stack.peek()).solved()) {
			sdk = (Sudoku)stack.pop();
			
			int max=0;
			boolean extended=false;
			
			if (sdk.isDead()) extended = true;
						
			while (!extended) {
				max++;
				
				for (int i = 0; i<9*9; i++) {
					int x=i%9;
					int y=i/9;
				
					if (sdk.get(x, y)==0) {
						int allowed;
						if ((allowed = sdk.allowedCount(x, y))==max) {
							
							Stack sExtend = sdk.extend(x, y);			
							while (!sExtend.empty()) {
								this.stack.push(sExtend.pop());
							}
							
							extended = true;
							break;							
						}
					};
				
				};
			
			};
			
			((Sudoku)stack.peek()).repaint();
			
			Thread.yield();
/*			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
*/				
			
		}
	}


    /**
     * Start creates the thread to do the timing.
     * It should return immediately to keep the dispatcher
     * from hanging.
     */
    public void startApp() {
		display.setCurrent((Canvas)stack.peek());
    }
    

    /**
     * Pause signals the thread to stop by clearing the thread field.
     * If stopped before done with the iterations it will
     * be restarted from scratch later.
     */
    public void pauseApp() {
    }

    /**
     * Destroy must cleanup everything.
     * Only objects exist so the GC will do all the cleanup
     * after the last reference is removed.
     */
    public void destroyApp(boolean unconditional) {
    }

    /**
     * Respond to a commands issued on any Screen
     */
    public void commandAction(Command c, Displayable s) {
	if (c == solveCommand) {
		this.run();
	} else if (c == loadCommand) {
		stack = new Stack();
		Sudoku sdk = new Sudoku(this);
		sdk.load(1);
		stack.push(sdk);
		((Sudoku)stack.peek()).repaint();
	} else if (c == saveCommand) {
		((Sudoku)stack.peek()).save(1);
	} else if (c == newCommand) {
		stack = new Stack();
		Sudoku sdk = new Sudoku(this);
		stack.push(sdk);
		((Sudoku)stack.peek()).repaint();
	} else if (c == helpCommand) {
		showHelp();
	} else if (c == backCommand) {
		display.setCurrent((Displayable)stack.peek());
//	} else if (c == prevCommand) {
//	} else if (c == aboutCommand) {
	} else {
	}
    }
    
    Form helpScreen;
    
    String helpText = new String("arrows - move\n0 - delete cell\n1-9 - set cell\n# - autosolve\n* - hint");
    
    void showHelp() {
    	if (helpScreen==null) {
    		helpScreen = new Form("Help");
    		helpScreen.append(helpText);
			helpScreen.addCommand(backCommand);
    	
    	}	
    	
    	helpScreen.setCommandListener(this);
    	display.setCurrent(helpScreen);
    }
    
    
}
