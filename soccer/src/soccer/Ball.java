/*
 * Ball.java
 *
 * Created on 1. bøezen 2007, 22:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package soccer;

import java.awt.*;

/**
 *
 * @author PC
 */
public class Ball extends P {

	/** Creates a new instance of Ball */
	public Ball() {
//		this.x = Pitch.WIDTH/2; 
//		this.y = Pitch.HEIGHT/2;
		reset();
	}

	void reset() {
		this.x = Pitch.WIDTH/2; 
		this.y = Pitch.HEIGHT/2;
	}

	protected V v = new V();
	protected V vhalf = new V();

	public double vx, vy, v_x, v_y;

	void kick(double vx, double vy) {
		kick(vx, vy, MAX_SPEED);
	}
	
	void kick(double vx, double vy, double speed) {

		this.v.x = vx;
		this.v.y = vy;

		double vl = v.length();

		speed = Math.min(speed, MAX_SPEED);
		
		if (vl>speed) {
			if (isOriginal()) Pitch.playClip(Pitch.kickClip);
			v.scale(speed/(vl));
		}

		this.vx = this.v_x = v.x;
		this.vy = this.v_y = v.y;   

		vhalf = new V(v);
		vhalf.scale(0.5);

	}

	protected void paint(Graphics g) {

		int r=3;

		g.setColor(Color.WHITE);
		g.fillOval((int)x-r, (int)y-r, 2*r, 2*r);

	}

	public static final int MAX_SPEED = 10;

	void doHalfMove() {
		if (!Pitch.ballType) {
			add(vhalf);

			if ((x<0)||(x>Pitch.WIDTH)) {
				if ((y>160)&&(y<320)) {

					if (isOriginal()) {

						System.out.println("GOOOOL");
						if (x>320) Pitch.team1.score++;
						else Pitch.team2.score++;

						Pitch.playClip(Pitch.goalClip);
					}

				}


				kick(-vx, vy);

//				v.x*=-1;
//vhalf.x*=-1;

			}
			
			if ((y<0)||(y>Pitch.HEIGHT)) {
				kick(vx, -vy);
//				v.y*=-1;
//				vhalf.y*=-1;
			}
		} else {
			add(vhalf);

			if ((y<Pitch.TOPY)||(y>Pitch.BOTTOMY)) {
				kick(0, 0);
			}

			if ((x<Pitch.LEFTX)||(x>Pitch.RIGHTX)) {
				if ((y>160)&&(y<320)) {

					if (isOriginal()) {
						System.out.println("GOOOOL");
						if (x>Pitch.CENTX) 
							Pitch.team1.score++;
						else
							Pitch.team2.score++;
						Pitch.playClip(Pitch.goalClip);
					}


					reset();

				}

				kick(0, 0);

			}

			x = Math.min(Math.max(Pitch.LEFTX, x), Pitch.RIGHTX);
			y = Math.min(Math.max(Pitch.TOPY, y), Pitch.BOTTOMY);

		}

	}


	public void doVMove() {
		if (isOriginal()) {
			System.err.println("Illegal use doVMove on original Ball");
			return;
		}
		doHalfMove();
		doHalfMove();
	}

	/**
	 * @return the velocity
	 */
	public V getV() {
		return new V(v);
	}

	/**
	 * @return the position
	 */
	public P getP() {
		return new P(x, y);
	}

	Ball original;

	boolean isOriginal() {
		return (original==this);
	}

	public Object clone() {
		Ball b = new Ball();
		b.x = x;
		b.y = y;
		b.vx = b.v_x = v.x;
		b.vy = b.v_y = v.y;   
		b.v = new V(v);
		b.vhalf = new V(vhalf);
		return b;
	}


}
