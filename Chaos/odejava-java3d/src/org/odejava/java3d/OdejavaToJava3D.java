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
package org.odejava.java3d;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Vector3f;

import org.odejava.Geom;
import org.odejava.GeomBox;
import org.odejava.GeomPlane;
import org.odejava.GeomSphere;
import org.odejava.GeomTransform;
import org.odejava.GeomTriMesh;
import org.odejava.Placeable;
import org.odejava.PlaceableGeom;

import org.odejava.display.*;

import javax.media.j3d.*;

/**
 * <p>
 * This class creates Java3D objects automatically based on Odejava objects.
 * </p>
 * <p>
 * User's may let this class automatically select what kind of Java3D shapes
 * are used for each Odejava Geom's, or define their own shape for each Geom.
 * It's also possible to use default shapes per Odejava object and change only
 * it's appearance or map user defined TransformGroup (composite shapes)
 * against Geom. Mapping Odejava Geom's to user defined shapes, appearances or
 * TransformGroups are done by Geom's name.
 * </p>
 * <p>
 * Usage example:
 * </p>
 * <p>1. Define your own appearances, shapes or TransformGroups (optional)
 * <li>If nothing is set, then default shapes and appearances are used.</li>
 * </p>
 * <p>2. Create Java3D objects with <code>createTransformGroups</code>
 * <li>Result's can be queried with methods <code>getBodyTransformGroup()</code>
 * and <code>getGeomTransformGroup()</code>. Note that Odejava Geom's are
 * unmovable static objects and Body objects are also Geom's but they have
 * dynamic position and orientation. Java3D application can then add resulting
 * <code>TransformGroup</code> objects into scene e.g. by using <code>addTransformGroupToScene</code>
 * conviniency method.</li>
 * </p>
 * <p>3. Modify your application's rendering process
 * <li>Update Odejava related TransformGroups by calling <code>updateBodyTGs</code>
 * because Odejava is constantly updating TransformGroup's position and
 * orientation.</li>
 * </p>
 * <p>
 * You can scale objects visually using setScale() method.
 * </p>
 * <p>
 * Complete usage example can be found from org.odejava.Java3D.test.CarExample
 * class.
 * </p>
 * 
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 * @author Java 3D port by Paul Byrne
 *  
 */
public class OdejavaToJava3D {
	// User defined TransformGroups for Odejava geometries
	// key is geom name, value is TransformGroup (composite Shape3D)
	HashMap userDefinedTransformGroups;

	// User defined shapes for Odejava geometries
	// key is geom name, value is Shape3D
	HashMap userDefinedShapes;

	// User defined appearances for Odejava geometries
	// key is geom name, value is Appearance
	// This is used only if using default shapes.
	HashMap userDefinedAppearances;

	// Appearances used by default shapes (box, sphere and trimesh)
	Appearance defaultBoxApp, defaultSphereApp, defaultTriMeshApp;

	// store of created objects
	DisplayBin boundObjects;
	
	
	// Scale objects visually on the Java3D side
	float scale = 1;

	// Temporary Transform3D object
	//Transform3D t3d = new Transform3D();


	/**
	 * Creates an Odejava to Java3D converter.
	 *
	 * @param boundObjects The DisplayBin container that all created Java3D
	 * 			Display objects will be placed.
	 */
	public OdejavaToJava3D(DisplayBin boundObjects) {
		initDefaultAppearances();
		userDefinedShapes = new HashMap();
		userDefinedAppearances = new HashMap();
		userDefinedTransformGroups = new HashMap();
		
		this.boundObjects = boundObjects;
	}
	
	
	/**
	 * Initialize appearances for default shapes.
	 */
	private void initDefaultAppearances() {
		// Box appearance
		defaultBoxApp = new Appearance();

		// Sphere appearance
		TexCoordGeneration envMap =
			new TexCoordGeneration(
				TexCoordGeneration.OBJECT_LINEAR,
				TexCoordGeneration.TEXTURE_COORDINATE_2);
		Material sphereMat = new Material();
		sphereMat.setLightingEnable(true);
		sphereMat.setColorTarget(Material.SPECULAR);
		sphereMat.setShininess(0.9f);
		defaultSphereApp = new Appearance();
		defaultSphereApp.setMaterial(sphereMat);
		defaultSphereApp.setTexCoordGeneration(envMap);

		// TriMesh appearance
		TexCoordGeneration envMap2 =
			new TexCoordGeneration(
				TexCoordGeneration.OBJECT_LINEAR,
				TexCoordGeneration.TEXTURE_COORDINATE_2);
		Material triMeshMat = new Material();
		triMeshMat.setLightingEnable(true);
		triMeshMat.setColorTarget(Material.SPECULAR);
		triMeshMat.setShininess(0.9f);
		defaultTriMeshApp = new Appearance();
		defaultTriMeshApp.setMaterial(triMeshMat);
		defaultTriMeshApp.setTexCoordGeneration(envMap2);
	}

