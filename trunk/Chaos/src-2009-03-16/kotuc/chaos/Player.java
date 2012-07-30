/**
 *	Player v 0.09;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */
package kotuc.chaos;

import java.awt.event.*;
import java.util.*;


import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.interpolators.*;

class Player extends Soldier3D implements MouseMotionListener, KeyListener, MouseListener {

    public static int playersCount = 0;
    private final int playerNumber;
    static java.util.List canvases = new LinkedList();
    TransformGroup objHeadTG = null;
    private TransformGroup objRightHandTG = null;
    private TransformGroup objLeftHandTG = null;
    private RotationInterpolator rotator = null;
    public static final int VM_FREE_VIEW = 0;
    public static final int VM_1ST_PERSON = 1;
    public static final int VM_3RD_PERSON = 2;
    int viewMode = 1;

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public Player() {
        super();

        this.playerNumber = playersCount;
        playersCount++;

        createAuraLight();

//		createView();
    }

//	private Frame viewFrame;
//	public void createView() {
//		viewFrame = new Frame("Player No."+this.getPlayerNumber());
//      viewFrame.setLayout(new BorderLayout());
//		GraphicsConfiguration config =
//			ConfiguredUniverse.getPreferredConfiguration();
//
//      Canvas3D canvas3D = new Canvas3D(config);
//		canvas3D.addKeyListener(this);
//		canvas3D.addMouseMotionListener(this);
//		canvas3D.addMouseListener(this);

//		this.viewMode=1;

//      viewFrame.add("Center", canvas3D);
//		Player.addCanvas3D(canvas3D);
//		viewFrame.setSize(256, 256);
//		viewFrame.setVisible(true);
//	}
    /**
     *	adds this as listener to canvas
     */
    public void setCanvas(Canvas3D canvas) {
        canvas.addKeyListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseListener(this);
    }

//	public static void addCanvas3D(Canvas3D canvas) {
//		if (canvases==null) canvases = new LinkedList();
//		Player.canvases.add(canvas);
//	}
    public void changeViewMode() {
        this.viewMode++;
        this.viewMode %= 3;
    }
    Group rightAttackBounds;
    RotPosScaleTCBSplinePathInterpolator rightAttackInterpolator;

    TCBKeyFrame[] createRightAttackKeyFrames() {
        TCBKeyFrame[] keyFrames = new TCBKeyFrame[4];

        Point3d pos0 = new Point3d(0.0, -0.4, 0.7);
        Point3d pos1 = new Point3d(0.6, -0.2, 0.5);
        Point3d pos2 = new Point3d(0.5, 0.1, 0.6);
        Point3d pos3 = new Point3d(pos0);


        Point3f p = new Point3f(pos0);            // position
        Quat4f q = new Quat4f(0.0f, 0.6f, 0.3f, 0.9f);
//		float head  = (float)Math.PI/2.0f;           // heading
//     	float pitch = 0.0f;                          // pitch 
//     	float bank  = 0.0f;                          // bank 
        Point3f s = new Point3f(1.0f, 1.0f, 1.0f); // uniform scale
        float t = 0.0f;		//tension
        float c = 0.0f;		//continuity
        float b = 0.0f;		//bias

        p = new Point3f(pos0);
        keyFrames[0] =
                new TCBKeyFrame(0.0f, 0, p, q, s, t, c, b);

        p = new Point3f(pos1);
        q = new Quat4f(0.1f, 0.6f, 0.3f, -0.4f);
        keyFrames[1] =
                new TCBKeyFrame(0.3f, 0, p, q, s, t, c, b);

        p = new Point3f(pos2);
        q = new Quat4f(0.6f, -0.2f, 0.3f, 0.4f);
        keyFrames[2] =
                new TCBKeyFrame(0.6f, 0, p, q, s, t, c, b);

        p = new Point3f(pos3);
        q = new Quat4f(0.3f, 0.7f, -0.9f, -0.3f);
        keyFrames[3] =
                new TCBKeyFrame(1.0f, 0, p, q, s, t, c, b);


        return keyFrames;
    }

    @Override
    public void shoot() {
        reloading = 0;
        super.shoot();
    }

