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
import com.xith3d.test.TestUtils;

// Use Jogl
import com.xith3d.loaders.texture.TextureLoader;
import com.xith3d.render.*;
import com.xith3d.render.jogl.*;

import org.odejava.xith3d.OdejavaToXith3D;
import org.odejava.xith3d.Xith3DDisplayObject;

// Import ode example world
import org.odejava.test.simple.TriMesh;

import org.odejava.display.*;

/**
 * Simple trimesh, four spheres and a box simulation. Contains bindings from
 * Odejava into Xith3D. Space restarts the simulation, esc ends the
 * application.
 * 
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 *  
 */
public class TriMeshExample {
	// Odejava world
	TriMesh odeTriMesh;

	// xith
	View view;
	Locale locale;
	BranchGroup scene;

	// Odejava to xith helper class
	OdejavaToXith3D odejavaToXith;
	// Each TransformGroup contains xith shapes, these are constantly updated
	// by Odejava. TransformGroup's userData contains object Odejava.body
	DisplayBin boundObjects = new DisplayBin();

	// View
	Vector3f viewLocation = new Vector3f(30f, 40f, 30f);
	Vector3f viewCenter = new Vector3f(4f, 4f, 5f);

	// Appearances
	Appearance groundApp, pyramidApp;

	private boolean exit = false;
	private boolean renderingAllowed = true;
	private boolean rendering = false;

	public static void main(String[] args) {
		TriMeshExample app = null;
		try {
			app = new TriMeshExample();
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

	public TriMeshExample() {
		
		odejavaToXith = new OdejavaToXith3D(boundObjects);
		
		// Initialize xith universe, view, locale, scene, canvas and keyboard
		// listener.
		initXith();
		// Create Odejava world
		odeTriMesh = new TriMesh();
		// Create xith lights
		initLights();
		// Create xith ground
		initGround();
		// Create xith objects based on Odejava
		initXithFromOdejava();
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
			AWTEvent.KEY_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);

		// Set view
		view.addCanvas3D(canvas);
		// location of eye, center of view, vector pointing up
		view.getTransform().lookAt(
			viewLocation,
			viewCenter,
			new Vector3f(0, 0, 1));

		// init appearances
		initAppearances();
	}

	/**
	 * Initialize xith transformgroups with shapes. Use a helper class
	 * OdejavaToXith for creating xith objects from Odejava objects.
	 *  
	 */
	private void initXithFromOdejava() {
		// Attach own Appearance for Pyramid
		odejavaToXith.addUserAppearance("pyramid", pyramidApp);

		// Create transform groups based on given Odejava object
		odejavaToXith.createTransformGroups(odeTriMesh.getGeoms());
		
		Xith3DDisplayObject.addToScene(boundObjects, scene);
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
				odeTriMesh.step();
				// update xith objects based on Odejava bodies
				boundObjects.updateAll();
				// render view
				view.renderOnce();
				rendering = false;
			}
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
		odeTriMesh.resetSimulation();
		renderingAllowed = true;
	}

	/**
	 * Initialize some lighting to the scene.
	 *  
	 */
	private void initLights() {
		Color3f col1 = new Color3f(0.5f, 0.5f, 0.5f);
		Transform3D t = new Transform3D();
		Vector3f pos1 = new Vector3f(5.0f, 5.0f, 15.0f);
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
			if (event instanceof KeyEvent) {
				KeyEvent e = (KeyEvent) event;
				switch (e.getID()) {
					case KeyEvent.KEY_TYPED :
						keyTyped(e);
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

	/**
	 * Catch key events. Space restarts the simulation, esc ends the
	 * application.
	 * 
	 * @param e
	 */
	private void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
			case 27 :
				exit = true;
				break;
			case ' ' :
				resetOdeWorld();
				break;
		}
	}

	/**
	 * Initialize appearances
	 *  
	 */
	private void initAppearances() {
		Material mat;
		TexCoordGeneration texGen;

		// Register texture path
		TextureLoader.tf.registerPath("data/images");

		// Ground appearance
		mat = new Material();
		mat.setAmbientColor(1f, 1f, 1f);
		mat.setLightingEnable(true);
		groundApp = new Appearance();
		groundApp.setPolygonAttributes(
			new PolygonAttributes(
				PolygonAttributes.POLYGON_FILL,
				PolygonAttributes.CULL_NONE,
				0));
		groundApp.setTexture(
			(Texture2D) TextureLoader.tf.getMinMapTexture(
				"groundandtrees3.png"));
		texGen =
			new TexCoordGeneration(
				TexCoordGeneration.SPHERE_MAP,
				TexCoordGeneration.TEXTURE_COORDINATE_2);
		groundApp.setTexCoordGeneration(texGen);
		groundApp.setMaterial(mat);

		// Pyramid appearance
		mat = new Material();
		mat.setAmbientColor(1f, 1f, 1f);
		mat.setLightingEnable(true);
		pyramidApp = new Appearance();
		pyramidApp.setPolygonAttributes(
			new PolygonAttributes(
				PolygonAttributes.POLYGON_FILL,
				PolygonAttributes.CULL_NONE,
				0));
		pyramidApp.setTexture(
			(Texture2D) TextureLoader.tf.getMinMapTexture("metal.png"));
		texGen =
			new TexCoordGeneration(
				TexCoordGeneration.SPHERE_MAP,
				TexCoordGeneration.TEXTURE_COORDINATE_2);
		pyramidApp.setTexCoordGeneration(texGen);
		pyramidApp.setMaterial(mat);
	}

	/**
	 * Initialize ground manually.
	 *  
	 */
	private void initGround() {
		float k = 100f; // number of texture tiles per narrow side
		float w = 2000f; // Width of grass line, length = 128*w
		float x1 = -1000f;
		float x2 = 1000f;
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

	public void cleanup() {
		if (odeTriMesh != null)
			odeTriMesh.cleanup();
	}
}
