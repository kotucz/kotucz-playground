package kotuc.chaos;
//import java.awt.*;

import javax.media.j3d.*;
import javax.vecmath.*;
import kotuc.chaos.arena.CommandListener;

public class Soldier3D extends Humanoid implements CommandListener {
//	double VELOCITY = 0.0;

    public Soldier3D() {
        super();

//		createSpotLight();

//		new Bullet();
//		new Bullet();
//		new Bullet();
//		new Bullet();
    }
    int reloading = 24;

    public void shoot() {
        if (this.reloading == 0) {
            this.reloading = 24;
            System.out.println(this + ": shooting");
//			Bullet.shoot(this);
            Vector3d v = new Vector3d(1.0, 0.0, 0.0);
            this.getTransform().transform(v);
            v.scale(30);
            Arrow a = new Arrow(new Color3f(0.2f, 0.7f, 0.1f), 100);
            a.set(this.getPos(), v);
        } else {
            this.reloading--;
        }
    }

    public void endOfTurn() {
        /*	try {
        Thread.sleep(10);
        } catch(InterruptedException e) {
        e.printStackTrace();
        }*/
    }

    public void hittedCallback(PhysicEntity e) {
        this.hitpoints -= 5;
        turnRight();
//		this.vel=new Vector3d(e.vel);
        if (this.hitpoints < 1) {
            this.kill();
        }
    }

    public void doEveryFrame() {
//		goForward();
        if (aimed()) {
//			if (Math.random()<0.1) playSound("targetdetected.wav");
            shoot();
            goForward();
//			goForward();						
        } else {

//			goUp();
//			if (Math.random()<0.01) turnUp();
//			if (Math.random()<0.01) inclineRight();
            turnRight();
        }
        ;
    //		if (Math.random()>0.99) {
    //			goBack();
    //			goBack();goBack();goBack();goBack();goBack();goBack();
    //		}

    }

    /**
     *	@returns if some entity is in this aim line
     *
     */
    public boolean aimed() {
//        for (PhysicEntity ent : getLocation().getEntities()) {
//            if (!ent.equals(this) && (!(ent instanceof Bullet)) && (ent).hitpoints > 0 && (ent).getBounds().intersect(
//                    this.getPos(),
//                    new Vector3d(Math.cos(this.getAngle()), Math.sin(this.getAngle()), 0.0))) {
////				if (Math.random()<0.1) ent.hittedCallback(this);
//                return true;
//            }
//        }
        return false;
    }
    SpotLight spotLight;

    public String toString() {
        return "Soldier " + this.hitpoints + "HP";
    }

    public void goForward() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void goBack() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void goLeft() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void goRight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void turnRight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void turnLeft() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
