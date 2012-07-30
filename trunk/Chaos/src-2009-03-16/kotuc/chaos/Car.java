/**
 *	Car v 0.01;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */
package kotuc.chaos;

import java.awt.event.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import javax.media.j3d.*;
import javax.vecmath.*;

class Car extends Player {

    public double maxSpeed = 0.4;
    public double acceleration = 0.;

    public Car() {
        ObjectFile f = new ObjectFile();
        Scene s = null;

        try {
            s = f.load("models/car1.obj");

        } catch (Exception e) {
            e.printStackTrace();

        }

        Transform3D tHeadSet = new Transform3D();
        tHeadSet.set(new Vector3f(0f, 0f, 1f));

        Transform3D tHeadRot = new Transform3D();
        tHeadRot.rotZ(0);
        tHeadRot.mul(tHeadSet);

        objHeadTG = new TransformGroup(tHeadRot);
        objHeadTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objHeadTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        objHeadTG.addChild(s.getSceneGroup());
        addChild(objHeadTG);

    }

    public void accelerate(double angle) {
        //	Vector3d acc = new Vector3d(Math.cos(this.getAngle()), Math.sin(this.getAngle()), 0);
    }
    /*
    public void goForward() {
    this.vel = new Vector3d(this.VELOCITY*Math.cos(this.getAngle()), this.VELOCITY*Math.sin(this.getAngle()), 0);
    }

    public void goRight() {
    this.vel = new Vector3d(this.VELOCITY*Math.cos(this.getAngle()-Math.PI/2.0), this.VELOCITY*Math.sin(this.getAngle()-Math.PI/2.0), 0);
    }

    public void goLeft() {
    this.vel = new Vector3d(this.VELOCITY*Math.cos(this.getAngle()+Math.PI/2.0), this.VELOCITY*Math.sin(this.getAngle()+Math.PI/2.0), 0);
    }

    public void goBack() {
    this.vel = new Vector3d(this.VELOCITY*Math.cos(this.getAngle()+Math.PI), this.VELOCITY*Math.sin(this.getAngle()+Math.PI), 0);
    }

    public void turnRight () {
    this.angle-=0.1;
    }

    public void turnLeft () {
    this.angle+=0.1;
    }
     */

    public void mouseMoved(MouseEvent e) {
//		this.mecAlfa=(e.getX()-160)/100;
//		this.mecBeta=(e.getY()-120)/100;

        /*		this.angle+=-(double)(e.getX()-lastx)/45.0;
        this.elevation+=-(double)(e.getY()-lasty)/45.0;
         */
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
    /*
    public void keyPressed (KeyEvent e) {
    switch (e.getKeyCode()) {
    case KeyEvent.VK_W:
    goForward();
    //			System.out.print("step forward");
    break;

    case KeyEvent.VK_S:
    goBack();
    //			System.out.print("step backward");
    break;

    case KeyEvent.VK_A:
    turnLeft();
    //			System.out.print("step left");
    break;

    case KeyEvent.VK_D:
    turnRight();
    //			System.out.print("step right");
    break;

    case KeyEvent.VK_R:
    performRightAttack();

    break;

    case KeyEvent.VK_F:
    shoot();

    break;

    case KeyEvent.VK_B:
    System.out.println("Players bounding object: " + this.getBounds());

    break;

    case KeyEvent.VK_V:
    this.changeViewMode();

    break;
    }
    }
     */
    /*	public void keyActions () {
    if (pressedKeys[KeyEvent.VK_W]) goForward();
    if (pressedKeys[KeyEvent.VK_S]) goBack();
    //		if (pressedKeys[KeyEvent.VK_A]) goLeft();
    //		if (pressedKeys[KeyEvent.VK_D]) goRight();
    if (pressedKeys[KeyEvent.VK_Q]) turnLeft();
    if (pressedKeys[KeyEvent.VK_E]) turnRight();
    if (pressedKeys[KeyEvent.VK_A]) turnRight();
    if (pressedKeys[KeyEvent.VK_D]) turnLeft();
    }
     */

    public void keyReleased(KeyEvent e) {
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
    }

//  abstract methods from entity
    public String toString() {
        return "Player " + this.hitpoints + "HP";
    }
}
	