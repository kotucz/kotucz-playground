package kotuc;

import java.awt.Graphics;


public class Unit {
//	Vertex[] verts;
//	static public Unit[] unitList;
	public Polygon[] pols;
	String name;
	
	public Unit () {
		pols = new Polygon[0];
	}
	
	public Unit (int length) {
		pols = new Polygon[length];
	}
	
	public Unit (String name) {
		super();
		this.name=name;
		pols = new Polygon[0];
	}
	
	public Unit (Polygon[] pols) {
		super();
		this.pols = pols;
	}
	
	public Unit (String name, Polygon[] pols) {
		super();
		this.name=name;
		this.pols = pols;
	}
	
	public String getName () {
		return new String(this.name);
	}
	
	public void swap(int i, int j) {
		Polygon p=this.pols[i];
		this.pols[i]=this.pols[j];
		this.pols[j]=p;
	}
	/** use only when adding a few
	 *  too slow for more poly
	 *	better adds them to new Unit(amount of pols)
	 *	and connect this unit
	 */
	
	public void addPoly(Polygon p) {
	// DEPRECATED
		if (p!=null) {
			Polygon[] npols = new Polygon[this.pols.length+1];// + 1 new Poly
			for (int i=0; i<this.pols.length; i++) {
				npols[i]=this.pols[i];	
			};
			npols[npols.length-1]=p;// the last new element
			this.pols=npols;
		}
		this.QuickSort(0, this.pols.length-1);
	}
	
	public void addUnit(Unit u) {
		if (u!=null) {
			Polygon [] npols = new Polygon[this.pols.length+u.pols.length];
			for (int i=0; i<this.pols.length; i++) {
				npols[i]=this.pols[i];	
			};
			for(int i=0; i<u.pols.length; i++) {
				npols[i+this.pols.length]=u.pols[i]; 
			}
			this.pols=npols;
		}
		this.QuickSort(0, this.pols.length-1);	
	}
	/**
	*	adds units root (vertex(0,0,0)) to place specified by the vertex
	*	and moves all polygons with it
	*
	*/
	public Unit moveTo(int x, int y, int z) {
		return this.moveTo(new Vertex(x, y, z));
	}
	
	public Unit moveTo(Vertex w) {
		Unit u = new Unit(this.pols.length);
		for (int i=0; i<this.pols.length; i++) {
			if (this.pols[i]!=null) {
				Vertex[] verts = new Vertex[this.pols[i].vertices.length];
				for (int j=0; j<this.pols[i].vertices.length; j++) {
					verts[j]=this.pols[i].vertices[j].moveTo(w);
				}
				u.pols[i] = new Polygon(this.pols[i].color, verts);
			};
		}
//		u.QuickSort(0, this.pols.length-1);
		return u;	
	}
	
	public Unit resize(double perc) {
		Unit u = new Unit(this.pols.length);

		for (int i=0; i<this.pols.length; i++) {
			if (this.pols[i]!=null) {
				Vertex[] verts = new Vertex[this.pols[i].vertices.length];
				for (int j=0; j<this.pols[i].vertices.length; j++) {
					verts[j]=this.pols[i].vertices[j].resize(perc);
				}
				u.pols[i] = new Polygon(this.pols[i].color, verts);
			};
		}
//		u.QuickSort(0, this.pols.length-1);
		return u;	
	}
	
	public Unit transformTo(Unit to, double perc) {
		Unit u = new Unit(this.pols.length);
		for (int i=0; i<this.pols.length; i++) {
			if (this.pols[i]!=null) {
				Vertex[] verts = new Vertex[this.pols[i].vertices.length];
				for (int j=0; j<this.pols[i].vertices.length; j++) {
					verts[j]=this.pols[i].vertices[j].moving(to.pols[i].vertices[j], perc);
				}
				u.pols[i] = new Polygon(this.pols[i].color, verts);
			};
		}
		u.QuickSort(0, this.pols.length-1);
		return u;	
	}

	public void orderPols() {
		//DEPRECATED - too slow
		
		for (int i=0; i<this.pols.length-1; i++) {
			if (this.pols[i]!=null)
			for (int j=i+1; j<this.pols.length; j++) {
				if (this.pols[j]!=null)
				 if (this.pols[i].getAvZ()<this.pols[j].getAvZ()) {
					swap(i, j);
				}
			}						
		}
	}
	
	public void QuickSort(int lo0, int hi0) {
      int lo = lo0;
      int hi = hi0;
      int mid;

      if ( hi0 > lo0)
      {
         mid = this.pols[ ( lo0 + hi0 ) / 2 ].getAvZ();

         while( lo <= hi )
         {
         	
	     while( ( lo < hi0 ) && ( this.pols[lo].getAvZ() < mid ))
		 ++lo;

         while( ( hi > lo0 ) && ( this.pols[hi].getAvZ() > mid ))
		 --hi;

           if( lo <= hi )
            {
               this.swap(lo, hi);
               ++lo;
               --hi;
            }
         }

         if( lo0 < hi )
            QuickSort(lo0, hi );

          if( lo < hi0 )
            QuickSort(lo, hi0 );
		}
	}
	
	public void paint(Graphics g){
		//this.orderPols();
		//this.QuickSort(0, this.pols.length-1);
		for (int i=0; i<this.pols.length; i++) {
			if (this.pols[i]!=null) this.pols[i].paint(g);
		}
	}
	
	public Unit rotX(double angle) {
		Unit u = new Unit(this.pols.length);
		for (int i = 0; i<this.pols.length; i++) {
			if (this.pols[i]!=null) u.pols[i]=this.pols[i].rotX(angle);
		}
		this.QuickSort(0, this.pols.length-1);
		return u;
	}
	
	public Unit rotY(double angle) {
		Unit u = new Unit(this.pols.length);
		for (int i = 0; i<this.pols.length; i++) {
			if (this.pols[i]!=null) u.pols[i]=this.pols[i].rotY(angle);
		}
		this.QuickSort(0, this.pols.length-1);
		return u;
	}
	
	public Unit rotZ(double angle) {
		Unit u = new Unit(this.pols.length);
		for (int i = 0; i<this.pols.length; i++) {
			if (this.pols[i]!=null) u.pols[i]=this.pols[i].rotZ(angle);
		}
		this.QuickSort(0, this.pols.length-1);
		return u;
	}
}
