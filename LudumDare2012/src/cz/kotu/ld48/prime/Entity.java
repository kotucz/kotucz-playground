package cz.kotu.ld48.prime;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * @author Kotuc
 */
public class Entity {

    Image image;
    final Rectangle2D.Double rect;
    double scale =
            Game.INV_PIXELS_PER_METER;

    public Entity(Image image, double x, double y, double w, double h) {
        this.image = image;
        rect = new Rectangle2D.Double(x, y, w, h);


    }

    void draw(Graphics2D g) {
        AffineTransform tf = new AffineTransform();
        tf.translate(rect.x, rect.y);
        tf.scale(scale, scale);
        g.drawImage(image, tf, null);
        if (Game.DEBUG) {
            g.draw(rect);
        }
    }

    void drawCentered(Graphics2D g) {
        drawCentered(g, 0);
    }

    void drawCentered(Graphics2D g, double rot) {
        AffineTransform tf = new AffineTransform();
        tf.translate(rect.getCenterX(), rect.getCenterY());
        tf.scale(scale, scale);
        tf.rotate(rot);
        tf.translate(-image.getWidth(null) / 2, -image.getHeight(null) / 2);
        AffineTransform tf1 = new AffineTransform(tf);
        g.drawImage(image, tf, null);
        if (image == R.id.moving_obstacle) {
            drawWheel(g, rect.x, -35, 25);
            drawWheel(g, rect.x, 35, 25);
        }
        if (image == R.id.villain) {
            drawWheel(g, rect.x, -35, 15);
            drawWheel(g, rect.x, 35, 15);
        }
        if (image == R.id.car_small) {
            drawWheel(g, rect.x, -20, 20);
            drawWheel(g, rect.x, 20, 20);
        }
        if (Game.DEBUG) {
            g.draw(rect);
        }
    }

    void drawWheel(Graphics2D g, double rot, int x, int y) {
        AffineTransform tf = new AffineTransform();
        tf.translate(rect.getCenterX(), rect.getCenterY());
        tf.scale(scale, scale);
        Image image = R.id.wheel;
        tf.translate(x, y);
        tf.rotate(rot);
        tf.translate(-image.getWidth(null) / 2, -image.getHeight(null) / 2);
        g.drawImage(image, tf, null);
    }

}
