/*
 * Player.java
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
public class Player extends P {
    
    public int n;
    private Team team;
    
    public static int RADIUS = 5;
    public static int RANGE = 10;
    public static int MAX_SPEED = 5;
    
    protected double dx, dy;
    public double cil_x, cil_y;
    
    public Player() {}
    
    /** Creates a new instance of Player */
    protected Player(int n, Team team) {
        this.n = n;
        this.team = team;
        x = 100; y = 100;
        if (pimage==null) pimage = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/soccer/data/player.png"));
    }
    
    static Image pimage;
    
    protected void paint(Graphics g) {

        V light = new V(this);
        light.sub(new P(150, 100));
        light.scale(0.02);
        P shad = new P(this);
        shad.add(light);
        
        V dir = new V(dx, dy);
        dir.sub(this);
        if (dir.length()<0.1) {
        	dir = new V(team.getBall());
            dir.sub(this);            
        }
        double ang = dir.angle(new V(1, 0));
        if (dir.y<0) ang*=-1;  
        
        try {
        	Graphics2D g2 = (Graphics2D)g;
//        	g2.translate((int)x, (int)y);
        	g2.rotate(ang, x, y);
        	g.setColor(team.getTeamColor());
        	g.fillRect((int)x-RADIUS, (int)y-RADIUS, 2*RADIUS, 2*RADIUS);
        	g.drawImage(pimage, (int)x-RADIUS, (int)y-RADIUS, null);
        	g2.rotate(-ang, x, y);
//        	g2.translate((int)-x, (int)-y);
        } catch (Exception e) {
        	g.setColor(Color.PINK);
            g.drawLine((int)shad.x, (int)shad.y, (int)(shad.x+20*Math.cos(ang)), (int)(shad.y+20*Math.sin(ang)));
        	
            g.setColor(team.getTeamColor());
            g.fillOval((int)x-RADIUS, (int)y-RADIUS, 2*RADIUS, 2*RADIUS);
        }
        
        

        //        team.println("p painted");
    }
    
    void doMove() {
//        System.out.println(this+""+n+":domove6");
        V dir = new V(dx, dy);
        dir.sub(this);
        
        double dirs = dir.length();
        
        if (dirs>MAX_SPEED) {
            dir.scale(MAX_SPEED/(dirs));
            add(dir);
        } else {
            x = dx;
            y = dy;
        } 
        
        

    }
    
    public void goTo(double gox, double goy) {
    	this.dx = this.cil_x = (float)gox;
    	this.dy = this.cil_y = (float)goy;
    }
    
    public void goTo(P p) {
    	goTo(p.x, p.y);
    }
    
}
