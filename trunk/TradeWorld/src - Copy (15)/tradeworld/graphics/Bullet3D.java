package tradeworld.graphics;

import com.sun.j3d.utils.geometry.Sphere;
import tradeworld.war.Bullet;

/**
 *
 * @author Kotuc
 */
public class Bullet3D extends Model3D {

    private final Bullet bullet;

    public Bullet3D(Bullet bullet) {
        this.bullet = bullet;
        addChild(new Sphere(1));
    }

    @Override
    public void refresh() {
        setPos(bullet.getPos(), 0);
    }
   

}
