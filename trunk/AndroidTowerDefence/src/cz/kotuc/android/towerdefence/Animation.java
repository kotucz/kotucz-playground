package cz.kotuc.android.towerdefence;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

class Animation {
	Bitmap image;
	int cellw;
	int cellh;				
	Rect rect = new Rect();
	RectF rectf = new RectF();
	void draw(Canvas canvas, int coll, int row, RectF target) {
		rect.set(coll*cellw, row*cellh, (coll+1)*cellw, (row+1)*cellh);
		canvas.drawBitmap(image, rect, target, null);
	}
}