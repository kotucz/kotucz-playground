package cz.kotu.ld48.prime;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Kotuc
 */
public class Entity {

    Image image;
    final Rectangle2D.Double rect;

    public Entity(Image image, double x, double y, double w, double h) {
        this.image = image;
        rect = new Rectangle2D.Double(x, y, w, h);
    }
}
