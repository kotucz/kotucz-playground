package cz.kotuc.android.towerdefence;

enum Dir {
	N(0, -1),
	E(1, 0),
	S(0, 1),	
	W(-1, 0);
	
	final int dx;
	final int dy;
	final Pos dp;
	
	Dir(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		this.dp = new Pos(dx, dy);
	}
	
	Dir opposite() {
		return values()[(ordinal()+2)%4];
	}
	
}