package kotuc;

import java.util.*;
import java.awt.*;

public class Location extends Unit {
	final int EDGE=100;
	
	final int width, height;
	
	Vertex[][] verts;	
	//static java.util.Random random = new java.util.Random();
	
	public Location(int width, int height) {
		super(width*height);
		this.width=width;
		this.height=height;
		
		verts = new Vertex[width+1][height+1];
		
		for (int y=0; y<this.height+1; y++) {
			for (int x=0; x<this.width+1; x++) {
				verts[x][y] = new Vertex(
						x*EDGE,
						y*EDGE,
						(int)(Math.random()*EDGE/3)					
					);
			};
		};
		
		for (int y=0; y<this.height; y++) {
			for (int x=0; x<this.width; x++) {
				this.pols[y*this.width+x]=new Polygon(
						new Color((float)(Math.random()/1.4), (float)Math.random(), (float)(Math.random()/16)),
						verts[x][y].copy(),
						verts[x+1][y].copy(),
						verts[x+1][y+1].copy(),
						verts[x][y+1].copy()	
				);
			};
		};	
	} 
		/*
	public void paint (Graphics g) {
		super.paint(g);	
	}
	*/
}
	