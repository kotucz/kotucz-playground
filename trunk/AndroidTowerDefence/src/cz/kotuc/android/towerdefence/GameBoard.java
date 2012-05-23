package cz.kotuc.android.towerdefence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.Log;

public class GameBoard {

	static final String TAG = "GB";

	final int W;
	final int H;

	final Patch[] fields;

	final List<Creep> creeps = new LinkedList<Creep>();
	final List<Bullet> bullets = new LinkedList<Bullet>();
	final List<Unit> units = new LinkedList<Unit>();

	int cash = 1000;

	final Patch finishPatch;

	final MediaPlayer mp;

	final GUI gui = new GUI(this);

	public GameBoard(Context context, int w, int h, GameView view) {
		Log.e(TAG, "gameboard init");

		this.data = new GraphicManager(context);

		this.W = w;
		this.H = h;

		gui.view = view;

		// this.act = act;

		OUTSIDE_PATCH = new Patch() {
			{
				walkable = false;
			}
		};

		this.fields = new Patch[W * H];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new Patch(i);
		}

		finishPatch = getFieldInt(W - 1, H / 2);
		updatePaths();

		mp = MediaPlayer.create(context, R.raw.gunshot_1);
		mp.start();

		// creeps = new LinkedList<Creep>();
		// bullets = new LinkedList<Bullet>();

		// setup level

//		fields[20].setTower(new Tower(TowerType.TYPE1));		
//		fields[20].setTower(new Tower(TowerType.TYPE1));

