package kotuc.chaos;
//import java.awt.*;

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Humanoid extends PhysicEntity {

    protected float walking = 0;

    public Humanoid() {

        this.addPart(new EntityPartObjModelTextured(new Vector3f(0f, 0f, 1.3f), "beethoven.obj", ""));// "ludvik.jpg"));
        this.addPart(new EntityPart(createLegs()));
//		this.addPart(new EntityPartObjModelTextured(new Vector3f(0f, 0f, 1.3f), "p51_mustang.obj", ""));

//		createVisualBounds();
        createSpotLight();

    }

    public float getWalking() {
        return this.walking;
    }
    int reloading = 240;

    /*	public void shoot() {
    if (this.reloading==0) {
    this.reloading=24;
    //			Bullet.shoot(this);
    } else this.reloading--;
    }
     */
    public void endOfTurn() {
        /*	try {
        Thread.sleep(10);
        } catch(InterruptedException e) {
        e.printStackTrace();
        }*/
    }
    /*
    public void paint(Graphics g) {
    g.drawOval((int) this.pos.x - 5, (int) this.pos.y - 5, 10, 10);
    g.drawString(this.toString(), (int) this.pos.x - 15, (int) this.pos.y - 10);
    }

    public void run() {
    while (true) {
    go(1);
    turnRight(1);

    }
    }
     */

    public void hitted(Bullet bullet) {
        this.hitpoints -= 5;
        if (this.hitpoints < 1) {
            this.kill();
        }
    }

    public void kill() {
    }

//	public void doEveryFrame() {					

//	}
    /*	public BranchGroup createAvatar() {


    ObjectFile f = new ObjectFile();
    Scene s = null;

    BranchGroup objPlayer = new BranchGroup();
    objPlayer.setPickable(false);

    try {
    s = f.load("models/head1.obj");

    } catch (Exception e) {
    e.printStackTrace();

    }

    Transform3D tHeadSet = new Transform3D();
    tHeadSet.set(new Vector3f(0f,0f,1f));

    Transform3D tHeadRot = new Transform3D();
    tHeadRot.rotZ(0);
    tHeadRot.mul(tHeadSet);

    TransformGroup objHeadTG = new TransformGroup(tHeadRot);
    objHeadTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objHeadTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

    objPlayer.addChild(objHeadTG);
    objHeadTG.addChild(s.getSceneGroup());

    objPlayer.addChild(createLegs());

    return objPlayer;
    }
     */
    public BranchGroup createLegs() {
        BranchGroup objLegs = new BranchGroup();
        TransformGroup objRLegTrans = new TransformGroup();
        objRLegTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objRLegTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        RotationInterpolator rri = new RotationInterpolator(new WalkingAlpha(this), objRLegTrans);
//		RotationInterpolator rri = new RotationInterpolator(new Alpha(), objRLegTrans);
        rri.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        objLegs.addChild(rri);
//		objRLegTrans.addChild(new Cylinder());
        objRLegTrans.addChild(new ColorCube(0.3));
        objLegs.addChild(objRLegTrans);

        return objLegs;
    }
    SpotLight spotLight;

    public void createSpotLight() {
        spotLight = new SpotLight(new Color3f(1f, 0.5f, 0.5f), new Point3f(0, 0, 1), new Point3f(1.0f, 0.0f, 0.0f), new Vector3f(1f, 0f, 0f), 0.02f, 0.8f);
//		spotLight.setCapability(SpotLight.ALLOW_ENABLE_WRITE);
        spotLight.addScope((Group)getLocation().getMainGroup());
        spotLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000.0));
        this.addPart(new EntityPart(spotLight));
    }

    @Override
    public String toString() {
        return "Soldier " + this.hitpoints + "HP";
    }

    @Override
    public void doEveryFrame() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
