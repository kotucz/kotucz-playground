package kotuc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import soccer.*;

public class VizTeam extends Team {

	Graphics g;

	Player[] plays;
	Player[] opps;
	Ball ball;

	Player pgo;
	
	boolean coolgraphics = true;
	
	WConfig config = new WConfig();
	
	public VizTeam () {
		coolgraphics = true;
//		locs = new P[80];
	}
	
	BufferedImage bIm;
	
	public void ui() {
				
		 bIm = new BufferedImage(Pitch.WIDTH, Pitch.HEIGHT, BufferedImage.TYPE_INT_ARGB|BufferedImage.OPAQUE);

		 g = bIm.getGraphics();

		g.setColor(getTeamColor());

		plays = getPlayers();
		opps = getOpponents();
		ball = getBall();


		pgo = nearestGo(); 
		
		
		P bestt = ballAiming();
		kickBall(bestt.x, bestt.y);
		
		if (coolgraphics) {
			g.setColor(Color.GRAY);
			g.drawLine((int)ball.x, (int)ball.y, (int)bestt.x, (int)bestt.y);
			
			for (Player p1:plays) {
				g.drawLine((int)p1.x, (int)p1.y, (int)p1.cil_x, (int)p1.cil_y);
			}
		}
		
		

		Player pbr = goalkeeper();
		
		for (Player p1:plays) {
			if ((p1!=pgo)&&(p1!=pbr)) {
				position(p1);
				break;
			}
		}
		
	}

	// TODO set to 0 and grow as enemy gets the ball
 	double criticalangle = 1.1;//rad
	
	/**
	 * 	neural where to kick the ball
	 * 
	 */	
	P ballAiming() {
		double bestqual = 0;
		P bestt = ball.getP();

//		P g0 = new P((1-getSide())*320, 165);
		P g1 = new P((1-getSide())*320, 165+Math.random()*150);
//		P g2 = new P((1-getSide())*320, 375);
						
		P ng1 = new P((1+getSide())*320, Pitch.CENTY);
		
		for (int i = 0; i<27; i++) {
			double ang = Math.PI*2*i/27.0;
			P t1 = new P(ball.x+50*Math.cos(ang), ball.y+50*Math.sin(ang));
			double qual = 0;

			for (Player p1:plays) {
				if (p1==pgo) continue;
				double diff = angle(p1, ball, t1);
//				anywhere where a friend
				qual+=config.get(W.teamcoop)*(Math.max(0, (criticalangle-diff)/criticalangle));// /(ball.distance(p1)/100);
			}

			
//			depending on the distance to goal        	
			qual+=config.get(W.agresivity)*Math.max((criticalangle-angle(g1, ball, t1))/criticalangle, 0);

			
			
			for (Player o1:opps) {
				double diff = angle(o1, ball, t1);
//				anywhere where no enemy 
				qual*=(Math.min(diff, criticalangle))/criticalangle;
			}
			
			qual*=Math.min(angle(ng1, ball, t1)/criticalangle, 1);
			
			if (qual>bestqual) {
				bestqual = qual;
				bestt = t1;
			}
		
			if (coolgraphics) {
				try {
					g.setColor(new Color((int)(Math.min((qual/bestqual), 1)*255), 0, 0));
					g.fillOval((int)t1.x-5, (int)t1.y-5, 10, 10);
//					g.drawString("x"+Math.round(qual*100), (int)t1.x, (int)t1.y);
				} catch (Exception e) {
					println(e.getMessage()+"colorred:"+(int)((qual/(6+3+3))*255));
				}
			}
			
		}
		
		
		
		
		return bestt;	    
	}
		
	Player nearestGo() {
		
		/**
		 * 	time nearest player go after ball in direct way
		 * 
		 * 	using virtual ball simulation
		 */

		Player pgo = null;
		
		if (coolgraphics) g.setColor(Color.GREEN);

		Ball vball = (Ball)ball.clone();

		end1 : for (int t = 0; t<500; t++) {
			
			vball.doVMove();
			
			if (coolgraphics) g.drawLine((int)vball.x, (int)vball.y, (int)vball.x, (int)vball.y);

			for (Player p1:plays) {
				if (p1.distance(vball)<(Player.MAX_SPEED*t)) {

					pgo = p1;
					pgo.goTo(vball);

					break end1;
				}
			}

		}

		if (coolgraphics) if (pgo!=null) g.drawLine((int)pgo.x, (int)pgo.y, (int)vball.x, (int)vball.y);

		return pgo;


	}

	Player goalkeeper() {
		/**
		 * 	nearest player to goal is goalkeeper go to goal
		 * 
		 */

		Player pbr = null;

		P gomin = new P(320+side*300, 240);

		double mindist = 1000;

		for (Player p1:plays) {
			if (p1.distance(gomin)<mindist) {
				if (p1!=pgo) {
					pbr = p1;
					mindist = p1.distance(gomin);
				}
			}
		}

		if (pbr!=null) pbr.goTo(gomin);

		return pbr;

	}
	
	P position(Player p1) {
		/**
		 * 	where players should go
		 * 
		 */        

		double bestq = 0;
		P bestd = p1;

		for (int i = 0; i<50; i++) {
			P l1 = new P(Math.random()*Pitch.WIDTH, Math.random()*Pitch.HEIGHT);
			double qual = 1;

			for (Player o1:opps) {
				double diff = angle(o1, l1, ball);

//				anywhere where closer to ball than enemy 
				qual+=config.get(W.blocking)*diff/Math.PI;//*100/o1.distance(ball);

			}

			qual+=config.get(W.locstability)*Math.max(0, 500-p1.distance(l1))/500;

//			depending on the distance to goal        	
			qual+=config.get(W.agresivityloc)*Math.max(0, (1-new P((1-getSide())*Pitch.WIDTH/2, Pitch.HEIGHT/2).distance(l1)/500));

			
			for (Player p2:plays) {
				if ((p2==p1)||(p1==pgo)) continue;
				double diff = angle(p2, ball, l1);
// to be in all directions around ball				
				qual*=(1+config.get(W.repelency)*Math.min(diff/criticalangle, 1))/(1+config.get(W.repelency));
			}
			
			if (qual>bestq) {
				bestq = qual;
				bestd = l1;
			}

//			if (coolgraphics) { 
			if (false) {
				try {
					g.setColor(new Color(0, 0, (int)(Math.min(qual/(bestq), 1)*255)));
					g.fillOval((int)l1.x-10, (int)l1.y-10, 20, 20);
				} catch (Exception e) {
					println(e.getMessage()+"colorblue:"+(int)(Math.min(qual/(6+1+2+1), 1)*255));
				}
			}
			
		}


		p1.goTo(bestd.x, bestd.y);

		return bestd;

	}
	
	public static double angle(P a, P v, P b) {
		V v1 = new V();
		V v2 = new V();
		v1.sub(a,v);
		v2.sub(b,v);
		return v1.angle(v2);//Math.acos((v1.));
	}
	
	

	public void paint(Graphics g) {

		if (bIm!=null) g.drawImage(bIm, 0, 0, null);

		super.paint(g);

//		bIm = new BufferedImage(Pitch.WIDTH, Pitch.HEIGHT, BufferedImage.TYPE_INT_ARGB|BufferedImage.OPAQUE);
	}
	
}
