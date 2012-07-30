/*
 * Copyright (c) 2004, William Denniss. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above 
 *   copyright notice, this list of conditions and the following 
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 *
 * Neither the name of William Denniss nor the names of
 * contributors may be used to endorse or promote products derived
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
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
 * RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE
 *
 */
package org.odejava.xith3d.test;

import java.awt.event.*;
import javax.vecmath.*;

import org.odejava.test.Demonstration;
import org.odejava.xith3d.*;
import org.odejava.display.*;

import com.xith3d.scenegraph.*;
import com.xith3d.render.*;
import com.xith3d.render.jogl.*;

/** 
 * Bootstrap class - can run any "org.odejava.test.Demonstration" compatable demo.
 *
 * @author William Denniss
 */
public class RunDemo {
	
	
	private DisplayBin boundObjects = new DisplayBin();
	
	public RunDemo(Demonstration demo) throws Exception {

	
		/*
		 * Xith3D scenegraph setup.
		 * Creates a Universe (the root scenegraph node), a Locale
		 * and a View and BranchGroup.
		 */
		VirtualUniverse universe = new VirtualUniverse();
		Locale locale = new Locale();
		View view = new View();
		BranchGroup geomRoot = new BranchGroup();
		
		// configures the universe and locale
		universe.addLocale(locale);
		universe.addView(view);
		locale.addBranchGraph(geomRoot);
				
		/*
		 * Xith3D canvas setup
		 * Creates the window and renderer used to display the
		 * Xith3D scene.
		 */
		RenderPeer rp = new RenderPeerImpl();
		
		// Creates a 640x480, 32bit colour canvas
		CanvasPeer cp = rp.makeCanvas(null,640,480,32,false);
		Canvas3D canvas = new Canvas3D();
		canvas.set3DPeer(cp);
		canvas.setView(new View());
		view.addCanvas3D(canvas);
		
		// Sets the title
		cp.setTitle("Basic Xith3D Example");
		
		// Adds a key listener to exit the app when escape is pressed
		cp.getComponent().addKeyListener(new KeyAdapter () {
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					
					case KeyEvent.VK_ESCAPE:
						System.exit(0);
						break;
				}
			}
			
		});
		
		

		OdejavaToXith3D odeConverter = new OdejavaToXith3D(boundObjects);
		
		// creates the bound transform groups
		odeConverter.createTransformGroups(demo.getGeoms());
		
		// adds all bound TransformGroups from the DisplayBin into the root BranchGroup
		Xith3DDisplayObject.addToScene(boundObjects, geomRoot);

		
		
		
		DirectionalLight lighting = new DirectionalLight(true,new Color3f(.6f,.6f,.6f),new Vector3f(0,0,-15));
		//PointLight lighting = new PointLight(new Color3f(1,1,1), new Point3f(4,4,4), new Point3f(1,1,1));
		geomRoot.addChild(lighting);
		
		view.getTransform().lookAt(
			new Vector3f(10f, 2, 2f), // location of eye
			new Vector3f(0, 0, 0), //center of view
			new Vector3f( 0, 1, 0)); //vector pointing up
		
		view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
		
		float rotate = 0f;

		for (;;) {
			
			
			demo.step();
			
			//System.out.println(.getPosition());
			
			boundObjects.updateAll();
			
			// Renders the scene
			view.renderOnce();
			
			
			// Sleep for a bit
			try {
				Thread.sleep(50);
			} catch (Exception e) {}
			
			
		}
	}
	
	public final static void main(String [] args) throws Exception {
		
		new RunDemo((Demonstration) ClassLoader.getSystemClassLoader().loadClass(args[0]).newInstance());
		
		
	}
}
