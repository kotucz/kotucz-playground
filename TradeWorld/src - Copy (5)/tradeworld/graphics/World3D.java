package tradeworld.graphics;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DecalGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.Light;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author Kotuc
 */
public class World3D {

    private Land land;
    private BranchGroup sceneRoot;
    private DecalGroup decalGroup = new DecalGroup();
    private RoadNetwork roadNetwork;

    public World3D() {
        sceneRoot = createSceneGraph();
        createObjects();
    }

    public Land getLand() {
        return land;
    }

    public BranchGroup getRoot() {
        return sceneRoot;
    }

    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }

    public void createObjects() {

        land = new Land(20, 20);
        this.roadNetwork = land.getRoadNetwork();

        add(land);

        for (int i = 0; i < 1; i++) {
            System.out.println("building " + i);
            final Building3D building3D = new Building3D(3, 2, 1);
            building3D.setPos(new Point3d(Math.random() * 20, Math.random() * 20, 0));
            building3D.refresh();
            this.add(building3D);
        }
        createLights();

        VehicleThread vehicleThread = new VehicleThread();

        for (int i = 0; i < 10; i++) {
            System.out.println("" + i);
            final Vehicle3D vehicle = new Vehicle3D();
            vehicle.setPos(roadNetwork.randomRoadPoint().getPos());
            vehicle.refresh();
            this.add(vehicle);
            vehicleThread.add(vehicle);
        }

//        add(new Notification(Tools.loadTexture("mega4.png").getDetailImage()).getRoot());
        add(new Notification3D(new Point3d()));

        //this.add(new Vehicle().getBranchGroup());
        new Thread(vehicleThread).start();
    }

    public void add(Model3D model) {
        model.setWorld(this);
        model.getRoot().setCapability(BranchGroup.ALLOW_DETACH);
        this.sceneRoot.addChild(model.getRoot());
    }

    public void remove(Model3D model) {
        this.sceneRoot.removeChild(model.getRoot());
    }

    void createLights() {
        BranchGroup lights = new BranchGroup();
        Light sunshine = new DirectionalLight(
                new Color3f(1.0f, 0.93f, 0.87f),
                new Vector3f(-1, -1, -1));

//            sunshine.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
//            sunshine.setCapability(DirectionalLight.ALLOW_DIRECTION_READ);
//            sunshine.setScope(sRoot, 0);
        sunshine.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000000.0));

        lights.addChild(sunshine);

        AmbientLight ambientLight = new AmbientLight(new Color3f(0.35f, 0.35f, 0.62f));
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000000.0));
//        ambientLight.setScope((Group)getMainGroup(), 0);
        lights.addChild(ambientLight);

        this.sceneRoot.addChild(lights);

    }

    private BranchGroup createSceneGraph() {
        // Create the root of the branch graph

        BranchGroup sRoot = new BranchGroup();

        sRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        sRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);

//        sRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
//        sRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);


//        objAxis = new TransformGroup();
//        objAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        objAxis.addChild(new Axis());
//        objRoot.addChild(objAxis);


//	objRoot.addChild(createBackground());


//        objRoot.addChild(location.getMainGroup());

//    pickObject = new PickObject(canvas3D, location.objLocation);

//  ViewActualizator va = new ViewActualizator();
//	PhysicEntity.addBehavior(va);
//    va.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
//    objRoot.addChild(va);

//	public void configureViewBranch () {


//        universe.getViewer().getPhysicalBody().setLeftEyePosition(new Point3d(0.3, 0, -1));
//        universe.getViewer().getPhysicalBody().setRightEyePosition(new Point3d(0.5, 0, -1));



//		scene.addChild(new MouseRotate(vpTrans));
//		scene.addChild(new MouseZoom(vpTrans));
//		scene.addChild(new MouseTranslate(vpTrans));

//		ViewPlatformAWTBehavior awtBehavior = new ViewPlatformAWTBehavior(ViewPlatformAWTBehavior.KEY_LISTENER);
//		awtBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
//		objRoot.addChild(awtBehavior);




//        AudioDevice audioDev = universe.getViewer().createAudioDevice();

//        ViewingPlatform vp = universe.getViewingPlatform();
//        vp.setPlatformGeometry(createPlatformGeometry());

//	}

//    objRoot.compile();

        return sRoot;

    } // end of CreateSceneGraph method of HelloJava3Db
}
