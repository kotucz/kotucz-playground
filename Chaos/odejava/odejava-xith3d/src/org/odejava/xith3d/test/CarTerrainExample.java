/*
 * Open Dynamics Engine for Java (odejava) Copyright (c) 2004, Jani Laakso, All
 * rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution. Neither the name of the odejava nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.odejava.xith3d.test;

import javax.vecmath.*;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

// xith
import com.xith3d.scenegraph.*;
import com.xith3d.test.*;

// Use Jogl
import com.xith3d.loaders.texture.TextureLoader;
import com.xith3d.render.*;
import com.xith3d.render.jogl.*;

// Import odejava to xith binder
import org.odejava.xith3d.OdejavaToXith3D;
import org.odejava.xith3d.AseObjects;
import org.odejava.xith3d.Xith3DDisplayObject;
import org.odejava.Body;

// Import ode example world
import org.odejava.test.car.CarTerrain;

import org.odejava.display.*;

/**
 * Simple car physics simulation using ODE and Xith3d. This has a car with four
 * wheels using hinges with suspension. Scene contains also bridges, boxes and
 * some spheres. Contains bindings from Odejava into Xith3D.
 * 
 * Use keys a and d for steering, and w and x for acceleration or braking. b is
 * handbrake. space restarts the simulation. Camera position can be changed
 * with 1,2,3 or 4. Keys j and m apply force to car chassis.
 * 
 * Note that some of the textures are from openmind project and some are from
 * http://www.mayang.com/textures/
 * 
 * Note: tireAndRim.ASE is a bit misplaced, hence it's not aligned correctly
 * 
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 *  
 */
public class CarTerrainExample {
	// Odejava world
	CarTerrain odeCar;

	// xith
	View view;
	Locale locale;
	BranchGroup scene;

	// Odejava to xith helper class
	OdejavaToXith3D odejavaToXith;

	// Each TransformGroup contains xith shapes, these are constantly updated
	// by Odejava. TransformGroup's userData contains object Odejava.body
	DisplayBin boundObjects = new DisplayBin();

	// Store car chassis body locally because it is frequently accessed
	Body chassisBody;

	// View
	private int camera = 2;
	Vector3f viewLocation = new Vector3f(60f, -150f, 30f);
	Vector3f viewCenter = new Vector3f(0f, 0f, 20f);

	// Appearances for ground, bridges, boxes, car chassis and wheels
	Appearance groundApp, bridgeApp, boxApp, chassisApp, wheelApp;
	AseObjects aseObjects;
	TransformGroup[] wheelTransformGroup = new TransformGroup[4];

	private boolean exit = false;
	private boolean renderingAllowed = true;
	private boolean rendering = false;

	public static void main(String[] args) {
		CarTerrainExample app = null;
		try {
			app = new CarTerrainExample();
			// Start stepping simulation ahead and render it constantly
			app.render();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Cleanup Odejava
			if (app != null)
				app.cleanup();
		}
		System.exit(1);
	}

	public CarTerrainExample() {
		
		odejavaToXith = new OdejavaToXith3D(boundObjects);

		
		// Initialize xith universe, view, locale, scene, canvas and keyboard
		// listener.
		initXith();
		// Create Odejava world
		odeCar = new CarTerrain();
		// Create xith lights
		initLights();
		// Create xith ground (plane)
		//initGround();
		// Create terrain (trimesh)
		initTerrain();

		// Create xith objects based on Odejava
		initXithFromOdejava();
	}

	/**
	 * Create TriMesh terrain out from ASE file.
	 *  
	 */
	private void initTerrain() {
		AseObjects ao = new AseObjects("data/scene/terrain.ASE");
		Shape3D terrainShape = (Shape3D) ao.getObject("terrain");
		scene.addChild(terrainShape);

		// Create Odejava terrain
		TriangleArray ta = (TriangleArray) terrainShape.getGeometry();
		float[] vertices = new float[ta.getVertexCount() * 3];
		int[] index = new int[ta.getVertexCount()];
		Point3f pos = new Point3f();
		for (int i = 0; i < ta.getVertexCount(); i++) {
			ta.getVertex(i, pos);
			vertices[i * 3 + 0] = pos.x;
			vertices[i * 3 + 1] = pos.y;
			vertices[i * 3 + 2] = pos.z;
			index[i] = i;
		}
		odeCar.createTriMesh("terrain", vertices, index);
	}

