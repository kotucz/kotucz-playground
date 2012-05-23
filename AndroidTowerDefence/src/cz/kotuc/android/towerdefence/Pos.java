package cz.kotuc.android.towerdefence;

import android.graphics.PointF;

class Pos extends PointF {

	public Pos() {
	}

	Pos(float x, float y) {
		super(x, y);
	}

	Pos(PointF p) {
		super(p.x, p.y);
	}

	Pos to(PointF p2) {
		return new Pos(p2.x - this.x, p2.y - this.y);
	}

	float normalize() {
		float n = length();
		if (n > 0) {
			this.mul(1f / n);
			// this.x/=n;
			// this.y/=n;
		}
		return n;
	}

	void mul(float m) {
		this.x *= m;
		this.y *= m;

	}

	void add(PointF p2) {
		this.x += p2.x;
		this.y += p2.y;
	}

}