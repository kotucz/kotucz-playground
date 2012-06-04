/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.tiles;

/**
 *
 * @author Kotuc
 */
import javax.vecmath.Point3d;

/**
 *
 * @author Kotuc
 */
public class Heighmap {

    private final Point3d[][] heighmap;

    public Heighmap(int tilesx, int tilesy) {
        double SIDE = 1;
        double random = 0.;
        this.heighmap = new Point3d[tilesx + 1][tilesy + 1];
        for (int y = 0; y < tilesy + 1; y++) {
            for (int x = 0; x < tilesx + 1; x++) {
                heighmap[x][y] = new Point3d(
                        (x * SIDE),
                        (y * SIDE),
                        (random * Math.random()));
            }
        }
    }

    public Point3d get(int x, int y) {
        return heighmap[x][y];
    }

    public void set(int x, int y, double height) {
        heighmap[x][y].z = height;
    }
}