	private void initXith() {
		// create the virtual universe
		VirtualUniverse universe = new VirtualUniverse();

		// add view to the universe
		view = new View();
		universe.addView(view);

		// add locale
		locale = new Locale();
		universe.addLocale(locale);

		// create scene
		scene = new BranchGroup();
		locale.addBranchGraph(scene);

		// create canvas for our graphics
		RenderPeer rp = new RenderPeerImpl();
		CanvasPeer cp = rp.makeCanvas(null, 1024, 768, 32, false);
		Canvas3D canvas = new Canvas3D();
		canvas.set3DPeer(cp);

		// create keyboard event listener
		Toolkit.getDefaultToolkit().addAWTEventListener(
			new EventListener(),
			AWTEvent.KEY_EVENT_MASK
				| AWTEvent.MOUSE_EVENT_MASK
				| AWTEvent.MOUSE_MOTION_EVENT_MASK
				| AWTEvent.WINDOW_EVENT_MASK);

		// Set view
		view.addCanvas3D(canvas);
		// location of eye, center of view, vector pointing up
		view.getTransform().lookAt(
			viewLocation,
			viewCenter,
			new Vector3f(0, 0, 1));

		// init appearances
		initAppearances();

		// init shapes
		initWheelTransformGroups();
	}

	/**
	 * Initialize xith transformgroups with shapes. Use a helper class
	 * OdejavaToXith for creating xith objects from Odejava objects. Set
	 * appearances for chassis, bridge and box objects. Set own transformgroup
	 * (tire and rim shape) for wheel objects.
	 *  
	 */
	private void initXithFromOdejava() {
		// Set user defined appearances for chassis body and bridge geoms
		odejavaToXith.addUserAppearance("chassis", chassisApp);
		odejavaToXith.addUserAppearance("frontBumper", chassisApp);
		odejavaToXith.addUserAppearance("bridge", bridgeApp);

		// Set user defined appearances for box bodies
		for (int i = 0; i < odeCar.getBoxCount(); i++)
			odejavaToXith.addUserAppearance("box" + i, boxApp);

		// Set user defined shapes for wheel bodies
		odejavaToXith.addUserTransformGroup(
			"wheelFrontLeft",
			wheelTransformGroup[0]);
		odejavaToXith.addUserTransformGroup(
			"wheelFrontRight",
			wheelTransformGroup[1]);
		odejavaToXith.addUserTransformGroup(
			"wheelBackLeft",
			wheelTransformGroup[2]);
		odejavaToXith.addUserTransformGroup(
			"wheelBackRight",
			wheelTransformGroup[3]);

		// Create transform groups based on given Odejava object
		odejavaToXith.createTransformGroups(odeCar.getGeoms());
		
		Xith3DDisplayObject.addToScene(boundObjects, scene);
		
		// Store car chassis object locally as it's accessed frequently
		chassisBody = odeCar.getBody("chassis");
	}

	/**
	 * Render xith scene after Odejava simulation is step ahead and transform
	 * groups are updated.
	 *  
	 */
	private void render() {
		// main rendering loop
		while (!exit) {
			if (renderingAllowed) {
				rendering = true;
				// step simulation
				odeCar.step();
				// update xith objects based on Odejava bodies
				boundObjects.updateAll();
				// Update camera position and orientation
				handleCamera();
				// render view
				view.renderOnce();
				rendering = false;
			}
		}
	}

