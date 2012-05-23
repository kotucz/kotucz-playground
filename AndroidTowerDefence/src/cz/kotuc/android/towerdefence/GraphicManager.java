package cz.kotuc.android.towerdefence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

public class GraphicManager {
	final Bitmap towerbm;
	final Bitmap towerbm2;
	final Bitmap backbm;
	final Bitmap creepbm;
	final Bitmap arrowsbm;
	final Animation arrowAnim = new Animation();
	final Paint bulletpaint = new Paint();
	{
		bulletpaint.setColor(0xffBB3300);
		bulletpaint.setStrokeWidth(0.1f);
	}
	
	final Paint paint = new Paint();
	{
		paint.setColor(0xffA47C23);
		paint.setStrokeWidth(0.05f);
		paint.setStyle(Paint.Style.STROKE);
	}

	final Paint healthPaint = new Paint();
	{
		healthPaint.setColor(0xff00FF00);
		healthPaint.setStrokeWidth(0.1f);
		healthPaint.setStyle(Paint.Style.STROKE);
	}

	final Paint fontpaint = new Paint();
	{
		fontpaint.setTextSize(30);
		fontpaint.setColor(0xff74AC23);
	}

	final Paint cashfontpaint = new Paint();
	{
		fontpaint.setTextSize(30);
		fontpaint.setColor(0xff74AC23);
	}

	
	public GraphicManager(Context context) {		

		towerbm = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tower1);

		towerbm2 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tower2);
		
		backbm = BitmapFactory
				.decodeResource(context.getResources(), R.drawable.wood);

		creepbm = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.creep1);

		arrowsbm = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.arrows);

		arrowAnim.cellh = 50;
		arrowAnim.cellw = 50;
		arrowAnim.image = arrowsbm;
		
	}
}