	/**
	 * <p>
	 * Create Java3D transform group lists based on given Odejava geometry
	 * list. Call this method before starting to step forward your Odejava
	 * simulation. Results can be obtained with <code>getBodyTransformGroup()</code>
	 * and <code>getGeomTransformGroup()</code>.
	 * </p>
	 * <p>
	 * Every Odejava geometry is created as shape(s) in Java3D. You can add
	 * your own transformgroup or shape to any Geom with <code>addUserTransformGroup()</code>
	 * or <code>addUserShape()</code> methods. Otherwise default shapes are
	 * used provided by this class. Default shapes are constructed
	 * automatically based on Odejava's geometry data like sphere's radius,
	 * length of the box's sides or trimesh vertices. Note that only Box,
	 * Sphere and TriMesh shapes are supported, if you have other type of Geoms
	 * then you must use your own shape.
	 * </p>
	 * <p>
	 * Mapping Geoms to renderable Java3D objects goes following:
	 * <li>If userDefinedTransformGroup map contains Geom's name as an key,
	 * then user's transformGroup will be used for rendering the Geom.</li>
	 * <li>If userDefinedShapes map contains Geom's name as an key, then
	 * user's shape will be used for rendering the Geom.</li>
	 * <li>If no match is found, then default shapes will be used for
	 * rendering the Geom.</li>
	 * </p>
	 * <p>
	 * Geoms that have Body are dynamic, their position and orientation may
	 * change during Odejava simulation step. If Geom is attached into Body,
	 * then it will be added to this classes bodyTransformGroup list
	 * </p>
	 * <p>
	 * Geoms that have no Body are static (unmovable). If Geom is not attached
	 * into Body, then it will be added to this classes geomTransformGroup
	 * list.
	 * </p>
	 * 
	 * @param odejavaGeoms
	 *            contains list of Odejava geoms to be added into result lists.
	 * @return
	 */
	public void createTransformGroups(List odejavaGeoms) {
		TransformGroup tg;
		Shape3D shape;
		Appearance app;
		Transform3D t3d = new Transform3D();

		// Result is an transform group containing shape(s).
		// Shape(s) have been created according to Odejava's geometries.
		//geomTransformGroup = new LinkedList();
		//bodyTransformGroup = new LinkedList();

		Iterator i = odejavaGeoms.iterator();
		while (i.hasNext()) {
			app = null;
			Geom odejavaGeom = (Geom) i.next();
			
			
			
			
			
			if (userDefinedTransformGroups
				.containsKey(odejavaGeom.getName())) {
				//
				// Use user defined transformgroup (composite object)
				//
				tg =
					(TransformGroup) userDefinedTransformGroups.get(
						odejavaGeom.getName());
			} else {
				//
				// Use default transformgroup and add shape into it
				//
				tg = new TransformGroup();
				if (userDefinedShapes.containsKey(odejavaGeom.getName())) {
					//
					// Use user defined shape
					//
					shape =
						(Shape3D) userDefinedShapes.get(odejavaGeom.getName());
				} else {
					//
					// Use default shapes
					//
					if (userDefinedAppearances
						.containsKey(odejavaGeom.getName()))
						//
						// Use user defined appearance
						//
						app =
							(Appearance) userDefinedAppearances.get(
								odejavaGeom.getName());
					// Create Java3D shape based on odejavaGeom data
					Geom shapeGeom;
					if (odejavaGeom instanceof GeomTransform) {
						shapeGeom = ((GeomTransform) odejavaGeom).getEncapsulatedGeom();
					} else {
						shapeGeom = odejavaGeom;
					}
					
					shape = createDefaultShape(shapeGeom, app);
					// Ignore Geom's that could not been created automatically
					if (shape == null)
						continue;
					
				}
				
				tg.addChild(shape);
			}
			
			
			// GeomTransform is not placable
			if (!(odejavaGeom instanceof GeomTransform) && !(odejavaGeom instanceof PlaceableGeom)) {
				t3d.setIdentity();
				
				// Rotate
				t3d.set(((PlaceableGeom) odejavaGeom).getQuaternion());
				// Translate
				t3d.setTranslation(((PlaceableGeom) odejavaGeom).getPosition());
				// Scale objects visually
				if (scale != 1) {
					t3d.setScale(scale);
                                        System.out.println("TODO - OdejavaToJava3D - scaleTranslation not implemented");
					//t3d.scaleTranslation(scale);
				}
	
				// Apply transform to transform group
				tg.setTransform(t3d);
			}
			
			// Add shape to result list (dynamic) or into staticScene
			// (static)
            // (static)
            boolean staticGeom = true;
            if (odejavaGeom instanceof PlaceableGeom) {
                    if (((PlaceableGeom) odejavaGeom).getBody() != null) {
                            staticGeom = false;
                    }

            }
            if (!staticGeom) {
				// dynamic object (Geom that has Body)
				// Bodies are stored to transform group's userdata
				//tg.setUserData(odejavaGeom.getBody());
				
				Placeable linked = ((PlaceableGeom) odejavaGeom).getBody();
				
				// when GeomTransfroms are used - those are linked
				// instead of the body as they contain the offset to the body as well
				if (odejavaGeom instanceof GeomTransform) {
					linked = (PlaceableGeom) odejavaGeom;
				}
				
				boundObjects.add(new BoundDisplayObject( new Java3DDisplayObject(tg), linked));
				
				// transform group's are stored into bodyTransformGroup
				// list.
				//bodyTransformGroup.add(tg);
			} else {
				// static object (Geom, without Body)
				// Geoms are stored to transform group's userdata
				//tg.setUserData(odejavaGeom);
				// transform group's are stored into geomTransformGroup
				// list.
				//geomTransformGroup.add(tg);
				boundObjects.add(new BoundDisplayObject( new Java3DDisplayObject (tg), (PlaceableGeom) odejavaGeom));
			}
		}
	}

