package kotuc.chaos;

import javax.vecmath.*;

import javax.media.j3d.*;

import java.util.*;

/**
 *	before using, getDefaultRoot() value must be added to scene graph so explosions could display
 */
public class Explosion extends Entity {

    private static List explosions = new LinkedList();
    static GeometryUpdater updater = new ExplosionUpdater();

    public Explosion() {
        getMainNode().addChild(this.getExplosionObject());
        explosions.add(this);
//        System.out.print(". new Explosion .");
    }

    public Explosion(Color3f color) {
        this();
//		System.out.print(shape.getAppearance());
        shape.getAppearance().setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.FASTEST));
    }
    private GeometryArray ga;
    private Shape3D shape;

    private Node getExplosionObject() {
        /*		if (ga==null) {
        ga = new LineArray(2, GeometryArray.COORDINATES|GeometryArray.BY_REFERENCE);
        ga.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        ga.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);

        ga.setCoordRefDouble(new double[6]);
        }
         */
        ga = ExplosionUpdater.createParticles(50);
        this.shape = new Shape3D(ga, new Appearance());
        Appearance appear = this.shape.getAppearance();
        appear.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        appear.setPointAttributes(new PointAttributes(3f, true));

        shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);

        Transform3D t = new Transform3D();
        t.set(new Vector3f(this.getPos()));
        TransformGroup tg = new TransformGroup(t);
        tg.addChild(shape);
        return tg;
    }

    @Override
    public void doEveryFrame() {
        updater.updateData(ga);
    }
    /*	public Object clone() {
    System.out.println("clone Physics");
    return this.clone();
    }	*/
}

class ExplosionUpdater implements GeometryUpdater {

    public void updateData(Geometry geometry) {
        GeometryArray geometryArray = (GeometryArray) geometry;
        float[] coords = geometryArray.getCoordRefFloat();

        int N = geometryArray.getValidVertexCount();

        for (int i = 0; i < N; i += 2) {
            if (coords[i * 3 + 2 + 3] > -1.0f) {
                float vx = coords[i * 3 + 0 + 3] - coords[i * 3 + 0];
                float vy = coords[i * 3 + 1 + 3] - coords[i * 3 + 1];
                float vz = coords[i * 3 + 2 + 3] - coords[i * 3 + 2];

                coords[i * 3 + 0] += vx;
                coords[i * 3 + 1] += vy;
                coords[i * 3 + 2] += vz;

                coords[i * 3 + 0 + 3] += vx;
                coords[i * 3 + 1 + 3] += vy;
                coords[i * 3 + 2 + 3] += vz - 0.01f;
            }
        }
    }

    /**	Creates GeometryArray, that will look like explosion
     *	if ExplosionUpdater is used
     *
     *	@param N the count of particles to be created
     *	when N is too high, your graphic card may not be able to paint them
     *	when N is too small, it wouldn´t look like an explosion
     *
     *	@return new GeometryArray
     */
    public static GeometryArray createParticles(int N) {
        N *= 3;

        GeometryArray particles = new LineArray(N * 2, GeometryArray.COORDINATES | GeometryArray.BY_REFERENCE);

        particles.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        particles.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        particles.setCapability(GeometryArray.ALLOW_COUNT_READ);

        float[] coordinates = new float[N * 2 * 3];

        for (int i = 0; i < N; i += 2) {
            coordinates[i * 3 + 0] = 0f;		//	x
            coordinates[i * 3 + 1] = 0f;		//	y
            coordinates[i * 3 + 2] = 0f;		//	z

            coordinates[i * 3 + 0 + 3] = (float) Math.random() * 0.2f - 0.1f;		//	x
            coordinates[i * 3 + 1 + 3] = (float) Math.random() * 0.2f - 0.1f;		//	y
            coordinates[i * 3 + 2 + 3] = (float) Math.random() * 0.2f - 0.05f;		//	z
        }

        particles.setCoordRefFloat(coordinates);

        return particles;
    }
}


	