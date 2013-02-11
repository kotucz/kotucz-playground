package kotuc;

import java.awt.*;
import java.awt.event.*;


public class Canvas3d extends Canvas implements MouseMotionListener, FocusListener {
	final static int MAX_POLS = 1000;
	
	static public Unit mainUnit;
	public Vertex cam;
	static public Echo10 echo;
	public double alfa=-1, beta=0;
	int lastx=0, lasty;
	public int percs=0;
	public int repaints=0;
	public long millis;
	public boolean paintDone=true;
	public Player player;
	public Location location;
	
	public Canvas3d () {

		setBackground(Color.cyan);
		
		mainUnit = new Unit();
		
		this.addMouseMotionListener(this);
		
		player=new Player();
		this.addKeyListener(player);
		this.addMouseMotionListener(player);
		
		echo = new Echo10();
		echo.main("kotuc\\unitList01.xml");
		
		location = new Location(20, 20);
	}	
	
	public void update(Graphics g) {
		percs++;		
		
		player.krok();
		
		mainUnit=new Unit();
		
		mainUnit.addUnit(location);
		mainUnit=mainUnit.rotZ(player.smer+Math.PI/2);
		
		mainUnit.addUnit(echo.unitList[0].moveTo(player.pos.rotZ(player.smer+Math.PI/2)).moveTo(0, 0, 35));
		mainUnit.addUnit(echo.unitList[1].moveTo(player.pos.rotZ(player.smer+Math.PI/2)));
		mainUnit.addUnit(echo.unitList[2].rotX(player.mecBeta).rotY(player.mecAlfa).moveTo(player.pos.rotZ(player.smer+Math.PI/2)).moveTo(30, -20, 20));
		
		mainUnit=mainUnit.moveTo(
			(int)-player.pos.rotZ(player.smer+Math.PI/2).x,
			(int)-player.pos.rotZ(player.smer+Math.PI/2).y-200,
		-500).rotX(-0.5);//+this.alfa);//.rotY(this.beta);
		super.update(g);
	}
				
	/**
	 * Method paint
	 *
	 *
	 */
	public void paint(Graphics g) {
		millis = System.currentTimeMillis();
		mainUnit.paint(g);
		//player.paint(g);
		repaints++;
		g.setColor(Color.black);
		g.drawString("repaints:", 220, 10);
		g.drawString(String.valueOf(repaints), 270, 10);
		g.drawString(String.valueOf(System.currentTimeMillis()-millis), 10, 10);
		paintDone=true;
 	}	
	
	public void mouseMoved(MouseEvent e) {
//		this.alfa=((160-e.getX())/100);
//		this.beta=((120-e.getY())/100);
		//this.lastx = e.getX();
		//this.lasty = e.getY();
	
	}
	
	public void mouseDragged(MouseEvent e) {
/*		alfa=((lastx-e.getX())/100);
		beta=((lasty-e.getY())/100);
	
		this.lastx = e.getX();
		this.lasty = e.getY();
*/	}
	
	public boolean isFocusTraversable() {
		return true;
	}
	
	public void focusLost(FocusEvent e) {
		
	}
	
	public void focusGained(FocusEvent e) {
		
	}
	
	
	       
}