	/**
	 * <p>
	 * Create Java3D <code>Shape3D</code> based on given Odejava's Geom.
	 * </p>
	 * <p>
	 * Supported Geom classes:
	 * <li>GeomBox</li>
	 * <li>GeomSphere</li>
	 * <li>GeomTriMesh</li>
	 * </p>
	 * <p>
	 * For other Geom classes an warning message is printed and null is
	 * returned.
	 * </p>
	 * 
	 * @param odejavaGeom
	 * @return shape for Java3D
	 */
	public Shape3D createDefaultShape(Geom odejavaGeom, Appearance app) {
		Shape3D shape = null;
		

		if (odejavaGeom instanceof GeomBox) {
			// Odejava geometry is a box
			boolean colorBox = false;
			if (app == null) {
				app = defaultBoxApp;
				colorBox = true;
			}
			float[] lengths = ((GeomBox) odejavaGeom).getLengths();

			Geometry xithGeom =
				BoxGeometry.createBoxViaTriangles(
					0,
					0,
					0,
					lengths[0],
					lengths[1],
					lengths[2],
					colorBox, true);
			shape = new Shape3D(xithGeom, app);
		} else if (odejavaGeom instanceof GeomSphere) {
			// Odejava geometry is a sphere
			float radius = ((GeomSphere) odejavaGeom).getRadius();
			Geometry j3dGeom = SphereGeometry.createSphere(radius, 16);
			if (app == null)
				app = defaultSphereApp;
                        
                        app = new Appearance();
                        PolygonAttributes polyAttr = new PolygonAttributes();
                        polyAttr.setCullFace( PolygonAttributes.CULL_NONE );
                        app.setPolygonAttributes( polyAttr );
			shape = new Shape3D(j3dGeom, app);
		} else if (odejavaGeom instanceof GeomTriMesh) {
			// Odejava geometry is a trimesh
			float[] vertexes = ((GeomTriMesh) odejavaGeom).getVertices();
			int[] index = ((GeomTriMesh) odejavaGeom).getIndices();
			IndexedTriangleArray ita =
				new IndexedTriangleArray(
					vertexes.length / 3,
					TriangleArray.COORDINATES | TriangleArray.NORMALS,
					index.length);
			ita.setValidVertexCount(vertexes.length / 3);
			ita.setCoordinates(0, vertexes);
			ita.setValidIndexCount(index.length);
			ita.setCoordinateIndices(0, index);
                        System.out.println("TODO - OdejavaToJava3D - calculateFaceNormals not implemented");
			//ita.calculateFaceNormals();
			if (app == null)
				app = defaultTriMeshApp;
			shape = new Shape3D(ita, app);
		} else {
			System.err.println(
				"Default shape for Odejava's "
					+ odejavaGeom.getClass()
					+ " is not supported."
					+ " Use user defined shape.");
		}

		return shape;
	}

	/**
	 * @return Returns the defaultBoxApp.
	 */
	public Appearance getDefaultBoxApp() {
		return defaultBoxApp;
	}

	/**
	 * @param defaultBoxApp
	 *            The defaultBoxApp to set.
	 */
	public void setDefaultBoxApp(Appearance defaultBoxApp) {
		this.defaultBoxApp = defaultBoxApp;
	}

