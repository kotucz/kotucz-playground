package kotuc.chaos;

import javax.vecmath.*;

import javax.media.j3d.*;

import java.util.*;

public class Arrow extends BranchGroup implements Runnable {

    /**
     *	current possition in 3D space
     *	x, y, z
     *	@unit m
     */
    protected Point3d pos = new Point3d(Math.random() * 20.0, Math.random() * 20.0, /*Math.random()*2*/ 0.0);

    public void setPos(Point3d p) {
        this.pos = p;
        double[] c = ga.getCoordRefDouble();
        c[0] = pos.x;
        c[1] = pos.y;
        c[2] = pos.z;
    }

    public void setPos(Transform3D t, Point3d point) {
        Point3d p = new Point3d(point);
        t.transform(p);
        setPos(p);
    }

    public Point3d getPos() {
        return this.pos;
    }
    /**
     *	current velocity in 3D space
     *	x, y, z
     *	@unit m/s
     */
    protected Vector3d vector = new Vector3d();

    public void setVector(Vector3d v) {
        this.vector = v;
        double[] c = ga.getCoordRefDouble();
        c[3] = vector.x + pos.x;
        c[4] = vector.y + pos.y;
        c[5] = vector.z + pos.z;
    }

    public void setVector(Transform3D t, Vector3d vect) {
        Vector3d v = new Vector3d(vect);
        t.transform(v);
        v.scale(100);
        setVector(v);
    }

    public Vector3d getVector() {
        return this.vector;
    }

    public void set(Point3d p, Vector3d v) {
        this.setPos(p);
        this.setVector(v);
    }
    ;
    protected Quat4d angle = new Quat4d(0.0, 0.0, 1.0, Math.random() * 6);
    protected Quat4d angularMoment = new Quat4d(0, 0, 0, 2);
    /*	final public Transform3D getTransform () {
    Transform3D tSet = new Transform3D();
    tSet.set(new Vector3f(this.getPos()));
    Transform3D tRot = new Transform3D();
    tRot.rotZ(this.orientation);
    Transform3D tElev = new Transform3D();
    tElev.rotY(-this.elevation);
    Transform3D tIncl = new Transform3D();
    tIncl.rotX(this.inclination);

    //		Transform3D tAngle = new Transform3D();
    //		tAngle.set(this.angle);

    tElev.mul(tIncl);
    tRot.mul(tElev);
    tSet.mul(tRot);
    //		tSet.mul(tAngle);

    //		tSet = new Transform3D(this.angle, new Vector3d(this.pos), 1.0);
    return tSet;
    }
     */
    /**
     *	group, in which arrows are added
     *	should have no transform to local world
     */
    protected static Group objArrows;

    /**
     *	add this method return value to your scene graph, to allow Arrows be visible
     * @return
     */
    public static Group getDefaultRoot() {
        objArrows = new BranchGroup();
        objArrows.setCapability(ALLOW_CHILDREN_EXTEND);
        objArrows.setCapability(ALLOW_CHILDREN_WRITE);
        return objArrows;
    }

    public static void setDefaultRoot(Group objRoot) {
        if (!objRoot.getCapability(ALLOW_CHILDREN_EXTEND)) {
            System.err.println("Invalid root group: " + "ALLOW_CHILDREN_EXTEND capability not set");
            return;
        }
        objArrows = objRoot;
    }
    private static List arrows = new LinkedList();

    public Arrow() {
        super.setCapability(ALLOW_DETACH);
        super.addChild(this.getArrowObject());
        objArrows.addChild(this);
        arrows.add(this);
//       	System.out.print(". new Arrow .");
    }

    public Arrow(long timeout) {
        this();
        setTimeout(timeout);
    }

    public Arrow(Color3f color) {
        this();
//		System.out.print(shape.getAppearance());
        shape.getAppearance().setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.FASTEST));
    }

    public Arrow(Color3f color, long timeout) {
        this(color);
        this.setTimeout(timeout);
    }
    private long ms = 0;

    private void setTimeout(long ms) {
        this.ms = ms;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            Thread.sleep(ms);
            this.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private GeometryArray ga;
    private Shape3D shape;

    private Node getArrowObject() {
        if (ga == null) {
            ga = new LineArray(2, GeometryArray.COORDINATES | GeometryArray.BY_REFERENCE);
            ga.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
            ga.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);

            ga.setCoordRefDouble(new double[6]);
        }
        this.shape = new Shape3D(ga, new Appearance());
        this.shape.getAppearance().setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        return shape;
    }

    /*	public Object clone() {
    System.out.println("clone Physics");
    return this.clone();
    }	*/
    public void remove() {
        objArrows.removeChild(this);
        arrows.remove(this);
    }
}
