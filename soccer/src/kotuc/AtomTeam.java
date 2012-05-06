package kotuc;

import java.awt.Color;
import java.awt.event.*;

import soccer.*;

public class AtomTeam extends EvolTeam2 implements MouseListener {
	
	P ballTgt;
	
	public void ui() {

		g = bIm.getGraphics();

		if (coolgraphics) g.setColor(getTeamColor());

		plays = getPlayers();
		opps = getOpponents();
		ball = getBall();

		P bestt = ball;
		if (ballTgt!=null) bestt = ballTgt;
//		ballTgt = null;
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

	}

	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		ballTgt = new P(e.getX(), e.getY());
	}


	public void mouseReleased(MouseEvent arg0) {
		ballTgt = null;
			
	}
}
