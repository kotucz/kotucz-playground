package kotuc;
import soccer.Pitch;
import soccer.Team;

import soccer.*;

import java.awt.*;
import java.awt.image.*;

import javax.vecmath.*;

public class GTeam extends Team {

	public GTeam() {}

//	public GTeam(Pitch pitch, int score) {
//	super(pitch, score);
//	}



	P[] tgts = new P[20];
	double toobad = 3.5; // if qual is less deletes tgt
	double agresivity = 6; // weight of dist to goal
	double careness = 1; // weight - mean of enemy presence
	double teamcoop = 1; // weight - mean of mate presence
	double randomizing = 0.1; // 0 .. no 1 .. all

	P[] locs = new P[20];
	double toobadloc = 3; // if qual is less deletes pos
	double blockingloc = 1; // weight of dist to goal
	double repelency = 2; // weight of concentration odpudive forces
	double agresivityloc = 3; // weight of dist to goal
	double randomizingloc = 0.1; // 0 .. no 1 .. all

	boolean coolgraphics = false;

	public void ui() {

		Graphics g = bIm.getGraphics();
		g.setColor(getTeamColor());

		Player[] plays = getPlayers();
		Player[] opps = getOpponents();
		Ball ball = getBall();

		Player pgo = null;
		Player pbr = null;




		/**
		 * 	where to kick the ball
		 * 
		 */	    


		double bestqual = 0;
		P bestt = null;

		for (int i = 0; i<tgts.length; i++) {
			P t1 = tgts[i];
			if (t1==null) t1=tgts[i]=new P(Math.random()*Pitch.WIDTH, Math.random()*Pitch.HEIGHT);
			double qual = 0;

			g.setColor(Color.RED);
			for (Player o1:opps) {
				double diff = angle(o1, ball, t1);
				g.drawLine((int)ball.x, (int)ball.y, (int)o1.x, (int)o1.y);
//				anywhere where no enemy 
				qual+=careness*Math.min(diff, 1);//*100/o1.distance(ball);

			}

			g.setColor(Color.GREEN);
			for (Player p1:plays) {
				double diff = angle(p1, ball, t1);
				g.drawLine((int)ball.x, (int)ball.y, (int)p1.x, (int)p1.y);
//				anywhere where a friend
				qual+=teamcoop*(1-Math.min(diff, 1));//*100/o1.distance(ball);
			}

//			depending on the distance to goal        	
			qual+=agresivity*Math.max(0, (1-Math.pow(new P((1-getSide())*Pitch.WIDTH/2, ((1+(float)Math.random())/3f)*Pitch.HEIGHT).distance(t1), 1.07)/1000));

			if (qual>bestqual) {
				bestqual = qual;
				bestt = t1;
			}

			g.setColor(new Color((int)(Math.min((qual/(14)), 1)*255), 0, 0));
			g.fillOval((int)t1.x-10, (int)t1.y-10, 20, 20);
			g.drawString("x"+Math.round(qual*100), (int)t1.x, (int)t1.y);
			if ((qual<toobad)||(Math.random()<randomizing)) tgts[i] = null;
		}

		// using other technology
//		bestp = new P((1-getSide())*Pitch.WIDTH/2,((1+(float)Math.random())/3f)*Pitch.HEIGHT); 

		kickBall(bestt.x, bestt.y);

		g.drawLine((int)ball.x, (int)ball.y, (int)bestt.x, (int)bestt.y);


		/**
		 * 	end ball computing
		 */	    



		/**
		 * 	where players should go
		 * 
		 */        


		/**
		 * 	time nearest player go after ball in direct way
		 * 
		 * 	using virtual ball simulation
		 */

		g.setColor(Color.GREEN);

		P xpoint1 = ball.getP();

		V bv = ball.getV();

		end1 : for (int t = 0; t<500; t++) {
			xpoint1.add(bv);
			if ((xpoint1.x<0)||(xpoint1.x>Pitch.WIDTH)) bv.x*=-1;
			if ((xpoint1.y<0)||(xpoint1.y>Pitch.HEIGHT)) bv.y*=-1;

			g.drawLine((int)xpoint1.x, (int)xpoint1.y, (int)xpoint1.x, (int)xpoint1.y);

			for (Player p1:plays) {
				if (p1.distance(xpoint1)<(Player.MAX_SPEED*t)) {

//					println("t = "+t);

					pgo = p1;
					pgo.goTo(xpoint1);

					break end1;
				}
			}

		}




//		println(pgo +""+ xpoint1);

		/**
		 * 	area divVision
		 */

		if (pgo!=null) g.drawLine((int)pgo.x, (int)pgo.y, (int)xpoint1.x, (int)xpoint1.y);

		P pxy = new P();
		for (pxy.x = 30; pxy.x<640; pxy.x+=30) {
			for (pxy.y = 30; pxy.y<480; pxy.y+=30) {
				Player bp = null; 
				double shor = 1000;
				for (Player p1:plays) {
					if (p1.distance(pxy)<shor) {
						bp = p1;
						shor = p1.distance(pxy);
						g.setColor(getTeamColor());
					}
				}
				for (Player p2:opps) {
					if (p2.distance(pxy)<shor) {
						bp = null;
						break;
//						bp = p2;
//						shor = p2.distance(pxy);
//						g.setColor(Color.blue);
					}
				}

				if (bp!=null) g.drawString(""+bp.n, (int)pxy.x-5, (int)pxy.y-5);
			}
		}




		/**
		 * 	nearest player to goal is goalkeeper go to goal
		 * 
		 */

		P gol = new P(320+side*300, 240);

		double mindist = 1000;

		for (Player p1:plays) {
			if (p1.distance(gol)<mindist) {
				if (p1!=pgo) {
					pbr = p1;
					mindist = p1.distance(gol);
				}
			}
		}

		pbr.goTo(gol);



		for (Player p1:plays) {
			if ((p1!=pgo)&&(p1!=pbr)) {


				double bestq = 0;
				P bestd = null;

				for (int i = 0; i<locs.length; i++) {
					P l1 = locs[i];
					//create new random
					if (l1==null) l1=locs[i]=new P(Math.random()*Pitch.WIDTH, Math.random()*Pitch.HEIGHT);
					double qual = 0;

//					g.setColor(Color.RED);
					for (Player o1:opps) {
						double diff = angle(o1, l1, ball);
//						g.drawLine((int)ball.x, (int)ball.y, (int)o1.x, (int)o1.y);
//						anywhere where closer to ball than enemy 
						qual+=blockingloc*diff/Math.PI;//*100/o1.distance(ball);

					}

//					g.setColor(Color.GREEN);

					for (Player p2:plays) {
						if ((p2==p1)) continue;
						qual+=repelency*Math.pow((p2.distance(l1)/1000), 0.7);
					}

//					depending on the distance to goal        	
					qual+=agresivityloc*Math.max(0, (1-Math.pow(new P((1-getSide())*Pitch.WIDTH/2, ((1+(float)Math.random())/3f)*Pitch.HEIGHT).distance(l1), 1.07)/1000));

					if (qual>bestq) {
						bestq = qual;
						bestd = l1;
					}

					g.setColor(new Color(0, 0, (int)(Math.min((qual/(14)), 1)*255)));
					g.fillOval((int)l1.x-10, (int)l1.y-10, 20, 20);
					g.drawString("x"+Math.round(qual*100), (int)l1.x, (int)l1.y);
					if ((qual<toobadloc)||(Math.random()<randomizingloc)) locs[i] = null;
				}


				p1.goTo(bestd.x, bestd.y);
//				p1.goTo(320+side*100, 100*p1.n);
			}
		}

		/**
		 * 	end players motion
		 */        


//		oval(g, plays[0], plays[1], plays[2]);

//		edge(g, plays[0], opps[0]);

//		g.setColor(Color.BLUE);
//		g.drawRect(50, 50, 50, 50);

	}



