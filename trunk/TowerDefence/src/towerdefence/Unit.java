package towerdefence;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public class Unit {

    protected Point3d pos = new Point3d();
    protected Board board;
    protected double speed;
    protected double shootRange;
    protected int health = 100;
    protected int fakeHealth = health;
    protected int buildTime = 0;
    protected int buildCost = 0;
    protected BufferedImage img = getImage("creep1_16.png");

    protected Unit() {
    }

    void setBoard(Board board) {
        this.board = board;
    }

    public Point3d getPos() {
        return new Point3d(pos);
    }

    public void setPos(Point3d pos) {
        this.pos = pos;
    }

    /**
     * 
     * @param g
     */
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
//        g.fillOval((int)pos.x-5, (int)pos.y-5, 10, 10);
        g.drawImage(img, (int) pos.x - 8, (int) pos.y - img.getWidth(null), null);
//        g.drawString(""+this, (int)pos.x-5, (int)pos.y-5);
    }

    /**
     * 
     */
    public void doRound() {
    }

    /**
     * 
     * @param dest
     * @return true if succeed
     * false if cannot move - steady or collision
     */
    public boolean goTo(Point3d dest) {
        Vector3d dir = dirTo(dest);
        dir.normalize();
        dir.scale(speed / 100.0);
        pos.add(dir);
        return true;
    }

    public Vector3d dirTo(Point3d point) {
        Vector3d dir = new Vector3d(point);
        dir.sub(this.pos);
        return dir;
    }

    public double dist(Unit unit2) {
        return pos.distance(unit2.pos);
    }

    public void destroy() {
        board.remove(this);
    }
    private static Map<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();

    protected BufferedImage getImage(String name) {
        BufferedImage result = imageMap.get(name);
        if (result == null) {
            try {
                result = ImageIO.read(getClass().getResourceAsStream("/images/" + name));
                imageMap.put(name, result);
            //return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/"+name));
            } catch (IOException ex) {
                Logger.getLogger(Unit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    //return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/"+name));
    }

    @Override
    public Unit clone() {
        try {
            Unit clone = getClass().newInstance();
            clone.fakeHealth = this.health; // set fake to full
            clone.health = this.health;
            clone.img = this.img;
            clone.shootRange = this.shootRange;
            clone.speed = this.speed;
            return clone;
        } catch (InstantiationException ex) {
            Logger.getLogger(Unit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Unit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
