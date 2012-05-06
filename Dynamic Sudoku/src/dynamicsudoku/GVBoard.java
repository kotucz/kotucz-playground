/*
 * SBoard.java
 *
 * Created on 9. únor 2007, 15:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dynamicsudoku;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.vecmath.*;

/**
 *
 * @author PC
 */
public class GVBoard extends java.applet.Applet 
        implements KeyListener, MouseListener {
    
    
    
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    public void init() {
        // TODO start asynchronous download of heavy resources
        
    }
           
    // TODO overwrite start(), stop() and destroy() methods

    public void start() {
        this.addMouseListener(this);
        this.addKeyListener(this);
        setSize(350, 350);
//        new Thread(this).start();
        new Timer().schedule(new RTTask(), 100, 100);
    } 
    
    java.util.List<Vert> verts = new LinkedList(); 
//    java.util.TreeSet<Vert> selVerts = new TreeSet();
    
    public void addVert(Vert v) {
        verts.add(v);
    }
    
    public void deleteVert(Vert v) {
        verts.remove(v);
    }
    public void recomputeForces() {
        
       for (Vert v1:verts) {
       v1.ax = 0;
       v1.ay = 0;
         for (Vert v2:verts) {
            if (v1!=v2) v1.apply(v2);
         }
       }
    }
  
    int height = getHeight();
    int width = getWidth();

    private Color bgcolor = new Color(0xFFFFFFFF);
    private Color vcolor  = new Color(0xFF888888);
    private Color gcolor  = new Color(0xFF44FF44);
    
    private Font font1 = new Font("Arial", Font.BOLD, 18);
  
    
    public void paintField(Graphics g) {
        
        g.setColor(bgcolor);
        
        g.fillRect(0, 0, width, height);        
    
        g.setFont(font1);
        
        for (int x = 0; x<9; x++) {
            for (int y = 0; y<9; y++) {
                
                
            }
        }
  
        
        
        g.setColor(gcolor);

        for (Vert v1:verts) {
        for (Vert v2:verts) {
        	if (v2==v1) continue;
        	
        	g.setColor(Color.green);
        	
        	for (Vert v3:verts) {
//        		if ((v3==v1)||(v3==v2)) break;
        		for (Vert v4:verts) {
        			if (v4==v3) break;
//        			if ((v4==v1)||(v4==v2)||(v4==v3)) break;
       			
        			Point2f p = getCross(v1, v2, v3, v4);
        			if (p!=null) {
        				g.drawRect((int)p.x-5, (int)p.y-5, 10, 10);
        				g.setColor(Color.blue);
        			}

        		}
        	}
            
        	g.drawLine((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
        	
        }
        }
        
        g.setColor(vcolor);
        
        for (Vert v0:verts) {
            v0.paint(g, v==v0);
        }
        
 
        
    }
   
    BufferedImage imageBuffer = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
    
    public void paint(Graphics g) {
        
       
        g.drawImage(imageBuffer, 0, 0, null);
        
//        g.setColor(bgcolor);
//        
//        g.fillRect(0, 0, width, height);        
//    
//        g.setFont(font1);
//        
//        for (int x = 0; x<9; x++) {
//            for (int y = 0; y<9; y++) {
//                
//                
//            }
//        }
//  
//        
//        
//        g.setColor(gcolor);
//
//        for (Vert v1:verts) {
//        for (Vert v2:verts) {
//            g.drawLine((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
//        }
//        }
//        
//        g.setColor(vcolor);
//        
//        for (Vert v0:verts) {
//            v0.paint(g, v==v0);
//        }
        
 
        
    }

    public void grabbing() {
        if (!grabbing) return;
//        System.out.println("grabbing: " + selVerts);
    }

    
    class RTTask extends TimerTask {
    
        public void run() {
//            while (true) {
            try {
                recomputeForces();
                BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
                paintField(bi.getGraphics());
                imageBuffer = bi;
                repaint();
            
            } catch (Exception ex) {}
          }
    }
    
    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar()=='n') action = Action.NEW_VERT;
        if (e.getKeyChar()=='s') action = Action.SELECT_VERT;
        if (e.getKeyCode()==e.VK_DELETE) deleteVert(v);
        if (e.getKeyChar()=='d') deleteVert(v);
        repaint();
    }


    
    public void keyTyped(KeyEvent e) {
        
    }
     
    public void update(Graphics g) {
        paint(g);
    }

    
    enum Action {
        NEW_VERT, 
        SELECT_VERT,
        NO
    }
    
    public Action action = Action.NEW_VERT;
        
    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    private boolean grabbing = false;
    
    private int grabx, graby;
    
    public void mousePressed(MouseEvent e) {
//        if (!selVerts.isEmpty()) {
        if ((v!=null)&&(action == Action.NO)) {
            grabx = e.getX();
            graby = e.getY();
            grabbing = true;
        }
        repaint();
    }
    
    public void mouseReleased(MouseEvent e) {
        if (grabbing) {
            v.x+= e.getX() - grabx;
            v.y+= e.getY() - graby;
            grabbing = false;
            action = Action.NEW_VERT;
        }
        
    }

    Vert v = null;
    
    public void mouseClicked(MouseEvent e) {
        float cx = e.getX();
        float cy = e.getY();
          
        System.out.println(action);
        
        if (e.getButton()==e.BUTTON1) {
            
        switch (action) {
            case NO:
                break;
            case NEW_VERT:
                v = null;
                for (Vert sv : verts) {
                    if (sv.distance(new Point2f(cx, cy))<10) {
                        v=sv;
                        action=Action.NO;
                        break;
                    }
                }
                if (v!=null) break;
                v = new Vert();
                v.x = cx;
                v.y = cy;
                addVert(v);
                break;
            case SELECT_VERT:
//                v = null;
                for (Vert sv : verts) {
                    if (sv.distance(new Point2f(cx, cy))<10) {
                        v=sv;
                        break;
                    }
                }
/*                if (v!=null) {
                if ((e.getModifiers()&e.SHIFT_DOWN_MASK)!=0) {
                    selVerts = new TreeSet();
                }
                selVerts.add(v);
                System.out.println(selVerts.size());
                }
*/                break;
        }           
        }
        if (e.getButton()==e.BUTTON3) {// button 3 is the right!!!
//            action = Action.NO;
            v = null;
        }   
//        e.translatePoint(0, 0) ;
        repaint();
    }
    
    public Point2f getCross(Vert a0, Vert a1, Vert b0, Vert b1) {
    	Vector2f a=new Vector2f();
    	Vector2f b=new Vector2f();
    	Vector2f c=new Vector2f();
    	
    	a.sub(a1, a0);
    	b.sub(b1, b0);
    	c.sub(a0, b0);
    	
    	float t = (-a.x*c.y+a.y*c.x)/(-b.x*a.y+b.y*a.x);    	
    	float s = ( b.x*c.y-b.y*c.x)/(-b.x*a.y+b.y*a.x); 
   	
    	if (((0>t)&&(t>1)&&(0>s)&&(s>1))) return null;
    	
//    	return new Point2f(b0.x+b.x*s, b0.y+b.y*s); 
    	return new Point2f(a0.x+a.x*s, a0.y+a.y*s);
    }
    
}
