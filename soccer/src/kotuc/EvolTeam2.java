package kotuc;

import soccer.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Date;

public class EvolTeam2 extends Team {

	public EvolTeam2() {
		for (int i = 0; i<configs.length; i++) configs[i] = new WConfig();
	}

	WConfig config = new WConfig();

	P[] tgts = new P[30];

	P[] locs = new P[30];

	boolean coolgraphics = false;
	boolean logsenabled = false;

	WConfig[] configs = new WConfig[5];

	Graphics g;

	Player[] plays;
	Player[] opps;
	Ball ball;

	Player pgo;
	
	public void ui() {

		g = bIm.getGraphics();

		if (coolgraphics) g.setColor(getTeamColor());

		plays = getPlayers();
		opps = getOpponents();
		ball = getBall();


		updateConfig();


		areaDivision();


		P bestt = ballAiming();
		kickBall(bestt.x, bestt.y);
		if (coolgraphics) {
			g.setColor(Color.GRAY);
			g.drawLine((int)ball.x, (int)ball.y, (int)bestt.x, (int)bestt.y);
		}

		pgo = nearestGo(); 

		Player pbr = goalkeeper();

		for (Player p1:plays) {
			if ((p1!=pgo)&&(p1!=pbr)) position(p1);
		}



		g.setColor(Color.CYAN);
		g.drawString(generation+"."+curconfig+". cuid:"+config.cuid+" "+config.homescore+"/"+config.hostscore+" ("+(int)(config.getFragrate()*100)+"%) t:-"+(updateInterval-(System.currentTimeMillis()-lastUpdate))/100, 410, 60);



	}

	
	long updateInterval = 60*1000; // one minute

	long lastUpdate;

	int curconfig = -1;

	int lasthost, lasthome;

	int generation = 0;