		Log.e(TAG, "gameboard init done");
	}

	void updatePaths() {
		bfs(finishPatch.getpX(), finishPatch.getpY());
	}

	int index(int x, int y) {
		return y * W + x;
	}

	int getX(int index) {
		return index % W;
	}

	int getY(int index) {
		return index / W;
	}

	long lastt;

	void draw(Canvas canvas) {
		// Log.e(TAG, "gameboard draw");

		canvas.drawColor(0xffFF8888);

		int cw = canvas.getWidth();
		int ch = canvas.getHeight();

		// canvas.translate(offx, offy);
		// canvas.scale(0.5f, 1.5f);

		canvas.setMatrix(vscreen.getDrawMatrix());

		canvas.drawBitmap(data.backbm, null, new RectF(0, 0, W, H), data.paint);
		// canvas.drawBitmap(data.backbm, null, new RectF(0, 0, W, H),
		// data.paint);

		// canvas.scale(one, one);

		// for (int x = 0; x < W; x++) {
		// for (int y = 0; y < H; y++) {
		//
		// // fields[index(x, y)].act();
		// fields[index(x, y)].draw(canvas);
		//
		// }
		// }

		for (Patch field : fields) {
			field.draw(canvas);
		}

		for (Creep creep : creeps) {
			// creep.act();
			creep.draw(canvas);
		}

		for (Unit bullet : units) {
			// bullet.act();
			bullet.draw(canvas);
		}

		for (Bullet bullet : bullets) {
			// bullet.act();
			bullet.draw(canvas);
		}

		gui.drawSelectedTower(canvas);

		// Matrix matrix = canvas.getMatrix();
		canvas.setMatrix(new Matrix());

		// draw menus

		// canvas.drawText("behold board! ", one, one, fontp);

		gui.draw(canvas);

		long t = System.currentTimeMillis();
		long dt = t - lastt;
		float fps = 1000f / dt;
		lastt = t;

		canvas.drawText("FPS: " + fps + " ms: " + dt, 300, 50, data.fontpaint);

		canvas.drawText("events " + gui.events, 20, 100, data.fontpaint);

		canvas.drawText("creeps: " + creeps.size(), 20, 150, data.fontpaint);

		canvas.drawText("bullets: " + bullets.size(), 20, 200, data.fontpaint);

		canvas.drawText("events " + gui.buildingTower, 20, 250, data.fontpaint);

		canvas.drawText("w: " + cw + " h: " + ch, 20, 20, data.fontpaint);

	}

	final ViewScreen vscreen = new ViewScreen();

	class ViewScreen {

		float swidth = 800;
		float sheigth = 480;

		// size of one tile
		volatile float one = 40;

		volatile float offx = 10;
		volatile float offy = 10;

		void correctBounds() {

			// float minZoom = screenWidth/mapWidth;

			// if () {

			// }
		}

		Patch getTouchField(float tx, float ty) {

			Matrix inverse = new Matrix();
			getDrawMatrix().invert(inverse);

			float[] txy = new float[] { tx, ty };
			inverse.mapPoints(txy);

			return getFieldFloat(txy[0], txy[1]);

		}

		Matrix getDrawMatrix() {
			Matrix m = new Matrix();
			m.setScale(one, one);
			m.postTranslate(offx, offy);
			return m;
		}

	}

	Patch getFieldFloat(float x, float y) {
		return getFieldInt((int) Math.floor(x), (int) Math.floor(y));
	}

	Patch getFieldInt(int fieldx, int fieldy) {
		if (fieldx < 0 || W <= fieldx || fieldy < 0 || H <= fieldy) {
			// Log.e(TAG, "oob " + fieldx + " " + fieldy);
			return OUTSIDE_PATCH; // instead of null
		}
		return fields[index(fieldx, fieldy)];
	}

	static final Random RANDOM = new Random();

	final Patch OUTSIDE_PATCH;

	// = new Patch() {
	//
	// {
	// setField(new Field() {
	// boolean isWalkable() {return false;};
	// });
	// }
	//
	// void draw(Canvas canvas) {
	// };
	//
	// // public void setField(Field field) {
	// // throw new IllegalStateException(
	// // "setting wrong patch field");
	// // };
	//
	// @Override
	// boolean isWalkable() {
	// return false;
	// }
	// };

	class Patch {

		public Patch() {
			index = -1;
		}

		int distance;
		final int index;
		// Field field; // State pattern

		Patch patch;
		boolean walkable = true;

		Dir next;// = Dir.values()[RANDOM.nextInt(4)];
		public Tower tower = null;

		// public Patch(int index, Field field) {
		// this.index = index;
		// setField(field);
		// }

		public Patch(int index) {
			this.index = index;
		}

//		public void setTower(Tower field) {
//			this.tower = field;			
//			if (tower != null) {
//				field.p.set(getpX(), getpY());
//			}
//		}

		void buildTower(TowerType type) {
			this.tower = new Tower(this, type);
		}
		
		// public void setField(Field field) {
		// this.field = field;
		// field.patch = this;
		// field.p.set(getpX(), getpY());
		// }

		// delegate method
		void draw(Canvas canvas) {
			float x = getpX();
			float y = getpY();
			RectF rectF = new RectF(x, y, (x + 1), (y + 1));

			if (walkable && next != null) {
				canvas.drawLine(x, y, x + 0.4f * next.dx, y + 0.4f * next.dy,
						data.paint);
				data.arrowAnim.draw(canvas, next.ordinal() + 1, 0, rectF);
			} else {
				canvas.drawOval(rectF, data.paint);
			} //
			if (tower != null) {
				tower.draw(canvas);
			}
			// field.draw(canvas);
		}

		// delegate method
		boolean isWalkable() {			
			return (tower==null)&&walkable;
			// return field.isWalkable();
		}

		// public void act() {
		// field.act();
		// }

		Patch getNeigh(Dir dir) {
			return getFieldInt(getpX() + dir.dx, getpY() + dir.dy);
		}

		int getpX() {
			return getX(this.index);
		}

		int getpY() {
			return getY(this.index);
		}

		public void sellTower() {
			this.tower = null;			
		}

	}

	// class Field extends Unit {
	//
	// Patch patch;
	// boolean walkable = true;
	//
	// Dir next;// = Dir.values()[RANDOM.nextInt(4)];
	//
	// public Field() {
	// }
	//
	// void draw(Canvas canvas) {
	// // canvas.drawBitmap(towerbm, x, y, null);
	//
	// // canvas.drawOval(new RectF(x*one, y*one, (x+1)*one,
	// // (y+1)*one), p);
	// // canvas.drawOval(new RectF(x, y, (x + 1), (y + 1)), p);
	//
	// RectF rectF = new RectF(p.x, p.y, (p.x + 1), (p.y + 1));
	//
	// if (walkable && next != null) {
	// canvas.drawLine(p.x, p.y, p.x + 0.4f * next.dx, p.y + 0.4f
	// * next.dy, data.paint);
	// data.arrowAnim.draw(canvas, next.ordinal() + 1, 0, rectF);
	// } else {
	// canvas.drawOval(rectF, data.paint);
	// } //
	//
	// }
	//
	// boolean isWalkable() {
	// return walkable;
	// }
	//
	// }	
	
	class Tower extends Unit {

		// float reload;
		final Patch patch;
		final Schedule reload = new Schedule();
		final TowerType tower;
		final RectF aabb;

		Tower(Patch patch, TowerType type) {
			this.patch = patch;
			this.tower = type;
			// walkable = false;
			reload.fromNow(1000);
			// next = Dir.E;
			float reach = tower.radius;
			p.set(patch.getpX(), patch.getpY());
			aabb = new RectF(p.x - reach, p.y - reach, (p.x + reach), (p.y + reach));
						
		}
		
		void draw(Canvas canvas) {
			// canvas.drawBitmap(towerbm, x, y, null);

			// canvas.drawOval(new RectF(x*one, y*one, (x+1)*one,
			// (y+1)*one), p);
			// canvas.drawOval(new RectF(x, y, (x + 1), (y + 1)), p);

			// if (walkable) {
			// canvas.drawLine(p.x, p.y, p.x + 0.4f * next.dx, p.y + 0.4f
			// * next.dy, data.paint);
			// } else {
			// canvas.drawOval(new RectF(p.x, p.y, (p.x + 1), (p.y + 1)),
			// data.paint);
			// } //


			
			canvas.drawOval(aabb, data.paint);

			// float reach = tower.radius;
			// canvas.drawOval(new RectF(p.x - reach, p.y - reach, (p.x +
			// reach),
			// (p.y + reach)), data.paint);

			canvas.drawBitmap(tower.getBitmap(), null, new RectF(p.x,
					p.y - 0.5f, (p.x + 1f), (p.y + 1f)), null);

			canvas.drawLine(p.x, p.y - 1.2f, p.x + reload.amountDone(),
					p.y - 1.2f, data.healthPaint);

			// break;
			// }
		}

		boolean inZone(Pos pos) {
			return p.to(pos).length() <= tower.radius;
		}

		boolean isWalkable() {
			return false;
		}

		@Override
		boolean act() {
			// if (reload < 0) {
			// // try shoot
			// reload += 50;
			// for (Creep creep : creeps) {
			// if (p.to(creep.p).length() <= reach) {
			// bullets.add(new Bullet(this, creep));
			// break;
			// }
			// }
			// }
			// reload -= 1;
			if (reload.passed()) {
				// try shoot
				reload.after(1000); // one second
				for (Creep creep : creeps) {
					if (inZone(creep.p)) {
						bullets.add(new Bullet(this, creep));
						mp.seekTo(0);
						mp.start();
						// try {
						// // mp.stop();
						// // mp.reset();
						// // mp.prepare();
						// mp.start();
						// } catch (Exception e) {
						// // Log.e(TAG, "prepare", e);
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
						break;
					}
				}
			}

			return REMAIN;
		}

	}

	static final int INF = Integer.MAX_VALUE;

	void bfs(int x, int y) {
		for (Patch patch : fields) {
			if (patch.isWalkable()) {
				patch.distance = INF;
				patch.next = null;
			}
		}
		Queue<Patch> queue = new LinkedList<Patch>();
		Patch startp = getFieldInt(x, y);
		startp.distance = 0;
		queue.add(startp);

		while (!queue.isEmpty()) {
			Patch cur = queue.poll();
			// Log.e("bfs", "p " + cur.getpX() + ", " + cur.getpY());
			for (Dir dir : Dir.values()) {
				Patch neigh = cur.getNeigh(dir);
				if (neigh.isWalkable() && neigh.next == null) {
					neigh.next = dir.opposite();
					neigh.distance = cur.distance + 1;
					queue.add(neigh);

				}
			}
		}
	}

	class Unit {
		Pos p = new Pos();

		static final boolean REMAIN = false;
		static final boolean REMOVE = true;

		void draw(Canvas canvas) {
		}

		boolean act() {
			return REMAIN;
		}

	}

	class Creep extends Unit {

		float health = 100f;
		Pos target = new Pos(10, 5);
		final float speed = 0.1f;

		void draw(Canvas canvas) {
			canvas.drawLine(p.x, p.y - 0.2f, p.x + health / 100f, p.y - 0.2f,
					data.healthPaint);
			canvas.drawBitmap(data.creepbm, null, new RectF(p.x, p.y,
					(p.x + 1), (p.y + 1)), null);
		}

		@Override
		boolean act() {
			if (health <= 0) {
				return REMOVE;
			}
			if (moveTo(p, target, speed)) {
				Patch patch = getFieldFloat(p.x, p.y);
				if (finishPatch.equals(patch)) {
					// attack player and remove
					return REMOVE;
				} else {
					// if (field.isWalkable()) {
					this.target.add(patch.next.dp);
					Patch tfield = getFieldFloat(target.x, target.y);

					if (!tfield.isWalkable()) {
						this.target.set(this.p); // do not try this time
						patch.next = Dir.values()[RANDOM.nextInt(4)];
					}
				}

			}

			// p.x += 0.1f;
			// if (W < p.x) {
			// p.x = 0;
			// p.y += 1;
			// }
			return REMAIN;
		}
	}

	static GraphicManager data;

	class Bullet extends Unit {

		Tower tower;
		Creep target;
		float speed = 0.1f;
		boolean splash = true;

		public Bullet(Tower tower, Creep creeo) {
			this.tower = tower;
			this.target = creeo;
			this.p.set(tower.p);
		}

		void draw(Canvas canvas) {
			canvas.drawCircle(p.x, p.y, 0.1f, data.bulletpaint);
			// canvas.drawLine(tower.p.x, tower.p.y, target.p.x, target.p.y,
			// bulletpaint);
		}

		boolean act() {
			// p.x += (target.p.x < p.x) ? -0.1f : 0.1f;
			// p.y += (target.p.y < p.y) ? -0.1f : 0.1f;

			if (moveTo(p, target.p, speed)) {
				// hit
				target.health -= 5;
				if (splash) {
					units.add(new Explosion(target.p));
				}
				return REMOVE;
			}

			return REMAIN;
		}

		void actSlow() {
			Pos v = this.p.to(target.p);
			float l = v.length();
			if (l <= speed) {
				// hit
				target.health -= 10;
			} else {
				// normalize and mul by speed
				v.mul(speed / l);
				this.p.add(v);
			}
		}

	}

	class Explosion extends Unit {

		final Schedule schedule = new Schedule();

		public Explosion(Pos pos) {
			schedule.fromNow(1000); //
			this.p.set(pos);
		}

		void draw(Canvas canvas) {
			canvas.drawCircle(p.x, p.y, schedule.amountDone(), data.bulletpaint);
			// canvas.drawLine(tower.p.x, tower.p.y, target.p.x, target.p.y,
			// bulletpaint);
		}

		boolean act() {
			// p.x += (target.p.x < p.x) ? -0.1f : 0.1f;
			// p.y += (target.p.y < p.y) ? -0.1f : 0.1f;
			return (schedule.passed()) ? REMOVE : REMAIN;
		}

	}

	// queue of creeps to come current level
	Queue<Creep> creepQueue = new LinkedList<Creep>();

	public void actAll() {

		gametime += 100;

		if (creeps.isEmpty()) {
			for (int i = 0; i < 10; i++) {
				Creep creep = new Creep();
				creep.health = 100;
				creep.p.set(5 + i, 5);
				creeps.add(creep);
			}
		}

		for (Patch field : fields) {
			if (field.tower != null) {
				field.tower.act();
			}
		}

		// for (Creep creep : creeps) {
		// creep.act();
		// }

		for (Iterator<Creep> it = creeps.iterator(); it.hasNext();) {
			Creep creep = it.next();
			if (creep.act()) {
				it.remove();
			}
		}

		// for (Bullet bullet : bullets) {
		// bullet.act();
		// }

		for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();) {
			Bullet b = it.next();
			if (b.act()) {
				it.remove();
			}
		}

		for (Iterator<Unit> it = units.iterator(); it.hasNext();) {
			Unit b = it.next();
			if (b.act()) {
				it.remove();
			}
		}

	}

	static boolean moveTo(Pos p, Pos target, float speed) {
		boolean done = true;
		float tdx = target.x - p.x;
		if (Math.abs(tdx) < speed) {
			p.x = target.x;
			// select new
		} else {
			p.x += (target.x < p.x) ? -speed : speed;
			done = false;
		}
		float tdy = target.y - p.y;
		if (Math.abs(tdy) < speed) {
			p.y = target.y;
			// select new
		} else {
			p.y += (target.y < p.y) ? -speed : speed;
			done = false;
		}
		return done;
	}

	long gametime = 0;

	class Schedule {
		long start;
		long end;

		boolean passed() {
			return end <= gametime;
		}

		void after(long millis) {
			start = end;
			end += millis;
		}

		void fromNow(long millis) {
			start = gametime;
			end = gametime + millis;
		}

		float amountDone() {
			return Math.min((float) (gametime - start) / (end - start), 1f);
		}

	}

}
