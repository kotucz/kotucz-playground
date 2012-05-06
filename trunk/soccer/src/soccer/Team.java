/*
 * Team.java
 *
 * Created on 1. bøezen 2007, 21:05
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
public abstract class Team {
    
    Player[] players = new Player[Pitch.numPlayers];
    Player[] opponents = null;
    
    String name;
    
    private Color teamColor = Color.CYAN;
    
    protected int side = 0;
    
    int score;
    
    static Pitch pitch;
    
    public Team() {
    	
    	name = getClass().getName();
    	
    	for (int i = 0; i<players.length; i++) {
            players[i] = new Player(i+1, this);
        }
    	
    }

    void setSide(int side) {
    	this.side = side;
        for (Player p1:players) {
            p1.x = p1.dx = 320 + side*100;
            p1.y = p1.dy = 100 + p1.n*40;
        }
        players[0].x = players[0].dx = 320 + side*300;
        players[0].y = players[0].dy = 240;
    
    }
    
    /** Creates a new instance of Team */
 /*   public Team(int s) {
        
    	this();
    	
    	this.side = s;
        if (side>0) teamColor = Color.RED;
        else teamColor = Color.GREEN;
        
        
    }*/
    
/*    public void run() {
        
        println(this +": redy");
        
        try {
        
                Thread.sleep(5000);
        } catch (InterruptedException ex) {
               ex.printStackTrace();
        }

        
        while (true) {
            
                        
            
                ui0();
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    */
    
    /**
	 * @return the players
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @return the side
	 */
	public int getSide() {
		return side;
	}

	public Color getTeamColor() {
        return teamColor;
    }
    
    protected void doMoves() {
   
       
        
        for (int i = 0; i<players.length; i++) players[i].doMove();
//       for (Player p1:plays) {
///           System.out.println("1");
 //          p1.doMove();
//       }
    }
    
    public Ball getBall() {
//        return (Ball)pitch.getBall().clone();
    	return Pitch.ball;
    }
    
    final void ui0() {
    	try {
			ui();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

    
	public void ui(Player[] plays, Player[] opps, Ball ball, int strana) {
		
	}

//  public abstract void ui() ;
	
	public void ui () {
		ui(getPlayers(), getOpponents(), getBall(), (getSide()>0)?1:0);
//		does nothing, since pascal is using	public abstract void ui(Player[] plays, Player[] opps, Ball ball, int strana) ;
//		calls pascal ui
	}
	
    
    public void kickBall(double bx, double by) {
        for (Player p1:players) {
          if (p1.distance(pitch.getBall())<Player.RANGE) pitch.getBall().kick(bx-getBall().x, by-getBall().y);
        }
    }
    
    protected void paint(Graphics g) {
       
       for (Player p1:players) {
           p1.paint(g);
       }
       
    }
    
    public void println(String text) {
        System.out.println(name+": "+text);
    }

	/**
	 * @param teamColor the teamColor to set
	 */
	public void setTeamColor(Color teamColor) {
		this.teamColor = teamColor;
	}

	public void setTeamColor(int teamColor) {
		try {
			this.teamColor = new Color(teamColor);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the opponents
	 */
	public Player[] getOpponents() {
		return opponents;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
    
	
	
}