	File logf;
	PrintWriter logpw;

	
	void updateConfig() {

		config.homescore = Pitch.getScore(side)-lasthome;		
		config.hostscore = Pitch.getScore(-side)-lasthost;

		if ((System.currentTimeMillis()-lastUpdate)<updateInterval) return;

		if (logsenabled) {
			if (logf==null) {
				try {
					logf = new File("C:/Documents and Settings/PC/Dokumenty/Java Projects/soccer/logs/11032007/gen"+generation+".log");
					logpw = new PrintWriter(logf);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		

		if (config==null) config = configs[0];

		curconfig++;

		if (curconfig>=configs.length) {

//			do selections:		

			println("all configs tested");

			for (int i = 0; i<configs.length; i++) {
				for (int j = i+1; j<configs.length; j++) {
					if (configs[i].getFragrate()<configs[j].getFragrate()) {
						WConfig wc1 = configs[i];
						configs[i] = configs[j];
						configs[j] = wc1;
					}
				}
			}


			try {
				if (logsenabled) {
					logpw.println(""+new Date(System.currentTimeMillis())+" generation "+generation);
				}

				for (int i=0; i<configs.length; i++) {
					println(""+i+": "+configs[i].cuid+" "+configs[i].homescore+"/"+configs[i].hostscore+" ("+configs[i].getFragrate()+")");
					println(configs[i].toString());
					if (logsenabled) {
						logpw.println(""+i+": "+configs[i].cuid+" "+configs[i].homescore+"/"+configs[i].hostscore+" ("+configs[i].getFragrate()+")");
						logpw.println(configs[i].toString());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			generation++;

			if ((generation%5)==0) {
				logpw.close();
				logpw = null;
				logf = null;

			}

			double changerate = 0.1;

			// now rewarding winners 
			configs[4].mutate(changerate);

			configs[3]=(WConfig)configs[2].clone();
			configs[3].mutate(changerate);;
			configs[2].mutate(changerate);;

			configs[1]=(WConfig)configs[1].clone();
			configs[1].mutate(changerate);;
			configs[0]=configs[0];


			println("starting with new configs set");

			curconfig = 0;
		}	


		config = configs[curconfig];

		println("SWITCHED TO : "+config.toString());

		lasthome = Pitch.getScore(side);
		lasthost = Pitch.getScore(-side);

		lastUpdate = System.currentTimeMillis();
	}


	/**
	 * 	neural where to kick the ball
	 * 
	 */	
	P ballAiming() {
		double bestqual = 0;
		P bestt = null;

		for (int i = 0; i<tgts.length; i++) {
			P t1 = tgts[i];
			if (t1==null) t1=tgts[i]=new P(Math.random()*Pitch.WIDTH, Math.random()*Pitch.HEIGHT);
			double qual = 0;

			if (coolgraphics) g.setColor(Color.RED);
			for (Player o1:opps) {
				double diff = angle(o1, ball, t1);
				if (coolgraphics) g.drawLine((int)ball.x, (int)ball.y, (int)o1.x, (int)o1.y);
//				anywhere where no enemy 
				qual+=config.get(W.careness)*Math.min(diff, 1);//*100/o1.distance(ball);

			}

			if (coolgraphics) g.setColor(Color.GREEN);
			for (Player p1:plays) {
				double diff = angle(p1, ball, t1);
				if (coolgraphics) g.drawLine((int)ball.x, (int)ball.y, (int)p1.x, (int)p1.y);
//				anywhere where a friend
				qual+=config.get(W.teamcoop)*(1-Math.min(diff, 1));//*100/o1.distance(ball);
			}

//			depending on the distance to goal        	
			qual+=config.get(W.agresivity)*Math.pow(Math.max(0, 1-new P((1-getSide())*Pitch.WIDTH/2, Pitch.HEIGHT/2).distance(t1)/500), config.get(W.agresivityexp));

			if (qual>bestqual) {
				bestqual = qual;
				bestt = t1;
			}

			if (coolgraphics) {
				try {
					g.setColor(new Color((int)(Math.min((qual/(14)), 1)*255), 0, 0));
					g.fillOval((int)t1.x-10, (int)t1.y-10, 20, 20);
					g.drawString("x"+Math.round(qual*100), (int)t1.x, (int)t1.y);
				} catch (Exception e) {
					println(e.getMessage()+"colorred:"+(int)((qual/(6+3+3))*255));
				}
			}

			if (qual<config.get(W.toogood))
				if ((qual<config.get(W.toobad))||(Math.random()<config.get(W.randomizing))) tgts[i] = null;
		}

		return bestt;	    
	}


	P position(Player p1) {
		/**
		 * 	where players should go
		 * 
		 */        

		double bestq = 0;
		P bestd = null;

		for (int i = 0; i<locs.length; i++) {
			P l1 = locs[i];
			//create new random
			if (l1==null) l1=locs[i]=new P(Math.random()*Pitch.WIDTH, Math.random()*Pitch.HEIGHT);
			double qual = 0;

//			g.setColor(Color.RED);
			for (Player o1:opps) {
				double diff = angle(o1, l1, ball);
//				g.drawLine((int)ball.x, (int)ball.y, (int)o1.x, (int)o1.y);
//				anywhere where closer to ball than enemy 
				qual+=config.get(W.blocking)*diff/Math.PI;//*100/o1.distance(ball);

			}



			for (Player p2:plays) {
				if (p2==p1) continue;
				qual+=config.get(W.repelency)*Math.pow(Math.min(p2.distance(l1)/500, 1), config.get(W.repelencyexp));
			}

			qual+=config.get(W.locstability)*Math.max(0, 500-p1.distance(l1))/500;

//			depending on the distance to goal        	
			qual+=config.get(W.agresivityloc)*Math.max(0, (1-new P((1-getSide())*Pitch.WIDTH/2, Pitch.HEIGHT/2).distance(l1)/500));

			if (qual>bestq) {
				bestq = qual;
				bestd = l1;
			}

			if (coolgraphics) { 
				try {
					g.setColor(new Color(0, 0, (int)(Math.min(qual/(14), 1)*255)));
					g.fillOval((int)l1.x-10, (int)l1.y-10, 20, 20);
					g.drawString("x"+Math.round(qual*100), (int)l1.x, (int)l1.y);
				} catch (Exception e) {
					println(e.getMessage()+"colorblue:"+(int)(Math.min(qual/(6+1+2+1), 1)*255));
				}
			}

			if (qual<config.get(W.toogoodloc))
				if ((qual<config.get(W.toobadloc))||(Math.random()<config.get(W.randomizingloc))) locs[i] = null;
		}


		p1.goTo(bestd.x, bestd.y);

		return bestd;

	}
	

	Player nearestGo() {
		
		/**
		 * 	time nearest player go after ball in direct way
		 * 
		 * 	using virtual ball simulation
		 */

		Player pgo = null;
		
		if (coolgraphics) g.setColor(Color.GREEN);

		P xpoint1 = ball.getP();

		V bv = ball.getV();

		end1 : for (int t = 0; t<500; t++) {
			xpoint1.add(bv);
			if ((xpoint1.x<0)||(xpoint1.x>Pitch.WIDTH)) bv.x*=-1;
			if ((xpoint1.y<0)||(xpoint1.y>Pitch.HEIGHT)) bv.y*=-1;

			if (coolgraphics) g.drawLine((int)xpoint1.x, (int)xpoint1.y, (int)xpoint1.x, (int)xpoint1.y);

			for (Player p1:plays) {
				if (p1.distance(xpoint1)<(Player.MAX_SPEED*t)) {

//					println("t = "+t);

					pgo = p1;
					pgo.goTo(xpoint1);

					break end1;
				}
			}

		}

		if (coolgraphics) if (pgo!=null) g.drawLine((int)pgo.x, (int)pgo.y, (int)xpoint1.x, (int)xpoint1.y);

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

	void areaDivision() {
		/**
		 * 	area divVision
		 */


		P pxy = new P();
		for (pxy.x = 30; pxy.x<640; pxy.x+=30) {
			for (pxy.y = 30; pxy.y<480; pxy.y+=30) {
				Player bp = null; 
				double shor = 1000;
				for (Player p1:plays) {
					if (p1.distance(pxy)<shor) {
						bp = p1;
						shor = p1.distance(pxy);
						if (coolgraphics) g.setColor(getTeamColor());
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

				if (coolgraphics) if (bp!=null) g.drawString(""+bp.n, (int)pxy.x-5, (int)pxy.y-5);
			}
		}
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


}
