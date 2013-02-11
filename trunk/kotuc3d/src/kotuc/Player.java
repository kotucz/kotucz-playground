package kotuc;

import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

class Player implements MouseMotionListener, KeyListener {
	
	Vertex pos;
	
	public int px, py;
	
	public double mecAlfa, mecBeta;
	
	public double smer, nSmer;
	
	public int vel=5, nVel; // veocity - rychlost
	
	public Image image;
	
	public Image getImage() {
		return this.image;
	}
	
	public Player () {
		pos=new Vertex(500, 500, 50);
		Toolkit t = Toolkit.getDefaultToolkit();
		this.image = t.getImage("hracDefaut.bmp");
	}	
	
	public void krok() {
//		smer%=2*Math.PI;
//		while (smer<0) smer+=2*Math.PI;
//		while (smer>2*Math.PI) smer-=2*Math.PI;
//		if ((Math.abs(smer-nSmer)>0.05)&(smer-nSmer-0.05<2*Math.PI)&(smer-nSmer+0.05>-2*Math.PI))
//		if (((smer-nSmer>-2*Math.PI)&(smer-nSmer<=-Math.PI))|((smer-nSmer>0)&(smer-nSmer<=Math.PI))) { 
//			smer-=Math.PI/32;
//		} else {
//			smer+=Math.PI/32;					
//		};		

		smer+=(nSmer-smer);
		this.pos.x+=Math.cos(smer)*vel;
		this.pos.y+=Math.sin(smer)*vel;
		
		if (this.vel>0) this.vel--;
		if (this.vel<0) this.vel++;
	};
	
	public void paint(Graphics g) {
		g.drawImage(this.image, (int)this.pos.x, (int)this.pos.y, Color.cyan, null);
//		g.fillOval(this.x-5, this.y-5, 10, 10);
//		g.drawLine(this.x, this.y, (int)(this.x+Math.cos(this.smer)*this.vel), (int)(this.y+(int)Math.sin(this.smer)*this.vel));	
	}

	public void mouseMoved (MouseEvent e) {
		this.mecAlfa=(e.getX()-160)/100;
		this.mecBeta=(e.getY()-120)/100;
	}
	
	public void mouseDragged (MouseEvent e) {
		this.mecAlfa=(e.getX()-160)/100;
		this.mecBeta=(e.getY()-120)/100;		
	}

	public void keyTyped (KeyEvent e) {	
	};	
		
	public void keyPressed (KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			if (this.vel<10) this.vel+=5; 
			break;
		case KeyEvent.VK_S:
			if (this.vel>-10) this.vel-=5;
			break;
		case KeyEvent.VK_A:
			this.nSmer=this.smer-Math.PI*(0.05); 
			break;
		case KeyEvent.VK_D:
			this.nSmer=this.smer+Math.PI*(0.05);
			break;
		}
//		this.krok();		
	};

	public void keyReleased (KeyEvent e) {
		
	};
}
	