package kotuc.chaos.arena;

import javax.vecmath.*;
import java.awt.*;
import kotuc.chaos.*;

public class Commander extends Thread {
	final double VELOCITY = 1.0;
	final double SIGHT_CONE = Math.PI / 4;
	
	public CommandListener cl;
	
	/**
	 * Method Soldier
	 *
	 *
	 */
	public Commander() {
		this.cl=new Soldier3D();
		this.start();
	}
	
	public Commander(CommandListener cl) {
		this.cl=cl;
		this.start();
	}	
	
	public void go(int distance) {
/*	  if (distance > 0) {
	  
	  	pos.x += Math.cos(angle) * VELOCITY;
	  	pos.y += Math.sin(angle) * VELOCITY;
	  	endOfTurn();
	  	
	  	
	  	go(--distance);
	  }
*///		cl.go(distance);
	}
	
	public void turnLeft(int angle) {
//		turnLeft(angle);
/*		
		if (angle > 0) {
			setAngle(this.angle - Math.toRadians(1.0));
			endOfTurn();
			turnLeft(angle - 1);
		}
*/	}
	
	public void turnRight(int angle) {
/*		if (angle > 0) {
			setAngle(this.angle + Math.toRadians(1.0));
			endOfTurn();
			turnRight(angle - 1);
			return;
		}
		if (angle < 0) {
			setAngle(this.angle - Math.toRadians(1.0));
			endOfTurn();
			turnRight(angle + 1);
			return;
		}
*/
//		cl.turnRight(angle);
	}
	
	public void turnRight(double angle) {
/*		System.out.println("Tocim se jak pako");
		if (Math.abs(angle) <= Math.toRadians(1.0)) {
			setAngle(this.angle - angle);
			endOfTurn();
			return;
		}
		if (angle > 0) {
			setAngle(this.angle + Math.toRadians(1.0));
			endOfTurn();
			turnRight(angle - Math.toRadians(1.0));
			return;
		}
		if (angle < 0) {
			setAngle(this.angle - Math.toRadians(1.0));
			endOfTurn();
			turnRight(angle + Math.toRadians(1.0));
			return;
		}*/
//		cl.turnRight(angle);
	}
	
	
	
	public void shoot() {
		/*stadium.things.add(new Bullet(this));
		for (int i = 0; i<20; i++) {
			//	reloading
			endOfTurn(); 
		}*/
		cl.shoot();
	}
	
	public void endOfTurn() {
		try {
			sleep(100);
		} catch(InterruptedException e) {
			e.printStackTrace();	
		}
	} 
/*	
	public void paint(Graphics g) {
		g.drawOval((int) this.pos.x - 5, (int) this.pos.y - 5, 10, 10);
		g.drawString(this.toString(), (int) this.pos.x - 15, (int) this.pos.y - 10);
	}
*/	
	public void run() {
		while (true) {
			//go(1);
			//System.out.println(angle);
			//System.out.println("I am alive");
			turnRight(5);
            //shoot(); 
			canSeeSoldier();
			
			
		}
	}
	
/*	public void hitted(Bullet bullet) {
		this.health-=5;
		if (this.health<1) {
			this.kill();
		} 
	}
*/	
/*	public void kill() {
		
	}
	
	public String toString() {
		return "Soldier "+this.health+"HP";
	}
	*/
	
	public void canSeeSoldier() {
/*		for (int i = 0; i<stadium.soldiers.size(); i++) {
			Soldier sold;
			double relAngle;
			if ((sold=(Soldier)stadium.soldiers.get(i))!=this) {
				if (Math.abs(relAngle = getRelativeAngle(sold.pos))< SIGHT_CONE) {
					this.onSoldierDetected(new OnSoldierDetectedEvent(relAngle, sold.pos.distance(this.pos)));
					/*return(sold);*/
			/*	}
			} 
			
			
		}*/
//		cl.canSeeSoldier();
		
	}
/*	
	public void onSoldierDetected(OnSoldierDetectedEvent e) {
		System.out.println("vidim " + e.toString() + (e.getAngle()));
		turnRight((e.getAngle()));
		shoot();
	}
*/	
/*	public double getAngle(Point3d pos) {
		double posAngle;
		try {
			posAngle = Math.acos(Math.abs(this.pos.x-pos.x)/(this.pos.distance(pos)));
			
		} catch (Exception e) {
			posAngle = Math.PI/2.0;
		}
		if (this.pos.y - pos.y < 0) posAngle *= -1;
		if (pos.x - this.pos.x < 0) posAngle = Math.PI - posAngle;
		
		posAngle = Math.PI * 2 - posAngle;
		
		System.out.println(posAngle);	
		System.out.println(this.angle);
		return posAngle;
	}
	
	public double getRelativeAngle(Point3d pos) {
		//System.out.println(  -normalizeSightAngle(this.angle -(getAngle(pos))) );
		return -(normalizeSightAngle(this.angle -(getAngle(pos))));
	}
	
	public void setAngle(double angle) {
		this.angle = angle % (Math.PI * 2.0);		
	}
*/	
/*	public double normalizeSightAngle(double angle) {
		while (angle < -Math.PI) {
			angle += Math.PI * 2;
		}
		while (angle >= Math.PI) {
			angle -= Math.PI * 2;
		}
		return angle;
	}
*/
}
