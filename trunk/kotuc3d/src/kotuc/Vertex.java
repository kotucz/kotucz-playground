package kotuc;

public class Vertex {
	public static int focusDistance = 320;
	
	public double x = 0;
	public double y = 0;
	public double z = 0;	
	
	public Vertex (int x, int y, int z) {
		super();
		
		this.x=x;
		this.y=y;
		this.z=z;
	};
	
	public Vertex (double x, double y, double z) {
		super();
		
		this.x=x;
		this.y=y;
		this.z=z;
	};
	
	/**
	 * 
	 * w...where
	 */
	public Vertex moveTo(Vertex w) {
		return new Vertex(
				this.x+w.x, 
				this.y+w.y, 
				this.z+w.z
			);
	}
	
	public Vertex moveTo(int x, int y, int z) {
		return new Vertex(
				this.x+x, 
				this.y+y, 
				this.z+z
			);
	}
	
	public Vertex copy() {
		return new Vertex(
				this.x, 
				this.y, 
				this.z
			);
	}
	/**
	 * to .. destinate vertex
	 * perc .. percents how much is to with to-vertex
	 */  
	public Vertex moving(Vertex to, double perc) {
		if (perc>1) perc=1;
		if (perc<0) perc=0;
		if (to!=null)
		return new Vertex(
				this.x+(to.x-this.x)*perc,
				this.y+(to.y-this.y)*perc,
				this.z+(to.z-this.z)*perc
			);
		else return this.copy();
	} 
	
	public Vertex resize(double perc) {
		return new Vertex(
				this.x*perc,
				this.y*perc,
				this.z*perc
			);
	}
	/**
	 * Method getX2D
	 *
	 *
	 * @return
	 *
	 */
	public int getX2d() throws NotInViewException {
		int x2d;
		if ((this.z<0)&&(0<(x2d=(int)(160+focusDistance*(this.x)/(-this.z))))&&(x2d<320)) return x2d; 
		throw new NotInViewException();
//		return (int)(this.x);
	}

	/**
	 * Method getY2D
	 *
	 *
	 * @return
	 *
	 */
	public int getY2d() throws NotInViewException {
		int y2d;
		if ((this.z<0)&&(0<(y2d=(int)(120+focusDistance*(this.y)/(-this.z))))&&(y2d<240)) return y2d;
		throw new NotInViewException();
	}
	
	public Vertex rotX(double angle) {
		double range = Math.sqrt(this.y*this.y+this.z*this.z);
		if (range!=0) {
			double alpha = Math.acos(this.z/range);
			if (this.y<0) alpha*=(-1);
			
			return new Vertex(this.x, Math.sin(alpha+angle)*range, Math.cos(alpha+angle)*range);
		}else{
			return this.copy();
		}		
	}
	
	public Vertex rotY(double angle) {
		double range = Math.sqrt(this.x*this.x+this.z*this.z);
		if (range!=0) {
			double alpha = Math.acos(this.z/range);
			if (this.x<0) alpha*=(-1);
			
			return new Vertex(Math.sin(alpha+angle)*range, this.y, Math.cos(alpha+angle)*range);
		}else{
			return this.copy();
		}		
	}
	
	public Vertex rotZ(double angle) {
		double range = Math.sqrt(this.y*this.y+this.x*this.x);
		if (range!=0) {
			double alpha = Math.acos(this.y/range);
			if (this.x<0) alpha*=(-1);
			
			return new Vertex(Math.sin(alpha+angle)*range, Math.cos(alpha+angle)*range, this.z);
		}else{
			return this.copy();
		}		
	}	
}
