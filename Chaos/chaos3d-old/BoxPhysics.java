package cz.kotuc.chaos;

import javax.vecmath.*;

import javax.media.j3d.*;

import java.util.*;

public class BoxPhysics extends Physics {
	
	public Bounds getBounds () {
		Bounds thisBounds = new BoundingBox();
		thisBounds.transform(thisBounds, this.getTransform());
		return thisBounds;
	}

/*
	public void perform () {
		double period = 1.0/60.0; // the time of one frame
		
//		if (vel.length()>maxVel) vel.scale(maxVel/vel.length());
		this.vel.scaleAdd(period, this.acceleration, this.vel);
		this.acceleration.scale(0.5);

		this.useAngularRotation(period);

// gravity 		
		if (gravityCenter==null) 
			this.vel.scaleAdd(period, gravity, this.vel);
		else {
			Vector3d g = new Vector3d();
			g.sub(Physics.gravityCenter, this.pos);
			g.scale(Physics.gravity.length()/g.length());
			this.vel.scaleAdd(period, g, this.vel);
		}
				
		this.vel.scale(1.0-this.slidingFriction*period);
		this.pos.scaleAdd(period, this.vel, this.pos);

//		Iterator it = Physics.solids.iterator();
		Iterator it = collisions().iterator();
		while (it.hasNext()) {
			Physics solid = (Physics)it.next(); 
//			if (!solid.colliding(this)) continue;
			
//			System.out.println(this+": collision ");
			
			Vector3d normal = new Vector3d();
			double angle;
			
			normal.sub(this.pos, solid.getPos());
			
			if ((angle=this.vel.angle(normal))>(Math.PI/2.0)) {
				normal.scale((Math.cos(Math.PI-angle)*this.vel.length())/normal.length());

				this.pos.scaleAdd(period*(1.0+this.elasticity), normal, this.pos);
				this.vel.add(normal);
			};
			
		};
		
		this.velocityArrow.set(pos, vel);
		
		return ;
	}



*/
	
		
	public BoxPhysics() {
		System.out.print(". type: Box .");
	}
	
	public static final Point3d[] corners = {
		new Point3d(+1.0, -1.0, -1.0),
		new Point3d(+1.0, -1.0, +1.0),
		new Point3d(+1.0, +1.0, -1.0),
		new Point3d(+1.0, +1.0, +1.0),
		
		new Point3d(-1.0, -1.0, -1.0),
		new Point3d(-1.0, -1.0, +1.0),
		new Point3d(-1.0, +1.0, -1.0),
		new Point3d(-1.0, +1.0, +1.0)
	};
	
	
	
	public Point3d getCollidingPoint(Physics p) {
		Point3d point;// = new Point3d(this.pos);
		//point.scaleAdd(0.5, p.getPos(), this.pos);
		Bounds bounds = p.getBounds();
		for (int i = 0; i<8; i++) {
			point = new Point3d(corners[i]);
			this.getTransform().transform(point);
			if (bounds.intersect(point)) 
				return point;
		}
		return null;
	}	
	
	static final Point3d[] edges = {
		new Point3d(0.0, 0.0, +1.0),
		new Point3d(0.0, 0.0, -1.0),
		
		new Point3d(+1.0, 0.0, 0.0),
		new Point3d(-1.0, 0.0, 0.0),
		
		new Point3d(0.0, +1.0, 0.0),
		new Point3d(0.0, -1.0, 0.0)
	};
		
	public Vector3d getSurfaceNormal(Point3d p) {
		Vector3d normal = new Vector3d();
		normal.sub(p, this.pos);
		normal.scale(1/normal.length());
		
		double minDist = 100.0; 
		for (int i = 0; i<6; i++) {
			Point3d edge = new Point3d(edges[i]);
			this.getTransform().transform(edge);
			if (p.distance(edge)<minDist) {
				minDist = p.distance(edge);
				normal = new Vector3d(edges[i]);
				this.getTransform().transform(normal);
			}	
		}
		//normal.sub(p, this.pos);
		//normal.scale(1/normal.length());
		return normal;
	}	
}
