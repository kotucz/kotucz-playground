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
package org.odejava.java3d.test;

import javax.vecmath.*;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

import javax.swing.JFrame;

import javax.media.j3d.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.image.TextureLoader;

// Import odejava to xith binder
import org.odejava.java3d.OdejavaToJava3D;

import org.odejava.display.*;
import org.odejava.java3d.Java3DDisplayObject;

// Import ode example world
import org.odejava.test.simple.HighLevelApiExample;

/**
 * Simple box, sphere and plane (ground) simulation. Contains bindings from
 * Odejava into Xith3D. Space restarts the simulation, esc ends the
 * application.
 * 
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 * @author Java 3D port by Paul Byrne
 *  
 */
public class SimpleExample {
	// Odejava world
	HighLevelApiExample odeSimple;

	// xith
	View view;
	Locale locale;
	BranchGroup scene;

	// Odejava to xith helper class
	OdejavaToJava3D odejavaToJ3D;
	
	
	// Each TransformGroup contains xith shapes, these are constantly updated
	// by Odejava. TransformGroup's userData contains object Odejava.body
	DisplayBin boundObjects = new DisplayBin();

	// View
	Point3d viewLocation = new Point3d(5, 7, 3);
	Point3d viewCenter = new Point3d(0, 0, 0);

	// Appearances for ground, box and sphere
	Appearance groundApp, boxApp, sphereApp;
        
        SimpleUniverse universe;

	private boolean exit = false;
	private boolean renderingAllowed = true;
	private boolean rendering = false;

	public static void main(String[] args) {
		SimpleExample app = null;
		try {
			app = new SimpleExample();
			// Start stepping simulation ahead and render it constantly
			//app.render();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public SimpleExample() {
                GraphicsConfiguration config =
                           SimpleUniverse.getPreferredConfiguration();

                Canvas3D c3d = new Canvas3D( config );
                JFrame f = new JFrame();
                f.setSize(1024, 768);
                f.setLayout(new BorderLayout());
                f.add("Center", c3d);
                f.setVisible(true);
                
                f.addWindowListener( new java.awt.event.WindowAdapter() {
                    public void windowClsoing(java.awt.event.WindowEvent we ) {
                        universe.removeAllLocales();
                        cleanup();
                        System.exit(0);
                    }                    
                });
                
                universe = new SimpleUniverse(c3d);

		// create scene
		scene = new BranchGroup();
                scene.setCapability( BranchGroup.ALLOW_CHILDREN_EXTEND );
                scene.setCapability( BranchGroup.ALLOW_CHILDREN_WRITE );
                scene.setCapability( BranchGroup.ALLOW_CHILDREN_READ );
                
                scene.addChild( new PhyscicsUpdate() );
                
                //scene.addChild( new com.sun.j3d.utils.geometry.ColorCube(0.4));

                // Create Odejava world
		odeSimple = new HighLevelApiExample();
		// Create xith lights
		initLights();
		// Create xith ground
		initGround();
		// Create xith objects based on Odejava
		initXithFromOdejava();
                
		// Start stepping simulation ahead and render it constantly
		//render();
                
		initAppearances();
		// create keyboard event listener
		Toolkit.getDefaultToolkit().addAWTEventListener(
			new EventListener(),
			AWTEvent.KEY_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);
                
		// location of eye, center of view, vector pointing up
                Transform3D t3d = new Transform3D();
                t3d.lookAt(
			viewLocation,
			viewCenter,
			new Vector3d(0, 0, 1));
                t3d.invert();
		universe.getViewingPlatform().getViewPlatformTransform().setTransform(t3d);
                
		universe.addBranchGraph(scene);
	}

	private void initXith() {
		// create the virtual universe


                




		// init appearances
	}

	/**
	 * Initialize xith transformgroups with shapes. Use a helper class
	 * OdejavaToXith for creating xith objects from Odejava objects.
	 *  
	 */
	private void initXithFromOdejava() {
		// Set user defined Shapes
		Shape3D shape;

		odejavaToJ3D = new OdejavaToJava3D(boundObjects);
		
		// Create transform groups based on given Odejava object
		odejavaToJ3D.createTransformGroups(odeSimple.getGeoms());
		
		Java3DDisplayObject.addToScene(boundObjects, scene);
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
		odeSimple.resetSimulation();
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

	/**
	 * Initialize appearances currently used only by ground.
	 *  
	 */
	private void initAppearances() {
		// Register texture path
		//TextureLoader.tf.registerPath("data/images");
            TextureLoader tl = new TextureLoader( "../data/images/groundandtrees3.png", universe.getCanvas() );

		// Ground appearance
		groundApp = new Appearance();
		groundApp.setPolygonAttributes(
			new PolygonAttributes(
				PolygonAttributes.POLYGON_FILL,
				PolygonAttributes.CULL_NONE,
				0));
                groundApp.setMaterial(
                        new Material());
		Texture texture = tl.getTexture();
//			(Texture2D) TextureLoader.tf.loadTexture(
//				"groundandtrees3.png",
//				"RGB",
//				true,
//				Texture.BASE_LEVEL_LINEAR,
//				Texture.MULTI_LEVEL_LINEAR_MIPMAP_LINEAR,
//				Texture.WRAP);
		groundApp.setTexture(texture);
	}

	public void cleanup() {
		if (odeSimple != null)
			odeSimple.cleanup();
	}
        
        class PhyscicsUpdate extends Behavior {
            WakeupOnElapsedFrames wakeup = new WakeupOnElapsedFrames(0);
            
            public void initialize() {
                setSchedulingBounds( new BoundingSphere( new Point3d(), Double.POSITIVE_INFINITY ));
                wakeupOn(wakeup);
            }
            
            /**
             * Render xith scene after Odejava simulation is step ahead and transform
             * groups are updated.
             *  
             */
            public void processStimulus(java.util.Enumeration e) {
                // step simulation
                odeSimple.step();
                // update xith objects based on Odejava bodies
                boundObjects.updateAll();
                wakeupOn(wakeup);
            }
        }
        
}
