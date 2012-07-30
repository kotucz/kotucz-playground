package kotuc.chaos;

import javax.vecmath.*;

import javax.media.j3d.*;

import java.util.*;

public class Physics {

    public static Vector3d gravity = new Vector3d(0, 0, -9.81);
    public static Point3d gravityCenter = null;
    public static List solids = new LinkedList();
    protected double orientation = 0;

    public void setOrientation(double angle) {
        this.orientation = angle;
    }

    public double getOrientation() {
        return this.orientation;
    }
    protected double elevation = 0.0;

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double eangle) {
        this.elevation = eangle;
    }
    /**
     *	maximum velocity
     *	@unit m/s
     */
    protected double maxVel = 4.0;
    /**
     *	maximum acceleration
     *	@unit (m/s)/s
     */
    protected double maxAcc = 3.0;
    /**
     *	mass
     *	@unit kg
     */
    protected double mass = 50.0;	//	kg
    /**
     *	sliding friction
     *	must be in [0, 1]
     *	0 .. no friction
     *	1 .. max friction .. fixed position
     *	@unit no unit
     */
    protected double slidingFriction = 0.5;
    /**
     *	body elasticity
     *	must be in [0, 1]
     *	0 .. non elastic
     *	1 .. max elasticity
     *	@unit no unit
     */
    protected double elasticity = 0.5;
    /**
     *	current possition in 3D space
     *	x, y, z
     *	@unit m
     */
    protected Point3d pos = new Point3d(Math.random() * 20.0, Math.random() * 20.0, /*Math.random()*2*/ 0.0);

    public void setPos(Point3d point) {
        this.pos = point;
    }

    public Point3d getPos() {
        return this.pos;
    }
    /**
     *	current velocity in 3D space
     *	x, y, z
     *	@unit m/s
     */
    protected Vector3d vel = new Vector3d();

    public void setVel(Vector3d vel) {
        this.vel = vel;
    }

    public Vector3d getVel() {
        return this.vel;
    }
    double period = 1.0 / 60.0;
    protected Vector3d acceleration = new Vector3d();

    public void setAccRel(Vector3d acc) {
        this.getTransform().transform(acc);
        this.acceleration = acc;
    }

    public void setAcc(Vector3d acc) {
        this.acceleration = acc;
    }

    public void accRelForward() {
        this.setAccRel(new Vector3d(this.maxAcc, 0.0, 0.0));
    }

    public void accRelRight() {
        this.setAccRel(new Vector3d(0.0, -this.maxAcc, 0.0));
    }

    public void accRelLeft() {
        this.setAccRel(new Vector3d(0.0, this.maxAcc, 0.0));
    }

    public void accRelBack() {
        this.setAccRel(new Vector3d(-this.maxAcc, 0.0, 0.0));
    }

    public void accRelUp() {
        this.setAccRel(new Vector3d(0.0, 0.0, this.maxAcc));
    }

    public void accRelDown() {
        this.setAccRel(new Vector3d(0.0, 0.0, -this.maxAcc));
    }

    public void accEast() {
        this.setAcc(new Vector3d(this.maxAcc, 0.0, 0.0));
    }

    public void accWest() {
        this.setAcc(new Vector3d(-this.maxAcc, 0.0, 0.0));
    }

    public void accSouth() {
        this.setAcc(new Vector3d(0.0, -this.maxAcc, 0.0));
    }

    public void accNorth() {
        this.setAcc(new Vector3d(0.0, +this.maxAcc, 0.0));
    }

    public void turnRight() {
        this.orientation -= 0.1;
        Transform3D t = new Transform3D();
        t.rotZ(-0.1);
        this.rotTransform.mul(t);
    }

    public void turnLeft() {
        this.orientation += 0.1;
        Transform3D t = new Transform3D();
        t.rotZ(0.1);
        this.rotTransform.mul(t);
    }

    public void inclineUp() {
        this.elevation += 0.1;
    }

    public void inclineDown() {
        this.elevation -= 0.1;
    }
    /**
     *	rotation around x-axis
     */
    protected double inclination = 0.0;

    public void inclineRight() {
        this.inclination += 0.1;
    }

    public void inclineLeft() {
        this.inclination -= 0.1;
    }
    protected Transform3D rotTransform = new Transform3D();
    protected Transform3D angularMoment = new Transform3D();

    protected void useAngularRotation(double period) {
        rotTransform.mul(angularMoment);
    }
    private Bounds bounds = new BoundingSphere();

    public void setBounds(Bounds b) {
        this.bounds = b;
    }

    public Bounds getBounds() {
        Bounds thisBounds = new BoundingSphere();//this.objAvatar.getBounds();
        thisBounds.transform(this.bounds, this.getTransform());
        return thisBounds;
    }

    public Transform3D getTransform() {
        Transform3D tSet = new Transform3D();
        tSet.set(new Vector3f(this.getPos()));
//		Transform3D tRot = new Transform3D();
//		tRot.rotZ(this.orientation);
//		Transform3D tElev = new Transform3D();
//		tElev.rotY(-this.elevation);
//		Transform3D tIncl = new Transform3D();
//		tIncl.rotX(this.inclination);

//		tElev.mul(tIncl);
//		tRot.mul(tElev);
//		tSet.mul(tRot);

        tSet.mul(this.rotTransform);
//		tSet = new Transform3D(this.rotTransform, new Vector3d(this.pos), 1.0);
        return tSet;
    }

    /**
     *	@returns List of colliding Physics
     */
    public List collisions() {
        Iterator it = solids.iterator();
        List colls = new LinkedList();
        while (it.hasNext()) {
            Physics p;
            if (!((p = (Physics) it.next()).equals(this)) //				&&(p.hitpoints>0)
                    //				&&(!(p instanceof Bullet)||((((Bullet)ent).shooter)!=this))
                    && (p.getBounds().intersect(this.getBounds()))) {
                colls.add(p);
            }
            
        }
        
        return colls;
    }
    Arrow velocityArrow = new Arrow(new Color3f(0.8f, 0.2f, 0.5f));
    Arrow a = new Arrow(new Color3f(0f, 0.7f, 1f));
    

    public final void perform() {
        double period = 1.0 / 60.0; // the time of one frame

//		if (vel.length()>maxVel) vel.scale(maxVel/vel.length());
        this.vel.scaleAdd(period, this.acceleration, this.vel);
        this.acceleration.scale(0.5);

        this.useAngularRotation(period);

// gravity 		
        if (gravityCenter == null) {
            if (this.pos.z > 1) {
                this.vel.scaleAdd(period, gravity, this.vel);
            }
            if (this.pos.z < 1) {
                this.pos.z = 1;
                this.vel.z = 0;
            }
        } else {
            Vector3d g = new Vector3d();
            g.sub(Physics.gravityCenter, this.pos);
            g.scale(Physics.gravity.length() / g.length());
            this.vel.scaleAdd(period, g, this.vel);
        }

        this.vel.scale(1.0 - this.slidingFriction * period);
        this.pos.scaleAdd(period, this.vel, this.pos);

////		Iterator it = Physics.solids.iterator();
//		Iterator it = collisions().iterator();
////	computes collisions for all colliding phycics
//		while (it.hasNext()) {
//			Physics solid = (Physics)it.next(); 
////			if (!solid.colliding(this)) continue;
//			
////			System.out.println(this+": collision ");
//			
//			Point3d corner = getCollidingPoint(solid);
//			
//			Vector3d normal = solid.getSurfaceNormal(corner);// = new Vector3d();
//			double rotTransform;
//			
//			a.set(corner, normal);
//			
//			//normal.sub(this.pos, solid.getPos());
//			
//			
//			if ((rotTransform=this.vel.rotTransform(normal))>(Math.PI/2.0)) {
//				normal.scale((Math.cos(Math.PI-rotTransform)*this.vel.length())/normal.length());
//
//				this.pos.scaleAdd(period*(1.0+this.elasticity), normal, this.pos);
//				this.vel.add(normal);
//			};
//			
//		};

//	shows this physics velocity		
        this.velocityArrow.set(pos, vel);

        return;
    }

    /**
     *	@return this object surface normal at position p<br>normal is oriented out with length 1.0m
     *	@param p position in local world
     */
    public Vector3d getSurfaceNormal(Point3d p) {
        Vector3d normal = new Vector3d();
        normal.sub(p, this.pos);
        normal.scale(3.0 / normal.length());
        return normal;
    }

    /**
     *	@param p
     * @return this objects point nearest to position p
     *	@ p position in local world
     */
    public Point3d getCollidingPoint(Physics p) {
        Point3d point = new Point3d();// = new Point3d(this.pos);
//		point.scaleAdd(0.5, p.getPos(), this.pos);

        point = new Point3d(this.pos);
        return point;
    }

    public Physics() {
        Physics.solids.add(this);
    }

    public void remove() {
        velocityArrow.remove();
        a.remove();
        Physics.solids.remove(this);
    }

}