    /*	public void step () {
    //		double prev = pos.x;
    //		pos.x += vel.x*Math.cos(angle)+vel.y*Math.cos(angle+Math.PI/2.0);
    //		if (!validate()) pos.x = prev;
    //		prev = pos.y;
    //		pos.y += vel.x*Math.sin(angle)+vel.y*Math.sin(angle+Math.PI/2.0);
    //		if (!validate()) pos.y = prev;
    //		pos.z += vel.z;

    double floorZ;
    if ((floorZ = location.getZatXY(pos.x, pos.y)+0.6)>pos.z) {
    pos.z = floorZ;
    //			if (vel.z<-0.5) {
    //				player.DEAD
    //			}
    vel.z=0.0;
    } else vel.z-=0.01;

    vel.x*=0.8;
    vel.y*=0.8;

    //		System.out.println("step" + vel);
    }
     */
    public void setView3rdPerson(ViewingPlatform vp) {
//eye moving			
        TransformGroup vpTrans = vp.getViewPlatformTransform();

        Transform3D tSet = new Transform3D();
        tSet.set(new Vector3f(this.getPos()));

        Transform3D tDist = new Transform3D();
        tDist.set(new Vector3f(0.0f, -5.0f, 40.0f));

        Transform3D tRot = new Transform3D();
        tRot.rotX(0.3);

//		Transform3D tVpSet = new Transform3D(); 		
//		tVpSet.set(new Vector3f(0f, 5f, 20f));
//		Transform3D tVpRot = new Transform3D();
//	 	tVpRot.rotZ(this.getAngle()-0.5*Math.PI);
//	 	Transform3D tVpRot2 = new Transform3D();
//	 	tVpRot2.rotX(this.getElevation());

//		tVpSet.lookAt(new Point3d(0.0, 0.0, 0.0), location.player.getPos(), new Vector3d(0.0, 1.0, 0.0));
//		objAxis.setTransform(tVpSet);

//		tVpRot2.mul(tVpSet);
//		tVpRot.mul(tVpRot2);
//		tSet.mul(tVpRot);


        tSet.mul(tDist);
        tRot.mul(tSet);
        vpTrans.setTransform(tRot);
    }

    public void setView1stPerson(ViewingPlatform vp) {
//eye moving			
        TransformGroup vpTrans = vp.getViewPlatformTransform();

//		Transform3D tSet = new Transform3D(); 		
//		tSet.set(new Vector3f(this.getPos()));

//		Transform3D tVpSet = new Transform3D(); 		
//		tVpSet.set(new Vector3f(0f, 0.3f, 2f));
//		Transform3D tVpRot = new Transform3D();
//	 	tVpRot.rotZ(this.getAngle()-0.5*Math.PI);
//	 	Transform3D tVpRot2 = new Transform3D();
//	 	tVpRot2.rotX(this.getElevation());

//		tVpSet.mul(tVpRot2);
//		tVpRot.mul(tVpSet);
//		tSet.mul(tVpRot);

//		location.player.rightAttackBounds.getLocalToVworld(tSet);

        Transform3D tSet = getPhysics().getTransform();

        Transform3D tPom1 = new Transform3D();
        tPom1.rotZ(-Math.PI / 2.0);

        Transform3D tPom2 = new Transform3D();
        tPom2.rotX(Math.PI / 2.0);

        tPom1.mul(tPom2);
        tSet.mul(tPom1);


        vpTrans.setTransform(tSet);
    }

    private void actualizeView() {
//        if (this.viewMode == 0) {
//            return;
//        }
//        ViewingPlatform vp = Entity.universe.getViewers()[0/*this.playerNumber*/].getViewingPlatform();
//        if (this.viewMode == VM_3RD_PERSON) {
//            setView3rdPerson(vp);
//            return;
//        }
//        if (this.viewMode == VM_1ST_PERSON) {
//            setView1stPerson(vp);
//            return;
//        }
    }
    private Light auraLight;

