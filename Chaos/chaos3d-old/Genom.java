package cz.kotuc.chaos;

public class Genom {
	
	public final static int BRANCH = 0;
	public final static int LEAF = 1;
	public final static int SEED = 2;
	
		public final static int GROW_DIRECTION_ANGLE = 0;
		public final static int GROW_ELEVATION_ANGLE = 1;
		public final static int GROW_LENGTH          = 2;
		public final static int GROW_CHILD_COUNT     = 3;
		public final static int GROW_BRANCH_ELEVATION_ANGLE = 4;
		public final static int GROW_PEAK            = 5;
		
	public final static int PARAMS = 3;
		/**
		 * Next branch_length will be in GROW_RATIO : 100
		 */ 
		public final static int GROW_RATIO         = 0;
		public final static int GROW_LEAVES_DEPTH  = 1;
		public final static int GROW_RATIO_UPDEPTH = 2;
	
	
	
	private byte[][] genom;
	/**
	 * Method Genom
	 *
	 *
	 */
	public Genom() {
		genom = new byte[4][];
		
		
		
		
	
		
		genom[BRANCH] = new byte[6];
		genom[BRANCH][0] = 10;
		genom[BRANCH][1] = 0;
		genom[BRANCH][2] = 10;
		genom[BRANCH][3] = 1;
		genom[BRANCH][4] = -20;
		genom[BRANCH][5] = 0;
		genom[LEAF] = new byte[4];
		genom[LEAF][0] = 0;
		genom[LEAF][1] = 68;
		genom[LEAF][2] = 50;
		genom[LEAF][3] = 1;
		genom[SEED] = new byte[4];
		genom[SEED][0] = 0;
		genom[SEED][1] = 66;
		genom[SEED][2] = 10;
		genom[SEED][3] = 2;
		genom[PARAMS] = new byte[3];
		genom[PARAMS][GROW_RATIO]        = 0;
		genom[PARAMS][GROW_LEAVES_DEPTH] = 1;
		genom[PARAMS][GROW_RATIO_UPDEPTH] = 1;
		
	//	this.shuffle();
	}
	
	public Genom(Genom g) {
		//genom = (byte[][])g.genom.clone();
		genom = new byte[g.genom.length][]; 
		for (int i = 0; i < g.genom.length; i++) {
			genom[i] = new byte[g.genom[i].length];
			for (int j = 0; j < g.genom[i].length; j++) {
				
				this.genom[i][j] = g.genom[i][j];
				if (Math.random() < 0.1) {
					this.genom[i][j]++;
				};
				if (Math.random() < 0.1) {
					this.genom[i][j]--;
				};
			};
		};
	}
	
	public double getGrowDirectionAngle (int type) {
		return Math.toRadians(this.genom[type][GROW_DIRECTION_ANGLE] * 360.0 / 256.0);
	}
	
	public double getGrowElevationAngle (int type) {
		return Math.toRadians(this.genom[type][GROW_ELEVATION_ANGLE] * 360.0 / 256.0);
	}
	
	public int getGrowLength (int type) {
		return this.genom[type][GROW_LENGTH] % 120; 
	}
	
	public int getChildCount (int type) {
		return this.genom[type][GROW_CHILD_COUNT] % 4;
		
	}
	
	public double getGrowRatio (int type) {
		if (type == BRANCH) return this.genom[PARAMS][GROW_RATIO] / 100.0;
		else return 1.0;
	}
		
	public int getGrowLeavesDepth () {
		return this.genom[PARAMS][GROW_LEAVES_DEPTH] % 120;
		
	}	
	
	public int getGrowRatioUpdepth () {
		return this.genom[PARAMS][GROW_RATIO_UPDEPTH] % 120;
		
	}
	
	public double getGrowBranchElevationAngle() {
		return this.genom[BRANCH][GROW_BRANCH_ELEVATION_ANGLE] * 360.0 / 256.0;
	}
	
	public boolean getGrowPeak () {
		if (this.genom[BRANCH][GROW_PEAK] % 2 == 1) return true;
		else return false;
		
	}
	
	public void shuffle() {
		for (int i = 0; i < genom.length; i++) {
			for (int j = 0; j < genom[i].length; j++) {
				genom[i][j] = (byte) (Math.random() * 256.0 - 128.0);
			}
		}
	}
}