	/**
	 * @return Returns the defaultSphereApp.
	 */
	public Appearance getDefaultSphereApp() {
		return defaultSphereApp;
	}

	/**
	 * Attach user defined <code>Appearance</code> to all Geoms that have
	 * name specified by geomName parameter.
	 * 
	 * @param defaultSphereApp
	 *            The defaultSphereApp to set.
	 */
	public void setDefaultSphereApp(Appearance defaultSphereApp) {
		this.defaultSphereApp = defaultSphereApp;
	}

	/**
	 * User defined shapes for Odejava Geoms. Key is Geom's name, value is
	 * Shape3D.
	 * 
	 * @return Returns the userShapes.
	 */
	public HashMap getUserShapes() {
		return userDefinedShapes;
	}

	/**
	 * Attach user defined <code>Shape3D</code> to all Geoms that have name
	 * specified by geomName parameter.
	 * 
	 * @param geomName
	 * @param shape
	 */
	public void addUserShape(String geomName, Shape3D shape) {
		userDefinedShapes.put(geomName, shape);
	}

	/**
	 * Get user defined <code>Shape3D</code> for Geom.
	 * 
	 * @param geomName
	 * @param shape
	 */
	public Shape3D getUserShape(String geomName, Shape3D shape) {
		return (Shape3D) userDefinedShapes.get(geomName);
	}

	/**
	 * Set user defined <code>Shape3D</code> mapping for Geoms. Key is Geom's
	 * name, value is <code>Shape3D</code>.
	 * 
	 * @param userShapes
	 *            The userShapes to set.
	 */
	public void setUserShapes(HashMap userShapes) {
		this.userDefinedShapes = userShapes;
	}

	/**
	 * Attach user defined <code>Appearance</code> to all Geoms that have
	 * name specified by geomName parameter. Used only with default shapes.
	 * 
	 * @param geomName
	 * @param app
	 */
	public void addUserAppearance(String geomName, Appearance app) {
		userDefinedAppearances.put(geomName, app);
	}

	/**
	 * Get user defined <code>Appearance</code> for Geom. Used only with
	 * default shapes.
	 * 
	 * @param geomName
	 * @param app
	 */
	public Appearance getUserAppearance(String geomName, Appearance app) {
		return (Appearance) userDefinedAppearances.get(geomName);
	}

	/**
	 * Attach user defined <code>TransformGroup</code> (composite object) to
	 * all Geoms that have name specified by geomName parameter.
	 * 
	 * @param geomName
	 * @param tg
	 */
	public void addUserTransformGroup(String geomName, TransformGroup tg) {
		userDefinedTransformGroups.put(geomName, tg);
	}

	/**
	 * Get user defined <code>TransformGroup</code> for Geom.
	 * 
	 * @param geomName
	 */
	public TransformGroup getUserTransformGroup(String geomName) {
		return (TransformGroup) userDefinedTransformGroups.get(geomName);
	}

	/**
	 * Get user defined <code>Appearance</code> mapping for Geoms. Key is
	 * Geom's name, value is <code>Appearance</code>.
	 * 
	 * @return Returns the userDefinedAppearances map.
	 */
	public HashMap getUserDefinedAppearances() {
		return userDefinedAppearances;
	}

	/**
	 * Set user defined <code>Appearance</code> mapping for Geoms. Key is
	 * Geom's name, value is <code>Appearance</code>.
	 * 
	 * @param userDefinedAppearances
	 *            The userDefinedAppearances map to set.
	 */
	public void setUserDefinedAppearances(HashMap userDefinedAppearances) {
		this.userDefinedAppearances = userDefinedAppearances;
	}

	/**
	 * Get user defined <code>TransformGroup</code> mapping for Geoms. Key is
	 * Geom's name, value is <code>TransformGroup</code>.
	 * 
	 * @return Returns the userDefinedTransformGroups map.
	 */
	public HashMap getUserDefinedTransformGroups() {
		return userDefinedTransformGroups;
	}

	/**
	 * Set user defined <code>TransformGroup</code> mapping for Geoms. Key is
	 * Geom's name, value is <code>TransformGroup</code>.
	 * 
	 * @param userDefinedTransformGroups
	 *            The userDefinedTransformGroups map to set.
	 */
	public void setUserDefinedTransformGroups(HashMap userDefinedTransformGroups) {
		this.userDefinedTransformGroups = userDefinedTransformGroups;
	}

	/**
	 * @return Returns the scale.
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Scale objects visually on the Java3D side, default is 1 which means no
	 * scaling is done.
	 * 
	 * @param scale
	 *            The scale to set.
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

}
