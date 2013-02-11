package kotuc;

import java.awt.Color;
import java.awt.Graphics;

public class Polygon {
	
	public Vertex[] vertices;
	public Color color = java.awt.Color.WHITE;	
		
	public Polygon (Color color, Vertex v0, Vertex v1, Vertex v2, Vertex v3) {
		super();
		
		this.color=color;
		
		this.vertices = new Vertex[4];
		this.vertices[0] = v0; 
        this.vertices[1] = v1;
		this.vertices[2] = v2;
		this.vertices[3] = v3;
	} 
	
	public Polygon (Color color, Vertex v0, Vertex v1, Vertex v2) {
		super();
		
		this.color=color;
		
		this.vertices = new Vertex[3];
		this.vertices[0] = v0; 
        this.vertices[1] = v1;
		this.vertices[2] = v2;
	}
	
	public Polygon (Color color, Vertex[] verts) {
		super();
		
		this.color=color;
		
		this.vertices = new Vertex[verts.length];
		for (int i = 0; i<verts.length; i++) {
			this.vertices[i]=verts[i].copy();
		}
	}
	
	public Polygon copy() {
		Vertex[] verts = new Vertex[this.vertices.length];
		for (int i = 0; i<this.vertices.length; i++) {
			verts[i] = this.vertices[i].copy();
		};
		
		return new Polygon (
			this.color,
			verts		
		);
	}
	
	public int getAvZ () {
		int z=0;
		for (int i=0; i<this.vertices.length; i++) {
			z+=vertices[i].z;
		};
		return z/this.vertices.length;
	}
	
	/**
	 * Method paint
	 *
	 *
	 */
	public void paint(Graphics g) {
		int [] xVerts;//= {0, 100, 100};
		int [] yVerts;// = {0,   0, 100};
		
		xVerts = new int[this.vertices.length];
		yVerts = new int[this.vertices.length];
		
		for (int i=0; i<this.vertices.length; i++) {
			try {
				xVerts[i]=vertices[i].getX2d();
				yVerts[i]=vertices[i].getY2d();
			} catch (NotInViewException nive) {
				//one or more of polygon vertices is behind camera so polygon could not be displayed
				return;
			}
			//g.drawString(i, vertices[i].getX2d(), vertices[i].getY2d());
		};
		
// transparent polygons - do not use - too slow 		
	//	g.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 55));
		g.setColor(this.color);
		g.fillPolygon(xVerts, yVerts, this.vertices.length);	
//
	//		g.setColor(Color.white);
	//	g.drawPolygon(xVerts, yVerts, this.vertices.length);	
	}	
	
	public Polygon rotX(double angle) { 
		Vertex[] verts = new Vertex[this.vertices.length];
		for (int i = 0; i<this.vertices.length; i++) {
			verts[i] = this.vertices[i].rotX(angle);
		};
		
		return new Polygon (
			this.color,
			verts		
		);
	}
	
	public Polygon rotY(double angle) { 
		Vertex[] verts = new Vertex[this.vertices.length];
		for (int i = 0; i<this.vertices.length; i++) {
			verts[i] = this.vertices[i].rotY(angle);
		};
		
		return new Polygon (
			this.color,
			verts		
		);
	}
	
	public Polygon rotZ(double angle) { 
	Vertex[] verts = new Vertex[this.vertices.length];
		for (int i = 0; i<this.vertices.length; i++) {
			verts[i] = this.vertices[i].rotZ(angle);
		};
		
		return new Polygon (
			this.color,
			verts		
		);
	}
}
