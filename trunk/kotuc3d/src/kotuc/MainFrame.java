import kotuc.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*; 

public class MainFrame extends Frame {
	
	Canvas3d canvas;
	Timer timer;

	
	MainFrame() {
		super("kotuc 3d");
		
//		Echo10 echo = new Echo10();
//		Unit[] ul = echo.getUnitListFromXML("kotuc\\unitList01.xml");
		
		canvas=new Canvas3d();
		this.add(canvas);
		this.setSize(320, 260);
		
		this.addMouseMotionListener(canvas);	
	
		this.addWindowListener(new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
        		System.exit(0);
        	}
        });
		
		canvas.requestFocus();
		
				timer=new Timer();
		timer.schedule( new TimerTask () {
			public void run () {
				while (true) {
					canvas.paintDone=false;
					canvas.repaint();
					while (!canvas.paintDone);
					try {Thread.sleep(100);
					} catch (InterruptedException ie) {}
				}
		}}, 0L);      
	}
	

	
	/**
	 * Method main
	 *
	 *
	 * @param args
	 *
	 */
	public static void main(String[] args) {
		new MainFrame().setVisible(true);
	}	
}