	BufferedImage bIm = new BufferedImage(Pitch.WIDTH, Pitch.HEIGHT, BufferedImage.TYPE_INT_ARGB|BufferedImage.OPAQUE);

	public void paint(Graphics g) {

		if (coolgraphics) g.drawImage(bIm, 0, 0, null);

		super.paint(g);

		bIm = new BufferedImage(Pitch.WIDTH, Pitch.HEIGHT, BufferedImage.TYPE_INT_ARGB|BufferedImage.OPAQUE);
	}

	public static double angle(P a, P v, P b) {
		V v1 = new V();
		V v2 = new V();
		v1.sub(a,v);
		v2.sub(b,v);
		return v1.angle(v2);//Math.acos((v1.));
	}

	void edge(Graphics g, P p1, P p2) {
		V m = new V();
		m.sub(p2, p1);
		V n = new V(m.y, -m.x);
		m.interpolate(p1, p2, 0.5);
		n.scale(10/n.length());
		n.add(m);
		g.drawLine((int)(n.x), (int)(n.y), (int)m.x, (int)m.y);
	}

	void oval(Graphics g, P p1, P p2, P p3) {
		double[] root = solve(
				p1.x, p1.y, 1, -(p1.x*p1.x+p1.y*p1.y),
				p2.x, p2.y, 1, -(p2.x*p2.x+p2.y*p2.y),
				p3.x, p3.y, 1, -(p3.x*p3.x+p3.y*p3.y)						   
		);

		double a = root[0];
		double b = root[1];
		double c = root[2];

//		println("a: "+a+"; b: "+b+"; c: "+c);

		double x0 = -a/2;
		double y0 = -b/2;
		double r0 = Math.sqrt(a*a+b*b-(c/4))/2;

		println(""+(Math.pow(p1.x-x0, 2)+Math.pow(p1.y-y0, 2)-Math.sqrt(r0)));
		println(""+(Math.pow(p2.x-x0, 2)+Math.pow(p2.y-y0, 2)-Math.sqrt(r0)));
		println(""+(Math.pow(p3.x-x0, 2)+Math.pow(p3.y-y0, 2)-Math.sqrt(r0)));

		double s = (p1.distance(p2)+p2.distance(p3)+p1.distance(p3))/2;
		double S = Math.sqrt(s*(s-p1.distance(p2))*(s-p3.distance(p2))*(s-p1.distance(p3)));

		double R = (p1.distance(p2))*(p3.distance(p2))*(p1.distance(p3))/4/S;

		println("S: "+S+"; R: "+R+"; r: "+r0);

		println("x: "+x0+"; y: "+y0+"; c: "+c);

//		r0 = R;

		P cent = new P(p1);
		cent.interpolate(p2, 0.5);
		cent.interpolate(p3, 0.33);

		g.setColor(getTeamColor());

		g.drawLine((int)p1.x, (int)p1.y, (int)x0, (int)y0);
		g.drawLine((int)p2.x, (int)p2.y, (int)x0, (int)y0);
		g.drawLine((int)p3.x, (int)p3.y, (int)x0, (int)y0);

//		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
//		g.drawLine((int)p1.x, (int)p1.y, (int)p3.x, (int)p3.y);
//		g.drawLine((int)p2.x, (int)p2.y, (int)p3.x, (int)p3.y);

		g.drawOval((int)(cent.x-r0), (int)(cent.y-r0), (int)(2*r0), (int)(2*r0));
		g.drawOval((int)(x0-r0), (int)(y0-r0), (int)(2*r0), (int)(2*r0));

	}

	double[] solve(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23) {
		double d0 = new Matrix3d(
				m01, m02, m03,
				m11, m12, m13,
				m21, m22, m23						   
		).determinant();
		double d1 = new Matrix3d(
				m00,      m02, m03,
				m10,      m12, m13,
				m20,      m22, m23						   
		).determinant();
		double d2 = new Matrix3d(
				m00, m01,      m03,
				m10, m11,      m13,
				m20, m21,      m23						   
		).determinant();
		double d3 = new Matrix3d(
				m00, m01, m02,     
				m10, m11, m12,     
				m20, m21, m22    						   
		).determinant();
		return new double[] {d0/d3, d1/d3, d2/d3};

	}



}
