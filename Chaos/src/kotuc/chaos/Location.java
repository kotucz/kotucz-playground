package kotuc.chaos;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import java.util.LinkedList;
import java.util.List;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class Location {

    private BranchGroup objLocation = null;
    private BranchGroup objEntities = null;
    private DirectionalLight sunshine = null;
    private TransformGroup objBgRot = null;
    private List<Entity> entities = new LinkedList<Entity>();

    void addEntityNode(Node node) {
        objEntities.addChild(node);
    }

    void removeEntityNode(Node node) {
        objEntities.removeChild(node);
    }

    TransformGroup viewTransformGroup;

    void setViewTransformGroup(TransformGroup viewTransformGroup) {
        this.viewTransformGroup = viewTransformGroup;
    }

    public TransformGroup getViewTranformGroup() {
        return viewTransformGroup;
    }

//	Player player = null;
//	Vector entities = new Vector();
    /*	public void addEntity(PhysicEntity entity) {
    this.entities.add(entity);
    }
     */
    /*	Vector obstacles = new Vector();

    public void addObstacle(Obstacle obstacle) {
    this.obstacles.add(obstacle);
    }
     */
    public Location() {
        objLocation = new BranchGroup();

        objLocation.addChild(Arrow.getDefaultRoot());

        objEntities = new BranchGroup();
        objEntities.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        objEntities.setCapability(Group.ALLOW_CHILDREN_WRITE);
        objLocation.addChild(objEntities);
//		initPhaseMap();
//		objLand.addChild(objPhaseMap);

//		new KotucCommander();
//		new PetrCommander();

//        addEntityNode(new LocationActualizator());

//        addEntityNode(createBox());
//        addEntity(new Land());

//        addEntity(new DarkBall());

        createLights();


//        addEntityNode(createEarth());



//        addEntityNode(createBackground());

//        addEntityNode(createTree(5.0, 6.0));

//        createTree(11.0, 6.0);

//        addEntityNode(createHouse(10.0, 10.0, 0.0));

//        new DarkBall(this);
//        createGrass(2.0, 2.0);

//    	createGalleon(20.0, 20.0, 0.0);
//		new Player();
//		new Player();
//		new Soldier3D();
//		new Soldier3D();
//		new Animal();
//		new Plant();	
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
//        System.out.println("add "+entities.size());
        entities.add(entity);
        entity.setLocation(this);
        addEntityNode(entity.getMainNode());
//        throw new UnsupportedOperationException();
    }

    public void removeEntity(Entity entity) {
//        System.out.println("remove "+entities.size());
        removeEntityNode(entity.getMainNode());
        entities.remove(entity);

//        objEntities.removeChild(entity.objAdd);
//        throw new UnsupportedOperationException();
    }

    public class LocationActualizator extends Behavior {

        WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);

        public LocationActualizator() {
            setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));
        }

        public void initialize() {
            wakeupOn(w);
        }

        public void processStimulus(java.util.Enumeration en) {

//	actualization of background
            double sunAngle = (double) System.currentTimeMillis() / 3000.0; //1 radian per minute = 30fps*60s

            Transform3D tRot = new Transform3D();
            tRot.rotY(-sunAngle);
            objBgRot.setTransform(tRot);

            sunshine.setDirection(new Vector3f((float) Math.cos(sunAngle - 0.5 * Math.PI), 0.0f, (float) Math.sin(sunAngle - 0.5 * Math.PI)));

            wakeupOn(w);
        }
    }

    Node createTree(double x, double y) {
        Transform3D t3D = new Transform3D();
        t3D.set(new Vector3f((float) x, (float) y, 2));
        Transform3D t3Dr = new Transform3D();
        t3Dr.rotX(0.5 * Math.PI);
        t3D.mul(t3Dr);
        TransformGroup objTree = new TransformGroup(t3D);

        objTree.addChild(new Cylinder(0.1f, 4.0f));
        return objTree;
    }

    Node createHouse(double x, double y, double rotZ) {

        ObjectFile f = new ObjectFile();
        Scene s = null;

        try {
            s = f.load("./models/house1.obj");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Transform3D tHouseSet = new Transform3D();
        tHouseSet.set(new Vector3d(x, y, 0.0));

        Transform3D tHouseRot = new Transform3D();
        tHouseRot.rotZ(rotZ);

        tHouseSet.mul(tHouseRot);

        TransformGroup objHouse = new TransformGroup(tHouseSet);
        objHouse.addChild(s.getSceneGroup());

        return objHouse;
    }

    Node createGalleon(double x, double y, double rotZ) {

        ObjectFile f = new ObjectFile();
        Scene s = null;

        try {
            s = f.load("./models/galleon.obj");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Transform3D tGalleonSet = new Transform3D();
        tGalleonSet.set(new Vector3d(x, y, 0.0));

        Transform3D tGalleonRot = new Transform3D();
        tGalleonRot.rotZ(rotZ);

        tGalleonSet.mul(tGalleonRot);

        TransformGroup objGalleon = new TransformGroup(tGalleonSet);
        objGalleon.addChild(s.getSceneGroup());

        return objGalleon;
    }

    Node createGrass(double x, double y) {

        Transform3D tGrassSet = new Transform3D();
        tGrassSet.set(new Vector3d(x, y, 0));

        TexturedPlane texPlane = new TexturedPlane(getClass().getResource("/images/grass.jpg").toString());

        TransformGroup objGrass = new TransformGroup(tGrassSet);
        //objGrass.addChild(new OrientedShape3D(gGeom, gAppear, OrientedShape3D.ROTATE_ABOUT_POINT, new Vector3f(0.0f, 0.0f, 1.0f));
        objGrass.addChild(new OrientedShape3D(texPlane.createGeometry(), texPlane.createAppearance("images/grass.jpg"), OrientedShape3D.ROTATE_ABOUT_POINT, new Vector3f(0.0f, 0.0f, 1.0f)));


        return objGrass;
    }
    /*
    public double getZatXY (double x, double y) {
    int ix, iy;

    double z;

    if ((x>0)&&(x<widthx*EDGE)&&(y>0)&&(y<widthy*EDGE)) {
    ix = (int)Math.floor(x);	//EDGE == 1
    iy = (int)Math.floor(y);

    z = Math.max(Math.max(verts[ix][iy].z, verts[ix+1][iy].z),
    Math.max(verts[ix+1][iy+1].z, verts[ix][iy+1].z));


    return z+0.2;
    }
    return 0.2;
    }
     */

    Node createBox() {
        Appearance boxAppear = new Appearance();
        TextureLoader tex = new TextureLoader("./images/metal068.jpg", null);
//        if (tex != null) 
//	    	boxAppear.setTexture(tex.getTexture());
        boxAppear.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_FRONT, 0.0f));
        boxAppear.setMaterial(new Material());



        Box objBox = new Box(10.0f, 10.0f, 10.0f, Box.GENERATE_NORMALS | Box.GENERATE_NORMALS_INWARD | Box.GENERATE_TEXTURE_COORDS, boxAppear, 256);

        Transform3D ts = new Transform3D();
        ts.set(new Vector3f(10.0f, 10.0f, 10.0f));
        TransformGroup tg = new TransformGroup(ts);

        tg.addChild(objBox);

        return tg;
    }

    public BranchGroup getMainGroup() {
        return objLocation;
    }

    /**
     *	creating Background
     * @return 
     */
    Background createBackground() {
        BranchGroup objBg = new BranchGroup();

// background		
        Background bg = new Background();

        bg.setApplicationBounds(new BoundingSphere(new Point3d(), 1000.0));
        Sphere sphereObj = new Sphere(1.0f, Sphere.GENERATE_NORMALS |
                Sphere.GENERATE_NORMALS_INWARD |
                Sphere.GENERATE_TEXTURE_COORDS, 45);
        Appearance backgroundApp = sphereObj.getAppearance();

        TextureLoader tex = new TextureLoader("./images/bg.jpg", null);
        if (tex != null) {
            backgroundApp.setTexture(tex.getTexture());
        }

//	background rotation showing time
        Transform3D tBgRot = new Transform3D();
        objBgRot =
                new TransformGroup(tBgRot);
        objBgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objBgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        bg.setGeometry(objBg);
        objBg.addChild(objBgRot);
        objBgRot.addChild(sphereObj);

        return bg;
    } // end of createBackground method

    /**
     *	creating Earth with gravitation center
     * @return
     */
    Node createEarth() {
        System.out.print("creating earth .. ");

        double eRadius = 10.0;

        Sphere sphereObj = new Sphere((float) eRadius, Sphere.GENERATE_NORMALS |
                Sphere.GENERATE_TEXTURE_COORDS, 200);
        Appearance earthAppear = sphereObj.getAppearance();
        earthAppear.setMaterial(new Material());

        TextureLoader tex = new TextureLoader("./images/earth.jpg", null);
        if (tex != null) {
            earthAppear.setTexture(tex.getTexture());
        }

        Physics.gravityCenter = new Point3d(0.0, 0.0, -eRadius);

        Physics earth = new Physics();
        earth.setBounds(new BoundingSphere(new Point3d(), eRadius));
        earth.setPos(Physics.gravityCenter);

//		Transform3D tBgRot = new Transform3D();
//		obj = new TransformGroup(tBgRot);
//		objBgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//		objBgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

//      bg.setGeometry(objBg);
//      objBg.addChild(objBgRot);

        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(Physics.gravityCenter));
        TransformGroup objEarth = new TransformGroup(t3d);

        objEarth.addChild(sphereObj);

        System.out.println("created");

        return objEarth;
    } // end of createBackground method

    void createLights() {
        sunshine = new DirectionalLight(
                new Color3f(1.0f, 0.93f, 0.87f),
                new Vector3f(0, 0, -1));

        sunshine.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
        sunshine.setCapability(DirectionalLight.ALLOW_DIRECTION_READ);
//        sunshine.setScope((Group)getMainGroup(), 0);
        sunshine.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000.0));

        addEntityNode(sunshine);

        AmbientLight ambientLight = new AmbientLight(new Color3f(0.35f, 0.35f, 0.62f));
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000.0));
//        ambientLight.setScope((Group)getMainGroup(), 0);
        addEntityNode(ambientLight);

    }
}