	private void handleCamera() {
		if (camera == 1) {
			// Camera in the car
			viewCenter = chassisBody.getPosition();
			viewLocation = chassisBody.getPosition();
			viewLocation.x -= 0;
			viewLocation.y -= 0;
			viewLocation.z += 3f;
			viewCenter.x += 0;
			viewCenter.y += 1;
			viewCenter.z += 3f;
			view.getTransform().lookAt(
				viewLocation,
				viewCenter,
				new Vector3f(0, 0, 1));
			Matrix3f m = chassisBody.getRotation();
			Matrix3f m2 = new Matrix3f();
			m2.rotX((float) Math.PI / 2);
			m.mul(m2);
			view.getTransform().setRotation(m);
		} else if (camera == 2) {
			// Chase Camera
			viewCenter = chassisBody.getPosition();
			viewLocation = chassisBody.getPosition();
			viewLocation.x -= 100;
			viewLocation.y -= 100f;
			viewLocation.z += 50f;
			viewCenter.x += 0;
			viewCenter.y += 1f;
			viewCenter.z += 15f;
			view.getTransform().lookAt(
				viewLocation,
				viewCenter,
				new Vector3f(0, 0, 1));
			Vector3f chaseV = new Vector3f(0, 10f, 50f);
			Transform3D chaseT3D = new Transform3D();
			chaseT3D.set(chaseV);
			view.getTransform().mul(chaseT3D);
		} else if (camera == 3) {
			// Chase Camera
			viewCenter = chassisBody.getPosition();
			viewLocation = chassisBody.getPosition();
			viewLocation.x -= 0;
			viewLocation.y -= 50f;
			viewLocation.z += 15f;
			viewCenter.x += 0;
			viewCenter.y += 1f;
			viewCenter.z += 5f;
			view.getTransform().lookAt(
				viewLocation,
				viewCenter,
				new Vector3f(0, 0, 1));
			Vector3f chaseV = new Vector3f(0, 10f, 50f);
			Transform3D chaseT3D = new Transform3D();
			chaseT3D.set(chaseV);
			view.getTransform().mul(chaseT3D);
		} else if (camera == 4) {
			// Chase Camera
			viewCenter = chassisBody.getPosition();
			viewLocation = chassisBody.getPosition();
			viewLocation.x -= 0;
			viewLocation.y -= 0f;
			viewLocation.z += 3f;
			viewCenter.x += 0;
			viewCenter.y += 1f;
			viewCenter.z += 3f;
			view.getTransform().lookAt(
				viewLocation,
				viewCenter,
				new Vector3f(0, 0, 1));
			Matrix3f m = chassisBody.getRotation();
			Matrix3f m2 = new Matrix3f();
			m2.rotX((float) Math.PI / 2);
			m.mul(m2);
			view.getTransform().setRotation(m);
			Vector3f chaseV = new Vector3f(0, 10f, 50f);
			Transform3D chaseT3D = new Transform3D();
			chaseT3D.set(chaseV);
			view.getTransform().mul(chaseT3D);
		}
	}