    public void createAuraLight() {
        auraLight = new PointLight(new Color3f(1.0f, 0.9f, 0.7f), new Point3f(0, 0, 1), new Point3f(0.6f, 0.2f, 0.0f));
//		spotLight.setCapability(SpotLight.ALLOW_ENABLE_WRITE);
        auraLight.addScope((Group) getLocation().getMainGroup());
        auraLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000.0));
        this.addPart(new EntityPart(auraLight));
    }
    double lastx, lasty;

    public void mouseMoved(MouseEvent e) {
//		this.mecAlfa=(e.getX()-160)/100;
//		this.mecBeta=(e.getY()-120)/100;


        /*		this.angle+=-(double)(e.getX()-lastx)/45.0;
        this.elevation+=-(double)(e.getY()-lasty)/45.0;
         */
        if (e.getX() > lastx) {
            turnRight();
        }
        if (e.getX() < lastx) {
            turnLeft();
        }

        if (e.getY() < lasty) {
//            turnUp();
        }
        if (e.getY() > lasty) {
//            turnDown();
        }

        lastx = e.getX();
        lasty = e.getY();
//		e.translatePoint(20, 0);
    }

    public void mouseDragged(MouseEvent e) {
//		this.mecAlfa=(e.getX()-160)/100;
//		this.mecBeta=(e.getY()-120)/100;		
    }

    public void keyTyped(KeyEvent e) {
    }
    ;
    protected boolean[] pressedKeys = new boolean[KeyEvent.KEY_LAST + 1];

    public void keyPressed(KeyEvent e) {
        pressedKeys[e.getKeyCode()] = true;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                if (viewMode == VM_1ST_PERSON) {
                    goForward();
                } else {
                    getPhysics().accNorth();
                }
//			System.out.print("step forward");
                break;

            case KeyEvent.VK_S:
                if (viewMode == VM_1ST_PERSON) {
                    goBack();

                } else {
                    getPhysics().accSouth();
                }
//			System.out.print("step backward");
                break;

            case KeyEvent.VK_A:
                if (viewMode == VM_1ST_PERSON) {
                    goLeft();
                } else {
                    getPhysics().accWest();
                }
//			System.out.print("step left"); 
                break;

            case KeyEvent.VK_D:
                if (viewMode == VM_1ST_PERSON) {
                    goRight();
                } else {
                    getPhysics().accEast();
                }
//			System.out.print("step right");
                break;

            case KeyEvent.VK_R:
//			performRightAttack();			

                break;

            case KeyEvent.VK_F:
                shoot();

                break;

            case KeyEvent.VK_E:
                new Explosion(new Color3f(0.8f, 0f, 0f));

                break;

            case KeyEvent.VK_B:
                System.out.println("Players bounding object: " + this.getBounds());

                break;

            case KeyEvent.VK_V:
                this.changeViewMode();

                break;
        }

//		keyActions();

    }

    /*	public void keyActions () {
    if (pressedKeys[KeyEvent.VK_W]) goForward();
    if (pressedKeys[KeyEvent.VK_S]) goBack();
    if (pressedKeys[KeyEvent.VK_A]) goLeft();
    if (pressedKeys[KeyEvent.VK_D]) goRight();
    if (pressedKeys[KeyEvent.VK_Q]) turnLeft();
    if (pressedKeys[KeyEvent.VK_E]) turnRight();
    //		if (pressedKeys[KeyEvent.VK_A]) goLeft();
    //		if (pressedKeys[KeyEvent.VK_D]) goRight();
    }
     */
    public void performRightAttack() {
        /*		Bounds bounds = new BoundingSphere();	//rightAttackBounds.getBounds();
        Transform3D tBounds = this.getTransform();
        Transform3D tRightHandSet = new Transform3D();
        tRightHandSet.set(new Vector3f(1.5f, -0.4f, 1.5f));
        rightAttackBounds.getLocalToVworld(tBounds);
        tBounds.mul(tRightHandSet);
        bounds.transform(new BoundingSphere(), tBounds);
        for (int i = 0; i<entities.size(); i++) {
        Entity attackedEntity = (Entity)entities.get(i);
        attack(attackedEntity, bounds);
        }
         */    }

    public void keyReleased(KeyEvent e) {
        pressedKeys[e.getKeyCode()] = false;
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        performRightAttack();
    }
//  abstract methods from entity

    @Override
    public void doEveryFrame() {
//		step();
        actualizeView();
    }

    @Override
    public void collisionDetected() {
//		System.out.println("Player"+location.time);	
//		step();
    }

    @Override
    public String toString() {
        return "Player " + this.hitpoints + "HP";
    }
}
	