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
        addChild(new Sphere(0.1f));
    }

    @Override
    public void refresh() {
        setPos(bullet.getPos(getWorld3D().getCurrentTime()), 0);
    }
   

}