	/**
	 * Reset Odejava simulation.
	 * 
	 * @param simulation
	 */
	private void resetOdeWorld() {
		System.out.println("Resetting Odejava world.");
		renderingAllowed = false;
		while (rendering) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
			}
		}
		// Rendering has stopped, reset Odejava simulation
		odeCar.setSimulation();
		renderingAllowed = true;
	}

	/**
	 * Initialize some lighting to the scene.
	 *  
	 */
	private void initLights() {
		Color3f col1 = new Color3f(0.5f, 0.5f, 0.5f);
		Transform3D t = new Transform3D();
		Vector3f pos1 = new Vector3f(0.0f, 0.0f, 30.0f);
		t.set(pos1);
		Vector3f dir1 = new Vector3f(pos1);
		dir1.negate();
		dir1.normalize();
		DirectionalLight light1 = new DirectionalLight(col1, dir1);
		scene.addChild(light1);
		light1.setEnable(true);
	}

	/**
	 * Mouse and keyboard event listener
	 *  
	 */
	class EventListener implements AWTEventListener {
		public void eventDispatched(AWTEvent event) {
			if (event instanceof MouseEvent) {
				MouseEvent e = (MouseEvent) event;
				switch (e.getID()) {
					case MouseEvent.MOUSE_PRESSED :
						//mousePressed(e);
						break;
					case MouseEvent.MOUSE_RELEASED :
						//mouseReleased(e);
						break;
					case MouseEvent.MOUSE_DRAGGED :
						//mouseDragged(e);
						break;
				}
			} else if (event instanceof KeyEvent) {
				KeyEvent e = (KeyEvent) event;
				switch (e.getID()) {
					case KeyEvent.KEY_PRESSED :
						keyPressed(e);
						break;
					case KeyEvent.KEY_RELEASED :
						keyReleased(e);
						break;
				}
			} else if (event instanceof WindowEvent) {
				WindowEvent e = (WindowEvent) event;
				switch (e.getID()) {
					case WindowEvent.WINDOW_CLOSING :
						exit = true;
						// Allow application to execute shutdown for 5 seconds
						try {
							Thread.sleep(5000);
						} catch (Exception ex) {
						}
						break;
				}
			}
		}
	}

	private void keyPressed(KeyEvent e) {
		//System.out.println("keypress="+e.getKeyChar());
		switch (e.getKeyChar()) {
			case 27 :
				exit = true;
				break;
			case ' ' :
				resetOdeWorld();
				break;
			case '1' :
				camera = 1;
				break;
			case '2' :
				camera = 2;
				break;
			case '3' :
				camera = 3;
				break;
			case '4' :
				camera = 4;
				break;
			case 'a' :
				odeCar.steer(-0.02f);
				break;
			case 'd' :
				odeCar.steer(0.02f);
				break;
			case 'w' :
				odeCar.accelerate(0.02f);
				break;
			case 's' :
				odeCar.accelerate(-0.02f);
				break;
			case 'b' :
				odeCar.setHandbrake(true);
				break;
			case 'j' :
				odeCar.applyForceToCar(100);
				break;
			case 'm' :
				odeCar.applyForceToCar(-100);
				break;
		}
	}

	private void keyReleased(KeyEvent e) {
		//	System.out.println("keyrelease="+e.getKeyChar());
		switch (e.getKeyChar()) {
			case 'a' :
				odeCar.steer(0f);
				break;
			case 'd' :
				odeCar.steer(0f);
				break;
			case 'w' :
				odeCar.accelerate(0f);
				break;
			case 's' :
				odeCar.accelerate(0f);
				break;
			case 'b' :
				odeCar.setHandbrake(false);
				break;
			case 'j' :
				odeCar.applyForceToCar(0);
				break;
			case 'm' :
				odeCar.applyForceToCar(0);
				break;
		}
	}

	/**
	 * Initialize ground manually.
	 *  
	 */
	private void initGround() {
		float k = 100f; // number of texture tiles per narrow side
		float w = 4000f; // Width of grass line, length = 128*w
		float x1 = -2000f;
		float x2 = 2000f;
		float y = 0f;
		float z1 = -64f * w;
		float z2 = 64f * w;
		float tsx = k * 1f;
		float tsy = k * 128f;
		Shape3D shape = new Shape3D();
		Geometry geom =
			TestUtils.createQuad(
				new Point3f(x1, z1, y),
				new Point3f(x1, z2, y),
				new Point3f(x2, z2, y),
				new Point3f(x2, z1, y),
				tsx,
				tsy);
		shape.setAppearance(groundApp);
		shape.setGeometry(geom);
		scene.addChild(shape);
	}

	/**
	 * Initialize appearances.
	 *  
	 */
	private void initAppearances() {
		// Register texture path
		TextureLoader.tf.registerPath("data/images");

		// Ground appearance
		groundApp = new Appearance();
		groundApp.setPolygonAttributes(
			new PolygonAttributes(
				PolygonAttributes.POLYGON_FILL,
				PolygonAttributes.CULL_NONE,
				0));
		Texture texture =
			(Texture2D) TextureLoader.tf.loadTexture(
				"groundandtrees3.png",
				"RGB",
				true,
				Texture.BASE_LEVEL_LINEAR,
				Texture.MULTI_LEVEL_LINEAR_MIPMAP_LINEAR,
				Texture.WRAP);
		groundApp.setTexture(texture);

		Texture concreteTexture =
			(Texture2D) TextureLoader.tf.getMinMapTexture("brushedMetal.png");
		Texture woodTexture =
			(Texture2D) TextureLoader.tf.getMinMapTexture("crate.png");
		Texture metalTexture =
			(Texture2D) TextureLoader.tf.getMinMapTexture("metal.png");

		bridgeApp = new Appearance();
		bridgeApp.setTexture(concreteTexture);

		boxApp = new Appearance();
		boxApp.setTexture(woodTexture);

		chassisApp = new Appearance();
		chassisApp.setTexture(metalTexture);

		Material rimMat = new Material();
		rimMat.setLightingEnable(true);
		rimMat.setColorTarget(Material.AMBIENT_AND_DIFFUSE);
		rimMat.setShininess(1f);
		wheelApp = new Appearance();
		wheelApp.setMaterial(rimMat);
		wheelApp.setTexCoordGeneration(
			new TexCoordGeneration(
				TexCoordGeneration.OBJECT_LINEAR,
				TexCoordGeneration.TEXTURE_COORDINATE_2));
	}

	/**
	 * Read tire and rim shapes and create TransformGroup.
	 *  
	 */
	private void initWheelTransformGroups() {
		// Load ASE objects
		aseObjects = new AseObjects("data/scene/tireAndRim.ASE");
		for (int i = 0; i < wheelTransformGroup.length; i++) {
			wheelTransformGroup[i] = new TransformGroup();
			Shape3D wheelShape = (Shape3D) aseObjects.getObject("Rim");
			wheelShape.setAppearance(wheelApp);
			Shape3D tireShape = (Shape3D) aseObjects.getObject("Tire");
			wheelTransformGroup[i].addChild(wheelShape);
			wheelTransformGroup[i].addChild(tireShape);
		}
	}

	public void cleanup() {
		if (odeCar != null)
			odeCar.cleanup();
	}
}
